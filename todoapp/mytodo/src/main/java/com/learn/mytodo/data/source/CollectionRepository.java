package com.learn.mytodo.data.source;

import android.content.Context;
import android.util.Log;

import com.learn.mytodo.data.source.local.CollectionsLocalDataSource;
import com.learn.mytodo.collection.CollectionItem;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollectionRepository {

    public static final String TAG = "CollectionRepository";

    private static CollectionRepository mInstance;
    private CollectionsLocalDataSource mDataSource;
    private OkHttpClient mOkHttpClient;

    public synchronized static CollectionRepository getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CollectionRepository(context);
        }
        return mInstance;
    }

    private CollectionRepository(Context context) {
        mDataSource = new CollectionsLocalDataSource(context);
        mOkHttpClient = new OkHttpClient();
    }

    public boolean save(CollectionItem item) {
        return mDataSource.save(item);
    }

    public List<CollectionItem> getCollections() {
        return mDataSource.getCollections();
    }

    public String test() {
        Log.d(TAG, "test: ");
        Request.Builder builder = new Request.Builder();
        Request request =  builder.url("https:\\\\www.bing.com").build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.d(TAG, "test: " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(CollectionItem item) {
        return mDataSource.delete(item);
    }
}
