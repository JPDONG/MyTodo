package com.learn.mytodo.user;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.*;
import retrofit2.http.Multipart;

import com.learn.mytodo.R;
import com.learn.mytodo.data.source.remote.ServiceGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dong on 2017/4/1 0001.
 */

public class UserInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CHOOSE_ICON = 1;

    private ImageView mUserIcon;
    private TextView mUserName;
    private final String TAG = "UserInformationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_infomation_frag);
        mUserIcon = (ImageView) findViewById(R.id.user_icon);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserIcon.setOnClickListener(this);
        initViews();
    }

    private void initViews() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mUserName.setText(sharedPreferences.getString("user_name","user name"));
        String imagePath = sharedPreferences.getString("user_icon","");
        if (!"".equals(imagePath)) {
            mUserIcon.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.user_icon:
                Intent getIconIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getIconIntent, REQUEST_CHOOSE_ICON);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHOOSE_ICON && resultCode == RESULT_OK) {
            Uri choosedIconUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), choosedIconUri);
                mUserIcon.setImageBitmap(bitmap);
                File cacheDir = getCacheDir();
                String fileName = "icon_" + System.currentTimeMillis() + ".jpg";
                FileOutputStream fileOutputStream = new FileOutputStream(cacheDir);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadFile(choosedIconUri);
        }
    }

    private void uploadFile(Uri uri) {
        RemoteFileService remoteFileService = ServiceGenerator.createService(RemoteFileService.class);
        String filePath = getRealFilePath(uri);
        File file = new File(filePath);
        String fileName = "icon_" + System.currentTimeMillis() + ".jpg";
        //Log.d(TAG, "uploadFile: uri = " + uri.getPath() );
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("jpeg", fileName, fileBody);
        //MultipartBody.Part multipartBody = MultipartBody.Part.create(requestBody);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_icon", filePath);
        editor.apply();
        String userId = sharedPreferences.getString("user_id","notregister");

        RequestBody stringBody = RequestBody.create(MediaType.parse("multipart/form-data"),userId);
        Log.d(TAG, "uploadFile: multipartBody = " + multipartBody.toString());
        Call<ResponseBody> call = remoteFileService.upload(stringBody, multipartBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public String getRealFilePath(final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
