package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.definition.Sign;
import com.fc.utils.Config;

/**
 * 单独的评价
 * @author wang
 *
 */
@SuppressLint({ "HandlerLeak" })
public class ServiceReportsAppraise extends FrameActivity implements
		Sign {
	private Button cancel;
	private Button confirm;
	private EditText et_ps;
	private EditText et_user;
	private String flag;
	private List<String> hpsql;
	private Intent intent;
	private Map<Integer, RelativeLayout> mmap;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
//	private RadioButton radioButton3;
	private RadioButton radioButton4;
	private RadioButton radioButton5;
	private CheckBox radioButton6;
	private RelativeLayout radioRL1;
	private RelativeLayout radioRL2;
//	private RelativeLayout radioRL3;
	private RelativeLayout radioRL4;
	private RelativeLayout radioRL5;
	private int index;
	private Map<String, String> itemmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		appendMainBody(R.layout.activity_servicereport_appr);
		initVariable();
		initView();
		initListeners();
	}

	@SuppressWarnings("rawtypes")
	private String generateSql(String paramString1, String paramString2,
			List<String> paramList) {
		if ((paramList != null) && (paramList.size() > 0)) {
			StringBuilder localStringBuilder = new StringBuilder();
			Iterator localIterator = paramList.iterator();
			while (true) {
				if (!localIterator.hasNext()) {
					String str = localStringBuilder.substring(0, -5
							+ localStringBuilder.length());
					return paramString1 + "*sql*" + paramString2 + "*sql*"
							+ str;
				}
				localStringBuilder.append((String) localIterator.next());
			}
		}
		return paramString1 + "*sql*" + paramString2;
	}

	protected void getWebService(String paramString) {
		String str1 = itemmap.get("oddnumber");
//		String gzlb2id = intent.getStringExtra("gzlb2id");
//		String gzlb1id = intent.getStringExtra("gzlb1id");
		if ("submit".equals(paramString))
			try {
//				this.sbsql = ("update shgl_ywgl_fwbgssbb set" + " xmmc='"
//						+ intent.getStringExtra("sblxid")
//						// + "',ydbm='"+itemmap.get("sbbm")//设备编码
//						// + "',sbbm='"+itemmap.get("sbbm")//设备编码
//						// + "',gzbm1='"+
//						// fthis.intent.getStringExtra("gzbm1")//故障类型 null
//						+ "' where zbh='" + str1 + "'");
				String str2 = "update shgl_ywgl_fwbgszb set "
				// +"sgdh='"+ itemmap.get("sgdh")//手工单号
//						+ "gzxx='"
//						+ intent.getStringExtra("gzxx")// 故障现象
//						+ "',bzr='"
//						+ itemmap.get("faultuser")// 报障人
//						+ "',bzrlxdh='"
//						+ itemmap.get("usertel")// 报障人电话
//						// + "',fwfylx='"+ itemmap.get("state")//服务费用类型
//						+ "',kzzd6='" + intent.getStringExtra("sfsfid")//是否收费
//						+ "',kzzd5='" + intent.getStringExtra("djlxid")//业务类型
//						+ "',kzzd14='" + (!gzlb1id .equals("null") ? gzlb1id : "")
//						+ "',kzzd15='" + (!gzlb2id .equals("null") ? gzlb2id : "")
//						+ "',clfs='" +itemmap.get("clfs")
						+ "kzzd13='" + this.et_user.getText() + "*"
						+ this.et_ps.getText() + "',fwpj='" + addone(index)
//						+ "',cljg='" + this.intent.getStringExtra("处理结果")
						+ "',ddsj="
						+ "to_date('"
						+ itemmap.get("ddsj")
						+ "','yyyy-mm-dd hh24:mi:ss')"// 到达时间
						+ ",kzzd3='"
						+ DataCache.getinition().getUserId()
						+ "',fwbg_wgsj=sysdate,wcsj=sysdate,=sysdate,"
						// + "sr1="
						// + this.intent.getStringExtra("rgfy")+","// 人工费用
						+ "kzzd7=sysdate,sfhf='1',djzt='3' where zbh='" + str1
						+ "'";

				this.flag = this.callWebserviceImp.getWebServerInfo(
						"_PAD_FWBG",
						str2,
						DataCache.getinition().getUserId(),
						str1 + "*" + DataCache.getinition().getUserId() + "*"
								+ this.et_user.getText() + "*"
								+ this.et_ps.getText(), "uf_json_setdata2",this)
						.getString("flag");

				int flags = Integer.parseInt(this.flag);
				if (flags > 0) {
					Message localMessage2 = new Message();
					localMessage2.what = 1;// 完成
					this.handler.sendMessage(localMessage2);
					return;
				} else if (flags == -5) {
					Message localMessage3 = new Message();
					localMessage3.what = -5;// 密码错误
					this.handler.sendMessage(localMessage3);
				} else if (flags == -6) {
					Message localMessage3 = new Message();
					localMessage3.what = -6;// 不是本营业厅的帐号
					this.handler.sendMessage(localMessage3);

				}

				return;
			} catch (Exception e) {
				e.printStackTrace();
				Message localMessage1 = new Message();
				localMessage1.what = -1;// 网络连接出错，你检查你的网络设置
				this.handler.sendMessage(localMessage1);
			}
	}

	protected void initListeners() {

		View.OnClickListener local2 = new View.OnClickListener() {

			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:// 确认

					if (radioButton6.isChecked()) {
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {

								getWebService("submit");
							}
						});

					} else {
						dialogShowMessage_P("对不起，你必须确认您对上述信息已经认真核实，确认无误。", null);
					}
					break;
				case R.id.cancel:
					skipActivity2(ServiceReportslist.class);
					break;
				case R.id.bt_topback:
					skipActivity2(ServiceReportslist.class);
					break;
				}
			}
		};
		this.topBack.setOnClickListener(local2);
		this.confirm.setOnClickListener(local2);
		this.cancel.setOnClickListener(local2);

		final RelativeLayout[] arrayRL = { radioRL1, radioRL2, 
				radioRL4, radioRL5 };
		final ArrayList<RadioButton> arrayRadioButton = new ArrayList<RadioButton>();
		arrayRadioButton.add(radioButton1);
		arrayRadioButton.add(radioButton2);
//		arrayRadioButton.add(radioButton3);
		arrayRadioButton.add(radioButton4);
		arrayRadioButton.add(radioButton5);

		View.OnClickListener radioOnClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = arrayRL.length;
				for (int j = 0;; j++) {
					if (j >= i) {
						RadioButton localRadioButton = (RadioButton) findViewById(v
								.getId());

						localRadioButton.getText().toString();
						mmap.get(Integer.valueOf(v.getId()))
								.setBackgroundColor(Color.parseColor("#CDE7F8"));
						localRadioButton.setChecked(true);
						index = arrayRadioButton.indexOf(localRadioButton);
						if (index >= 0 && index <= 2) {
							dialogShowMessage_P("感谢您对我们本次服务工作的支持", null);
						}
						if (index > 2) {
							dialogShowMessage_P("我们非常重视您的评价，我们将在24小时内与您联系",
									null);
						}
						return;
					}
					arrayRadioButton.get(j).setChecked(false);
					arrayRL[j].setBackgroundResource(R.drawable.edittext_bg);
				}

			}

		};

		radioButton1.setOnClickListener(radioOnClickListener);
		radioButton2.setOnClickListener(radioOnClickListener);
//		radioButton3.setOnClickListener(radioOnClickListener);
		radioButton4.setOnClickListener(radioOnClickListener);
		radioButton5.setOnClickListener(radioOnClickListener);

	}

	@SuppressLint({ "UseSparseArrays" })
	protected void initVariable() {
		title.setText("服务报告评价");
		int index = ServiceReportCache.getIndex();
		itemmap = ServiceReportCache.getData().get(index);

		radioButton1 = ((RadioButton) findViewById(R.id.radioButton1));
		radioButton2 = ((RadioButton) findViewById(R.id.radioButton2));
//		radioButton3 = ((RadioButton) findViewById(R.id.radioButton3));
		radioButton4 = ((RadioButton) findViewById(R.id.radioButton4));
		radioButton5 = ((RadioButton) findViewById(R.id.radioButton5));
		radioButton6 = ((CheckBox) findViewById(R.id.RadioButton6));
		radioRL1 = ((RelativeLayout) findViewById(R.id.radioRL1));
		radioRL2 = ((RelativeLayout) findViewById(R.id.radioRL2));
//		radioRL3 = ((RelativeLayout) findViewById(R.id.radioRL3));
		radioRL4 = ((RelativeLayout) findViewById(R.id.radioRL4));
		radioRL5 = ((RelativeLayout) findViewById(R.id.radioRL5));
		confirm = ((Button) findViewById(R.id.incl).findViewById(R.id.confirm));
		cancel = ((Button) findViewById(R.id.incl).findViewById(R.id.cancel));
/*		et_user = ((EditText) findViewById(R.id.et_user));
		et_ps = ((EditText) findViewById(R.id.et_ps));*/
		mmap = new HashMap<Integer, RelativeLayout>();
		mmap.put(Integer.valueOf(R.id.radioButton1), radioRL1);
		mmap.put(Integer.valueOf(R.id.radioButton2), radioRL2);
//		mmap.put(Integer.valueOf(R.id.radioButton3), radioRL3);
		mmap.put(Integer.valueOf(R.id.radioButton4), radioRL4);
		mmap.put(Integer.valueOf(R.id.radioButton5), radioRL5);
		hpsql = getIntent().getStringArrayListExtra("货品明细sql");


	}

	protected void initView() {
	
		this.intent = getIntent();
	}

	@Override
	public void onBackPressed() {
		skipActivity2(ServiceReportslist.class);
		finish();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message paramAnonymousMessage) {
			super.handleMessage(paramAnonymousMessage);
			switch (paramAnonymousMessage.what) {

			case -1:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置,错误标志：" + flag, null);
				break;
			case 1:
				dialogShowMessage_P("服务报告提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face, int in) {
								skipActivity2(MainActivity.class);
								finish();
							}
						});

				break;
			case -5:
				dialogShowMessage_P("请输入正确的密码", null);
				break;
			case -6:
				dialogShowMessage_P("请输入工单对应营业厅的帐号和密码", null);
				break;
			}
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			
		}
	};

}