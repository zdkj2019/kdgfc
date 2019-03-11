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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.definition.Sign;
import com.fc.utils.Config;

/**
 * @author Van 派工单接受确认
 */
@SuppressLint("HandlerLeak")
public class DispatchingInformationConfirmActivity extends FrameActivity
		implements Sign {

	private TextView pgdh, khdh, khmc, khdz, pgbm, pgsj, bzsj, gzxx, bzr, lxdh,
			sblx, spin_pgdx;// , ds, sgdh, bz
	private String flag, id, name, pgId, pgdx;
	private Button confirm, cancel;// ,bt_choose;
	private List<Map<String, String>> data;
	private int set_djzt = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationconfirm);
		initVariable();
		initView();
		initListeners();
		// showProgressDialog();
		// new Thread() {
		//
		// @Override
		// public void run() {
		//
		// getWebService("queryUser");
		// }
		//
		// }.start();
	}

	@Override
	protected void initVariable() {

		data = new ArrayList<Map<String, String>>();

		spin_pgdx = (TextView) findViewById(R.id.spin_pgdx);
		pgdh = (TextView) findViewById(R.id.pgdh);
		// ds = (TextView) findViewById(R.id.ds);
		khdh = (TextView) findViewById(R.id.khdh);
		khmc = (TextView) findViewById(R.id.khmc);
		khdz = (TextView) findViewById(R.id.khdz);
		// sgdh = (TextView) findViewById(R.id.sgdh);
		pgbm = (TextView) findViewById(R.id.pgbm);
		pgsj = (TextView) findViewById(R.id.pgsj);
		bzsj = (TextView) findViewById(R.id.bzsj);
		gzxx = (TextView) findViewById(R.id.gzxx);
		bzr = (TextView) findViewById(R.id.bzr);
		lxdh = (TextView) findViewById(R.id.lxdh);
		// bz = (TextView) findViewById(R.id.bz);
		// ydbm = (TextView) findViewById(R.id.ydbm);
		// sbbm = (TextView) findViewById(R.id.sbbm);
		sblx = (TextView) findViewById(R.id.sblx);

		// bt_choose = (Button) findViewById(R.id.bt_choose);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		// if(title.getText().equals("组长信息接受")){
		// bt_choose.setVisibility(View.VISIBLE);
		// }

		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());

		// ds.setText("重庆");
		pgId = itemmap.get("oddnumber");
		pgdh.setText(pgId);
		khdh.setText(itemmap.get("khdh"));
		khmc.setText(itemmap.get("customername"));
		khdz.setText(itemmap.get("dz"));
		pgbm.setText(itemmap.get("pgbm"));
		pgsj.setText(itemmap.get("dispatchingtime"));
		bzsj.setText(itemmap.get("faulttime"));
		gzxx.setText(itemmap.get("gzxx"));
		bzr.setText(itemmap.get("faultuser"));
		lxdh.setText(itemmap.get("usertel"));
		
		sblx.setText(itemmap.get("sblx"));
		pgdx = itemmap.get("dispatchinguser");
		spin_pgdx.setText(pgdx);// 显示派公对象

		if (DataCache.getinition().getTitle().equals("工程师接收")) {
			//1
			set_djzt = 1;
			//2
			lxdh.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent phoneIntent = new Intent(
							"android.intent.action.CALL",

							Uri.parse("tel:" + lxdh.getText()));

					startActivity(phoneIntent);

				}
			});
			//3
			CharacterStyle span = new UnderlineSpan();
			SpannableString tel = new SpannableString(itemmap.get("usertel"));
			tel.setSpan(span, 0, tel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			lxdh.setText(tel);
		}
	}

	@Override
	protected void initListeners() {

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
					dialogShowMessage("确认接收 ？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(
											new Runnable() {

												@Override
												public void run() {

													getWebService("submit");
												}
											});

								}
							}, null);
					break;

				// case R.id.bt_choose://组长 改 派工对象
				//
				// spinner.performClick();
				//
				// break;

				default:
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
		// bt_choose.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);

//		OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//
//				if (arg2 > 0) {
//					selectedId = data.get(arg2).get("id");
//
//					if (selectedId != null) {
//						spin_pgdx.setText(data.get(arg2).get("name"));
//
//						DataCache.getinition().setPgdxbm(selectedId);
//					}
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//
//			}
//		};

		// spinner.setOnItemSelectedListener(onItemSelectedListener);

	}

	@Override
	protected void getWebService(String s) {

		if ("queryUser".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_RY", "", "uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					Map<String, String> title = new HashMap<String, String>();
					title.put("id", "员工编码");
					title.put("name", "姓名");
					data.add(title);

					for (int i = 0; i < jsonArray.length(); i++) {
						Map<String, String> item = new HashMap<String, String>();

						JSONObject temp = jsonArray.getJSONObject(i);
						id = temp.getString("ygbh");
						name = temp.getString("xm");
						item.put("id", id);
						item.put("name", name);
						data.add(item);
					}

					Message msg = new Message();
					msg.what = 2;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = -2;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		}
		if ("submit".equals(s)) {

			try {
				String sql = "update shgl_ywgl_fwbgszb set " + "djzt= "
						+ set_djzt + "," + "slsj=sysdate where zbh='" + pgId
						+ "'";

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_RZ", sql, DataCache.getinition().getUserId(),
						"uf_json_setdata",this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
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
				dialogShowMessage_P("工单 " + pgId + " 接收完成",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								skipActivity2(MainActivity.class);
							}

						});
				break;

			case 2:// 初始化派工对象数据处理
				if (pgdx != null && !"".equals(pgdx)) {
					for (int i = 0; i < data.size(); i++) {
						if (pgdx.equals(data.get(i).get("name"))) {
							break;
						}
					}
				}

				// SimpleAdapter adapter = new SimpleAdapter(
				// DispatchingInformationConfirmActivity.this, data,
				// R.layout.spinner_item, from, to);
				// spinner.setAdapter(adapter);
				// spinner.setSelection(index, true);
				spin_pgdx.setText(pgdx);
				break;

			case FAIL:
				dialogShowMessage_P("工单派送失败，请检查稍后重试...", null);
				break;

			case -2:
				dialogShowMessage_P("派工对象查询失败，请检查网络后重试...",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								skipActivity(DispatchingInformationReceivingActivity.class);
							}

						});
				break;

			}
			progressDialog.dismiss();
		}

	};

	@Override
	public void onBackPressed() {

		skipActivity2(DispatchingInformationReceivingActivity.class);
	}
}
