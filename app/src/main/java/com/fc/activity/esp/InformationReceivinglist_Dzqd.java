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
 *  电子抢单
 * @author wlj
 *
 */
@SuppressLint("HandlerLeak")
public class InformationReceivinglist_Dzqd extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private int query_djzt = -1;

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

		from = new String[] { "faultuser", "oddnumber", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
		
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

					Intent intent = new Intent(
							InformationReceivinglist_Dzqd.this,
							InformationReceivingActivity_Dzqd.class);
					startActivity(intent);
					finish();
				}

			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {//_PAD_SHGL_PGDCZ

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_GDQD_CX1",DataCache.getinition().getUserId(),"uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
			
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
/**
 *{"shgl_ywgl_fwbgszb_khbm":"00000001","shgl_ywgl_fwbgszb_bzrlxdh":"123","shgl_ywgl_fwbgszb_kzzd11":"123","zzjgb_pz_pgbm":"aaa",
 *"shgl_ywgl_fwbgszb_kzzd10":"ss","shgl_ywgl_fwbgszb_kzzd5":"2","ccgl_wlsjb_cx":"城区",
 *"shgl_ywgl_fwbgszb_fwgcs":"aaa","shgl_ywgl_fwbgszb_bz":"55","shgl_ywgl_fwbgszb_djzt":"2",
 *"shgl_ywgl_fwbgszb_sgdh":"55","shgl_ywgl_fwbgszb_gzxx":"44","shgl_ywgl_fwbgszb_pgdx":"00001",
 *"shgl_ywgl_fwbgszb_bzsj":"2014-10-16 16:06:35","ccgl_wlsjb_bzr":"a小区2",
 *"shgl_ywgl_fwbgszb_khxxdz":"*","shgl_ywgl_fwbgszb_kzzd18":"","shgl_ywgl_fwbgszb_zbh":"PG0000000000031",
 *"main_jcsj_khjgb_khbmmc":"a","main_jcsj_dqb_ds":"解放碑","dzpt_htfyxmb_jbf_jg":"150.0000000000000000"}
 */
						String timeff = temp.getString("shgl_ywgl_fwbgszb_bzsj");
						timeff = timeff.substring(2);
						item.put("no", String.valueOf(i + 1));
						item.put("oddnumber",temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("faultuser", temp.getString("ccgl_wlsjb_bzr"));//w网点
						item.put("usertel", temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						item.put("kzzd5", temp.getString("shgl_ywgl_fwbgszb_kzzd5"));
						item.put("khbm", temp.getString("shgl_ywgl_fwbgszb_khbm"));
						item.put("khbmmc", temp.getString("main_jcsj_khjgb_khbmmc"));
						item.put("ds", temp.getString("main_jcsj_dqb_ds"));
						item.put("khxxdz", temp.getString("shgl_ywgl_fwbgszb_khxxdz"));
						item.put("fbdw", temp.getString("zzjgb_pz_pgbm"));
						item.put("bz", temp.getString("shgl_ywgl_fwbgszb_bz"));
						item.put("gzxx", temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						
						item.put("smsf", temp.getString("shgl_ywgl_fwbgszb_fwgcs"));//上门身份
						item.put("kfxm", temp.getString("shgl_ywgl_fwbgszb_sgdh"));//客户姓名
						//item.put("gdfy", temp.getString("dzpt_htfyxmb_jbf_jg"));//客户姓名
						item.put("gdfy", "0");
						
						item.put("cx", temp.getString("ccgl_wlsjb_cx"));//结算位置
						item.put("kzzd10", temp.getString("shgl_ywgl_fwbgszb_kzzd10"));//发包人
						item.put("kzzd11", temp.getString("shgl_ywgl_fwbgszb_kzzd11"));//发包人手机
						item.put("kzzd18", temp.getString("shgl_ywgl_fwbgszb_kzzd18"));//服务要求
						item.put("clfs", temp.getString("shgl_ywgl_fwbgszb_clfs"));//clfs
						
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
						InformationReceivinglist_Dzqd.this,
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

}
