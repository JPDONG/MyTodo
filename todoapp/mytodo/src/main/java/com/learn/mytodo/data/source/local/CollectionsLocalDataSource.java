package com.learn.mytodo.data.source.local;

import android.content.Context;
import com.learn.mytodo.task.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CollectionsLocalDataSource {

    private DBHelper mDBHelper;

    public CollectionsLocalDataSource(Context context) {
        checkNotNull(context);
        mDBHelper = new DBHelper(context);
    }

    public List<CollectionItem> getCollections() {
        return null;
    }

    public boolean save(CollectionItem item) {
        return false;
    }
}
