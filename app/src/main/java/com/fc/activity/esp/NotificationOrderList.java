package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.login.LoginActivity;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;

/**
 * 提醒工单列表
 * 
 * @author wlj
 */
public class NotificationOrderList extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private SharedPreferences spf;

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

		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(1);
		
		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, String>>();

		from = new String[] { "bzr", "zbh", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
//		time = getIntent().getStringExtra("time");
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
	}

	@Override
	protected void initView() {
		title.setText("工单提醒");
		
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {
				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					Intent intent = new Intent(getApplicationContext(),
							NotificationOrderActivity.class);
					startActivity(intent);
					finish();
				}

			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_TIS_YY_SM2", spf.getString("userId", "")+"*"+spf.getString("userId", ""), "uf_json_getdata",this);
				
				flag = jsonObject.getString("flag");
				Log.e("NotificationOrderList", jsonObject+"");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						String timeff = temp.getString("bzsj");

						item.put("zbh", temp.getString("zbh"));
						item.put("txbs", temp.getString("txbs"));
						item.put("kzzd8", temp.getString("kzzd8"));
						item.put("bzsj", timeff);
						item.put("gdyysj", temp.getString("gdyysj"));
						item.put("dqsj", temp.getString("dqsj"));
						item.put("khbm", temp.getString("khbm"));
						item.put("kzzd5", temp.getString("kzzd5"));
						item.put("bzr", temp.getString("bzr"));
						item.put("bzrlxdh", temp.getString("bzrlxdh"));
						item.put("kdzh", temp.getString("kdzh"));
						item.put("gzxx", temp.getString("gzxx"));
						item.put("khxxdz", temp.getString("khxxdz"));
						item.put("txzd", temp.getString("txzd"));
						
						item.put("timemy",// 时间
								mdateformat(1, timeff));
						timeff = timeff.substring(2);
						item.put("datemy",// 年月日
								mdateformat(0, timeff));
						    
						data.add(item);
					}
					ServiceReportCache.setData(data);
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
			// 直接加载
			Message msg = new Message();
			msg.what = SUCCESSFUL;// 成功
			handler.sendMessage(msg);
		}
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
				adapter = new SimpleAdapter(getApplicationContext(),
						ServiceReportCache.getData(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;

			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有派工单号",null);
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};
	@Override
	public void onBackPressed() {
		finish();
	};
}
