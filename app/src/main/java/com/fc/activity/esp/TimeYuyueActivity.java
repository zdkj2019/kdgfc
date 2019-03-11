package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.definition.Sign;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;
import com.fc.utils.DateTimePickerDialog;
import com.fc.utils.MyDialog;
import com.fc.utils.MyDialog.buttonSure;

/**
 *  时间预约
 * @author wlj
 */
@SuppressLint("HandlerLeak")
public class TimeYuyueActivity extends FrameActivity
		implements Sign {

	private TextView tv_bzr,tv_gzxx;
	private TextView tv_wxaz,tv_pgdh,tv_servicepeople,tv_clfs,tv_yuyueshijian;
	private Button confirm,cancel;
	private List<Map<String, String>> data;
	private String pgdx;
	private EditText tv_bz,et_ecyy,et_sjgzyy,et_yyyy;
	private List<String> clfs_list = new ArrayList<String>();
	private Map<String, String> itemmap;
	private String[] spinneritem,gzlxid;
	private LinearLayout phone_lin ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_timeyuyue);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {
			
			@Override
			public void run() {
				
				getWebService("queryUser");
				
			}
		});
	}

	@Override
	protected void initVariable() {
		myDialog = new MyDialog(this);
		tv_bzr = (TextView) findViewById(R.id.tv_bzr);
		tv_wxaz = (TextView) findViewById(R.id.tv_wxaz);
		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		tv_servicepeople = (TextView) findViewById(R.id.tv_servicepeople);
		tv_clfs = (TextView) findViewById(R.id.tv_clfs);
		et_ecyy = (EditText) findViewById(R.id.et_ecyy);
		tv_yuyueshijian = (TextView) findViewById(R.id.tv_yuyuetime);
		tv_bz = (EditText) findViewById(R.id.tv_bz);
		et_sjgzyy = (EditText) findViewById(R.id.et_sjgzyy);
		et_yyyy = (EditText) findViewById(R.id.et_yyyy);
		phone_lin = (LinearLayout)findViewById(R.id.phone_lin);
		
		confirm = (Button) findViewById(R.id.include_botto).findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(R.id.cancel);
		
		data = new ArrayList<Map<String, String>>();
	
		clfs_list.add("上门服务派工");
		clfs_list.add("电话完工消单");
		
		bzradiotext.add("客户电话无人接听"); 
	}

	@Override
	protected void initView() {
		
		title.setText(DataCache.getinition().getTitle());
		
		itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		int wx = Integer.parseInt(itemmap.get("kzzd5"))-1;
		if(wx != 0 && wx != 1 && wx != 2 && wx != 3){wx = 3;}
		tv_pgdh.setText(itemmap.get("oddnumber"));
		tv_bzr.setText(itemmap.get("faultuser"));
//		tv_lxdh.setText(itemmap.get("usertel"));
		tv_wxaz.setText(wxaz.get(wx));
		tv_gzxx.setText(itemmap.get("gzxx"));
		tv_bz.setText(itemmap.get("bz"));
		et_sjgzyy.setText(itemmap.get("fwnr"));
		
		((TextView) findViewById(R.id.tv_suxq)).setText(itemmap.get("khbm"));
		((TextView) findViewById(R.id.tv_supq)).setText(itemmap.get("ds"));
		((TextView) findViewById(R.id.tv_kdzh)).setText(itemmap.get("kdzh"));
		((TextView) findViewById(R.id.tv_yuyuetime)).setText(itemmap.get("cfsj"));
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("khxxdz"));
		
		pgdx = itemmap.get("dispatchinguser");
		tv_servicepeople.setText(pgdx);// 显示派公对象
		tv_servicepeople.setTag(itemmap.get("pgdx"));// 显示派公对象
		
		if ("".equals(itemmap.get("clfs")) || itemmap.get("clfs").length() == 0
				|| itemmap.get("clfs") == null) {
			
			tv_clfs.setText(clfs_list.get(0));
		
		} else if(itemmap.get("clfs").equals("0")) {
				tv_clfs.setText(clfs_list.get(0));
		}else{
			
			tv_clfs.setText(clfs_list.get(Integer.parseInt(itemmap.get("clfs")) - 1));
		}
		
		String usertel = itemmap.get("usertel");
		
		if (usertel != null) {
			
			usertel = usertel.replaceAll("[\\D]+", ",");
			String[] telarry = usertel.split(",");
			
			for (String tel : telarry) {
				
				addpohoneview(tel);
			}
		}
		
	}
	private void  addpohoneview(String phone){
		
		TextView textView = new TextView(getApplicationContext());
		
		textView.setTextSize(18l);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setBackgroundResource(R.drawable.edittext_bg);
		textView.setPadding(5, 0, 0, 0);
		
		setunderline(phone, textView);
		
		phone_lin.addView(textView);
		
	}
	private void setunderline(String tel,TextView view){
		
		CharacterStyle span = new UnderlineSpan();
		SpannableString telss = new SpannableString(tel);
		telss.setSpan(span, 0, telss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		view.setText(telss);
		
		telonclick(view, tel);
	}

	private void telonclick(View v,final String tel){
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + tel));
				
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
		
		tv_servicepeople.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				defined2(tv_servicepeople, "派工对象", data, "员工编码", "姓名");
			}
		});
		
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (isNotNull(tv_clfs) && isNotNull(et_yyyy) && isNotNull(tv_servicepeople) && isNotNull(tv_yuyueshijian)) {

					String yuyueshijian = tv_yuyueshijian.getText().toString();
					String timeff = itemmap.get("timeff");
					DataUtil dataUtil = new DataUtil();
					Date yuyueDate = dataUtil.StringToDate(yuyueshijian);
					Date timeffDate = dataUtil.StringToDate(timeff);
					if (yuyueDate.before(timeffDate) ) {
						dialogShowMessage_P("预约时间不能小于报障时间", null);
						return;
					} else {
						dialogShowMessage("确认预约 ？",
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
		
		tv_yuyueshijian.setOnClickListener(new DateTimeOnClick());
		
		tv_clfs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				myDialog.inputdialog("处理方式", clfs_list, "电话完工消单", spinneritem,new buttonSure() {
					
					@Override
					public void onClick(String s, Spinner sp) {
						tv_clfs.setText(s);
						if("电话完工消单".equals(s) && sp.getSelectedItem() != null){
							
							int id = (int)sp.getSelectedItemId();
							findViewById(R.id.rlgzlx).setVisibility(View.VISIBLE);
							findViewById(R.id.linegzlx).setVisibility(View.VISIBLE);
							((TextView)findViewById(R.id.tv_gzlx)).setText(sp.getSelectedItem().toString());
							((TextView)findViewById(R.id.tv_gzlx)).setTag(gzlxid[id]);
						}else{
							
							findViewById(R.id.rlgzlx).setVisibility(View.GONE);
							findViewById(R.id.linegzlx).setVisibility(View.GONE);
							((TextView)findViewById(R.id.tv_gzlx)).setText("");
							((TextView)findViewById(R.id.tv_gzlx)).setTag("");
						}
					}
				});
				
			}
		});
		
	}
	
	String flag;

	@Override
	protected void getWebService(String s) {
		
		if ("queryUser".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_RY", DataCache.getinition().getUserId(),
						"uf_json_getdata",this);
				Log.e("_PAD_RY", jsonObject.toString());
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					Config.getExecutorService().execute(new Runnable() {
						
						@Override
						public void run() {
							getWebService("querygzlb");
						}
					});
					
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");

					for (int i = 0; i < jsonArray.length(); i++) {
						Map<String, String> item = new HashMap<String, String>();

						JSONObject temp = jsonArray.getJSONObject(i);
						String id = temp.getString("ygbh");
						String name = temp.getString("xm");
						item.put("id", id);
						item.put("name", name);
						data.add(item);
					}

//					Message msg = new Message();
//					msg.what = 2;// 成功
//					handler.sendMessage(msg);
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
		
		if ("querygzlb".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", "","uf_json_getdata",this);
				Log.e("_PAD_SBGZLB", jsonObject.toString());
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");

					Map<String, String> gzlxMap = new LinkedHashMap<String, String>();
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject temp = jsonArray.getJSONObject(i);
						
						String dhwglx = temp.getString("dhwglx");
						
						if("1".equals(dhwglx)){
							
							String gzlbmc = temp.getString("gzlbmc");
							String gzlbbm = temp.getString("gzlbbm");
							gzlxMap.put(gzlbbm, gzlbmc);
							
						}
						
					}
					Set<String> set = gzlxMap.keySet();
					gzlxid = (String[])set.toArray(new String[set.size()]);
					Collection<String> values = gzlxMap.values();
					spinneritem = (String[]) values.toArray(new String[values.size()]);
					
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
		
		if (s.equals("submit")) {// 提交
			int clfsid = clfs_list.indexOf(tv_clfs.getText()) + 1;
			String bz = tv_bz.getText().toString().trim()+"";
		
			String gzyy;
			if(et_sjgzyy.getText() == null || "".equals(et_sjgzyy.getText())){
				gzyy = "";
			}else{
				gzyy = et_sjgzyy.getText().toString().trim();
			}
			//故障类型
			Object gzlx = ((TextView)findViewById(R.id.tv_gzlx)).getTag();
			if(gzlx == null){
				gzlx = "";
			}
			/**
			 * 预约原因
			 */
			CharSequence yyyy = et_yyyy.getText();
			if(yyyy == null){
				yyyy = "";
			}
			/**
			 * 二次预约原因
			 */
			String ecyy ="";
			CharSequence csecyy = et_ecyy.getText();
			/**
			 * 历史预约原因
			 */
			String kzzd6 = itemmap.get("kzzd6").trim();
//			kzzd1 = "转派原因";
			String kzzd1 = itemmap.get("kzzd1").trim();
			
			String sql = null;
			if("".equals(csecyy) || csecyy == null){
				if("".equals(kzzd6) || kzzd6 == null){
					kzzd6="to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||':'||'"+kzzd1+"'";
				}else{
					kzzd6 = "'"+kzzd6+"'"+"||' / '||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||':'||'"+kzzd1+"'";
				}
				
			}else{
				ecyy = et_ecyy.getText().toString().trim();
				
				if("".equals(kzzd6) || kzzd6 == null){
					
					kzzd6="to_char(sysdate,'yyyy-mm-dd  hh24:mi:ss')||':'||'"+ecyy+"'";
				}else{
					
					kzzd6= "'"+kzzd6+"'"+"||' / '||to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')||':'||'"+ecyy+"'";
				}
				
			}
			String jdsj = itemmap.get("jdsj").trim();
			
			if(jdsj == null || "".equals(jdsj) ){
				
				sql = "update shgl_ywgl_fwbgszb set  jdsj=sysdate,bz='"+bz+"',fwnr='"+gzyy+"',kzzd14='" + gzlx + "',pgdx='"+ tv_servicepeople.getTag()+ "',clfs='" + clfsid + "',yyyy='" + yyyy + "',kzzd1='" + ecyy + "',kzzd6=" + kzzd6 + ",cfsj=to_date('"+ tv_yuyueshijian.getText()+ "','yyyy-mm-dd hh24:mi:ss') where zbh='" + tv_pgdh.getText()+ "'";
			}else{
				
				sql = "update shgl_ywgl_fwbgszb set bz='"+bz+"',fwnr='"+gzyy+"',kzzd14='" + gzlx + "',pgdx='"+ tv_servicepeople.getTag()+ "',clfs='" + clfsid + "',yyyy='" + yyyy + "',kzzd1='" + ecyy + "',kzzd6=" + kzzd6 + ",cfsj=to_date('"+ tv_yuyueshijian.getText()+ "','yyyy-mm-dd hh24:mi:ss') where zbh='" + tv_pgdh.getText()+ "'";
			}
			
			
			Log.e("TimeYuyueActivity", sql.toString());
			try {//cfsj=to_date('2013-11-22','yyyy-mm-dd') 

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
				Message localMessage1 = new Message();
				localMessage1.what = 0;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(localMessage1);
			}

		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -2:
				dialogShowMessage_P("获取数据失败，请重新获取",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						onBackPressed();
					}
				});
				break;
			case -1:
				dialogShowMessage_P("网络可能有问题，请重试！！",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						onBackPressed();
					}
				});
				break;
			case 1:
				dialogShowMessage_P("预约成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								skipActivity2(MainActivity.class);
							}
						});

				break;
			case 0:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;

			case 2:
//				nameToId();//spinneritem
				break;
			}
			progressDialog.dismiss();
		}
	};
//	/**
//	 * 根据 派工对象 得到 派工对象 的id
//	 */
//	private void nameToId() {
//		pgdx = tv_servicepeople.getText().toString();
//		if (pgdx != null && !"".equals(pgdx)) {
//			for (int i = 0; i < data.size(); i++) {
//				if (pgdx.equals(data.get(i).get("name"))) {
//				
//					tv_servicepeople.setTag(data.get(i).get("id"));
//					break;
//				}
//			}
//		}
//
//	}
	@Override
	public void onBackPressed() {
		skipActivity2(TimeYuyuelist.class);
	}

	private final class DateTimeOnClick implements OnClickListener {
		public void onClick(View v) {
			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
					TimeYuyueActivity.this);
			dateTimePicKDialog.dateTimePicKDialog(tv_yuyueshijian, 0);
		}
	}
	private List<String> bzradiotext = new ArrayList<String>();
	private MyDialog myDialog;
	
	public void bzonClick(View v){
		myDialog.redioDialog(tv_bz, "备注", bzradiotext, "");
	}
}
