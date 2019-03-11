package com.fc.activity.esp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;
import com.fc.utils.GPS;
import com.fc.utils.WebDateModel;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class BaiDuGpsActivity extends FrameActivity {
	private TextView mTextView_Longitude;
	private TextView mTextView_Latitude;
	private TextView mTextView_Message;
	private TextView tv_fwgcs;
	private ImageView iv_telphone;
	private TextView time;
	private Button confirm, cancel;
	private String pgdhId, flag;
	private Intent intent;
	Double xzbf = 3333.3;
	Double yzbf = 3333.3;
	boolean sure = false;
	private BDLocation location;
	private String slsj, xxx, yyy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_showgps);
		initVariable();
		initView();
		initListeners();

	}

	public LocationClient mLocClient;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void initVariable() {
		mTextView_Longitude = (TextView) findViewById(R.id.jd);
		mTextView_Latitude = (TextView) findViewById(R.id.wd);
		mTextView_Message = (TextView) findViewById(R.id.state);
		tv_fwgcs = (TextView) findViewById(R.id.tv_fwgcs);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		time = (TextView) findViewById(R.id.time);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		intent = this.getIntent();
		pgdhId = intent.getStringExtra("派工单号");
		slsj = intent.getStringExtra("slsj");
		xxx = intent.getStringExtra("xxx");
		yyy = intent.getStringExtra("yyy");
		tv_fwgcs.setText(intent.getStringExtra("zxsname"));
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(intent.getStringExtra("zxstel"));

			}
		});
		/*
		 * 此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
		 * Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
		 */
		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();

	}

	@Override
	protected void initView() {
		title.setText("工程师到达确认");
		getyytzb();// 营业厅在服务器中的坐标
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:

					boolean flag = false;
					// 不满15分钟不能提交
					Date now = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					Date pgdate;
					try {
						pgdate = sdf.parse(slsj);
						long time = now.getTime() - pgdate.getTime();
						long gd_time = 15 * 60 * 1000;
						if (time > gd_time) {
							// 计算距离 方圆200m内才能定位
							double from_lat = Double
									.parseDouble(mTextView_Longitude.getText()
											+ "");
							double from_lng = Double
									.parseDouble(mTextView_Latitude.getText()
											+ "");
							double to_lat = Double.parseDouble(xxx + "");
							double to_lng = Double.parseDouble(yyy + "");
							double s = GPS.getDistance(from_lat, from_lng,
									to_lat, to_lng);
							// if (s > 200) {
							// flag = false;
							// dialogShowMessage_P("请到达后定位！", null);
							// } else {
							// flag = true;
							// }
							flag = true;
						} else {
							dialogShowMessage_P("暂时无法定位！", null);
						}

						if (flag) {
							dialogShowMessage("确认到达 ？",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface face,
												int paramAnonymous2Int) {
											showProgressDialog();
											// sure = true;
											Config.getExecutorService()
													.execute(new Runnable() {

														@Override
														public void run() {

															getWebService("submit");
														}
													});

										}
									}, null);
						}

					} catch (ParseException e) {
						Message msg = new Message();
						msg.what = FAIL;// 失败
						handler.sendMessage(msg);
						e.printStackTrace();
					}

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
	}

	String daoda = "定位未到达";

	@Override
	protected void getWebService(String s) {
		if ("submit".equals(s)) {
			String czrz3 = "'0'||chr(42)||'"
					+ DataCache.getinition().getUserId() + "'||chr(42)||'"
					+ new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss") + "'";
			String sql = "update shgl_ywgl_fwbgszb set kzzd12='"
					+ mTextView_Longitude.getText() + ","
					+ mTextView_Latitude.getText() + "," + daoda
					+ "',ddsj=sysdate,djzt=3.5,czrz35=" + czrz3
					+ " where zbh='" + pgdhId + "'";
			JSONObject jsonObject;
			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_RZ", sql,
						DataCache.getinition().getUserId(), "uf_json_setdata",
						this);
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

	/*
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null)
				return;
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("time : ");
			// sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(location.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(location.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(location.getRadius());
			// if (location.getLocType() == BDLocation.TypeGpsLocation) {
			// sb.append("\nspeed : ");
			// sb.append(location.getSpeed());
			// sb.append("\nsatellite : ");
			// sb.append(location.getSatelliteNumber());
			// } else if (location.getLocType() ==
			// BDLocation.TypeNetWorkLocation) {
			// sb.append("\naddr : ");
			// sb.append(location.getAddrStr());
			// }
			//
			// // logMsg(sb.toString());
			// Log.d("dd", sb.toString());
			location = locations;
			Message msg = new Message();
			msg.what = 7;// 成功
			handler.sendMessage(msg);
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// if (poiLocation == null) {
			// return;
			// }//poi这里不需要
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("Poi time : ");
			// sb.append(poiLocation.getTime());
			// sb.append("\nerror code : ");
			// sb.append(poiLocation.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(poiLocation.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(poiLocation.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(poiLocation.getRadius());
			// if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
			// sb.append("\naddr : ");
			// sb.append(poiLocation.getAddrStr());
			// }
			// if (poiLocation.hasPoi()) {
			// sb.append("\nPoi:");
			// sb.append(poiLocation.getPoi());
			// } else {
			// sb.append("noPoi information");
			// }
			// Log.d("dd", sb.toString());
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/**
	 * 服务器上营业厅的坐标
	 */
	private void getyytzb() {

		if ("".equals(intent.getStringExtra("khbm"))) {
			toastShowMessage("取得坐标为空");
			return;
		} else {
			xzbf = parseDouble(intent.getStringExtra("xxx"));
			yzbf = parseDouble(intent.getStringExtra("yyy"));
		}
	}

	public Double parseDouble(String s) {
		if (s != null && !"".equals(s))
			return Double.parseDouble(s);
		return 3333.3;
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
								onBackPressed();
							}

						});
				// if (sure) {
				progressDialog.dismiss();
				// }
				break;

			case FAIL:
				dialogShowMessage_P("到达确认失败，请检查后重试...", null);
				break;

			case 7:
				// if (location != null) {
				time.setText(location.getTime());
				mTextView_Longitude.setText("" + location.getLongitude());
				mTextView_Latitude.setText("" + location.getLatitude());

				// } else {
				// time.setText("0000-00-00 00:00:00");
				// mTextView_Longitude.setText("0");
				// mTextView_Latitude.setText("0");
				// // mTextView_Message.setText("定位中……");
				// }
				// xzbf = 106.522804;
				// yzbf = 29.61751;
				if (xzbf == 0.0 && yzbf == 0.0) {
					mTextView_Message.setText("服务端经纬度为0");

				} else {
					// 则小于0在范围内
					Double jd = Math.abs(xzbf - location.getLongitude()) - 0.00516;// 经度差-范围
					Double wd = Math.abs(yzbf - location.getLatitude()) - 0.004603;// 维度差-范围

					if (jd <= 0.0 && wd <= 0.0) {
						// 在范围内
						daoda = "定位到达";
						mTextView_Message.setText(daoda);
						// mLocClient.stop();
					} else {
						// 没在范围内
						daoda = "定位未到达";
						mTextView_Message.setText(daoda);
					}
					// Log.e("dd", "经纬度范围差：" + jd + "  :   " + wd);
				}
				break;
			}

		}

	};

	/**
	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		// option.setAddrType("all");// 返回的定位信息包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		// option.setPoiNumber(5); // 最多返回POI个数
		// option.setPoiDistance(1000); // poi查询距离
		// option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();

		// /*
		// * 发起定位请求。请求过程是异步的，定位结果在上面的监听函数的onReceiveLocation中获取。
		// */
		// if (mLocClient != null && mLocClient.isStarted()) {
		// int i = mLocClient.requestLocation();
		// Log.d("dd", "" + i);
		// } else
		// Log.d("dd", "locClient is null or not started");
		// /*
		// * 发起POI查询请求。请求过程是异步的，定位结果在上面的监听函数onReceivePoi中获取
		// */
		// if (mLocClient != null && mLocClient.isStarted())
		// mLocClient.requestPoi();

	}

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}

}
