package com.fl.searchingapp.network

import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.nodes.Document
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface INetWork
{
    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36"
    )
    @GET("search")
    fun search(
        @Query("text", encoded = true) text: String
    ):Observable<Document>


    companion object Factory
    {
        val interceptor: Interceptor = Interceptor{chain: Interceptor.Chain ->
            val responce = chain.proceed(chain.request())
            responce
        }

        fun create(): INetWork
        {
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BASIC



            val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://yandex.ru/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(DocumAdapter.FACTORY)
                .client(okHttpClient).build()

            return retrofit.create(INetWork::class.java)
        }
    }
}