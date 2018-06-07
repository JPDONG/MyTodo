package com.learn.mytodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.learn.mytodo.collection.CollectionItem;
import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.Status;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CollectionsLocalDataSource {

    private static final String TAG = "CollectionsLocalDataSource";

    private DBHelper mDBHelper;
    private static final String[] projection = {"id","title","create_at"};

    public CollectionsLocalDataSource(Context context) {
        checkNotNull(context);
        mDBHelper = new DBHelper(context);
    }

    public List<CollectionItem> getCollections() {
        List<CollectionItem> list = new ArrayList<CollectionItem>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.COLLECTIONS_TABLE_NAME, projection, "status != " + Status.STATUS_DELETE, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                int nums = (int) DatabaseUtils.queryNumEntries(database,DBHelper.TASKS_TABLE_NAME,"collection_id like '" + id +"'",null);
                String title = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String createAt = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                list.add(new CollectionItem(id,title,createAt,nums));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        /*for (CollectionItem item : list) {
            item.nums =  (int) DatabaseUtils.queryNumEntries(database,DBHelper.TASKS_TABLE_NAME,"id like '" + item.id +"'",null);
        }*/
        database.close();
        Log.d(TAG, "getCollections: " + list.size());
        return list;
    }

    public boolean save(CollectionItem item) {
        checkNotNull(item);
        Log.d(TAG, String.format("save: id %s, title %s",item.id,item.title));
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",item.id);
        contentValues.put("title", item.title);
        contentValues.put("create_at", String.valueOf(System.currentTimeMillis()));
        contentValues.put("status",Status.STATUS_ADD);
        return sqLiteDatabase.insert(DBHelper.COLLECTIONS_TABLE_NAME, null, contentValues) == -1?false:true;
    }
}
