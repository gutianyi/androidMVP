package top.horsttop.appcore.dagger.module

import android.app.Application
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import top.horsttop.appcore.BuildConfig
import top.horsttop.appcore.network.RetrofitInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by horsttop on 2018/4/13.
 */
@Singleton
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun ofMoshi(): Moshi {
        Timber.i("to ofMoshi")
        return Moshi.Builder()
                // Add any other JsonAdapter factories.
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    private class CachingControlInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            val requestBuilder = chain.request().newBuilder()
            val cacheControl = CacheControl.Builder()
                    .maxStale(1, TimeUnit.MINUTES)
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()

            requestBuilder.cacheControl(cacheControl)
//            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.build()

            val originalResponse = chain.proceed(request)
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=604800")
                    .build()
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpCache(pApplication: Application): Cache =
            Cache(pApplication.cacheDir, 10 * 1024 * 1024)

    @Provides
    @Singleton
    fun ofOkHttpClient(cache: Cache,context:Application): OkHttpClient {
        Timber.i("to ofOkHttpClient")

        return OkHttpClient.Builder()
                .addInterceptor(RetrofitInterceptor(context))
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
//                .addNetworkInterceptor(CachingControlInterceptor())
//                .cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun ofMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        Timber.i("to ofMoshiConverterFactory")
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun ofRetrofit(okHttpClient: OkHttpClient, factory: MoshiConverterFactory): Retrofit {
        Timber.i("to ofRetrofit")
        return Retrofit.Builder()
                .baseUrl(BuildConfig.END_POINT)
                .client(okHttpClient)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}