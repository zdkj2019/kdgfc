package com.fc.activity.esp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.fc.R;
import com.fc.Parser.JSONObjectParser;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;
import com.fc.utils.SpinnerAdapter;

/**
 * 派工单明细查询 显示
 * 
 * @author cheng
 */
public class PGDCaseActivity extends FrameActivity {

	private Spinner strzh,strzt;
	private TextView strpgdh;
	JSONObjectParser jsonObjectParser;
//		  
	private Button confirm,cancel;
	private RadioGroup rg_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_pgdcase);
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
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		
		strzh = (Spinner) findViewById(R.id.strzh);
		strzt = (Spinner) findViewById(R.id.strzt);
		strpgdh = (TextView) findViewById(R.id.strpgdh);
		
		jsonObjectParser = new JSONObjectParser(getApplicationContext());
		
		strpgdh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(PGDCaseActivity.this, ChooseXQ.class);
				intent.putExtra("data_khsspq", data_xq);
				startActivityForResult(intent, 12);
				
			}
		});

	}

	@Override
	protected void initView() {
		
		title.setText(DataCache.getinition().getTitle());
		rg_time = (RadioGroup) findViewById(R.id.rg_time);
		
	}

	@Override
	protected void initListeners() {
//		
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
					String zh = strzh.getSelectedItem().toString();
					String zt = strzt.getSelectedItem().toString();
					Object dh = strpgdh.getTag();
					
					int cx_time = rg_time.getCheckedRadioButtonId()==R.id.rb_time_0?5:10;
					
					if(dh == null){
						
						dh = "";
					}
					
					Intent intent = new Intent(PGDCaseActivity.this,PGDquerychooseActivity.class);
					
					intent.putExtra("zh", zh);
					intent.putExtra("cx_time", cx_time);
					intent.putExtra("zt", zt);
					intent.putExtra("dh", dh.toString());
					
					startActivity(intent);
					
//					finish();
					
					break;

				case R.id.cancel:
					onBackPressed();
					break;

				case R.id.bt_topback:
					onBackPressed();
					break;
				

				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);

		
	}
	ArrayList<Map<String, String>> data_xq;
	@Override
	protected void getWebService(String s) {

		if ("xq".equals(s)) {
			
			data_xq = new ArrayList<Map<String, String>>();
			
			final JSONObject jsonObject;
			
			String readFile = null;
			readFile = Config.readFile("_PAD_PQSZ", this);
			try {
				if (readFile == null) {
					jsonObject = callWebserviceImp.getWebServerInfo("_PAD_PQSZ","","uf_json_getdata",this);
					
					Config.getExecutorService().execute(new Runnable() {
						
						@Override
						public void run() {
							
							Config.writeFile("_PAD_PQSZ", jsonObject.toString(),getApplicationContext());
						}
					});
				} else {
					jsonObject = new JSONObject(readFile);
				}
				try {
					data_xq = jsonObjectParser.xqParser(jsonObject);
					Message msg = new Message();
					msg.what = 2;// 解析出错
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = -3;// 解析出错
					handler.sendMessage(msg);
				}
				
			}catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		

	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if(requestCode == 12 && resultCode == 12){
			
			String id = data.getStringExtra("id");
			String name = data.getStringExtra("name");
			
			strpgdh.setText(name);
			strpgdh.setTag(id);
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

			case 2:
				// 维护厂商

				break;
			}
			if (!backboolean) {
				progressDialog.dismiss();
			}

		}
	};
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("currType", 5);
		startActivity(intent);
		finish();
	}
}
