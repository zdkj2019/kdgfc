package com.fc.activity.esp;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;

/**
 * @author Van 派工信息接收的activity 2013年3月20日 14:30:57
 */
@SuppressLint("HandlerLeak")
public class DispatchingInformationReceivingActivity extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private int query_djzt = -2;

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

		from = new String[] { "customername", "oddnumber", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
		if (DataCache.getinition().getTitle().equals("工程师接收")) {
			query_djzt = 0;
		}
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		// showUserId.setText(DataCache.getUserId());
		// ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
		// .cancel(1);
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

				// case R.id.bt_toplogout:
				// skipActivity(LoginActivity.class);
				// break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		// topLogout.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {

				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {

					Intent intent = new Intent(
							DispatchingInformationReceivingActivity.this,
							DispatchingInformationConfirmActivity.class);
					// intent.putExtra("派工单号",
					// "" + data.get(index).get("oddnumber"));
					// intent.putExtra("客户电话", "" +
					// data.get(index).get("khdh"));
					// intent.putExtra("客户名称",
					// "" + data.get(index).get("customername"));
					// intent.putExtra("客户地址", "" + data.get(index).get("dz"));
					// intent.putExtra("手工单号", "" +
					// data.get(index).get("sgdh"));
					// intent.putExtra("派工部门", "" +
					// data.get(index).get("pgbm"));
					// intent.putExtra("派工时间",
					// "" + data.get(index).get("dispatchingtime"));
					// intent.putExtra("故障现象", "" +
					// data.get(index).get("gzxx"));
					// intent.putExtra("报障时间",
					// "" + data.get(index).get("faulttime"));
					// intent.putExtra("报障人", ""
					// + data.get(index).get("faultuser"));
					// intent.putExtra("联系电话", "" +
					// data.get(index).get("usertel"));
					// intent.putExtra("备注", "" + data.get(index).get("bz"));
					// intent.putExtra("移动编码", "" +
					// data.get(index).get("ydbm"));
					// intent.putExtra("设备编码", "" +
					// data.get(index).get("sbbm"));
					// intent.putExtra("设备类型", "" +
					// data.get(index).get("sblx"));
					// intent.putExtra("派工对象",
					// "" + data.get(index).get("dispatchinguser"));
					// intent.putExtra("处理方式", "" +
					// data.get(index).get("clfs"));
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
						"_PAD_SHGL_YWGL_XXJS", "2000-01-01*3000-01-01*"
								+ query_djzt + "*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");

				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						String timeff = temp
								.getString("shgl_ywgl_fwbgszb_bzsj");
						item.put("no", String.valueOf(i + 1));
						item.put("oddnumber",// 派工单号
								temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("customername",// 营业厅
								temp.getString("ccgl_wlsjb_khbm"));
						item.put("faulttime",// 时间
								temp.getString("shgl_ywgl_fwbgszb_bzsj"));
						item.put("faultuser",
								temp.getString("shgl_ywgl_fwbgszb_bzr"));
						item.put("khdh", temp.getString("ccgl_wlsjb_dh"));
						item.put("usertel",
								temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						item.put("dispatchinguser",
								temp.getString("ccgl_ygb_pgdx"));
						item.put("dispatchingtime",
								temp.getString("shgl_ywgl_fwbgszb_pgsj"));
						item.put("state",
								temp.getString("shgl_ywgl_fwbgszb_djzt"));
						item.put("clfs",
								temp.getString("shgl_ywgl_fwbgszb_clfs"));
						item.put("clr", temp.getString("shgl_ywgl_fwbgszb_czy"));
						item.put("sgdh",
								temp.getString("shgl_ywgl_fwbgszb_sgdh"));
						item.put("bz", temp.getString("shgl_ywgl_fwbgszb_bz"));
						item.put("dz", temp.getString("ccgl_wlsjb_dz"));
						item.put("pgbm", temp.getString("zzjgb_pz_pgbm"));
						item.put("gzxx",
								temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						item.put("ydbm",
								temp.getString("shgl_ywgl_fwbgssbb_ydbm"));
						item.put("sbbm",
								temp.getString("shgl_ywgl_fwbgssbb_sbbm"));
						item.put("sblx",
								temp.getString("main_jcsj_sblbb_sblbmc"));

						item.put("timemy",// 时间
								mdateformat(1, timeff));

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

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
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
						DispatchingInformationReceivingActivity.this,
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

								skipActivity2(MainActivity.class);
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
