package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;

/**
 * 派工单明细查询
 * 
 * @author cheng
 */
@SuppressLint("HandlerLeak")
public class PGDquerychooseActivity extends FrameActivity {

	private String flag, time;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private String strxq = "like '%'",strdjzt = "like '%'",strzh = "like '%'";
	private int cx_time = 5;

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
		data = new ArrayList<Map<String, String>>();
		
		from = new String[] { "textView1","bzr", "zbh", "timemy", "datemy" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
		time = getIntent().getStringExtra("time");
		
		if (!backboolean) {
			
			Intent intent = getIntent();

			String zh = intent.getStringExtra("zh");
			String zt = intent.getStringExtra("zt");
			String xqbm = intent.getStringExtra("dh");
			cx_time = intent.getIntExtra("cx_time", 5);

			// <item>其他帐号</item>
			// <item>当前帐号</item>
			
//			 <item>当前帐号</item>
//	         <item>区域所有帐号</item>
			
			// </string-array>
			// <string-array name="strzt">
			// <item>全部工单</item>
			// <item>完结工单</item>
			// <item>未完结工单</item>

			if ("当前帐号".equals(zh)) {
				strzh = "= '" + DataCache.getinition().getUserId() + "'";
			} else {
				strzh = "like '%'";
			}

			if ("全部工单".equals(zt)) {

				strdjzt = "like '%'";
			} else if ("完结工单".equals(zt)) {

				strdjzt = ">='5'";
			} else if ("未完结工单".equals(zt)) {

				strdjzt = "<'5'";
			}

			if ("".equals(xqbm.trim())) {

				strxq = " in (select xqbm from GT_SHGL_JCSJ_XQRYPZ where rybm = (select ygbh from CCGL_YGB where pym = '"+DataCache.getinition().getUserId()+"'))";
			} else {

				strxq = "like '" + xqbm + "'";
			}
		}
		
	}

	@Override
	protected void initView() {
		title.setOnClickListener(null);
		title.setSelected(true);
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {
				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					Intent intent = new Intent(getApplicationContext(),
							PGDqueryActivity.class);
					startActivity(intent);
//					finish();
				}

			}
		});

	}

	@Override
	protected void getWebService(String s) {
		
		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SHGL_DZYWCX_PGDCX",
								"2013-6-28*2013-7-13" + "*" +cx_time+"*"
							 	+ strxq + "*"
								+ strdjzt + "*" 
								+ strzh + "*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						String timeff = temp.getString("bzsj");
						timeff = timeff.substring(2);
						item.put("textView1", getListItemIcon(i)+"");
						item.put("pgdx", temp.getString("pgdx"));
						item.put("fwpj", temp.getString("fwpj"));
						item.put("bzr", temp.getString("bzr"));
						item.put("ddsj", temp.getString("ddsj"));
						item.put("kzzd2", temp.getString("kzzd2"));
						item.put("pgbm", temp.getString("pgbm"));
						item.put("kzzd5", temp.getString("kzzd5"));
						item.put("czy", temp.getString("czy"));
						item.put("jdgs", temp.getString("jdgs"));
						item.put("zbh", temp.getString("zbh"));
						item.put("cljg", temp.getString("cljg"));
						item.put("djzt", temp.getString("djzt"));
						item.put("clfs", temp.getString("clfs"));
						item.put("bzsj", temp.getString("bzsj"));
						item.put("khxl", temp.getString("ds"));
						item.put("czsj", temp.getString("czsj"));
						item.put("gzxx", temp.getString("gzxx"));
						item.put("kzzd6", temp.getString("kzzd6"));
						item.put("sfcs", "");
						item.put("bzrlxdh", temp.getString("bzrlxdh"));
						item.put("khbm", temp.getString("khbm"));
						item.put("wcsj", temp.getString("wcsj"));
						
						item.put("timemy",// 时间
								mdateformat(1, timeff));

						item.put("datemy",// 年月日
								mdateformat(0, timeff));
						if(!"2".equals(temp.getString("clfs"))){
							data.add(item);
						}
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
				dialogShowMessage_P("你查询的时间段内，没有派工单号",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								skipActivity2(PGDCaseActivity.class);
								finish();
							}

						});
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

	@Override
	public void onBackPressed() {
		skipActivity2(PGDCaseActivity.class);
		finish();
	}

}
