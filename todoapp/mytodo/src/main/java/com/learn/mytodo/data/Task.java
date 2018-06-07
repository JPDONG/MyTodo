package com.learn.mytodo.data;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import java.util.UUID;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public final class Task {

    @NonNull
    private final String id;
    private String collectionId;
    private final String title;
    private final String description;
    private boolean completed;
    private String status;
    private String modifiedTime;

    public Task(String id, String title, String description, boolean completed,String collectionId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.collectionId = collectionId;
    }

    public Task(String title, String description, String collectionId){
        this(UUID.randomUUID().toString(),title,description,false, collectionId);
    }

    public Task(String id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCollectionId() {
        return collectionId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Task) {
            Task o = (Task)obj;
            result = this.getId().equals(o.getId());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "" + this.id + this.getTitle() + this.getDescription() + this.isCompleted();
    }
}
