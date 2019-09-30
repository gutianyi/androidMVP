package top.horsttop.appcore.util.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;


/**
 * Created by horsttop on 2018/4/18.
 */

public class NetworkStateReceiver extends BroadcastReceiver {
	private static Boolean networkAvailable = false;
	private static NetWorkUtil.NetType netType;
	private static ArrayList<NetChangeObserver> netChangeObserverArrayList = new ArrayList();
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private static BroadcastReceiver receiver;


	private static BroadcastReceiver getReceiver() {
		if (receiver == null) {
			receiver = new NetworkStateReceiver();
		}
		return receiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		receiver = NetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
			if (!NetWorkUtil.isNetworkAvailable(context)) {
				netType = NetWorkUtil.getNetworkType(context);
				networkAvailable = false;
			} else {
				netType = NetWorkUtil.getNetworkType(context);
				networkAvailable = true;
			}
			notifyObserver();
		}
	}

	/**
	 * 注册网络状态广播
	 *
	 * @param mContext
	 */
	public static void registerNetworkStateReceiver(Context mContext) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
	}

	/**
	 * 检查网络状态
	 *
	 * @param mContext
	 */
	public static void checkNetworkState(Context mContext) {
		Intent intent = new Intent();
		mContext.sendBroadcast(intent);
	}

	/**
	 * 注销网络状态广播
	 *
	 * @param mContext
	 */
	public static void unRegisterNetworkStateReceiver(Context mContext) {
		if (receiver != null) {
			try {
				mContext.getApplicationContext().unregisterReceiver(receiver);
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 *
	 * @return
	 */
	public static Boolean isNetworkAvailable() {
		return networkAvailable;
	}

	public static NetWorkUtil.NetType getAPNType() {
		return netType;
	}

	private void notifyObserver() {

		for (int i = 0; i < netChangeObserverArrayList.size(); i++) {
			NetChangeObserver observer = netChangeObserverArrayList.get(i);
			if (observer != null) {
				if (isNetworkAvailable()) {
					observer.onConnect(netType);
				} else {
					observer.onDisConnect();
				}
			}
		}

	}

	/**
	 * 注册网络连接观察者
	 *
	 * @param observer
	 *            observer
	 */
	public static void registerObserver(NetChangeObserver observer) {
		if (netChangeObserverArrayList == null) {
			netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
		}
		netChangeObserverArrayList.add(observer);
	}

	/**
	 * 注销网络连接观察者
	 *
	 * @param observer
	 *            observerKey
	 */
	public static void removeRegisterObserver(NetChangeObserver observer) {
		if (netChangeObserverArrayList != null) {
			netChangeObserverArrayList.remove(observer);
		}
	}

}