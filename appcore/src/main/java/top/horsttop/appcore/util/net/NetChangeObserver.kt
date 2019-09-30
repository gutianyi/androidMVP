package top.horsttop.appcore.util.net


import javax.inject.Inject


/**
 * Created by horsttop on 2018/4/18.
 */
interface NetChangeObserver{

    /**
     * 网络连接连接时调用
     */
    fun onConnect(type: NetWorkUtil.NetType)

    /**
     * 当前没有网络连接
     */
    fun onDisConnect()
}
