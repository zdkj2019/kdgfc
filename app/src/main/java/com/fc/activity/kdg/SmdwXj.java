package com.fc.activity.kdg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.esp.AddParts;
import com.fc.activity.esp.ServiceReportsComplete;
import com.fc.activity.kdg.SmdwKdg.MyLocationListener;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.common.Constant;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;
import com.fc.utils.ImageUtil;
import com.fc.zxing.CaptureActivity;

/**
 * 巡检-上门定位
 * 
 * @author zdkj
 *
 */
public class SmdwXj extends FrameActivity {

	private TextView tv_time, tv_jd, tv_wd, tv_dz;
	private Button confirm, cancel;
	private String flag, zbh, msgStr,bzsj;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private boolean hasDw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_xj_smdw);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		bzsj = itemmap.get("bzsj").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("sbbm").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_jddz)).setText(itemmap.get("jddz").toString());
		// ((TextView)
		// findViewById(R.id.tv_fwgcs)).setText(itemmap.get("zxsname").toString());
		// iv_telphone.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Call(itemmap.get("zxstel").toString());
		// }
		// });
		/*
		 * 此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
		 * Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
		 */
		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
//					if(tv_jd.getText().toString().indexOf("4.9E")!=-1){
//						dialogShowMessage_P("定位失败，请到开阔地重试", null);
//						return;
//					}
//					long now = new Date().getTime();
//					long sj = DataUtil.StringToDate(bzsj).getTime()+15*60*1000;
//					if(now<sj){
//						toastShowMessage("时间未到，不能定位。");
//						return;
//					}
					
					if (hasDw) {
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("submit");
							}
						});
					} else {
						toastShowMessage("定位中，请稍后......");
					}
					break;
				case R.id.btn_sm:
					startSm();
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {
		
		if (s.equals("submit")) {// 提交
			try {

				String typeStr = "smdy";
				msgStr = "定位成功！";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += tv_jd.getText().toString();
				str += "*PAM*";
				str += tv_wd.getText().toString();
				str += "*PAM*";
				str += tv_dz.getText().toString();
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_XJ_ALL", str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					flag = json.getString("msg");
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}

	private void startSm() {
		// 二维码
		Intent intent = new Intent(getApplicationContext(),
				CaptureActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2 && data != null) {
			
		}
	}

	/*
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null) {
				return;
			} else {
				location = locations;
				Message msg = new Message();
				msg.what = Constant.NUM_7;// 成功
				handler.sendMessage(msg);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

	}

	/**
	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}
	
//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		startActivity(intent);
//		finish();
//	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(msgStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent  = getIntent();
								setResult(-1, intent);
								finish();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_7:
				tv_time.setText(location.getTime());
				tv_jd.setText("" + location.getLongitude());
				tv_wd.setText("" + location.getLatitude());
				tv_dz.setText("" + location.getAddrStr());
				mLocClient.stop();
				hasDw = true;
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}
