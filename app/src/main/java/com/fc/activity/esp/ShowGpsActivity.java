package com.fc.activity.esp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.utils.Config;
import com.fc.utils.WebDateModel;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class ShowGpsActivity extends FrameActivity implements // Listener,
		LocationListener {
	private TextView mTextView_Longitude;
	private TextView mTextView_Latitude;
	private TextView mTextView_Message;
	private TextView time;

	private LocationManager mLocationManager;
	private Button confirm, cancel;
	private String bestProvider, flag, pgdhId;
	private Location location;
	Double xzbf, yzbf;
	boolean sure = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_showgps);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void onStart() {
		super.onStart();
		// 绑定监听，有4个参数
		// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
		// 参数2，位置信息更新周期，单位毫秒
		// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		// 参数4，监听
		// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

		// 2秒更新一次，或最小位移变化超过2米更新一次；
		// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				2000, 0, this);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();
		mLocationManager.removeUpdates(this);
	}

	@Override
	public void onBackPressed() {
		skipActivity2(ArrivalConfirmationActivity.class);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.activity_main, menu); return true; }
	 */

	// @Override
	// public void onGpsStatusChanged(int event) {
	//
	// switch (event) {
	// // 第一次定位
	// case GpsStatus.GPS_EVENT_FIRST_FIX:
	// mTextView_Message.setText("第一次定位");
	// break;
	// // 卫星状态改变
	// case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	// mTextView_Message.setText("卫星状态改变");
	// // 获取当前状态
	// GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
	// // 获取卫星颗数的默认最大值
	// int maxSatellites = gpsStatus.getMaxSatellites();
	// // 创建一个迭代器保存所有卫星
	// Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
	// int count = 0;
	// // GpsSatellite s =null;
	// while (iters.hasNext() && count <= maxSatellites) {
	// iters.next();
	// count++;
	// }
	// mTextView_Message.setText("搜索到：" + count + "颗卫星");
	//
	// break;
	// // 定位启动
	// case GpsStatus.GPS_EVENT_STARTED:
	// mTextView_Message.setText("定位启动");
	// break;
	// // 定位结束
	// case GpsStatus.GPS_EVENT_STOPPED:
	// mTextView_Message.setText("定位结束");
	// break;
	// }
	//
	// }

	/**
	 * 返回查询条件
	 * 
	 * @return
	 */
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(false);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(false);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	/**
	 * 实时更新文本内容
	 * 
	 * @param location
	 */
	private void updateView(Location location) {
		this.location = location;
		sure = false;
		Message msg = new Message();
		msg.what = 7;// 成功
		handler.sendMessage(msg);

	}

	public Double parseDouble(String s) {
		if (!s.equals(null) && !"".equals(s))
			return Double.parseDouble(s);
		return 3333.3;
	}

	// 位置信息变化时触发
	@Override
	public void onLocationChanged(Location location) {
		updateView(location);
	}

	// gps状态变化时触发
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		// GPS状态为可见时
		case LocationProvider.AVAILABLE:
			mTextView_Message.setText("当前GPS状态为可见状态");
			break;
		// GPS状态为服务区外时
		case LocationProvider.OUT_OF_SERVICE:
			mTextView_Message.setText("当前GPS状态为服务区外状态");
			break;
		// GPS状态为暂停服务时
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			mTextView_Message.setText("当前GPS状态为暂停服务状态");
			break;
		}
	}

	// gps开启时触发
	@Override
	public void onProviderEnabled(String provider) {
		Location location = mLocationManager.getLastKnownLocation(provider);
		updateView(location);
	}

	// gps禁用时触发
	@Override
	public void onProviderDisabled(String provider) {
		updateView(null);
	}

	@Override
	protected void initVariable() {
		mTextView_Longitude = (TextView) findViewById(R.id.jd);
		mTextView_Latitude = (TextView) findViewById(R.id.wd);
		mTextView_Message = (TextView) findViewById(R.id.state);
		time = (TextView) findViewById(R.id.time);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		Intent intent = this.getIntent();
		pgdhId = intent.getStringExtra("派工单号");

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 为获取地理位置信息时设置查询条件
		bestProvider = mLocationManager.getBestProvider(getCriteria(), true);
		// 获取位置信息
		// 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
		Location location1 = mLocationManager
				.getLastKnownLocation(bestProvider);

		//
		WebDateModel yyt = DataCache.getinition().getYyt();
		JSONArray jsonArray = null;
		try {
			jsonArray = yyt.getJsonObject().getJSONArray("tableA");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject temp = jsonArray.getJSONObject(i);

				if (temp.getString("wlsjmc").equals(
						intent.getStringExtra("yyt"))) {
					// 营业厅坐标
					xzbf = parseDouble(temp.getString("xxx"));
					yzbf = parseDouble(temp.getString("yyy"));

					break;
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		updateView(location1);
	}

	@Override
	protected void initView() {
		title.setText("工程师到达确认");
		// showUserId.setText(DataCache.getUserId());

	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:

					dialogShowMessage("确认到达 ？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									sure = true;
									Config.getExecutorService().execute(
											new Runnable() {

												@Override
												public void run() {

													getWebService("submit");
												}
											});

								}
							}, null);

					break;

				case R.id.cancel:
					onBackPressed();
					break;

				case R.id.bt_topback:
					onBackPressed();
					break;
				//
				// case R.id.bt_toplogout:
				// skipActivity(LoginActivity.class);
				// break;
				}
			}
		};
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		topBack.setOnClickListener(onClickListener);
		// topLogout.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		if ("submit".equals(s)) {
			String sql = "update shgl_ywgl_fwbgszb set kzzd12='"
					+ mTextView_Longitude.getText() + ","
					+ mTextView_Latitude.getText()
					+ "',ddsj=sysdate where zbh='" + pgdhId + "'";
			JSONObject jsonObject;
			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_RZ", sql,
						DataCache.getinition().getUserId(), "uf_json_setdata",this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
	}

	public String formatDateTime(long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return dateFormat.format(date);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {

			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case SUCCESSFUL:
				dialogShowMessage_P("到达确认成功",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								skipActivity2(MainActivity.class);
							}

						});
				break;

			case FAIL:
				dialogShowMessage_P("到达确认失败，请检查后重试...", null);
				break;
			case 7:

				if (location != null) {
					time.setText(formatDateTime(location.getTime()));
					mTextView_Longitude.setText(String.valueOf(location
							.getLongitude()));
					mTextView_Latitude.setText(String.valueOf(location
							.getLatitude()));
				} else {
					time.setText("0000-00-00 00:00:00");
					mTextView_Longitude.setText("0");
					mTextView_Latitude.setText("0");
					mTextView_Message.setText("定位中……");
				}
//				if(){}else{}
				if (Math.abs(xzbf
						- parseDouble(mTextView_Longitude.getText().toString())) <= 0.00516
						&& Math.abs(yzbf
								- parseDouble(mTextView_Latitude.getText()
										.toString())) <= 0.004603) {
					// 在范围内
					mTextView_Message.setText("定位在营业厅范围内");
				} else {
					// 没在范围内
					mTextView_Message.setText("定位没在营业厅范围内");
				}
				Log.e("dd",xzbf+"  :   "+yzbf);
				break;
			}
			if (sure) {
				progressDialog.dismiss();
			}
		}

	};
}
