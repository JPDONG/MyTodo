package com.learn.mytodo.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.learn.mytodo.data.source.UserIdentityService;

/**
 * Created by dong on 2017/3/12 0012.
 */

public class UserIdentityResult implements UserIdentityService.UserResult,Parcelable {

    private Context mContext;

    public UserIdentityResult() {
    }

    public static final Creator<UserIdentityResult> CREATOR = new Creator<UserIdentityResult>() {
        @Override
        public UserIdentityResult createFromParcel(Parcel in) {
            Context context = in.readValue(Context.class);
            return new UserIdentityResult();
        }

        @Override
        public UserIdentityResult[] newArray(int size) {
            return new UserIdentityResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mContext);
    }

    void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void success(String s) {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(String s) {

    }
}
