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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;

/**
 * 信息接受
 * 
 * @author wlj
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class InformationReceivingActivity extends FrameActivity {

	private TextView tv_pgdh, tv_clfs, tv_phone_kh;
	private TextView tv_bzr, tv_gzxx;
	private Button confirm, cancel;
	private ImageView iv_telphone, iv_telphone_kh;
	private List<String> clfs_list = new ArrayList<String>();
	private Spinner sp_gzlb, sp_gzzl, sp_gzlbb;
	private String[] from;
	private int[] to;
	private List<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm,
			gzbm_2_list, gzbm_3_list;
	private String tel;
	private boolean ist16 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_informationreceiving);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("querygzlx");

			}
		});
	}

	@Override
	protected void initVariable() {

		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		tv_bzr = (TextView) findViewById(R.id.tv_bzr);

		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		tv_phone_kh = (TextView) findViewById(R.id.tv_phone_kh);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

		iv_telphone_kh = (ImageView) findViewById(R.id.iv_telphone_kh);

		tv_clfs = (TextView) findViewById(R.id.tv_clfs);

		sp_gzlb = (Spinner) findViewById(R.id.sp_gzlb);
		sp_gzzl = (Spinner) findViewById(R.id.sp_gzzl);
		sp_gzlbb = (Spinner) findViewById(R.id.sp_gzlbb);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		data_gzbm = new ArrayList<Map<String, String>>();
		data_2_gzbm = new ArrayList<Map<String, String>>();
		data_3_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("接单");
		cancel.setText("拒绝");

		clfs_list.add("人工上门");
		clfs_list.add("电话完工");
		tv_clfs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				defined(tv_clfs, "处理方式", clfs_list);
			}
		});
		OnItemSelectedListener onItemSelectedListener_gzlb = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = data_gzbm.get(arg2).get("id");
				gzbm_2_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_2_list.add(map);
				// 选择的大类 设置中类
				for (int i = 0; i < data_2_gzbm.size(); i++) {

					String whcs_id = data_2_gzbm.get(i).get("id");
					if (whcs_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_2_list.add(data_2_gzbm.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(
						InformationReceivingActivity.this, gzbm_2_list,
						R.layout.spinner_item, from, to);
				sp_gzzl.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		OnItemSelectedListener onItemSelectedListener_gzzl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = gzbm_2_list.get(arg2).get("id");
				gzbm_3_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_3_list.add(map);

				for (int i = 0; i < data_3_gzbm.size(); i++) {

					String whcs_id = data_3_gzbm.get(i).get("id");
					if (whcs_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_3_list.add(data_3_gzbm.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(
						InformationReceivingActivity.this, gzbm_3_list,
						R.layout.spinner_item, from, to);
				sp_gzlbb.setAdapter(adapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};
		sp_gzlb.setOnItemSelectedListener(onItemSelectedListener_gzlb);// 故障类别
		// 大类
		sp_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障类别
		// 中类
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		tv_pgdh.setText(itemmap.get("oddnumber").toString());
		tv_bzr.setText(itemmap.get("faultuser").toString());
		tv_gzxx.setText(itemmap.get("gzxx").toString());

		ist16 = (Boolean) itemmap.get("ist16");

		((TextView) findViewById(R.id.tv_suxq)).setText(itemmap.get("khbmmc")
				.toString());
		((TextView) findViewById(R.id.tv_smsf)).setText(itemmap.get("smsf")
				.toString());
		((TextView) findViewById(R.id.tv_kfyq)).setText(itemmap.get("kzzd18")
				.toString());
		((TextView) findViewById(R.id.tv_supq)).setText(itemmap.get("ds")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("khxxdz")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_kfxm)).setText(itemmap.get("kfxm")
				.toString());
		((TextView) findViewById(R.id.tv_jsdw)).setText(itemmap.get("cx")
				.toString());// 结算位置
		((TextView) findViewById(R.id.tv_clfs)).setText("1".equals(itemmap
				.get("clfs")) ? "人工上门" : "电话完工");// 结算位置
		((TextView) findViewById(R.id.tv_fwgcs)).setText(itemmap.get("zxsname").toString());
		tel = itemmap.get("zxstel").toString();
		String gdfy = itemmap.get("gdfy").toString();
		if (gdfy == null || "".equals(gdfy)) {
			((TextView) findViewById(R.id.tv_gdfy)).setText(gdfy);// 工单费用
		} else {
			((TextView) findViewById(R.id.tv_gdfy))
					.setText(Float.parseFloat(itemmap.get("gdfy").toString()
							.length() > 7 ? itemmap.get("gdfy").toString()
							.substring(0, 7) : itemmap.get("gdfy").toString())
							+ "");// 工单费用
		}
		// 电话
		String usertel = itemmap.get("usertel").toString();
		tv_phone_kh.setText(usertel);
		// if (usertel != null) {
		//
		// usertel = usertel.replaceAll("[\\D]+", ",");
		// String[] telarry = usertel.split(",");
		//
		// for (String tel : telarry) {
		//
		// addpohoneview(tel);
		// }
		// }

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
					Intent intent = new Intent(
							InformationReceivingActivity.this,
							InformationReceivingRefuseActivity.class);
					startActivity(intent);
					// dialogShowMessage("确认拒绝接单？",
					// new DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface face,
					// int paramAnonymous2Int) {
					// refuseList();
					// }
					// },null);
					break;
				case R.id.iv_telphone:
					Call(tel);
					break;
				case R.id.iv_telphone_kh:
					Call(tv_phone_kh.getText().toString());
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		iv_telphone.setOnClickListener(backonClickListener);
		iv_telphone_kh.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isNotNull(tv_bzr) && isNotNull(tv_clfs)) {

					boolean f = true;
					if (tv_clfs.getText().equals("电话完工")) {
						if (sp_gzlb.getSelectedItemPosition() == 0
								|| sp_gzzl.getSelectedItemPosition() == 0
								|| sp_gzlbb.getSelectedItemPosition() == 0) {
							f = false;
							dialogShowMessage("请录入故障信息！", null, null);
						}
					}

					if (f) {
						dialogShowMessage("确认接受 ？",
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
					}
				} else {
					dialogShowMessage_P("请填写完整", null);
				}
			}
		});

	}

	String flag;

	@Override
	protected void getWebService(String s) {
		/**
		 * 故障类别
		 */
		if ("querygzlx".equals(s)) {

			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", "", "uf_json_getdata", this);
				String flag2 = jsonObject.getString("flag");

				if (Integer.parseInt(flag2) > 0) {
					JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

					Map<String, String> item = new HashMap<String, String>();

					item.put("id", "     ");
					item.put("name", " ");
					data_gzbm.add(item);
					data_2_gzbm.add(item);
					data_3_gzbm.add(item);

					for (int i = 0; i < jsonArray2.length(); i++) {
						JSONObject temp = jsonArray2.getJSONObject(i);
						item = new HashMap<String, String>();
						String id = temp.getString("gzlbbm");

						item.put("id", id);
						item.put("name", temp.getString("gzlbmc"));
						if (id.length() == 2) {
							data_gzbm.add(item);
						} else if (id.length() == 4) {
							data_2_gzbm.add(item);
						} else {
							data_3_gzbm.add(item);
						}

					}

					// DataCache.getinition().setErji_GZLB(data_erji_gzbm);
					// DataCache.getinition().setGZLB(data_gzbm);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = -1;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}

		}

		if (s.equals("submit")) {// 提交
			try {

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JDCF_CX", tv_pgdh.getText() + "",
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray array = jsonObject.getJSONArray("tableA");
					jsonObject = (JSONObject) array.get(0);
					if ("2".equals(jsonObject.get("djzt"))) {
						String czrz3 = "'0'||chr(42)||'"
								+ DataCache.getinition().getUserId()
								+ "'||chr(42)||'"
								+ new DataUtil()
										.toDataString("yyyy-MM-dd HH:mm:ss")
								+ "'";
						Log.e("dd", czrz3);
						String djzt = tv_clfs.getText().equals("电话完工") ? "4"
								: "3";
						String sql = "";
						if ("4".equals(djzt)) {

							sql = "update shgl_ywgl_fwbgszb set djzt="
									+ djzt
									+ ","
									+ "clfs=2,"
									+ "kzzd1='"
									+ (sp_gzlb.getSelectedItem() == null ? ""
											: ((Map<String, String>) sp_gzlb
													.getSelectedItem())
													.get("id"))
									+ "',"
									+ "kzzd15='"
									+ (sp_gzzl.getSelectedItem() == null ? ""
											: ((Map<String, String>) sp_gzzl
													.getSelectedItem())
													.get("id"))
									+ "',"
									+ "kzzd16='"
									+ (sp_gzlbb.getSelectedItem() == null ? ""
											: ((Map<String, String>) sp_gzlbb
													.getSelectedItem())
													.get("id")) + "',"
									+ "slsj=sysdate,wcsj=sysdate,czrz3="
									+ czrz3 + " where zbh='"
									+ tv_pgdh.getText() + "'";

						} else {
							sql = "update shgl_ywgl_fwbgszb set djzt=" + djzt
									+ ",clfs=1,slsj=sysdate,czrz3=" + czrz3
									+ " where zbh='" + tv_pgdh.getText() + "'";
						}
						jsonObject = callWebserviceImp.getWebServerInfo("_RZ",
								sql, DataCache.getinition().getUserId(),
								"uf_json_setdata", this);
						flag = jsonObject.getString("flag");

						if (Integer.parseInt(flag) > 0) {
							Message msg = new Message();
							msg.what = SUCCESSFUL;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = FAIL;// 失败
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					}

				} else {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}

		}

	}

	private void refuseList() {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String czrz3 = "0*"
							+ DataCache.getinition().getUserId()
							+ "*"
							+ new DataUtil()
									.toDataString("yyyy-MM-dd HH:mm:ss") + "";
					String sql = "update shgl_ywgl_fwbgszb set djzt='1.5',chjdrz = '"
							+ czrz3
							+ "'"
							+ " where zbh='"
							+ tv_pgdh.getText()
							+ "'";
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
							"_RZ", sql, DataCache.getinition().getUserId(),
							"uf_json_setdata", getApplicationContext());
					flag = jsonObject.getString("flag");

					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = FAIL;// 失败
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}

			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				dialogShowMessage_P("获取基础数据失败", null);
				break;
			case 1:
				String message = "接收成功!";
				if (ist16) {
					message = message + "（特别提示：该工单若按T16标准完成，可获得额外奖励。）";
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case 0:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;

			case 2:
				SimpleAdapter adapter = new SimpleAdapter(
						InformationReceivingActivity.this, data_gzbm,
						R.layout.spinner_item, from, to);

				sp_gzlb.setAdapter(adapter);
				break;
			case 3:
				dialogShowMessage_P("拒绝成功！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case 4:
				dialogShowMessage_P("工单已接收！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			}
			if (progressDialog != null) {
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
