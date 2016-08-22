package io.swagger.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import th.co.siamkubota.kubota.app.Config;

public class ServiceGenerator {
  // No need to instantiate this class.
  private ServiceGenerator() { }

  public static <S> S createService(Class<S> serviceClass) {
    Gson gson = new GsonBuilder()
            //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setDateFormat("yyyy-MM-dd' 'HH:mm:ss")
            .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
            .create();

    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(30, TimeUnit.SECONDS);
    client.setReadTimeout(30, TimeUnit.SECONDS);
    client.setWriteTimeout(30, TimeUnit.SECONDS);

    client.interceptors().add(new LoggingInterceptor());

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(Config.base)
              .addConverterFactory(GsonConverterFactory.create(gson))
              .client(client)
              .build();

    return retrofit.create(serviceClass);
  }
}

