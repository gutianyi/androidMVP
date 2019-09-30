package top.horsttop.appcore.network

import android.content.Context
import android.util.Base64
import okhttp3.*


import java.io.IOException

import top.horsttop.appcore.BuildConfig
import top.horsttop.appcore.util.net.NetWorkUtil
import top.horsttop.core.exception.ExceptionCode
import top.horsttop.core.exception.QuitException
import java.lang.RuntimeException

/**
 * Created by horsttop on 2018/4/13.
 * Interceptor for Retrofit to add auth key to header
 */

class RetrofitInterceptor(var context:Context) : Interceptor {

    companion object {
         var token: String = ""
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response ?{


        if(!NetWorkUtil.isNetworkConnected(context)){
            throw NetworkErrorException()
        }
        var request = chain.request()



        if(token.isEmpty().not()){
            request = request.newBuilder()
                    .addHeader("Authorization", token)
                    .build()
        }
        val response = chain.proceed(request)



        when(response.code()){
            401 ->{
                throw QuitException(ExceptionCode.ACCESS_EXCEPTION.message,ExceptionCode.ACCESS_EXCEPTION.code)
            }
            500 ->{
                throw QuitException(ExceptionCode.SERVER_EXCEPTION.message,ExceptionCode.SERVER_EXCEPTION.code)
            }
        }



        return response
    }

    private fun getToken(): Request {
        val bearerToken = BuildConfig.CONSUMER_KEY +
                ":" + BuildConfig.CONSUMER_SECRET

        val base64BearerToken = "Basic " + Base64.encodeToString(bearerToken.toByteArray(), Base64.NO_WRAP)
        val requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), "grant_type=client_credentials")

        return Request.Builder()
                .url(BuildConfig.AUTH_END_POINT)
                .post(requestBody)
                .header("Authorization", base64BearerToken)
                .header("Content-Encoding", "gzip")
                .header("User-Agent", "horsttop")
                .header("Content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                .build()
    }

//    Authorization


    class NetworkErrorException() : RuntimeException()
}
