package io.swagger.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import th.co.siamkubota.kubota.app.Config;

public class ServiceGenerator {
  // No need to instantiate this class.
  private ServiceGenerator() { }

  public static <S> S createService(Class<S> serviceClass) {
    Gson gson = new GsonBuilder()
            //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
            .create();

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(Config.base)
              .addConverterFactory(GsonConverterFactory.create(gson))
              .build();

    return retrofit.create(serviceClass);
  }
}

