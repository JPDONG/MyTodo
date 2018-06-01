package com.learn.mytodo.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.mytodo.collection.CollectionItem;
import com.learn.mytodo.data.Task;
import com.learn.mytodo.data.source.Status;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CollectionsLocalDataSource {

    private DBHelper mDBHelper;
    private static final String[] projection = {"id","title","createAt"
    };

    public CollectionsLocalDataSource(Context context) {
        checkNotNull(context);
        mDBHelper = new DBHelper(context);
    }

    public List<CollectionItem> getCollections() {
        List<CollectionItem> list = new ArrayList<CollectionItem>();
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TASKS_TABLE_NAME, projection, "status != '" + Status.STATUS_DELETE + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String createAt = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                list.add(new CollectionItem(id,title,createAt));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        return list;
    }

    public boolean save(CollectionItem item) {
        checkNotNull(item);
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",item.id);
        contentValues.put("title", item.title);
        contentValues.put("createAt", String.valueOf(System.currentTimeMillis()));
        sqLiteDatabase.insert(DBHelper.COLLECTIONS_TABLE_NAME, null, contentValues);
        return false;
    }
}
