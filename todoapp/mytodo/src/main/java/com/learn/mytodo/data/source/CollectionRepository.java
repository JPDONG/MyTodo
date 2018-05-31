package com.learn.mytodo.data.source;

import android.content.Context;

import com.learn.mytodo.data.source.local.CollectionsLocalDataSource;
import com.learn.mytodo.task.CollectionItem;

import java.util.List;

import io.reactivex.Observable;

public class CollectionRepository {

    private static CollectionRepository mInstance;
    private CollectionsLocalDataSource mDataSource;

    public synchronized static CollectionRepository getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CollectionRepository(context);
        }
        return mInstance;
    }

    private CollectionRepository(Context context) {
        mDataSource = new CollectionsLocalDataSource(context);
    }

    public boolean save(CollectionItem item) {
        return mDataSource.save(item);
    }

    public List<CollectionItem> getCollections() {
        return mDataSource.getCollections();
    }
}
