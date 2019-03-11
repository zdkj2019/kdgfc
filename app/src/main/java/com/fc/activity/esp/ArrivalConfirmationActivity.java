package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.location.f;
import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;

/**
 * 派工信息到达确认
 * 
 * @author asus
 * 
 */
@SuppressLint("HandlerLeak")
public class ArrivalConfirmationActivity extends FrameActivity {

	private String flag, pgdhId;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private String[] from;
	private int[] to;
	private LocationManager alm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, Object>>();

		from = new String[] { "textView1", "faultuser", "oddnumber", "timemy",
				"datemy" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy };

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
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

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
					// gps为打开状态
					final int index = arg2;
					pgdhId = ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("oddnumber");

					Intent intent = new Intent(
							ArrivalConfirmationActivity.this,
							BaiDuGpsActivity.class);
					intent.putExtra("派工单号", pgdhId);
					intent.putExtra("xxx", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("xxx"));
					intent.putExtra("yyy", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("yyy"));
					intent.putExtra("slsj", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("slsj"));
					intent.putExtra("khbm", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("khbm"));
					intent.putExtra("bzr", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("faultuser"));

					intent.putExtra("zxsname", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("zxsname"));
					intent.putExtra("zxstel", ""
							+ ServiceReportCache.getObjectdata().get(index)
									.get("zxstel"));

					startActivity(intent);
					finish();
				} else {
					showmm();
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_PGDCZ", "2000-01-01*3000-01-01*" + 3 + "*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata", this);

				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						// {"shgl_ywgl_fwbgszb_kzzd15":"","shgl_ywgl_fwbgszb_kzzd6":"","shgl_ywgl_fwbgszb_kzzd7":"","shgl_ywgl_fwbgszb_kzzd8":"","shgl_ywgl_fwbgszb_kzzd14":"0","shgl_ywgl_fwbgszb_kzzd11":"15320506138","shgl_ywgl_fwbgszb_kzzd2":"","shgl_ywgl_fwbgszb_kzzd3":"","shgl_ywgl_fwbgszb_kzzd4":"","shgl_ywgl_fwbgszb_fwyj":"","shgl_ywgl_fwbgszb_kzzd10":"测试","shgl_ywgl_fwbgszb_jdr":"","shgl_ywgl_fwbgszb_kzzd5":"2","shgl_ywgl_fwbgszb_kzzd1":"","shgl_ywgl_fwbgszb_djzt":"3","shgl_ywgl_fwbgszb_jdsj":"","shgl_ywgl_fwbgszb_fwbg_wgsj":"","shgl_ywgl_fwbgszb_fwpj":"","shgl_ywgl_fwbgszb_pgdx":"00159","shgl_ywgl_fwbgszb_bzsj":"2015-02-27 08:45:53","shgl_ywgl_fwbgszb_zbh":"PG0000000003867","shgl_ywgl_fwbgszb_xgry":"0001","shgl_ywgl_fwbgszb_fwnr":"","ccgl_wlsjb_xxx":"106.5226310000000000","shgl_ywgl_fwbgszb_dz":"00000001","shgl_ywgl_fwbgszb_sfhf":"","shgl_ywgl_fwbgszb_lxdh":"2","shgl_ywgl_fwbgszb_fwfylx":"","shgl_ywgl_fwbgszb_jdgs":"001001","shgl_ywgl_fwbgszb_fwgcs":"测试","shgl_ywgl_fwbgszb_xgsj":"","shgl_ywgl_fwbgszb_ddsj":"","shgl_ywgl_fwbgszb_clfs":"1","shgl_ywgl_fwbgszb_sgdh":"管理员","shgl_ywgl_fwbgszb_gzxx":"显示异常。","shgl_ywgl_fwbgszb_wcsj":"","shgl_ywgl_fwbgszb_cfsj":"","shgl_ywgl_fwbgszb_slsj":"2015-02-27 08:46:48","shgl_ywgl_fwbgszb_kdzh":"","shgl_ywgl_fwbgszb_clr":"","shgl_ywgl_fwbgszb_sf":"000000000000019","ccgl_wlsjb_yyy":"29.61770600000000000","shgl_ywgl_fwbgszb_dh":"00000001","shgl_ywgl_fwbgszb_sr1":"0","shgl_ywgl_fwbgszb_sr2":"0","shgl_ywgl_fwbgszb_pgbm":"00000008","shgl_ywgl_fwbgszb_sr3":"0","shgl_ywgl_fwbgszb_sr4":"0","shgl_ywgl_fwbgszb_sr5":"0","ccgl_ygb_xm":"测试","shgl_ywgl_fwbgszb_czsj":"2015-02-27 08:45:53","shgl_ywgl_fwbgszb_bzrlxdh":"15320506138","main_jcsj_khjgb_khbm":"大渡口区（城区）","shgl_ywgl_fwbgszb_czy":"0001","shgl_ywgl_fwbgszb_bz":"","shgl_ywgl_fwbgszb_pgsj":"2015-02-27 08:46:09","shgl_ywgl_fwbgszb_cljg":"","ccgl_wlsjb_bzr":"小区1","shgl_ywgl_fwbgszb_khxxdz":"18号","main_jcsj_dqb_ds":"重庆"}
						item.put("textView1", getListItemIcon(i));
						item.put("oddnumber",//
								temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("faultuser", temp.getString("ccgl_wlsjb_bzr"));
						item.put("khbm",//
								temp.getString("main_jcsj_khjgb_khbm"));
						item.put("xxx", temp.getString("ccgl_wlsjb_xxx"));
						item.put("yyy", temp.getString("ccgl_wlsjb_yyy"));
						item.put("slsj",
								temp.getString("shgl_ywgl_fwbgszb_slsj"));
						item.put("zxsname", temp.getString("ccgl_ygb_xm"));// 服务咨询师
						item.put("zxstel", temp.getString("ccgl_ygb_lxdh"));// 服务咨询师电话
						String timeff = temp
								.getString("shgl_ywgl_fwbgszb_bzsj");
						timeff = timeff.substring(2);
						// item.put("faulttime", timeff);
						// item.put("faultuser",
						// temp.getString("shgl_ywgl_fwbgszb_bzr"));
						// item.put("usertel",
						// temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						// item.put("dispatchinguser",
						// temp.getString("ccgl_ygb_pgdx"));
						// item.put("dispatchingtime",

						// item.put("state",
						// temp.getString("shgl_ywgl_fwbgszb_djzt"));
						// item.put("clfs",
						// temp.getString("shgl_ywgl_fwbgszb_clfs"));
						// item.put("clr",
						// temp.getString("shgl_ywgl_fwbgszb_czy"));
						// item.put("sgdh",
						// temp.getString("shgl_ywgl_fwbgszb_sgdh"));
						// item.put("bz",
						// temp.getString("shgl_ywgl_fwbgszb_bz"));
						// item.put("dz", temp.getString("ccgl_wlsjb_dz"));
						// item.put("pgbm", temp.getString("zzjgb_pz_pgbm"));
						// item.put("gzxx",
						// temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						// item.put("ydbm",
						// temp.getString("shgl_ywgl_fwbgssbb_ydbm"));
						// item.put("sbbm",
						// temp.getString("shgl_ywgl_fwbgssbb_sbbm"));
						item.put("timemy", mdateformat(1, timeff));// 时间
						item.put("datemy", mdateformat(0, timeff));// 年月日
						data.add(item);
					}
					ServiceReportCache.setObjectdata(data);
					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
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

		} else {
			Message msg = new Message();
			msg.what = SUCCESSFUL;// 成功
			handler.sendMessage(msg);
		}

	}

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
		finish();
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
				adapter = new SimpleAdapter(ArrivalConfirmationActivity.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				openGPSSettings();
				break;

			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有派工单号",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}

						});
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

	public void openGPSSettings() {
		alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			showmm();
		}

	}

	public void showmm() {
		dialogShowMessage("您的GPS没有打开，点击确认进入GPS设置界面",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(intent, 0);
					}

				}, null);
	}
}
