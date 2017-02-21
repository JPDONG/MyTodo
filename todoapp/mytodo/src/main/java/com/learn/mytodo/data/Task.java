package com.learn.mytodo.data;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by dongjiangpeng on 2017/2/21 0021.
 */

public final class Task {
    private final String mId;
    private final String mTitle;
    private final String mDescription;
    private final boolean mCompleted;

    public Task(String mId, String mTitle, String mDescription, boolean mCompleted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mCompleted = mCompleted;
    }

    public Task(String mTitle, String mDescription){
        this(UUID.randomUUID().toString(),mTitle,mDescription,false);
    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public boolean ismCompleted() {
        return mCompleted;
    }

    @Override
    public boolean equals(Object obj) {
        Task o = (Task) obj;
        return mId == o.mId && mTitle == o.mTitle && mDescription == o.mDescription && mCompleted == o.mCompleted;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }
}
