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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.common.Constant;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;
import com.fc.utils.ImageUtil;
import com.fc.zxing.CaptureActivity;

/**
 * 快递柜-上门定位
 * 
 * @author zdkj
 *
 */
public class SmdwKdgWx extends FrameActivity {

	private TextView tv_time, tv_jd, tv_wd, tv_dz;
	private Button confirm, cancel;
	private Spinner spinner_smfs;
	private ImageView iv_telphone;
	private List<Map<String, String>> data_smfs;
	private String flag, zbh, message,bzsj,lxdh,ddh;
	private String[] from;
	private int[] to;
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
		appendMainBody(R.layout.activity_kdg_smdw);
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
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		spinner_smfs = (Spinner) findViewById(R.id.spinner_smfs);
		
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		data_smfs = new ArrayList<Map<String, String>>();
		Map<String, String> item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "");
		data_smfs.add(item);
		item = new HashMap<String, String>();
		item.put("id", "1");
		item.put("name", "人工上门");
		data_smfs.add(item);
		item = new HashMap<String, String>();
		item.put("id", "2");
		item.put("name", "电话完工");
		data_smfs.add(item);
		SimpleAdapter adapter = new SimpleAdapter(SmdwKdgWx.this, data_smfs,
				R.layout.spinner_item, from, to);
		spinner_smfs.setAdapter(adapter);

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		ddh = itemmap.get("ddh").toString();
		bzsj = itemmap.get("bzsj").toString();
		lxdh = itemmap.get("lxdh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(itemmap.get("axdh").toString());
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("ddh_mc").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("lxdh").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("dygdh1_mc").toString());
		((TextView) findViewById(R.id.tv_6)).setText(bzsj);
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("yqsx").toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("kzzf6").toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("kzzf7").toString());
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("kzzf8").toString());
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("gzxx").toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("kzsz1").toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("bz").toString());
		((TextView) findViewById(R.id.tv_jddz)).setText(itemmap.get("jddz").toString());
		
		if("1".equals(ddh)){
			findViewById(R.id.ll_1).setVisibility(View.VISIBLE);
		}
		if("3".equals(ddh)){
			findViewById(R.id.ll_2).setVisibility(View.VISIBLE);
		}
		
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
//					String smfsbm = data_smfs.get(spinner_smfs.getSelectedItemPosition()).get("id");
//					if ("".equals(smfsbm)) {
//						toastShowMessage("请选择上门方式");
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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(lxdh)) {
					toastShowMessage("请选择联系电话！");
					return;
				}
				Call(lxdh);
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {

				String typeStr = "smdy";
				message = "定位成功！";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId();
				str += "*PAM*";
				str += tv_jd.getText().toString();
				str += "*PAM*";
				str += tv_wd.getText().toString();
				str += "*PAM*";
				str += tv_dz.getText().toString();

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_ALL", str, typeStr, typeStr,
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(message,
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
//
//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}