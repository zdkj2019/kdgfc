package com.fc.activity.esp;
//package com.fc.activity.esp;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.fc.Parser.JSONObjectParser;
//import com.fc.activity.FrameActivity;
//import com.fc.cache.DataCache;
//import com.fc.R;
//import com.fc.utils.Config;
//
//public class AddPartsAdd20151202 extends FrameActivity{
//	
//	private TextView part_etx_pjdl,part_etx_pjzl,part_etx_pjxl;
//	private EditText part_etx_sl,part_etx_bz;
//	private String flag;
//	private ArrayList<Map<String, String>> data,dl_data,zl_data,xl_data;
//	private Button confirm, cancel;
//	private JSONObject jsonObject;
//	private JSONObjectParser jsonObjectParser;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// 默认焦点不进入输入框，避免显示输入法
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		appendMainBody(R.layout.activity_addpartsadd);
//		initVariable();
//		initView();
//		initListeners();
//	}
//
//	@Override
//	protected void initVariable() {
//		data = new ArrayList<Map<String, String>>();
//		dl_data = new ArrayList<Map<String, String>>();
//		zl_data = new ArrayList<Map<String, String>>();
//		xl_data = new ArrayList<Map<String, String>>();
//		jsonObjectParser = new JSONObjectParser(this);
//		
//		try {
//			//大类
//			String dl_str = Config.readFile("_PAD_SBPJDL", this);
//			JSONObject jsonObject_dl = new JSONObject(dl_str);
//			dl_data = jsonObjectParser.PjlxParser(jsonObject_dl,"dl");
//			//中类
//			String zl_str = Config.readFile("_PAD_SBPJZL", this);
//			JSONObject jsonObject_zl = new JSONObject(zl_str);
//			zl_data = jsonObjectParser.PjlxParser(jsonObject_zl,"zl");
//			//小类
//			String xl_str = Config.readFile("_PAD_SBPJXL", this);
//			JSONObject jsonObject_xl = new JSONObject(xl_str);
//			xl_data = jsonObjectParser.PjlxParser(jsonObject_xl,"xl");
//		} catch (Exception e) {
//			Message msg = new Message();
//			msg.what = FAIL;// 失败
//			handler.sendMessage(msg);
//		}
//	}
//
//	@Override
//	protected void initView() {
//		part_etx_pjdl = (TextView) findViewById(R.id.part_etx_pjdl);
//		part_etx_pjzl = (TextView) findViewById(R.id.part_etx_pjzl);
//		part_etx_pjxl = (TextView) findViewById(R.id.part_etx_pjxl);
//		
//		part_etx_sl = (EditText) findViewById(R.id.part_etx_sl);
//		part_etx_bz = (EditText) findViewById(R.id.part_etx_bz);
//		confirm = (Button) findViewById(R.id.include_botto).findViewById(
//				R.id.confirm);
//		cancel = (Button) findViewById(R.id.include_botto).findViewById(
//				R.id.cancel);
//		
//		title.setText("新增配件");
//	}
//
//	@Override
//	protected void initListeners() {
//		OnClickListener backonClickListener = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		};
//
//		topBack.setOnClickListener(backonClickListener);
//		cancel.setOnClickListener(backonClickListener);
//
//		confirm.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(part_eetx_pjxl.getTag()==null||"".equals(part_eetx_pjxl.getTag())){
//					dialogShowMessage_P("请选择配件小类", null);
//				}else if("".equals(part_etx_sl.getText().toString())){
//					dialogShowMessage_P("请录入配件数量", null);
//				}else{
//					String name = part_etx_pjxl.getText().toString();
//					String id = part_eetx_pjxl.getTag().toString();
//					String num = part_etx_sl.getText().toString();
//					String bz = part_etx_bz.getText().toString();
//					Intent intent = getIntent();
//
//					intent.putExtra("id", id);
//					intent.putExtra("name", name);
//					intent.putExtra("num", num);
//					intent.putExtra("bz", bz);
//					setResult(21, intent);
//					finish();
//				}
//			}
//		});
//		
//		part_etx_pjdl.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(AddPartsAdd20151202.this,
//						ChoosePjlx.class);
//				intent.putExtra("type", "dl");
//				intent.putExtra("data", dl_data);
//				startActivityForResult(intent, 11);
//			}
//		});
//		
//		part_etx_pjzl.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				data.clear();
//				if(part_eetx_pjdl.getTag()==null){
//					dialogShowMessage_P("请选择配件类型", null);
//				}else{
//					String pjdl = part_eetx_pjdl.getTag().toString();
//					if(!"".equals(pjdl)){ 
//						for(int i=0;i<zl_data.size();i++){
//							Map<String, String> item  = zl_data.get(i);
//							if(pjdl.equals(item.get("type"))){
//								data.add(item);
//							}
//						}
//					}
//					Intent intent = new Intent(AddPartsAdd20151202.this,
//							ChoosePjlx.class);
//					intent.putExtra("type", "zl");
//					intent.putExtra("data", data);
//					startActivityForResult(intent, 12);
//				}
//				
//			}
//		});
//		
//		part_etx_pjxl.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				data.clear();
//				if(part_eetx_pjzl.getTag()==null){
//					dialogShowMessage_P("请选择配件中类", null);
//				}else{
//					String pjzl = part_eetx_pjzl.getTag().toString();
//					if(!"".equals(pjzl)){
//						for(int i=0;i<xl_data.size();i++){
//							Map<String, String> item  = xl_data.get(i);
//							if(pjzl.equals(item.get("type"))){
//								data.add(item);
//							}
//						}
//					}
//					Intent intent = new Intent(AddPartsAdd20151202.this,
//							ChoosePjlx.class);
//					intent.putExtra("type", "xl");
//					intent.putExtra("data", data);
//					startActivityForResult(intent, 13);
//				}
//				
//			}
//		});
//		
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		if (requestCode == 11 && resultCode == 11 && data != null) {
//			part_etx_pjdl.setText(data.getStringExtra("name"));
//			part_eetx_pjdl.setTag(data.getStringExtra("id"));
//			
//			part_etx_pjzl.setText("");
//			part_eetx_pjzl.setTag("");
//			part_etx_pjxl.setText("");
//			part_eetx_pjxl.setTag("");
//		}else if (requestCode == 12 && resultCode == 12 && data != null) {
//			part_etx_pjzl.setText(data.getStringExtra("name"));
//			part_eetx_pjzl.setTag(data.getStringExtra("id"));
//			
//			part_etx_pjxl.setText("");
//			part_eetx_pjxl.setTag("");
//		}else if(requestCode == 13 && resultCode == 13 && data != null) {
//			part_etx_pjxl.setText(data.getStringExtra("name"));
//			part_eetx_pjxl.setTag(data.getStringExtra("id"));
//		}
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	protected void getWebService(String s) {
//		
//	}
//	
//	private Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case NETWORK_ERROR:
//				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
//				break;
//
//			case SUCCESSFUL:
//				dialogShowMessage_P("设备信息添加成功", null);
//				break;
//			case 2:
//
//				break;
//
//			case FAIL:
//				dialogShowMessage_P("设备信息添加失败，请检查网络后重试", null);
//				break;
//			case -2:
//				dialogShowMessage_P("信息查询失败，请检查网络后重试...", null);
//				break;
//
//			}
//		}
//
//	};
//
//}
