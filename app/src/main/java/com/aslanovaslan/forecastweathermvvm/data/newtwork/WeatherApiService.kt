package com.aslanovaslan.forecastweathermvvm.data.newtwork

import com.aslanovaslan.forecastweathermvvm.data.newtwork.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "bb150486e81346249cd132033200307"

interface WeatherApiService {
//https://api.weatherapi.com/v1/current.json?key=bb150486e81346249cd132033200307&q=Paris&lang=fr

    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherResponse>


    // https://api.apixu.com/v1/forecast.json?key=89e8bd89085b41b7a4b142029180210&q=Los%20Angeles&days=1
    /*  @GET("forecast.json")
      fun getFutureWeather(
          @Query("q") location: String,
          @Query("days") days: Int,
          @Query("lang") languageCode: String = "en"
      ): Deferred<FutureWeatherResponse>
  */
    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key",
                        API_KEY
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.weatherapi.com/v1/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }

}