package com.fc.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.common.Constant;
import com.fc.utils.Config;

/**
 * 快递柜-组长转派（维修）
 * 
 * @author zdkj
 *
 */
public class ZzzpKdgWx extends FrameActivity {

	private Button confirm, cancel;
	private TextView tv_phone;
	private ImageView iv_telphone,iv_telphone1;
	private String flag, zbh, type = "1", msgStr,ywlx,pqid,qxid,ddh,lxdh;
	private Spinner spinner_pq,spinner_qx,spinner_jdry;
	private String[] from;
	private int[] to;
	private ArrayList<Map<String, String>> data_ry,data_pq,data_qx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zzzp);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("getpq");
			}
		});

	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_telphone1 = (ImageView) findViewById(R.id.iv_telphone1);
		spinner_jdry = (Spinner) findViewById(R.id.spinner_jdry);
		spinner_pq = (Spinner) findViewById(R.id.spinner_pq);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		lxdh = itemmap.get("lxdh").toString();
		ywlx = itemmap.get("ywlx").toString();
		ddh = itemmap.get("ddh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("axdh").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("sx").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("qy").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("ddh_mc").toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("bzsj").toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("yqsx").toString());
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("lxdh").toString());
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("dygdh1_mc").toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("dygdh2_mc").toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("kzzf6").toString());
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("kzzf7").toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("kzzf8").toString());
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("gzxx").toString());
		((TextView) findViewById(R.id.tv_17)).setText(itemmap.get("kzsz1").toString());
		((TextView) findViewById(R.id.tv_18)).setText(itemmap.get("bz").toString());
		((TextView) findViewById(R.id.tv_jddz)).setText(itemmap.get("jddz").toString());
		
		if("1".equals(ddh)){
			findViewById(R.id.ll_1).setVisibility(View.VISIBLE);
		}
		if("3".equals(ddh)){
			findViewById(R.id.ll_2).setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							type = "1";
							getWebService("submit");
						}
					});
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		
		spinner_pq.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				pqid = data_pq.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getqx");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinner_qx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				qxid = data_qx.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getry");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinner_jdry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				tv_phone.setText(data_ry.get(position).get("lxdh"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		iv_telphone1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(tv_phone.getText().toString());
			}
		});
		
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(lxdh);
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		
		if (s.equals("getpq")) {
			try {
				data_pq = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_PQ", DataCache.getinition().getUserId(), "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("jcsj_dqrypzb_pqbm"));
						item.put("name", temp.getString("jcsj_pqb_pqmc"));
						data_pq.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.NUM_6;
					handler.sendMessage(msg);

				} else {
					flag = "查询失败，没有片区数据";
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
		
		if (s.equals("getqx")) {// 提交
			try {
				data_qx = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_QX", pqid, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("jcsj_pqqxpzb_qxbm"));
						item.put("name", temp.getString("jcsj_dqb_dqmc"));
						data_qx.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_7;
					handler.sendMessage(msg);

				} else {
					flag = "查询失败，没有区县数据";
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
		
		
		if (s.equals("getry")) {
			try {
				data_ry = new ArrayList<Map<String, String>>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_RY", qxid, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("user_password_userid"));
						item.put("name", temp.getString("user_password_username"));
						item.put("lxdh", temp.getString("user_password_sjhm"));
						data_ry.add(item);
					}

					Message msg = new Message();
					msg.what = Constant.NUM_8;
					handler.sendMessage(msg);

				} else {
					Map<String, String> item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					item.put("lxdh", "");
					data_ry.add(item);
					Message msg = new Message();
					msg.what = Constant.NUM_8;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				String sqlid = "";
				if("巡检".equals(ywlx)){
					sqlid = "c#_PAD_KDG_XJ_ALL";
				}else{
					sqlid = "c#_PAD_KDG_ALL";
				}
				String typeStr = "zzzp";
				msgStr = "转派成功";
				String str = zbh + "*PAM*" + DataCache.getinition().getUserId()+"*PAM*" +data_ry.get(spinner_jdry.getSelectedItemPosition()).get("id");
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						sqlid, str, typeStr, typeStr,
						"uf_json_setdata2", this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					flag = json.getString("msg");
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(flag, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(msgStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent  = getIntent();
								setResult(-1, intent);
								finish();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;

			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter1 = new SimpleAdapter(ZzzpKdgWx.this, data_pq,
						R.layout.spinner_item, from, to);
				spinner_pq.setAdapter(adapter1);
				break;
			case Constant.NUM_7:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter2 = new SimpleAdapter(ZzzpKdgWx.this, data_qx,
						R.layout.spinner_item, from, to);
				spinner_qx.setAdapter(adapter2);
				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter3 = new SimpleAdapter(ZzzpKdgWx.this, data_ry,
						R.layout.spinner_item, from, to);
				spinner_jdry.setAdapter(adapter3);
				break;
			}
		}
	};

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.putExtra("currType", 1);
//		startActivity(intent);
//		finish();
//	}

}