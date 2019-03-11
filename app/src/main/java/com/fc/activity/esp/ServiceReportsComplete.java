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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.fc.R;
import com.fc.Parser.JSONObjectParser;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;
import com.fc.utils.MyDialog;
import com.fc.zxing.CaptureActivity;

/**
 * 报告填写，确认
 * 
 * @author wlj
 * 
 */
@SuppressLint("HandlerLeak")
public class ServiceReportsComplete extends FrameActivity {

	private TextView tv_cljg, tv_jsdw, tv_bzr, tv_xzpj, tv_fwgcs;
	private TextView tv_supq, tv_suqy;
	private TextView tv_wxaz, tv_pgdh, tv_clfs;
	private TextView tv_gzxx, tv_phone_kh;
	private EditText et_zsdz, et_xxdz, tv_wgyy, et_sbbm, tv_bz, et_zdh;
	private List<String> clfs_list = new ArrayList<String>();
	private MyDialog myDialog;
	private Button basicconfirm, basiccancel, bt_sbbm;
	private List<String> cljg_list = new ArrayList<String>();
	private Map<String, String> itemmap;
	private ArrayList<Map<String, String>> data_xq, data_FBF, data;
	private JSONObjectParser jsonObjectParser;
	private String before_lxdh;
	private List<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm,
			gzbm_2_list, gzbm_3_list, zdcs_list;
	private Spinner sp_gzlb, sp_gzlbb, sp_gzzl, spinner_pgzt, spinner_zzzdcs;
	private String[] from;
	private int[] to;
	ArrayList<String> hpsql, hpdata;
	private String xzpj_str = "";
	private LinearLayout tl_pgzt;
	private View v_pgzt;
	private CheckBox tv_sfhpj;
	private ImageView iv_telphone,iv_telphone_kh;

	private EditText tv_mmjpxlh, tv_eposcs, tv_xh, tv_shbh, tv_bddh;
	private String _PAD_EPOS_STR = "";
	private String _PAD_SFSCZZBG_STR = "";
	private String canselect = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_servicereport_complete);
		try {
			initVariable();
			initView();
			initListeners();
			if (!backboolean) {
				showProgressDialog();
			}
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("querygzlx");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void initVariable() {

		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		tv_supq = ((TextView) findViewById(R.id.tv_supq));
		tv_suqy = ((TextView) findViewById(R.id.tv_suqy));

		tv_wxaz = (TextView) findViewById(R.id.tv_wxaz);
		tv_clfs = (TextView) findViewById(R.id.tv_clfs);
		tv_cljg = (TextView) findViewById(R.id.tv_cljg);
		tv_bzr = (TextView) findViewById(R.id.tv_bzr);
		tv_phone_kh = (TextView) findViewById(R.id.tv_phone_kh);
		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		tv_bz = (EditText) findViewById(R.id.tv_bz);
		et_xxdz = (EditText) findViewById(R.id.et_xxdz);
		tv_jsdw = (TextView) findViewById(R.id.tv_jsdw);
		et_zsdz = (EditText) findViewById(R.id.et_zsdz);
		tv_xzpj = (TextView) findViewById(R.id.tv_xzpj);
		tv_sfhpj = (CheckBox) findViewById(R.id.tv_sfhpj);
		iv_telphone_kh = (ImageView) findViewById(R.id.iv_telphone_kh);

		et_zdh = (EditText) findViewById(R.id.et_zdh);
		et_sbbm = (EditText) findViewById(R.id.et_sbbm);
		bt_sbbm = (Button) findViewById(R.id.bt_sbbm);
		tv_fwgcs = (TextView) findViewById(R.id.tv_fwgcs);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

		tv_mmjpxlh = (EditText) findViewById(R.id.tv_mmjpxlh);
		tv_eposcs = (EditText) findViewById(R.id.tv_eposcs);
		tv_xh = (EditText) findViewById(R.id.tv_xh);
		tv_shbh = (EditText) findViewById(R.id.tv_shbh);
		tv_bddh = (EditText) findViewById(R.id.tv_bddh);

		tv_wgyy = (EditText) findViewById(R.id.tv_wgyy);

		tl_pgzt = (LinearLayout) findViewById(R.id.tl_pgzt);
		v_pgzt = findViewById(R.id.v_pgzt);

		basiccancel = (Button) findViewById(R.id.include_botton).findViewById(
				R.id.cancel);
		basicconfirm = (Button) findViewById(R.id.include_botton).findViewById(
				R.id.confirm);

		sp_gzlb = (Spinner) findViewById(R.id.sp_gzlb);
		sp_gzlbb = (Spinner) findViewById(R.id.sp_gzlbb);
		sp_gzzl = (Spinner) findViewById(R.id.sp_gzzl);
		spinner_pgzt = (Spinner) findViewById(R.id.spinner_pgzt);
		spinner_zzzdcs = (Spinner) findViewById(R.id.spinner_zzzdcs);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		List pgztList = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "1");
		map.put("name", "一次完工");
		pgztList.add(map);
		map = new HashMap<String, String>();
		map.put("id", "2");
		map.put("name", "申请二次上门");
		pgztList.add(map);

		SimpleAdapter adapter = new SimpleAdapter(ServiceReportsComplete.this,
				pgztList, R.layout.spinner_item, from, to);
		spinner_pgzt.setAdapter(adapter);

		cljg_list.add("完工");
		cljg_list.add("未完工");

		clfs_list.add("上门");
		clfs_list.add("电话");

		itemmap = ServiceReportCache.getData().get(ServiceReportCache.getIndex());

		myDialog = new MyDialog(this);
		data_xq = new ArrayList<Map<String, String>>();
		data_FBF = new ArrayList<Map<String, String>>();
		data = new ArrayList<Map<String, String>>();
		data_gzbm = new ArrayList<Map<String, String>>();
		data_2_gzbm = new ArrayList<Map<String, String>>();
		data_3_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();
		zdcs_list = new ArrayList<Map<String, String>>();
		jsonObjectParser = new JSONObjectParser(this);

		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String readFile = Config.readFile("_PAD_PQSZ",
							getApplicationContext());

					data_xq = jsonObjectParser
							.xqParser(new JSONObject(readFile));

					String FBF = Config.readFile("_PAD_QX_FBF",
							getApplicationContext());

					data_FBF = jsonObjectParser.FBFParser(new JSONObject(FBF));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	protected void initView() {

		title.setText("电子服务报告");
		ServiceReportCache.setItemmap(ServiceReportCache.getData().get(ServiceReportCache.getIndex()));
		itemmap = ServiceReportCache.getItemmap();

		int wx = Integer.parseInt(itemmap.get("kzzd5")) - 1;
//		if (wx != 0 && wx != 1 && wx != 2 && wx != 3 && wx != 4) {
//			wx = 3;
//		}
		tv_pgdh.setText(itemmap.get("oddnumber"));
		tv_bzr.setTag(itemmap.get("faultuser"));
		tv_bzr.setText(itemmap.get("网点名称"));
		tv_wxaz.setText(wxaz.get(wx));
		tv_wxaz.setTag(wx + 1);
		tv_bz.setText(itemmap.get("bz"));
		tv_gzxx.setText(itemmap.get("gzxx"));
		tv_cljg.setText("完工");

		tv_jsdw.setTag(itemmap.get("cx"));
		tv_jsdw.setText(itemmap.get("位置名称"));

		tv_supq.setText(itemmap.get("khbmmc"));
		tv_suqy.setText(itemmap.get("dsmc"));
		tv_supq.setTag(itemmap.get("khbm"));
		tv_suqy.setTag(itemmap.get("ds"));
		tv_fwgcs.setText(itemmap.get("zxsname"));
		et_xxdz.setText(itemmap.get("khxxdz"));
		tv_phone_kh.setText(itemmap.get("usertel"));
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("zxstel"));

			}
		});
		
		iv_telphone_kh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Call(tv_phone_kh.getText().toString());
				
			}
		});

		if ("".equals(itemmap.get("clfs")) || itemmap.get("clfs") == null
				|| itemmap.get("clfs").length() == 0) {

			tv_clfs.setText(clfs_list.get(0));

		} else {

			tv_clfs.setText(clfs_list.get(Integer.parseInt(itemmap.get("clfs")) - 1));
		}

		tv_xzpj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						AddParts.class);

				intent.putExtra("oddnumber", itemmap.get("oddnumber"));
				intent.putStringArrayListExtra("hpdata", hpdata);
				startActivityForResult(intent, 1);
			}

		});
		
	}

	@Override
	protected void initListeners() {

		tv_cljg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// myDialog.redioDialog((TextView)v, "处理结果", cljg_list, "未完工");
				myDialog.inputdialog((TextView) v, "处理结果", cljg_list, "未完工");

			}
		});// 处理结果

		// Button点击
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.et_sbbm:

					break;
				case R.id.bt_sbbm:
					Intent intent = new Intent(getApplicationContext(),
							CaptureActivity.class);
					startActivityForResult(intent, 2);
					break;
				case R.id.confirm://
					try {
						boolean flag = true;
						boolean flag_zx = true;
						String f = ((Map<String, String>) sp_gzlbb
								.getSelectedItem()).get("name");
						String ywxz = (String) tv_wxaz.getText();
						if (!isNotNull(et_xxdz)) {
							flag = false;
							dialogShowMessage_P("请录入详细地址", null);
						} else if ("维修".equals(ywxz)) {
							if (" ".equals(f)) {
								flag = false;
								dialogShowMessage_P("请选择故障小类", null);
							}
						}
						if (tv_sfhpj.isChecked() && flag) {
							if ("".equals(tv_xzpj.getText().toString())) {
								flag = false;
								dialogShowMessage_P("请新增配件", null);
							}
						}

						if (flag) {
							if ("1".equals(_PAD_SFSCZZBG_STR)) {
								if (_PAD_EPOS_STR.equals(itemmap.get("jdgs"))) {
									if ("3".equals((tv_wxaz.getTag() + ""))
											|| "5".equals((tv_wxaz.getTag() + ""))) {
										flag_zx = false;
										dialogShowMessage_P(
												"请录入纸质报告",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface face,
															int in) {
														Intent intent = new Intent(
																ServiceReportsComplete.this,
																BusinessAcceptanceSure.class);
														intent.putExtra(
																"picPath1", "");
														intent.putExtra(
																"zbh",
																itemmap.get("oddnumber"));
														intent.putExtra(
																"fromActivity",
																"ServiceReportsComplete");
														startActivityForResult(
																intent, 21);
													}
												});
									}
								} else {
									flag_zx = false;
									dialogShowMessage_P(
											"请录入纸质报告",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface face,
														int in) {
													Intent intent = new Intent(
															ServiceReportsComplete.this,
															BusinessAcceptanceSure.class);
													intent.putExtra("picPath1",
															"");
													intent.putExtra(
															"zbh",
															itemmap.get("oddnumber"));
													intent.putExtra(
															"fromActivity",
															"ServiceReportsComplete");
													startActivityForResult(
															intent, 21);
												}
											});
								}

							}

							if (flag_zx) {

								if (tv_clfs.getText().equals("电话")) {

									if (true/*
											 * isNotNull(tv_bzr) &&
											 * isNotNull(et_lxdh)
											 */) {
										showProgressDialog();
										Config.getExecutorService().execute(
												new Runnable() {

													@Override
													public void run() {

														getWebService("submit");
													}
												});
									} /*
									 * else {
									 * dialogShowMessage_P("用户姓名和联系电话为必填项",
									 * null); }
									 */

								} else {
									if (isNotNull(tv_cljg)) {
										sure();
									} else {
										dialogShowMessage_P("您录入的数据不完整,请检查录入",
												null);

									}
								}

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						Message localMessage1 = new Message();
						localMessage1.what = -1;// 网络连接出错，你检查你的网络设置
						handler.sendMessage(localMessage1);
					}

					break;
				default:
					skipActivity2(ServiceReportslist.class);
					finish();
					break;

				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		basiccancel.setOnClickListener(onClickListener);
		basicconfirm.setOnClickListener(onClickListener);

		et_sbbm.setOnClickListener(onClickListener);
		bt_sbbm.setOnClickListener(onClickListener);

		/*
		 * tv_supq.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent intent = new Intent(ServiceReportsComplete.this,
		 * ChooseXQ.class); intent.putExtra("data_xq", data_xq);
		 * 
		 * startActivityForResult(intent, 11);
		 * 
		 * } });
		 */

		tv_wxaz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				defined(tv_wxaz, "维修安装", wxaz);
			}
		});
		tv_clfs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				defined(tv_clfs, "处理方式", clfs_list);
			}
		});

		/*
		 * tv_bzr.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent intent = new Intent(ServiceReportsComplete.this,
		 * ChooseAZXQ.class); intent.putExtra("data", data);
		 * 
		 * startActivityForResult(intent, 10); } });
		 */
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
						ServiceReportsComplete.this, gzbm_2_list,
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
						ServiceReportsComplete.this, gzbm_3_list,
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 11 && resultCode == 11 && data != null) {

			tv_supq.setText(data.getStringExtra("name"));
			tv_supq.setTag(data.getStringExtra("id"));
			tv_suqy.setText(data.getStringExtra("qymc"));
			tv_suqy.setTag(data.getStringExtra("qybm"));

			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("wd");
				}
			});
		} else if (requestCode == 2 && resultCode == 2 && data != null) {
			et_sbbm.setText(data.getStringExtra("result"));
		} else if (requestCode == 10 && resultCode == 10 && data != null) {

			tv_bzr.setText(data.getStringExtra("name"));
			tv_bzr.setTag(data.getStringExtra("id"));
			et_xxdz.setText(data.getStringExtra("xxdz"));
			tv_jsdw.setText(data.getStringExtra("wz"));

		} else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
			// 配件hpsql

			hpsql = data.getStringArrayListExtra("hpsql");
			hpdata = data.getStringArrayListExtra("hpdata");
			xzpj_str = "";
			try {
				if (hpdata != null) {
					for (int i = 0; i < hpdata.size(); i++) {
						String[] hps = hpdata.get(i).split(",");
						if (hps.length < 4) {
							xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
									+ "\n";
						} else {
							xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
									+ hps[3] + "\n";
						}
					}
					xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			tv_xzpj.setText(xzpj_str);

		} else if (requestCode == 21 && resultCode == 21 && data != null) {

			if (tv_clfs.getText().equals("电话")) {

				if (true/* isNotNull(tv_bzr) && isNotNull(et_lxdh) */) {
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							getWebService("submit");
						}
					});
				} /*
				 * else { dialogShowMessage_P("用户姓名和联系电话为必填项", null); }
				 */

			} else {
				if (isNotNull(tv_cljg)) {
					sure();
				} else {
					dialogShowMessage_P("您录入的数据不完整,请检查录入", null);

				}
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void sure() {
		// toastShowMessage(itemmap.get("kzzd17"));

		Intent intent = new Intent(ServiceReportsComplete.this,
				ServiceReportsAppraiseActivity.class);

		intent.putExtra("zbh", itemmap.get("oddnumber"));

		intent.putExtra("khbm", tv_supq.getTag() + "");
		intent.putExtra("khbmmc", tv_supq.getText() + "");

		intent.putExtra("ds", tv_suqy.getTag() + "");
		intent.putExtra("dsmc", tv_suqy.getText() + "");

		intent.putExtra("pgzt",
				spinner_pgzt.getSelectedItemPosition() == 0 ? "1" : "2");
		intent.putExtra("zdh", et_zdh.getText() + "");

		intent.putExtra("bzrbm", tv_bzr.getTag() + "");
		intent.putExtra("bzr", tv_bzr.getText() + "");

		intent.putExtra("bzrlxdh", tv_phone_kh.getText() + "");

		intent.putExtra("kzzd5", tv_wxaz.getTag() + "");
		intent.putExtra("kzzd5mc", tv_wxaz.getText() + "");

		intent.putExtra("khxxdz", et_xxdz.getText() + "");
		intent.putExtra("sfhpj", tv_sfhpj.isChecked() ? "1" : "0");

		intent.putExtra("bz", tv_bz.getText() + "");

		intent.putExtra("gzxx", tv_gzxx.getText() + "");

		intent.putExtra("cljg", tv_cljg.getText() + "");
		intent.putExtra("fwyj", tv_cljg.getTag() + "");// 未完工原因、
		intent.putExtra("kzzd17", itemmap.get("kzzd17") + "");
		intent.putExtra("fbsw", itemmap.get("fbsw") + "");
		intent.putExtra("kzzd19", et_zsdz.getText() + "");
		intent.putStringArrayListExtra("hpdata", hpdata);

		intent.putExtra("xzpj", tv_xzpj.getText().toString());

		intent.putExtra("gzlbid", (sp_gzlb.getSelectedItem() == null ? ""
				: ((Map<String, String>) sp_gzlb.getSelectedItem()).get("id")));
		intent.putExtra(
				"gzlbname",
				(sp_gzlb.getSelectedItem() == null ? ""
						: ((Map<String, String>) sp_gzlb.getSelectedItem())
								.get("name")));

		intent.putExtra("gzzlid", (sp_gzzl.getSelectedItem() == null ? ""
				: ((Map<String, String>) sp_gzzl.getSelectedItem()).get("id")));
		intent.putExtra(
				"gzzlname",
				(sp_gzzl.getSelectedItem() == null ? ""
						: ((Map<String, String>) sp_gzzl.getSelectedItem())
								.get("name")));

		intent.putExtra("gzlbbid", (sp_gzlbb.getSelectedItem() == null ? ""
				: ((Map<String, String>) sp_gzlbb.getSelectedItem()).get("id")));
		intent.putExtra(
				"gzlbbname",
				(sp_gzlbb.getSelectedItem() == null ? ""
						: ((Map<String, String>) sp_gzlbb.getSelectedItem())
								.get("name")));

		intent.putExtra(
				"zdcs",
				(spinner_zzzdcs.getSelectedItem() == null ? ""
						: ((Map<String, String>) spinner_zzzdcs
								.getSelectedItem()).get("id")));

		intent.putExtra("mmjpxlh", tv_mmjpxlh.getText() + "");
		intent.putExtra("eposcs", tv_eposcs.getText() + "");
		intent.putExtra("eposxh", tv_xh.getText() + "");
		intent.putExtra("sfbh", tv_shbh.getText() + "");
		intent.putExtra("pddh", tv_bddh.getText() + "");

		intent.putExtra("ddsj", itemmap.get("ddsj"));
		intent.putExtra("slsj", itemmap.get("slsj"));
		intent.putExtra("bzsj", itemmap.get("bzsj"));
		intent.putExtra("wgsj", itemmap.get("wgsj"));
		intent.putExtra("sfhj", hpsql == null ? "否" : "是");

		intent.putExtra("kzzd6", tv_wgyy.getText() + "");

		intent.putExtra("sbbm", et_sbbm.getText() + "");

		startActivity(intent);
		finish();
	}

	@Override
	protected void getWebService(String s) {

		if ("submit".equals(s) && !backboolean) {
			String str1 = itemmap.get("oddnumber");
			String cljg = tv_cljg.getText().toString().trim();
			String czrz3 = "'0'||chr(42)||'"
					+ DataCache.getinition().getUserId() + "'||chr(42)||'"
					+ new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss") + "'";
			try {
				String str2 = "update shgl_ywgl_fwbgszb set "
				// + "pgbm='"+ tv_fbdw.getTag() + "',"
						+ "khbm='"
						+ tv_supq.getTag()
						+ "',"
						+ "ds='"
						+ tv_suqy.getTag()
						+ "',"
						+ "bzr='"
						+ tv_bzr.getTag()
						+ "',"
						// + "bzrlxdh='"+ et_lxdh.getText()+ "',"
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
						+ "kzzd5='"
						+ tv_wxaz.getTag()
						+ "',"
						+ "kzzd19='"
						+ et_zsdz.getText()
						+ "',"
						+ "clfs='"
						+ 2
						+ "',"
						+ "khxxdz='"
						+ et_xxdz.getText()
						+ "',"
						+ "mmjpxlh='"
						+ tv_mmjpxlh.getText()
						+ "',"
						+ "eposcs='"
						+ tv_eposcs.getText()
						+ "',"
						+ "eposxh='"
						+ tv_xh.getText()
						+ "',"
						+ "sfbh='"
						+ tv_shbh.getText()
						+ "',"
						+ "pddh='"
						+ tv_bddh.getText()
						+ "',"
						// + "bz='"+ et_bz.getText()+ "',"
						// + "gzxx='"+ et_gzxx.getText()+ "',"
						+ "cljg= "
						+ ("完工".equals(cljg) ? "'1'" : "'2'" + ",fwyj='"
								+ tv_cljg.getTag() + "'")
						+ ","
						+ "kzzd3='"
						+ DataCache.getinition().getUserId()
						+ "',"
						+ "fwbg_wgsj=sysdate,wcsj=sysdate,kzzd7=sysdate,sfhf='1',djzt='4',czrz5="
						+ czrz3 + " where " + "zbh='" + str1 + "'";
				str2 = str2 + "*sql*";
				Log.e("dd", str2);
				String flag = null;
				flag = this.callWebserviceImp.getWebServerInfo(
						"_PAD_FWBG",
						str2,
						DataCache.getinition().getUserId(),
						str1 + "*" + DataCache.getinition().getUserId() + "*"
								+ "0000" + "*"// 营业厅账号
								+ "0000" // 营业厅密码
						, "uf_json_setdata2", this).getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message localMessage2 = new Message();
					localMessage2.what = 1;// 完成
					this.handler.sendMessage(localMessage2);
				} else {
					Message localMessage2 = new Message();
					localMessage2.what = 1;// 完成
					this.handler.sendMessage(localMessage2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message localMessage1 = new Message();
				localMessage1.what = -1;// 网络连接出错，你检查你的网络设置
				this.handler.sendMessage(localMessage1);
			}
		}
		/**
		 * 网点
		 */
		if ("wd".equals(s) && !backboolean) {

			JSONObject webServerInfo;
			try {
				webServerInfo = callWebserviceImp.getWebServerInfo("_PAD_WD",
						itemmap.get("fbsw") + "*" + tv_supq.getTag(),
						"uf_json_getdata", getApplicationContext());
				// {"tableA":[{"ccgl_wlsjb_email":"城区","ccgl_wlsjb_wlsjmc":"a小区2","ccgl_wlsjb_xxdz":"","ccgl_wlsjb_wlsjbm":"0000002"},
				// {"ccgl_wlsjb_email":"城区","ccgl_wlsjb_wlsjmc":"a小区1","ccgl_wlsjb_xxdz":"详细地址","ccgl_wlsjb_wlsjbm":"0000001"}],"flag":"2","costTime":197}
				String flag = webServerInfo.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = webServerInfo.getJSONArray("tableA");
					data.clear();
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();

						item.put("wz", temp.getString("ccgl_wlsjb_email"));
						item.put("name", temp.getString("ccgl_wlsjb_wlsjmc"));
						item.put("id", temp.getString("ccgl_wlsjb_wlsjbm"));
						item.put("xxdz", temp.getString("ccgl_wlsjb_xxdz"));

						data.add(item);
					}

					Message localMessage2 = new Message();
					localMessage2.what = 4;// 完成
					this.handler.sendMessage(localMessage2);
				} else if (Integer.parseInt(flag) == 0) {

					Message localMessage2 = new Message();
					localMessage2.what = 3;// 完成
					this.handler.sendMessage(localMessage2);

				} else {
					Message localMessage2 = new Message();
					localMessage2.what = 2;// 完成
					this.handler.sendMessage(localMessage2);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Message localMessage1 = new Message();
				localMessage1.what = -1;// 网络连接出错，你检查你的网络设置
				this.handler.sendMessage(localMessage1);
			}

		}

		/**
		 * 故障类别
		 */
		if ("querygzlx".equals(s)) {

			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", itemmap.get("jdgs"), "uf_json_getdata",
						this);
				JSONObject jsonObject1 = callWebserviceImp.getWebServerInfo(
						"_PAD_EPOS", "1", "uf_json_getdata", this);
				JSONObject jsonObject3 = callWebserviceImp.getWebServerInfo(
						"_PAD_SFSCZZBG", itemmap.get("jdgs"),
						"uf_json_getdata", this);

				// JSONObject jsonObject_zdcs =
				// callWebserviceImp.getWebServerInfo(
				// "_PAD_ZCZDCSMC", "", "uf_json_getdata",
				// this);

				// 二次派工是否可选
				JSONObject jsonObject4 = callWebserviceImp.getWebServerInfo(
						"_PAD_SFZDPG",
						itemmap.get("fbsw") + "*" + itemmap.get("jdgs"),
						"uf_json_getdata", this);
				String flag4 = jsonObject4.getString("flag");
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
				if (Integer.parseInt(flag4) > 0) {
					JSONArray jsonArray4 = jsonObject4.getJSONArray("tableA");
					JSONObject temp = jsonArray4.getJSONObject(0);
					canselect = temp.getString("sfec");
				}

				// String flag_zdcs = jsonObject_zdcs.getString("flag");
				// if(Integer.parseInt(flag_zdcs) > 0){
				// JSONArray jsonArray_zdcs =
				// jsonObject_zdcs.getJSONArray("tableA");
				// for(int i=0;i<jsonArray_zdcs.length();i++){
				// JSONObject temp = jsonArray_zdcs.getJSONObject(i);
				// Map<String, String> item = new HashMap<String, String>();
				// item.put("id", temp.getString("zdbm"));
				// item.put("name", temp.getString("zdmc"));
				// zdcs_list.add(item);
				// }
				// } else {
				// Message msg = new Message();
				// msg.what = -3;// 失败
				// handler.sendMessage(msg);
				// }

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
					msg.what = 5;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = -2;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = -2;
				handler.sendMessage(msg);
			}

		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {

			case 4:

				break;
			case 3:
				dialogShowMessage_P("网点数据为空", null);

				break;
			case 2:
				dialogShowMessage_P("网点查询失败", null);

				break;
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case FAIL:
				dialogShowMessage_P("获取数据失败", null);
				break;
			case 1:
				dialogShowMessage_P("电话消单成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face, int in) {
								skipActivity2(MainActivity.class);
								finish();
							}
						});

				break;
			case 5:
				if (!"1".equals(canselect)) {
					v_pgzt.setVisibility(View.GONE);
					tl_pgzt.setVisibility(View.GONE);
				}

				SimpleAdapter adapter = new SimpleAdapter(
						ServiceReportsComplete.this, data_gzbm,
						R.layout.spinner_item, from, to);

				sp_gzlb.setAdapter(adapter);

				adapter = new SimpleAdapter(ServiceReportsComplete.this,
						zdcs_list, R.layout.spinner_item, from, to);

				spinner_zzzdcs.setAdapter(adapter);

				break;
			case -2:
				dialogShowMessage_P("故障类别信息获取失败！", null);
				break;
			case -3:
				dialogShowMessage_P("自助终端厂商信息获取失败！", null);
				break;
			}

			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
