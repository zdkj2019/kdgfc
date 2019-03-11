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
public class InformationReceivingActivity_Dzqd extends FrameActivity {

	private TextView tv_pgdh, tv_clfs;
	private TextView tv_bzr, tv_gzxx;
	private Button confirm, cancel;
	private LinearLayout phone_lin;
	private List<String> clfs_list = new ArrayList<String>();
	private Spinner sp_gzlb, sp_gzzl, sp_gzlbb;
	private String[] from;
	private int[] to;
	private List<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm,
			gzbm_2_list, gzbm_3_list;
	private String _PAD_EPOS_STR = "";
	private String _PAD_SFSCZZBG_STR = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_informationreceiving_dzqd);
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
		phone_lin = (LinearLayout) findViewById(R.id.phone_lin);

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
						InformationReceivingActivity_Dzqd.this, gzbm_2_list,
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
						InformationReceivingActivity_Dzqd.this, gzbm_3_list,
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

		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());

		tv_pgdh.setText(itemmap.get("oddnumber"));
		tv_bzr.setText(itemmap.get("faultuser"));
		tv_gzxx.setText(itemmap.get("gzxx"));

		((TextView) findViewById(R.id.tv_suxq)).setText(itemmap.get("khbmmc"));
		((TextView) findViewById(R.id.tv_smsf)).setText(itemmap.get("smsf"));
		((TextView) findViewById(R.id.tv_kfyq)).setText(itemmap.get("kzzd18"));
		((TextView) findViewById(R.id.tv_supq)).setText(itemmap.get("ds"));
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("khxxdz"));
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz"));
		((TextView) findViewById(R.id.tv_kfxm)).setText(itemmap.get("kfxm"));
		((TextView) findViewById(R.id.tv_jsdw)).setText(itemmap.get("cx"));// 结算位置
		((TextView) findViewById(R.id.tv_clfs)).setText("1".equals(itemmap
				.get("clfs")) ? "人工上门" : "电话完工");// 结算位置
		String gdfy = itemmap.get("gdfy");
		if (gdfy == null || "".equals(gdfy)) {
			((TextView) findViewById(R.id.tv_gdfy)).setText(gdfy);// 工单费用
		} else {
			((TextView) findViewById(R.id.tv_gdfy)).setText(Float
					.parseFloat(itemmap.get("gdfy").length() > 7 ? itemmap.get(
							"gdfy").substring(0, 7) : itemmap.get("gdfy"))
					+ "");// 工单费用
		}
		// 电话
		String usertel = itemmap.get("usertel");

		if (usertel != null) {

			usertel = usertel.replaceAll("[\\D]+", ",");
			String[] telarry = usertel.split(",");

			for (String tel : telarry) {

				addpohoneview(tel);
			}
		}

	}

	private void addpohoneview(String phone) {

		TextView textView = new TextView(getApplicationContext());

		textView.setTextSize(18l);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setBackgroundResource(R.drawable.edittext_bg);
		textView.setPadding(5, 0, 0, 0);

		setunderline(phone, textView);

		phone_lin.addView(textView);

	}

	private void setunderline(String tel, TextView view) {

		CharacterStyle span = new UnderlineSpan();
		SpannableString telss = new SpannableString(tel);
		telss.setSpan(span, 0, telss.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(telss);

		telonclick(view, tel);
	}

	private void telonclick(View v, final String tel) {

		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + tel));

				startActivity(phoneIntent);

			}
		});

	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				onBackPressed();
			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
//				if (isNotNull(tv_bzr) && isNotNull(tv_clfs)) {
//					
//					boolean f = true;
//					if(tv_clfs.getText().equals("电话完工")){
//						if(sp_gzlb.getSelectedItemPosition()==0||sp_gzzl.getSelectedItemPosition()==0||sp_gzlbb.getSelectedItemPosition()==0){
//							f = false;
//							dialogShowMessage("请录入故障信息！", null,null);
//						}
//					}
//					
//					if(f){
//						dialogShowMessage("确认接受 ？",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface face,
//											int paramAnonymous2Int) {
//										showProgressDialog();
//										Config.getExecutorService().execute(
//												new Runnable() {
//
//													@Override
//													public void run() {
//
//														getWebService("submit");
//													}
//												});
//
//									}
//								}, null);
//					}
//				} else {
//					dialogShowMessage_P("请填写完整", null);
//				}
				

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
				JSONObject jsonObject1 = callWebserviceImp.getWebServerInfo(
						"_PAD_EPOS", "1", "uf_json_getdata", this);
				JSONObject jsonObject3 = callWebserviceImp.getWebServerInfo(
						"_PAD_SFSCZZBG", "", "uf_json_getdata", this);
				String flag1 = jsonObject1.getString("flag");
				String flag3 = jsonObject3.getString("flag");
				if (Integer.parseInt(flag1) > 0) {
					JSONArray jsonArray2 = jsonObject1.getJSONArray("tableA");
					JSONObject temp = jsonArray2.getJSONObject(0);
					_PAD_EPOS_STR = temp.getString("cs");
				}

				if (Integer.parseInt(flag3) > 0) {
					JSONArray jsonArray3 = jsonObject3.getJSONArray("tableA");
					JSONObject temp = jsonArray3.getJSONObject(0);
					_PAD_SFSCZZBG_STR = temp.getString("ssjg");
				}

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
				String czrz3 = "'0'||chr(42)||'"
						+ DataCache.getinition().getUserId() + "'||chr(42)||'"
						+ new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss")
						+ "'";
				Log.e("dd", czrz3);
				String djzt = "3";//tv_clfs.getText().equals("电话完工") ? "5" : "3";
				String sql = "";
				if ("5".equals(djzt)) {
					
						sql = "update shgl_ywgl_fwbgszb set djzt=" + djzt
								+ ","
								+"clfs=2,"
								+ "kzzd1='"
						+ (sp_gzlb.getSelectedItem() == null ? ""
								: ((Map<String, String>) sp_gzlb
										.getSelectedItem()).get("id"))
						+ "',"
						+ "kzzd15='"
						+ (sp_gzzl.getSelectedItem() == null ? ""
								: ((Map<String, String>) sp_gzzl
										.getSelectedItem()).get("id"))
						+ "',"
						+ "kzzd16='"
						+ (sp_gzlbb.getSelectedItem() == null ? ""
								: ((Map<String, String>) sp_gzlbb
										.getSelectedItem()).get("id"))
						+ "',"
								+"slsj=sysdate,wcsj=sysdate,czrz3=" + czrz3
								+ " where zbh='" + tv_pgdh.getText() + "'";
						
					
					
				} else {
					
					sql = "update shgl_ywgl_fwbgszb t set "
							+ "t.djzt = (case when t.djzt <'"+djzt+"' then '"+djzt+"' else t.djzt  end),"
							+ "t.pgdx = (case when t.djzt <'"+djzt+"' then (select ygbh from CCGL_YGB where pym = '"+DataCache.getinition().getUserId()+"') else t.pgdx  end),slsj=sysdate,czrz3=" + czrz3 + " where zbh='"
							+ tv_pgdh.getText() + "'";
					
					
//					sql = "update shgl_ywgl_fwbgszb set djzt=" + djzt
//							+ ",clfs=1,slsj=sysdate,czrz3=" + czrz3 + " where zbh='"
//							+ tv_pgdh.getText() + "'";
				}

				Log.e("dd", sql.toString());

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_RZ", sql, DataCache.getinition().getUserId(),
						"uf_json_setdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					//保存成功后再次查询 派工对象
					//如果是当前用户 返回1 抢单成功   否则返回0 抢单失败
					//sql = "select case when (select ygbh from CCGL_YGB where pym = 's%') = t.pgdx then 1 else 0 end from  shgl_ywgl_fwbgszb t where zbh='s%'";
					
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_QDCX", DataCache.getinition().getUserId()+"*"+tv_pgdh.getText(), "uf_json_getdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");
						JSONObject temp = jsonArray2.getJSONObject(0);
						String jg = temp.getString("yyg");
						if("1.000000000000000000".equals(jg)){
							Message msg = new Message();
							msg.what = SUCCESSFUL;
							handler.sendMessage(msg);
						}else{
							Message msg = new Message();
							msg.what = 9;// 失败
							handler.sendMessage(msg);
						}
					}else{
						Message msg = new Message();
						msg.what = 9;// 失败
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 9;// 失败
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				dialogShowMessage_P("获取基础数据失败", null);
				break;
			case 1:
				dialogShowMessage_P("接收成功",
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
						InformationReceivingActivity_Dzqd.this, data_gzbm,
						R.layout.spinner_item, from, to);

				sp_gzlb.setAdapter(adapter);
				break;
			case 9:
				dialogShowMessage_P("抢单失败！",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,
							int paramAnonymous2Int) {
						onBackPressed();
//						Intent intent = new Intent(InformationReceivingActivity_Dzqd.this,InformationReceivinglist_Dzqd.class);
//						startActivity(intent);
//						finish();
					}
				});
				
				break;
			}
			progressDialog.dismiss();
		}
	};

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
		finish();
	}

}
