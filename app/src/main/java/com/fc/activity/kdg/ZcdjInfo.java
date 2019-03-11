package com.fc.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.login.LoginActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.common.Constant;
import com.fc.utils.Config;
import com.fc.utils.ImageUtil;
import com.fc.zxing.CaptureActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 新增设备信息
 * 
 * @author Administrator 20170410
 */
@SuppressLint("HandlerLeak")
public class ZcdjInfo extends FrameActivity {

	private TextView tv_pz;
	private EditText et_sbid, et_gjbm, et_zgccbm, et_xxdz,et_jddz,et_gks,et_tfzs,et_bz;
	private Button confirm, cancel, btn_sm;
	private Spinner spinner_sf, spinner_ds, spinner_qx,spinner_wdmc,spinner_sfdzs,spinner_sblx,spinner_sccs;
	private ArrayList<Map<String, String>> data_sf, data_ds, data_qx,data_wdmc,data_sfdzs,data_sblx,data_sccs,data_zp;
	private ArrayList<String> list_photo;
	private String[] from;
	private int[] to;
	private SimpleAdapter adapter;
	private String sfbm, dsbm, qxbm, sbid, wdbm,hpbm,sbbm,sblxbm,sccsbm,msgStr = "";
	
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
		appendMainBody(R.layout.activity_kdg_zcdjinfo);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("getsf");
			}
		});

	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

		tv_pz = (TextView) findViewById(R.id.tv_pz);
		et_sbid = (EditText) findViewById(R.id.et_sbid);
		et_gjbm = (EditText) findViewById(R.id.et_gjbm);
		et_zgccbm = (EditText) findViewById(R.id.et_zgccbm);
		et_xxdz = (EditText) findViewById(R.id.et_xxdz);
		et_jddz = (EditText) findViewById(R.id.et_jddz);
		et_gks = (EditText) findViewById(R.id.et_gks);
		et_tfzs = (EditText) findViewById(R.id.et_tfzs);
		et_bz = (EditText) findViewById(R.id.et_bz);

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		spinner_sf = (Spinner) findViewById(R.id.spinner_sf);
		spinner_ds = (Spinner) findViewById(R.id.spinner_ds);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		spinner_wdmc = (Spinner) findViewById(R.id.spinner_wdmc);
		spinner_sfdzs = (Spinner) findViewById(R.id.spinner_sfdzs);
		spinner_sblx = (Spinner) findViewById(R.id.spinner_sblx);
		spinner_sccs = (Spinner) findViewById(R.id.spinner_sccs);
		btn_sm = (Button) findViewById(R.id.btn_sm);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		
		data_sfdzs = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "1");
		map.put("name", "是");
		data_sfdzs.add(map);
		map = new HashMap<String, String>();
		map.put("id", "2");
		map.put("name", "否");
		data_sfdzs.add(map);
		
		adapter = new SimpleAdapter(ZcdjInfo.this, data_sfdzs,
				R.layout.spinner_item, from, to);
		spinner_sfdzs.setAdapter(adapter);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());
		try {
			sbid = itemmap.get("kzzf1").toString();
		} catch (Exception e) {
			
		}
		if(sbid==null||"".equals(sbid)){
			
		}else{
			btn_sm.setEnabled(false);
			btn_sm.setBackgroundDrawable(null);
		}
		
		hpbm = itemmap.get("hpbm").toString();
		sbbm = itemmap.get("sbbm").toString();
		sfbm = itemmap.get("sf").toString();
		dsbm = itemmap.get("ds").toString();
		qxbm = itemmap.get("qx").toString();
		wdbm = itemmap.get("wdbmwd").toString();
		sblxbm = itemmap.get("sblx").toString();
		sccsbm = itemmap.get("sccs").toString();
		et_sbid.setText(sbid);
		et_gjbm.setText(sbbm);
		et_zgccbm.setText(itemmap.get("kzzf3").toString());
		et_xxdz.setText(itemmap.get("xxdz").toString());
		et_jddz.setText(itemmap.get("jddz").toString());
		et_gks.setText(itemmap.get("kzsz3").toString());
		et_tfzs.setText(itemmap.get("kzsz4").toString());
		et_bz.setText(itemmap.get("bz").toString());
		String dypsdh = itemmap.get("dypsdh").toString();
		if("1".equals(dypsdh)){
			spinner_sfdzs.setSelection(0);
		}else{
			spinner_sfdzs.setSelection(1);
		}
		
		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

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
					if (DataCache.getinition().getUserId() == null) {
						dialogShowMessage_P("登陆超时，请重新登陆",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface face,
											int paramAnonymous2Int) {
										Intent intent = new Intent(
												getApplicationContext(),
												LoginActivity.class);
										startActivity(intent);
										finish();
									}
								});
						return;
					}
					if (!isNotNull(et_sbid)) {
						toastShowMessage("请扫描二维码");
						return;
					}
					sccsbm = data_sccs.get(spinner_sccs.getSelectedItemPosition())
							.get("id");
					if ("".equals(sccsbm)) {
						toastShowMessage("请选择生产厂商");
						return;
					}
					sblxbm = data_sblx.get(spinner_sblx.getSelectedItemPosition())
							.get("id");
					if ("".equals(sblxbm)) {
						toastShowMessage("请选择设备类型");
						return;
					}
					
					sfbm = data_sf.get(spinner_sf.getSelectedItemPosition())
							.get("id");
					if ("".equals(sfbm)) {
						toastShowMessage("请选择省份");
						return;
					}
					dsbm = data_ds.get(spinner_ds.getSelectedItemPosition())
							.get("id");
					if ("".equals(dsbm)) {
						toastShowMessage("请选择地市");
						return;
					}
					qxbm = data_qx.get(spinner_qx.getSelectedItemPosition())
							.get("id");
					if ("".equals(qxbm)) {
						toastShowMessage("请选择区县");
						return;
					}
					wdbm = data_wdmc.get(
							spinner_wdmc.getSelectedItemPosition()).get(
							"id");
					if ("".equals(wdbm)) {
						toastShowMessage("请选择网点名称");
						return;
					}
					if (!isNotNull(et_jddz)) {
						toastShowMessage("请录入街道地址");
						return;
					}
					if (!isNotNull(et_xxdz)) {
						toastShowMessage("请录入投放地址");
						return;
					}
					if (!isNotNull(et_gks)) {
						toastShowMessage("请录入格口数");
						return;
					}
					if (!isNotNull(et_tfzs)) {
						toastShowMessage("请录入投放组数");
						return;
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							getWebService("submit");
						}
					});
					break;
				case R.id.btn_sm:
					startSm();
					break;
				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		btn_sm.setOnClickListener(onClickListener);

		spinner_sf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sfbm = data_sf.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getds");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_ds.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				dsbm = data_ds.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getqx");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_qx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				qxbm = data_qx.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {

						getWebService("getwdmc");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_wdmc.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				wdbm = data_wdmc.get(position).get("id");
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		
		

		tv_pz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(5, list_photo);
			}
		});
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
			// 二维码
			sbid = data.getStringExtra("result").trim();
			Message msg = new Message();
			msg.what = Constant.NUM_11;
			handler.sendMessage(msg);
		}
		if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
			list_photo = data.getStringArrayListExtra("imglist");
			if (list_photo != null) {
				if (list_photo.size() > 0) {
					tv_pz.setText("继续选择");
					tv_pz.setBackgroundResource(R.drawable.btn_normal_yellow);
				} else {
					tv_pz.setText("选择图片");
					tv_pz.setBackgroundResource(R.drawable.btn_normal);
				}
			}

		}

		if (requestCode == 7 && resultCode == 1) {
			onBackPressed();
		}
	}

	private void upload() {
		try {
			boolean flag = true;
			for (int i = 0; i < list_photo.size(); i++) {
				if (flag) {
					String filepath = list_photo.get(i);
					if(filepath.indexOf(Constant.ImgPath)==-1){
						filepath = filepath.substring(7, filepath.length());
						// 压缩图片到100K
						filepath = ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()), 200, "jpg");
						File file = new File(filepath);
						flag = uploadPic(i + "", sbid, readJpeg(file),
								"c#_PAD_ERP_CCGL_SBZP");
						file.delete();
					}
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = Constant.SUCCESS;
				handler.sendMessage(msg);
			} else {
				msgStr = "上传图片失败";
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

	protected boolean uploadPic(String num, String mxh, final byte[] data1,
			String sqlid) throws Exception {

		if (data1 != null && mxh != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(sqlid,
					num, mxh, "0001", data1, "uf_json_setdata2_p11",
					getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	protected void getWebService(String s) {

		if ("getsf".equals(s)) {
			try {
				data_sf = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_sf.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX1", "", "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("sfbm"));
						item.put("name", temp.getString("sfmc"));
						data_sf.add(item);
					}
					
					data_sblx = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_sblx.add(item);
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_NT_SBLX", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_sblx.add(item);
						}
					} else {
						msgStr = "获取设备类型失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
					
					data_sccs = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_sccs.add(item);
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_NT_SBCS", "", "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_sccs.add(item);
						}
					} else {
						msgStr = "获取生产厂商失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
					
					
					data_zp = new ArrayList<Map<String, String>>();
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_SHGL_SBXX_ZPCX", hpbm, "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("mxh", "1");
							item.put("tzz", temp.getString("tzz"));
							item.put("path", temp.getString("path"));
							data_zp.add(item);
						}
					}
					
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);
				} else {
					msgStr = "获取省份信息失败";
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}
		
		if ("getds".equals(s)) {
			try {
				data_ds = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_ds.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX2", sfbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("dsbm"));
						item.put("name", temp.getString("dsmc"));
						data_ds.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getqx".equals(s)) {
			try {
				data_qx = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_qx.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_DQXX3", dsbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("qxbm"));
						item.put("name", temp.getString("qxmc"));
						data_qx.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if ("getwdmc".equals(s)) {
			try {
				data_wdmc = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_wdmc.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGL_SBLR_WDXX", qxbm, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("wdbm"));
						item.put("name", temp.getString("wdmc"));
						item.put("kzzf5", temp.getString("kzzf5"));
						item.put("xxdz", temp.getString("xxdz"));
						data_wdmc.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_10;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		
		
		if ("submit".equals(s)) {

			try {
				String str = "";
				str += hpbm + "*PAM*";
				str += et_sbid.getText().toString() + "*PAM*";
				str += sbbm + "*PAM*";
				str += et_zgccbm.getText().toString().trim() + "*PAM*";
				str += data_sfdzs.get(spinner_sfdzs.getSelectedItemPosition()).get("id") + "*PAM*";
				str += qxbm + "*PAM*";
				str += wdbm + "*PAM*";
				str += et_xxdz.getText().toString().trim() + "*PAM*";
				str += et_gks.getText().toString().trim() + "*PAM*";
				str += et_tfzs.getText().toString().trim() + "*PAM*";
				str += et_bz.getText().toString().trim() + "*PAM*";
				str += location.getLongitude() + "*PAM*";
				str += location.getLatitude() + "*PAM*";
				str += location.getAddrStr() + "*PAM*";
				str += DataCache.getinition().getUserId()+ "*PAM*";
				str += sblxbm+ "*PAM*";
				str += sccsbm+ "*PAM*";
				str += et_jddz.getText().toString().trim();
						
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_KDG_SBXXLY", str, "", "", "uf_json_setdata2",
						this);
				String flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					if (list_photo != null) {
						upload();
					} else {
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					}
				} else {
					msgStr = json.getString("msg");
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
				msg.what = Constant.NUM_9;// 成功
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

	private void loadZp(){
		if(data_zp.size()>0){
			for (int i = 0; i < data_zp.size(); i++) {
				Map<String, String> map =  data_zp.get(i);
				final String mxh = map.get("mxh");
				String tzz = map.get("tzz");
				String path = map.get("path");
				try {
					if(!"".equals(path)){
						String[] paths = path.split(",");
						list_photo = new ArrayList<String>();
						for(int n=0;n<paths.length;n++){
							list_photo.add(Constant.ImgPath+tzz+"/"+paths[n]);
						}
						tv_pz.setText("继续选择");
						tv_pz.setBackgroundResource(R.drawable.btn_normal_yellow);
					}
				} catch (Exception e) {
					
				}
			}
		}
		tv_pz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_pz = (TextView) v;
				ArrayList<String> list = list_photo;
				camera(4, list);
			}
		});
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + msgStr, null);
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P("提交成功！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_6:
				adapter = new SimpleAdapter(ZcdjInfo.this, data_sf,
						R.layout.spinner_item, from, to);
				spinner_sf.setAdapter(adapter);
				if (sfbm != null && !"".equals(sfbm)) {
					for (int i = 0; i < data_sf.size(); i++) {
						if (sfbm.equals(data_sf.get(i).get("id"))) {
							spinner_sf.setSelection(i);
						}
					}
				}
				
				adapter = new SimpleAdapter(ZcdjInfo.this, data_sblx,
						R.layout.spinner_item, from, to);
				spinner_sblx.setAdapter(adapter);
				if (sblxbm != null && !"".equals(sblxbm)) {
					for (int i = 0; i < data_sblx.size(); i++) {
						if (sblxbm.equals(data_sblx.get(i).get("id"))) {
							spinner_sblx.setSelection(i);
						}
					}
				}
				
				adapter = new SimpleAdapter(ZcdjInfo.this, data_sccs,
						R.layout.spinner_item, from, to);
				spinner_sccs.setAdapter(adapter);
				if (sccsbm != null && !"".equals(sccsbm)) {
					for (int i = 0; i < data_sccs.size(); i++) {
						if (sccsbm.equals(data_sccs.get(i).get("id"))) {
							spinner_sccs.setSelection(i);
						}
					}
				}
				loadZp();
				break;
			case Constant.NUM_7:
				adapter = new SimpleAdapter(ZcdjInfo.this, data_ds,
						R.layout.spinner_item, from, to);
				spinner_ds.setAdapter(adapter);
				if (dsbm != null && !"".equals(dsbm)) {
					for (int i = 0; i < data_ds.size(); i++) {
						if (dsbm.equals(data_ds.get(i).get("id"))) {
							spinner_ds.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_8:
				adapter = new SimpleAdapter(ZcdjInfo.this, data_qx,
						R.layout.spinner_item, from, to);
				spinner_qx.setAdapter(adapter);
				if (qxbm != null && !"".equals(qxbm)) {
					for (int i = 0; i < data_qx.size(); i++) {
						if (qxbm.equals(data_qx.get(i).get("id"))) {
							spinner_qx.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_9:
				
				break;
			case Constant.NUM_10:
				adapter = new SimpleAdapter(ZcdjInfo.this, data_wdmc,
						R.layout.spinner_item, from, to);
				spinner_wdmc.setAdapter(adapter);
				if (wdbm != null && !"".equals(wdbm)) {
					for (int i = 0; i < data_wdmc.size(); i++) {
						if (wdbm.equals(data_wdmc.get(i).get("id"))) {
							spinner_wdmc.setSelection(i);
						}
					}
				}
				break;
			case Constant.NUM_11:
				et_sbid.setText(sbid);
				break;
			case Constant.NUM_12:
				
				break;
			case Constant.NUM_13:
				
				break;
			}

		}

	};
}