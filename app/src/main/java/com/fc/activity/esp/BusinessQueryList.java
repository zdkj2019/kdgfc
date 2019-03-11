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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;

/**
 * 业务受理查询
 * 
 * @author cheng
 */
@SuppressLint("HandlerLeak")
public class BusinessQueryList extends FrameActivity {

	private String flag, time;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
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
		data = new ArrayList<Map<String, String>>();

		from = new String[] { "toXin", "mc", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
		time = getIntent().getStringExtra("time");
		
//		data_xq = new ArrayList<Map<String, String>>();
//		data_tc = new ArrayList<Map<String, String>>();
//		data_JRLX = new ArrayList<Map<String, String>>();
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
							BusinessQueryActivity.class);
					
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
						"_PAD_XS_YWSLCX", time + "*" + DataCache.getinition().getUserId(),"uf_json_getdata",this);
/**
 * {"zdkhb_yzxx":"尊敬的用户esn，您预约在黔江示范营业厅小区8855栋移动FTTH办理的套餐1宽带套餐，您的安装联系电话为15826009415，若确认无误，请将套餐确认验证码8028告知现场下单工作人员","zdkhb_dls":"","zdkhb_zbh":"YW0000000000013","zdkhb_czsj4":"1900-01-01 00:00:00","zdkhb_czsj3":"1900-01-01 00:00:00","zdkhb_czsj2":"1900-01-01 00:00:00","zdkhb_djzt":"2","zdkhb_khmc":"esn","zdkhb_kzzf10":"","ccgl_wlsjb_wlsjmc":"黔江示范营业厅","zdkhb_czry4":"","zdkhb_czry2":"","zdkhb_czry3":"","zdkhb_zjdz":"ghgfg","jrmsb_jrms":"移动FTTH","zdkhb_csrq":"2014-03-05 00:00:00","zdkhb_czry":"0001","zdkhb_azdz":"8855","zdkhb_zjyxq1":"2014-03-05 00:00:00","zdkhb_lxdh":"15826009415","zdkhb_kzzf8":"","zdkhb_kdlx":"002","zdkhb_kzzf7":"","zdkhb_zjyxq2":"2014-03-05 00:00:00","zdkhb_sfzh":"85","zdkhb_xb":"男","tcb_tcmc":"套餐1","zdkhb_kzzf9":"","zdkhb_kdzh":"","zdkhb_azxq":"0000002","zdkhb_kzzf4":"","zdkhb_kzzf3":"","zdkhb_kzzf6":"","zdkhb_yhdh2":"55","zdkhb_kzzf5":"","zdkhb_kzzf2":"","zdkhb_czsj":"2014-03-05 23:51:32","zdkhb_tcdc":"001","zdkhb_kzzf1":"bx"}
 */	
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						String timeff = temp.getString("zdkhb_czsj");
						timeff = timeff.substring(2);
//						xb,azxq,kdlx,csrq,zjyxq1,zjyxq2,tcmc
//						khmc/username,lxdhr/lxdh,lxdhr2/yhdh2 ,xqlh/azdz ,sfzh,zjdz,kzzf1
						
						item.put("xb", temp.getString("zdkhb_xb"));//性别
						item.put("azxq", temp.getString("zdkhb_azxq"));//小区。。
						item.put("azxqname", temp.getString("ccgl_wlsjb_wlsjmc"));//小区。。
						item.put("kdlx", temp.getString("zdkhb_kdlx"));//宽带类型。。。
						item.put("kdlxname", temp.getString("jrmsb_jrms"));//宽带类型。。。
//						item.put("csrq", temp.getString("zdkhb_csrq"));//出生日期
//						item.put("zjyxq1", temp.getString("zdkhb_zjyxq1"));//证件有效期
//						item.put("zjyxq2", temp.getString("zdkhb_zjyxq2"));//证件有效期
						item.put("tcdc", temp.getString("zdkhb_tcdc"));//套餐名称
						item.put("tcdcname", temp.getString("tcb_tcmc"));//中文 套餐名称
						
						String toXin = nameToXin(temp.getString("zdkhb_khmc"));
						item.put("toXin", toXin);//客户名称
						item.put("khmc", temp.getString("zdkhb_khmc"));//客户名称
						item.put("lxdh", temp.getString("zdkhb_lxdh"));//联系电话
						item.put("yhdh2", temp.getString("zdkhb_yhdh2"));//备用联系电话
						item.put("azdz", temp.getString("zdkhb_azdz"));//小区楼号
//						item.put("sfzh", temp.getString("zdkhb_sfzh"));//身份证号
//						item.put("zjdz", temp.getString("zdkhb_zjdz"));//证件地址
						item.put("kzzf1", temp.getString("zdkhb_kzzf1"));//备注
						item.put("zbh", temp.getString("zdkhb_zbh"));
						item.put("mc", temp.getString("ccgl_wlsjb_wlsjmc")+"、"+temp.getString("jrmsb_jrms")+"、"+temp.getString("zdkhb_kzzf1"));
						
//						item.put("kzzf9", temp.getString("kzzf9"));
//						item.put("kzzf7", temp.getString("kzzf7"));
//						item.put("kzzf8", temp.getString("kzzf8"));
//						item.put("kzzf5", temp.getString("kzzf5"));
//						item.put("czry", temp.getString("czry"));
//						item.put("kzzf6", temp.getString("kzzf6"));
//						item.put("kzzf3", temp.getString("kzzf3"));
//						item.put("kzzf4", temp.getString("kzzf4"));
//						item.put("kzzf2", temp.getString("kzzf2"));
//						item.put("czry2", temp.getString("czry2"));
//						item.put("czry3", temp.getString("czry3"));
//						item.put("czry4", temp.getString("czry4"));
//						item.put("djzt", temp.getString("djzt"));
//						item.put("kdzh", temp.getString("kdzh"));
//						item.put("kzzf10", temp.getString("kzzf10"));
//						item.put("dls", temp.getString("dls"));
//						item.put("czsj3", temp.getString("czsj3"));
//						item.put("czsj2", temp.getString("czsj2"));
//						item.put("czsj4", temp.getString("czsj4"));
//						item.put("yzxx", temp.getString("yzxx"));
						
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
	private String nameToXin(String name){
		int le = name.length();
		String substring;
		if (le >= 1) {
			substring = name.substring(0, 1);
			for (int j = 1; j < le; j++) {
				substring +=  "*";
			}

		}else{
			substring = name;
		}
		return substring;
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

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
	}

}
