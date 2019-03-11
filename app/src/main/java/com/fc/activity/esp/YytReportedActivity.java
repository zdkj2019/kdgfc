package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.f;
import com.fc.R;
import com.fc.Parser.JSONObjectParser;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;

/**
 * 营业厅报障
 * 
 * @author cheng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class YytReportedActivity extends FrameActivity {

	private SharedPreferences spf;
	private Editor spfe;
	private CheckBox efb_khdx;
	private EditText efb_khxm,efb_khsj,efb_khdz,efb_bwnr,efb_bz;
	private TextView efb_sssqx,efb_dyxz,efb_khsspq,efb_khwd,efb_yylxxm,eefb_khwd,eefb_khsspq,eefb_sssqx;
	private Button confirm, cancel;
	private JSONObjectParser jsonObjectParser;
	String flag;
	private ArrayList<Map<String, String>> data_khsspq; // 所属片区缓存数据
	private ArrayList<Map<String, String>> data_khwd; // 所属网点缓存数据
	
	private JSONObject data_pgdm;
	private  JSONObject jsonObject_khwd;
	private String readFile_khwd = null;
	private String sspqid_str = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_yytreported);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("xq");
			}
		});

	}

	

	@Override
	protected void initVariable() {

		efb_khwd = (TextView) this.findViewById(R.id.efb_khwd);
		efb_khsspq = (TextView) this.findViewById(R.id.efb_khsspq);
		efb_sssqx = (TextView) this.findViewById(R.id.efb_sssqx);
		efb_dyxz = (TextView) this.findViewById(R.id.efb_dyxz);
		efb_khxm = (EditText) this.findViewById(R.id.efb_khxm);
		efb_khsj = (EditText) this.findViewById(R.id.efb_khsj);
		efb_khdz = (EditText) this.findViewById(R.id.efb_khdz);
		efb_khdx = (CheckBox) this.findViewById(R.id.efb_khdx);
		efb_bwnr = (EditText) this.findViewById(R.id.efb_bwnr);
		efb_bz = (EditText) this.findViewById(R.id.efb_bz);

		eefb_khwd = (TextView) this.findViewById(R.id.eefb_khwd);
		eefb_khsspq = (TextView) this.findViewById(R.id.eefb_khsspq);
		eefb_sssqx = (TextView) this.findViewById(R.id.eefb_sssqx);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

		data_khsspq = new ArrayList<Map<String, String>>();
		data_khwd = new ArrayList<Map<String, String>>();
		jsonObjectParser = new JSONObjectParser(this);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		spf = getPreferences(MODE_PRIVATE);
		// 设置默认选择
		
		efb_khsspq.setText(spf.getString("khsspq_name", ""));
		eefb_khsspq.setTag(spf.getString("khsspq_id", ""));
		efb_sssqx.setText(spf.getString("sssqx_name", ""));
		eefb_sssqx.setTag(spf.getString("sssqx_id", ""));
		efb_khwd.setText(spf.getString("khwd_name", ""));
		eefb_khwd.setTag(spf.getString("khwd_id", ""));
		efb_khxm.setText(spf.getString("khxm", ""));
		efb_khdz.setText(spf.getString("khdz", ""));
		efb_khsj.setText(spf.getString("khsj", ""));
		efb_dyxz.setText(spf.getString("dyxz", ""));

		spfe = spf.edit();

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
				if (isNotNull(efb_khwd)
						&& isNotNull(efb_khsspq)&& isNotNull(efb_khsj)
						&& isNotNull(efb_khdz)&& isNotNull(efb_bwnr)) {
					dialogShowMessage("确认提交 ？",
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
				} else {
					dialogShowMessage_P("请填写完整", null);
				}
			}
		});
		
		//所属市区县
		efb_sssqx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(YytReportedActivity.this,
						ChooseSssqx.class);
				intent.putExtra("data_khsspq", data_khsspq);
				startActivityForResult(intent, 11);
			}
		});

		//所属片区
		efb_khsspq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(YytReportedActivity.this,
						ChooseXQ.class);
				String sssqxid = eefb_sssqx.getTag()==null?"":eefb_sssqx.getTag().toString().trim();
				intent.putExtra("data_khsspq", data_khsspq);
				intent.putExtra("sssqxid", sssqxid);
				startActivityForResult(intent, 12);
			}
		});
		
		//客户网点
		efb_khwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					
					String sspqid = (String) eefb_khsspq.getTag();
					sspqid_str = sspqid;
					if("".equals(sspqid)||sspqid==null){
						dialogShowMessage_P("请选择所属片区",null);
					}else{
						data_khwd = new ArrayList<Map<String, String>>();
						
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {
							@Override
							public void run() {
								getWebService("_PAD_WD");
							}
						});
					}
					
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = NETWORK_ERROR;// 网络不通
					handler.sendMessage(msg);
				}
				
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 11 && resultCode == 11 && data != null) {
			efb_sssqx.setText(data.getStringExtra("qymc"));
			eefb_sssqx.setTag(data.getStringExtra("qybm"));
			
			efb_khsspq.setText("");
			eefb_khsspq.setTag("");
			//客户网点
			efb_khwd.setText("");
			eefb_khwd.setTag("");
			efb_dyxz.setText("");
		}else if (requestCode == 12 && resultCode == 12 && data != null) {
			efb_khsspq.setText(data.getStringExtra("name"));
			eefb_khsspq.setTag(data.getStringExtra("id"));
			
			//客户网点
			efb_khwd.setText("");
			eefb_khwd.setTag("");
			efb_dyxz.setText("");
		}else if(requestCode == 15 && resultCode == 15 && data != null) {
			efb_khwd.setText(data.getStringExtra("name"));
			eefb_khwd.setTag(data.getStringExtra("id"));
			efb_dyxz.setText(data.getStringExtra("email"));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	

	@Override
	protected void getWebService(String s) {

		if ("xq".equals(s)) {
			data_khsspq = new ArrayList<Map<String, String>>();
			final JSONObject jsonObject_sspq;
			String readFile_sspq = null;
			try {
				readFile_sspq = Config.readFile("_PAD_PQSZ", this);
				if (readFile_sspq == null) {// _PAD_XQ_KD_LC
					jsonObject_sspq = callWebserviceImp.getWebServerInfo(
							"_PAD_PQSZ", "1", "uf_json_getdata", this);

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_PQSZ",
									jsonObject_sspq.toString(),
									getApplicationContext());
						}
					});
				} else {
					jsonObject_sspq = new JSONObject(readFile_sspq);
				}
				data_pgdm =  callWebserviceImp.getWebServerInfo(
						"_PAD_ZDFBF", "1", "uf_json_getdata", this);
				
				try {
					data_khsspq = jsonObjectParser.xqParser(jsonObject_sspq);
					//data_khwd = jsonObjectParser.KhwdParser(jsonObject_khwd);
					Message msg = new Message();
					msg.what = 2;// 解析出错
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = -3;// 解析出错
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {// khbm,ds,kdzh,cfsj,khxxdz
				int random = (int)((Math.random()*9+1)*1000);
				int ywlxid = 2;//wxaz.indexOf(efb_ywlx.getText()) + 1;
				String khwd = (String) eefb_khwd.getTag();
				
				String pgdm = "00000001";
				
				if("1".equals(data_pgdm.getString("flag"))){
					data_pgdm = (JSONObject) data_pgdm.getJSONArray("tableA").get(0);
					pgdm = data_pgdm.getString("cs");
				}
				
				String sql = "insert into shgl_ywgl_fwbgszb (zbh,djzt,pgbm,kzzd5,jdgs,fwgcs,sf,kzzd18,kzzd10,kzzd11,kzzd13,khbm,ds,bzr,sgdh,bzrlxdh,kzzd14,khxxdz,gzxx,bz,lxdh,kzzd17,bzsj,czy,czsj,czrz1) "
						+ "values (" + "'%s'," + "'" + "1"
						+ "','" + pgdm//eefb_fbdw.getTag() 
						+ "','" + ywlxid + "','"
						+ ""//eefb_rzsblx.getTag()
						+ "','"
						+ ""//efb_smsf.getText()
						+ "','"
						+ ""//eefb_yylxxm.getTag()
						+ "','"
						+ ""//efb_smyq.getText()
						+ "','"
						+ ""//efb_fbr.getText()
						+ "','"
						+ ""//efb_fbrsj.getText()
						+ "','"
						+ ""//(efb_fbrdx.isChecked()==true?"0":"1")
						+ "','"
						+ eefb_khsspq.getTag()
						+ "','"
						+ eefb_sssqx.getTag()
						+ "','"
						+ eefb_khwd.getTag()
						+ "','"
						+ efb_khxm.getText()
						+ "','"
						+ efb_khsj.getText()
						+ "','"
						+ (efb_khdx.isChecked()==true?"0":"1")
						+ "','"
						+ efb_khdz.getText()
						+ "','"
						+ efb_bwnr.getText()
						+ "','"
						+ efb_bz.getText()
						+ "','"
						+"2"
						+ "','"
						+random
						+ "',sysdate,'"
						+DataCache.getinition().getUserId()
						+ "',sysdate,'"
						+ "0*"+DataCache.getinition().getUserId()+"*"+new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss") 
						+ "')";
				Log.e("YytReportedActivity", sql);
				flag = callWebserviceImp.getWebServerInfo("c#_PAD_BZLR", sql,
						DataCache.getinition().getUserId(), ""+"0001*0001"+"",//eefb_fbdw.getTag() + "*" + eefb_rzsblx.getTag(),
						"uf_json_setdata2", this).getString("flag");
				Log.e("YytReportedActivity", flag);
				if (Integer.parseInt(flag) > 0) {
					Message localMessage2 = new Message();
					localMessage2.what = 1;// 完成
					handler.sendMessage(localMessage2);

				} else {
					Message localMessage3 = new Message();
					localMessage3.what = 0;// 服务报告完成失败，请检查后重试...
					handler.sendMessage(localMessage3);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message localMessage1 = new Message();
				localMessage1.what = 0;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(localMessage1);
			}
			// 保存填写记录
//			spfe.putString("bzr", efb_fbr.getText().toString().trim());
//			spfe.putString("lxdh", efb_fbrsj.getText().toString().trim());
			
			spfe.putString("khsspq_name", efb_khsspq.getText().toString().trim());
			spfe.putString("khsspq_id", eefb_khsspq.getTag().toString().trim());
			spfe.putString("sssqx_name", efb_sssqx.getText().toString().trim());
			spfe.putString("sssqx_id", eefb_sssqx.getTag().toString().trim());
			spfe.putString("khwd_name", efb_khwd.getText().toString().trim());
			spfe.putString("khwd_id", eefb_khwd.getTag().toString().trim());
			spfe.putString("khxm", efb_khxm.getText().toString().trim());
			spfe.putString("khdz", efb_khdz.getText().toString().trim());
			spfe.putString("khsj", efb_khsj.getText().toString().trim());
			spfe.putString("dyxz", efb_dyxz.getText().toString().trim());
			
			spfe.commit();

		}
		
		if("_PAD_WD".equals(s)){
			try {
				
				jsonObject_khwd = callWebserviceImp.getWebServerInfo(
						"_PAD_WD", sspqid_str, "uf_json_getdata", YytReportedActivity.this);
				data_khwd = jsonObjectParser.KhwdParser(jsonObject_khwd);
				Intent intent = new Intent(YytReportedActivity.this,
						ChooseKhwd.class);
				String pqid = eefb_khsspq.getTag()==null?"":eefb_khsspq.getTag().toString();
				intent.putExtra("data_khwd", data_khwd);
				intent.putExtra("pqid", pqid);
				progressDialog.dismiss();
				startActivityForResult(intent, 15);
			} catch (Exception e) {
				Message localMessage1 = new Message();
				localMessage1.what = -1;// 获取失败
				handler.sendMessage(localMessage1);
			}
			
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -3:
				dialogShowMessage_P("小区数据解析失败", null);
				break;
			case -2:
				dialogShowMessage_P("小区数据获取失败", null);
				break;
			case -1:
				dialogShowMessage_P("获取基础数据失败", null);
				break;
			case 1:
				dialogShowMessage_P("报障提交完成",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case 0:
				dialogShowMessage_P("报障失败，请检查后重试...错误标识：" + flag, null);
				break;

			case 2:
				// 维护厂商

				break;
			}
			if(progressDialog!=null){
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
