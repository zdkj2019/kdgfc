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

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;

/**
 * 服务报告choose
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class ServiceReportslist extends FrameActivity {

	private String flag;
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

		from = new String[] { "textView1","网点名称", "oddnumber", "timemy", "datemy" };
		to = new int[] { R.id.textView1,R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };

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
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				ServiceReportCache.setIndex(index);

				if (index >= 0 && DataCache.getinition().getBs() == 1) {

					Intent intent = new Intent(ServiceReportslist.this,ServiceReportsComplete.class);
			
					startActivity(intent);
					finish();

				}else{
					skipActivity(ServiceReportsAppraise.class);
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {

	
		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_FWBG_CX1",
						"2000-01-01*3000-01-01*"
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
						timeff = timeff.substring(2);
						item.put("no", String.valueOf(i + 1));
						item.put("textView1", getListItemIcon(i)+"");
						item.put("oddnumber",// 派工单号
								temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("faultuser",temp.getString("shgl_ywgl_fwbgszb_bzr"));
						item.put("网点名称",temp.getString("ccgl_wlsjb_bzr"));
						item.put("usertel",temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						item.put("kzzd5",temp.getString("shgl_ywgl_fwbgszb_kzzd5"));
						item.put("gzxx",temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						item.put("clfs",temp.getString("shgl_ywgl_fwbgszb_clfs"));
						item.put("khbm",temp.getString("shgl_ywgl_fwbgszb_khbm"));
						item.put("ds",temp.getString("shgl_ywgl_fwbgszb_ds"));
						item.put("dsmc",temp.getString("main_jcsj_dqb_qymc"));
						item.put("khxxdz",temp.getString("shgl_ywgl_fwbgszb_khxxdz"));
						item.put("bz",
								temp.getString("shgl_ywgl_fwbgszb_bz"));
						item.put("fbsw",
								temp.getString("shgl_ywgl_fwbgszb_pgbm"));
						item.put("fbswmc",
								temp.getString("zzjgb_pz_fbdw"));
						item.put("khbmmc",
								temp.getString("main_jcsj_khjgb_khbmmc"));
						item.put("kzzd17",temp.getString("shgl_ywgl_fwbgszb_kzzd17"));//验证码
						item.put("kzzd10",temp.getString("shgl_ywgl_fwbgszb_kzzd10"));//发包人
						item.put("kzzd11",temp.getString("shgl_ywgl_fwbgszb_kzzd11"));//发包人手机
						item.put("cx", temp.getString("shgl_ywgl_fwbgszb_cx"));//结算位置
						item.put("位置名称", temp.getString("ccgl_wlsjb_cx"));//结算位置
						item.put("服务要求", temp.getString("shgl_ywgl_fwbgszb_kzzd18"));//结算位置
						item.put("jdgs", temp.getString("shgl_ywgl_fwbgszb_jdgs"));//认证设备类型
						
						item.put("wcsj", temp.getString("shgl_ywgl_fwbgszb_wcsj"));//认证设备类型
						item.put("ddsj", temp.getString("shgl_ywgl_fwbgszb_ddsj"));//认证设备类型
						item.put("slsj", temp.getString("shgl_ywgl_fwbgszb_slsj"));//认证设备类型
						item.put("bzsj", temp.getString("shgl_ywgl_fwbgszb_bzsj"));//认证设备类型
						item.put("zxsname", temp.getString("ccgl_ygb_xm"));//服务咨询师
						item.put("zxstel", temp.getString("ccgl_ygb_lxdh"));// 服务咨询师电话
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
				adapter = new SimpleAdapter(ServiceReportslist.this,
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
