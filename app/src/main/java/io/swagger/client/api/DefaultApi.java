package io.swagger.client.api;

import io.swagger.client.CollectionFormats.*;

import retrofit.Call;
import retrofit.http.*;
import com.squareup.okhttp.RequestBody;

import io.swagger.client.model.Response;
import io.swagger.client.model.LoginResponse;
import io.swagger.client.model.Task;
import java.util.*;
import io.swagger.client.model.UploadResponse;
import java.io.File;



import java.util.*;



public interface DefaultApi {
  
  /**
   * login
   * login
   * @param username username.
   * @param password password.
   * @return Call<LoginResponse>
   */
  
  @FormUrlEncoded
  @POST("login")
  Call<LoginResponse> login(
          @Field("username") String username, @Field("password") String password
  );

  
  /**
   * submit form.
   * submit form.
   * @param taskList body data.
   * @return Call<Response>
   */
  
  @POST("submit-task-mobile")
  Call<Response> submitTask(
          @Body List<Task> taskList
  );

  
  /**
   * upload multiple image.
   * upload multiple image.
   * @param images username.
   * @return Call<UploadResponse>
   */
  
  @Multipart
  @POST("upload-multiple")
  Call<UploadResponse> uploadMultiple(
          @Part("images\"; filename=\"images\"") RequestBody images
  );

  
}

