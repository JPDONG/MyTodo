package com.learn.mytodo.user;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by dong on 2017/4/1 0001.
 */

public interface RemoteFileService {

    @Multipart
    @POST("files")
    Call<ResponseBody> upload(@Part("userid") RequestBody string, @Part MultipartBody.Part file);

    @GET("files")
    Call<ResponseBody> download();
}
