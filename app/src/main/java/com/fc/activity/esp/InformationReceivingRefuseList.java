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
 *  拒绝接单List
 * @author hs
 *
 */
@SuppressLint("HandlerLeak")
public class InformationReceivingRefuseList extends FrameActivity {

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

		from = new String[] {"textView1", "faultuser", "oddnumber", "timemy", "datemy" };
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

					Intent intent = new Intent(
							InformationReceivingRefuseList.this,
							InformationReceivingRefuseActivity.class);
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
						"_PAD_JDXY_JJJD", "2000-01-01*3000-01-01*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),"uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
			
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						
						String timeff = temp.getString("shgl_ywgl_fwbgszb_bzsj");
						timeff = timeff.substring(2);
						boolean flag = "00000020".equals(temp.getString("shgl_ywgl_fwbgszb_fbf"));
						item.put("textView1",flag==true ?R.drawable.ls_item_t16:getListItemIcon(i));
						item.put("ist16", flag);
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
						item.put("gdfy", temp.getString("dzpt_htfyxmb_jbf_jg"));//客户姓名
						
						item.put("cx", temp.getString("ccgl_wlsjb_cx"));//结算位置
						item.put("kzzd10", temp.getString("shgl_ywgl_fwbgszb_kzzd10"));//发包人
						item.put("kzzd11", temp.getString("shgl_ywgl_fwbgszb_kzzd11"));//发包人手机
						item.put("kzzd18", temp.getString("shgl_ywgl_fwbgszb_kzzd18"));//服务要求
						item.put("clfs", temp.getString("shgl_ywgl_fwbgszb_clfs"));//clfs
						item.put("chjdrz", temp.getString("shgl_ywgl_fwbgszb_chjdrz"));//clfs
						item.put("timemy",// 时间
								mdateformat(1, timeff));

						item.put("datemy",// 年月日
								mdateformat(0, timeff));
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
				adapter = new SimpleAdapter(
						InformationReceivingRefuseList.this,
						ServiceReportCache.getObjectdata(),
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
			if (progressDialog!=null) {
				progressDialog.dismiss();
			}
		}

	};
	
	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
		finish();
	}

}
