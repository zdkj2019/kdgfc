package com.fc.service;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fc.R;
import com.fc.activity.esp.NotificationOrderList;
import com.fc.activity.esp.SheBeiPDActivity.MyLocationListener;
import com.fc.activity.kdg.ListKdg;
import com.fc.activity.login.LoginActivity;
import com.fc.cache.DataCache;
import com.fc.definition.Sign;
import com.fc.utils.AppUtils;
import com.fc.utils.Config;
import com.fc.webservice.CallWebserviceImp;

@SuppressLint("HandlerLeak")
public class PatrolService extends Service implements Sign{

	private CallWebserviceImp callWebserviceImp;
	private String flag;
	String txzd = "";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		callWebserviceImp = new CallWebserviceImp();
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		getWebService("query");
		return super.onStartCommand(intent, flags, startId);
	}

	Calendar c = Calendar.getInstance();

	private void getWebService(String s) {

		try {
			new Thread() {

				@Override
				public void run() {

					int currentTime = c.get(Calendar.HOUR_OF_DAY);

					if (currentTime >= 8 && currentTime <= 22) {
						query();
					}

				}

			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void query() {
		try {
			String userid = DataCache.getinition().getUserId();
			JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
					"_PAD_SHGL_KDG_WWGDS", userid + "*" + userid,
					"uf_json_getdata", this);
			flag = jsonObject.getString("flag");
			if (Integer.parseInt(flag) > 0) {
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject temp = jsonArray.getJSONObject(i);
					txzd = txzd + temp.getString("wwgd");
				}
				if (!"".equals(txzd)) {
					
					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
				}else{
					AppUtils.sendBadgeNumber(this,"");//清空
				}
			} else {
				Message msg = new Message();
				msg.what = FAIL;// 失败
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = NETWORK_ERROR;// 网络不通
			handler.sendMessage(msg);
		}

	}

	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				break;

			case SUCCESSFUL:
				break;
			}
			txzd = "";
			stopSelf();
		}

	};

}
