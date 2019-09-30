package top.horsttop.core.exception

import android.os.Handler
import android.os.Looper

/**
 * Created by horsttop on 2017/12/24.
 */

object Cockroach {

    private var sExceptionHandler: ExceptionHandler? = null
    private var sUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    private var sInstalled = false//标记位，避免重复安装卸载

    interface ExceptionHandler {

        fun handlerException(thread: Thread, throwable: Throwable)
    }

    /**
     * 当主线程或子线程抛出异常时会调用exceptionHandler.handlerException(Thread thread, Throwable throwable)
     *
     *
     * exceptionHandler.handlerException可能运行在非UI线程中。
     *
     *
     * 若设置了Thread.setDefaultUncaughtExceptionHandler则可能无法捕获子线程异常。
     *
     * @param exceptionHandler
     */
    @Synchronized
    fun install(exceptionHandler: ExceptionHandler) {
        if (sInstalled) {
            return
        }
        sInstalled = true
        sExceptionHandler = exceptionHandler

        Handler(Looper.getMainLooper()).post(Runnable {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    //                        Binder.clearCallingIdentity();
                    if (e is QuitException) {
                        return@Runnable
                    }
                    if (sExceptionHandler != null) {
                        sExceptionHandler!!.handlerException(Looper.getMainLooper().thread, e)
                    }
                }

            }
        })

        sUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (sExceptionHandler != null) {
                sExceptionHandler!!.handlerException(t, e)
            }
        }

    }

    @Synchronized
    fun uninstall() {
        if (!sInstalled) {
            return
        }
        sInstalled = false
        sExceptionHandler = null
        //卸载后恢复默认的异常处理逻辑，否则主线程再次抛出异常后将导致ANR，并且无法捕获到异常位置
        Thread.setDefaultUncaughtExceptionHandler(sUncaughtExceptionHandler)
        Handler(Looper.getMainLooper()).post {
            throw QuitException("Quit Cockroach.....")//主线程抛出异常，迫使 while (true) {}结束
        }

    }
}
