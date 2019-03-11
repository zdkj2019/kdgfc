package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
 * 时间预约
 * @author wlj
 */
@SuppressLint("HandlerLeak")
public class TimeYuyuelist extends FrameActivity {

	private String flag, mtitle;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private int query_djzt = 1;

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

		from = new String[] { "khbm", "oddnumber", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };

	}

	@Override
	protected void initView() {
		mtitle = DataCache.getinition().getTitle();
		title.setText(mtitle);

	}

	@Override
	protected void initListeners() {
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {
//				Intent intent = new Intent(TimeYuyuelist.this, TimeYuyueActivity.class);
//				startActivity(intent);
				
				skipActivity(TimeYuyueActivity.class);
				ServiceReportCache.setIndex(arg2);

			}
		});
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
	}

	@Override
	protected void getWebService(String s) {
	
		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SHGL_PGDCZ",
						"2000-01-01*3000-01-01*" + query_djzt + "*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");

				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
/**
 *{"shgl_ywgl_fwbgszb_kzzd15":"","shgl_ywgl_fwbgszb_kzzd6":"2014-07-30  09:50:22: \/ 2014-07-30 09:57:02:转派原因 \/ 2014-07-30 10:20:26: \/ 2014-07-30 10:22:54:","shgl_ywgl_fwbgszb_kzzd7":"","shgl_ywgl_fwbgszb_kzzd8":"2014-07-30 09:52:19","shgl_ywgl_fwbgszb_kzzd14":"","shgl_ywgl_fwbgszb_kzzd2":"","shgl_ywgl_fwbgszb_kzzd3":"","shgl_ywgl_fwbgszb_kzzd4":"","shgl_ywgl_fwbgszb_fwyj":"","shgl_ywgl_fwbgszb_jdr":"","shgl_ywgl_fwbgszb_kzzd5":"4","shgl_ywgl_fwbgszb_kzzd1":"","shgl_ywgl_fwbgszb_bzr":"wlj","shgl_ywgl_fwbgszb_djzt":"1","shgl_ywgl_fwbgszb_jdsj":"2014-07-30 09:50:22","shgl_ywgl_fwbgszb_fwbg_wgsj":"","shgl_ywgl_fwbgszb_fwpj":"","shgl_ywgl_fwbgszb_pgdx":"00001","shgl_ywgl_fwbgszb_bzsj":"2014-07-30 09:41:31","shgl_ywgl_fwbgszb_zbh":"PG0000000020054","shgl_ywgl_fwbgszb_xgry":"","shgl_ywgl_fwbgszb_fwnr":"实际故障原因","shgl_ywgl_fwbgszb_dz":"0000001","shgl_ywgl_fwbgszb_sfhf":"","shgl_ywgl_fwbgszb_lxdh":"2","shgl_ywgl_fwbgszb_fwfylx":"","shgl_ywgl_fwbgszb_jdgs":"001001","ccgl_wlsjb_khbm":"三二四医院家属区","shgl_ywgl_fwbgszb_fwgcs":"","shgl_ywgl_fwbgszb_xgsj":"","shgl_ywgl_fwbgszb_ddsj":"","shgl_ywgl_fwbgszb_clfs":"2","shgl_ywgl_fwbgszb_sgdh":"","shgl_ywgl_fwbgszb_gzxx":"测试","shgl_ywgl_fwbgszb_wcsj":"","shgl_ywgl_fwbgszb_cfsj":"2014-07-30 12:52:13","shgl_ywgl_fwbgszb_slsj":"2014-07-30 09:41:52","shgl_ywgl_fwbgszb_kdzh":"重庆动车","shgl_ywgl_fwbgszb_clr":"","shgl_ywgl_fwbgszb_sf":"","shgl_ywgl_fwbgszb_dh":"0000001","shgl_ywgl_fwbgszb_sr1":"0","shgl_ywgl_fwbgszb_sr2":"0","shgl_ywgl_fwbgszb_pgbm":"10101","shgl_ywgl_fwbgszb_sr3":"0","shgl_ywgl_fwbgszb_sr4":"0","shgl_ywgl_fwbgszb_sr5":"0","ccgl_ygb_xm":"管理员","shgl_ywgl_fwbgszb_czsj":"2014-07-30 09:41:31","shgl_ywgl_fwbgszb_bzrlxdh":"15826009415","shgl_ywgl_fwbgszb_czy":"0001","shgl_ywgl_fwbgszb_bz":"测试单","shgl_ywgl_fwbgszb_pgsj":"2014-07-30 09:41:31","shgl_ywgl_fwbgszb_cljg":"","shgl_ywgl_fwbgszb_khxxdz":"测试","main_jcsj_khjgb_ds":"观音桥"}
 */
						String timeff = temp
								.getString("shgl_ywgl_fwbgszb_bzsj");
						String cfsj = temp
								.getString("shgl_ywgl_fwbgszb_cfsj");
						if(!"".equals(cfsj)){
							cfsj = cfsj.substring(2);
							
							item.put("timemy",// 时间
									mdateformat(1, cfsj));

							item.put("datemy",// 年月日
									mdateformat(0, cfsj));
						}else{
							item.put("timemy","");

							item.put("datemy","");
						}
						item.put("no", String.valueOf(i + 1));
						item.put("oddnumber",// 派工单号
								temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("faultuser",
								temp.getString("shgl_ywgl_fwbgszb_bzr"));
						item.put("usertel",
								temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						item.put("kzzd1",
								temp.getString("shgl_ywgl_fwbgszb_kzzd1"));
						item.put("kzzd5",
								temp.getString("shgl_ywgl_fwbgszb_kzzd5"));
						item.put("kzzd6",
								temp.getString("shgl_ywgl_fwbgszb_kzzd6"));
						item.put("gzxx",
								temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						item.put("dispatchinguser",
								temp.getString("ccgl_ygb_xm"));
						item.put("pgdx",temp.getString("shgl_ywgl_fwbgszb_pgdx"));
						item.put("clfs",temp.getString("shgl_ywgl_fwbgszb_clfs"));
						item.put("khbm",temp.getString("ccgl_wlsjb_khbm"));
						item.put("ds",temp.getString("main_jcsj_khjgb_ds"));
						item.put("kdzh",temp.getString("shgl_ywgl_fwbgszb_kdzh"));
						item.put("cfsj",temp.getString("shgl_ywgl_fwbgszb_cfsj"));
						item.put("khxxdz",temp.getString("shgl_ywgl_fwbgszb_khxxdz"));
						item.put("jdsj",temp.getString("shgl_ywgl_fwbgszb_jdsj"));
						item.put("bz",temp.getString("shgl_ywgl_fwbgszb_bz"));
						item.put("fwnr",temp.getString("shgl_ywgl_fwbgszb_fwnr"));//故障原因
						
						item.put("timeff",timeff);
						
						
//						item.put("customername",// 营业厅
//								temp.getString("ccgl_wlsjb_khbm"));
//						item.put("faulttime",// 时间
//								temp.getString("shgl_ywgl_fwbgszb_bzsj"));
//						item.put("khdh", temp.getString("ccgl_wlsjb_dh"));
//						item.put("dispatchingtime",
//								temp.getString("shgl_ywgl_fwbgszb_pgsj"));
//						item.put("state",
//								temp.getString("shgl_ywgl_fwbgszb_djzt"));
//						item.put("clfs",
//								temp.getString("shgl_ywgl_fwbgszb_clfs"));
//						item.put("clr", temp.getString("shgl_ywgl_fwbgszb_czy"));
//						item.put("sgdh",
//								temp.getString("shgl_ywgl_fwbgszb_sgdh"));
//						item.put("bz", temp.getString("shgl_ywgl_fwbgszb_bz"));
//						item.put("dz", temp.getString("ccgl_wlsjb_dz"));
//						item.put("pgbm", temp.getString("zzjgb_pz_pgbm"));
//						item.put("ydbm",
//								temp.getString("shgl_ywgl_fwbgssbb_ydbm"));
//						item.put("sbbm",
//								temp.getString("shgl_ywgl_fwbgssbb_sbbm"));
//						item.put("sblx",
//								temp.getString("main_jcsj_sblbb_sblbmc"));

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
				adapter = new SimpleAdapter(TimeYuyuelist.this,
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
