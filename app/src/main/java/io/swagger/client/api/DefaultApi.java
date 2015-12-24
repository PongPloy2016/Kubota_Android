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
import th.co.siamkubota.kubota.app.Config;

import java.io.File;



import java.util.*;



public interface DefaultApi {

  /**
   * login
   * login
   *
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
   *
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
   *
   * @param images username.
   * @return Call<UploadResponse>
   */

  @Multipart
  @POST("upload-multiple")
  Call<UploadResponse> uploadMultiple(
          @Part("image1\"; filename=\"image1.jpg\"") RequestBody image1,
          @Part("image2\"; filename=\"image2.jpg\"") RequestBody image2,
          @Part("image3\"; filename=\"image3.jpg\"") RequestBody image3,
          @Part("image4\"; filename=\"image4.jpg\"") RequestBody image4,
          @Part("signature1\"; filename=\"signature1.jpg\"") RequestBody signature1,
          @Part("signature2\"; filename=\"signature2.jpg\"") RequestBody signature2);

}

/*  @Multipart
  @POST (Config.mediaUploadFileService)
  Call<UploadFileResponse> uploadFile (
          @Header("x-access-token") String xAccessToken,
          @Part("file\"; filename=\"pp.jpg\" ") RequestBody file ,
          @Part("dateInfo") RequestBody dateInfo,
          @Part("path") RequestBody path);

  
*/

