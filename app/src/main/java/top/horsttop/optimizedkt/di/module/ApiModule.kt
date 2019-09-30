package top.horsttop.optimizedkt.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import timber.log.Timber
import top.horsttop.optimizedkt.model.api.HttpApi
import javax.inject.Singleton

/**
 * Created by horsttop on 2018/4/19.
 */
@Singleton
@Module
class ApiModule {

    @Provides
    fun ofRetrofit(retrofit: Retrofit): HttpApi {
        Timber.i("to HttpApi")
       return retrofit.create(HttpApi::class.java)
    }

}