package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.AdapterView.OnItemClickListener;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;
/**
 * 巡检
 * @author zdkj
 *
 */
public class XjListActivity extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private String[] from;
	private int[] to;

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
		from = new String[] {"textView1", "faultuser", "zbh", "timemy", "datemy" };
		to = new int[] {R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
		
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {

				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					Intent intent = null;
					intent = new Intent(XjListActivity.this,XjShowActivity.class);
					startActivity(intent);
				}

			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				String sqlid = "_PAD_XJD_ZBCK";
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, DataCache.getinition().getUserId(),"uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = temp.getString("shgl_xjdzb_bzsj");
						item.put("textView1",getListItemIcon(i));
						item.put("faultuser",temp.getString("ccgl_wlsjb_wlsjmc"));
						item.put("sblx",temp.getString("shgl_xjdzb_sblx"));
						item.put("bzsj",timeff);
						timeff = timeff.substring(2);
						item.put("zbh", temp.get("shgl_xjdzb_zbh"));
						item.put("tv_1",temp.getString("ccgl_wlsjb_wlsjmc"));
						item.put("tv_2",temp.getString("shgl_xjdzb_xxdz"));
						item.put("tv_3",temp.getString("shgl_xjdzb_gzxx"));
						item.put("tv_4",temp.getString("shgl_xjdzb_lxdh"));
						item.put("tv_5",temp.getString("hpgl_jcsj_hplbb_hplbmc"));
						item.put("tv_6",temp.getString("main_jcsj_dqb_dqmc"));
						item.put("tv_7",temp.getString("main_jcsj_dqb_dqmc_1"));
						item.put("tv_8",temp.getString("main_jcsj_khjgb_khjgmc"));
						item.put("tv_9",temp.getString("shgl_xjdzb_mmjpxh"));
						item.put("tv_10",temp.getString("shgl_xjdzb_zdh"));
						item.put("tv_11",temp.getString("shgl_xjdzb_zgsl"));
						item.put("tv_12",temp.getString("shgl_xjdzb_fgsl"));
						item.put("sblx2",temp.getString("sblx2"));
						item.put("timemy",mdateformat(1, timeff));//时间
						item.put("datemy",mdateformat(0, timeff));// 年月日
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
			msg.what = FAIL;
			handler.sendMessage(msg);
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
				adapter = new SimpleAdapter(
						XjListActivity.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;
			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有派工单号",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						startActivity(intent);
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

}
