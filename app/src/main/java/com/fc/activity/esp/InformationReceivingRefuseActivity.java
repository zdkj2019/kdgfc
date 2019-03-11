package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.utils.Config;
import com.fc.utils.DataUtil;

/**
 * 拒绝接单
 * 
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class InformationReceivingRefuseActivity extends FrameActivity {

	private TextView tv_pgdh,tv_phone_kh;
	private TextView tv_bzr, tv_gzxx;
	private ImageView iv_telphone_kh;
	private Button confirm, cancel;
	private LinearLayout ll_checkboxs;
	private List<Map<String, String>> jdyy_list = new ArrayList<Map<String, String>>();
	private String flag, jdyy,jjjdrz;
	private int curr_jdgs = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_informationreceivingrefuse);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");

			}
		});
	}

	@Override
	protected void initVariable() {

		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		tv_bzr = (TextView) findViewById(R.id.tv_bzr);
		tv_phone_kh = (TextView) findViewById(R.id.tv_phone_kh);
		iv_telphone_kh = (ImageView) findViewById(R.id.iv_telphone_kh);
		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		ll_checkboxs = (LinearLayout) findViewById(R.id.ll_checkboxs);

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("拒绝");
		cancel.setText("取消");
		
	}

	@Override
	protected void initView() {

		title.setText("拒绝接单");

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		tv_pgdh.setText(itemmap.get("oddnumber").toString());
		tv_bzr.setText(itemmap.get("faultuser").toString());
		tv_gzxx.setText(itemmap.get("gzxx").toString());

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
		jjjdrz = itemmap.get("chjdrz").toString();
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
				case R.id.confirm:
					jdyy = "";
					int num = ll_checkboxs.getChildCount();
					for(int i=0;i<num;i++){
						LinearLayout ll = (LinearLayout) ll_checkboxs.getChildAt(i);
						CheckBox cb = (CheckBox) ll.getChildAt(0);
						if (cb.isChecked()) {
							jdyy = jdyy + cb.getTag() + ",";
						}
					}
					if ("".equals(jdyy)) {
						Toast.makeText(getApplicationContext(), "请选择拒单原因", 1)
								.show();
						return;
					}else{
						jdyy = jdyy.substring(0, jdyy.length()-1);
					}
					String ts = "当前已拒单数："+curr_jdgs+"次，根据平台星级评定标准，拒单扣2分，确认拒绝接单？";
					if(curr_jdgs>=3){
						ts = "当前已拒单数："+curr_jdgs+"次，拒单已累计满3次，平台可能暂停派发工单，确认拒绝接单？";
					}
					
					dialogShowMessage(ts,
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
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.iv_telphone_kh:
					Call(tv_phone_kh.getText().toString());
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		iv_telphone_kh.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JJGDYY_XX", "", "uf_json_getdata", this);
				JSONObject jsonObject_jdgs = callWebserviceImp.getWebServerInfo(
						"_PAD_GDJJCS", DataCache.getinition().getUserId(), "uf_json_getdata", this);
				JSONArray jsonArray = jsonObject_jdgs.getJSONArray("tableA");

				flag = jsonObject_jdgs.getString("flag");
				if(Integer.parseInt(flag) > 0){
					JSONObject temp = jsonArray.getJSONObject(0);
					curr_jdgs = temp.getInt("cs");
				}else{
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}
				
				flag = jsonObject.getString("flag");
				jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("id", temp.getString("bm"));
						item.put("name", temp.getString("mc"));
						jdyy_list.add(item);
					}
					
					Message msg = new Message();
					msg.what = 3;// 成功
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

		if (s.equals("submit")) {// 提交
			try {
				String czrz3 = "'"+jjjdrz+"|"
						+ DataCache.getinition().getUserId() + "_"
						+ new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss")
						+ "'";
				String sql = "update shgl_ywgl_fwbgszb set djzt='1.5'"
						+ ",clfs='1',slsj=sysdate,chjdrz=" + czrz3 + ",jjgdyy='"
						+ jdyy + "' where zbh='" + tv_pgdh.getText() + "'";
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_RZ", sql, DataCache.getinition().getUserId(),
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
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = FAIL;
				handler.sendMessage(msg);
			}

		}

	}

	private Handler handler = new Handler() {
		@SuppressLint({ "NewApi", "ResourceAsColor" })
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESSFUL:
				String message = "拒绝成功！";
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接错误，请检查网络连接是否正常。", null);
				break;
			case 3:
				for(int i=0;i<jdyy_list.size();i++){
					LinearLayout ll_box = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.include_checkbox, null);
					CheckBox cb = (CheckBox) ll_box.findViewById(R.id.cb);
					cb.setTag(jdyy_list.get(i).get("id"));
					cb.setText(jdyy_list.get(i).get("name"));
					ll_checkboxs.addView(ll_box);
				}
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
