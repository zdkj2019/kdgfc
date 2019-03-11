package com.fc.activity.esp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
import com.fc.common.Constant;
import com.fc.utils.Config;
import com.fc.utils.ImageUtil;

public class XjShowActivity extends FrameActivity {

	private LinearLayout ll_xjnr,ll_show;
	private Button confirm, cancel;
	private String flag, zbh, message, sblx,cs,sblx2;
	private List<Map<String, String>> data;
	private ImageView image_1, image_2;
	private EditText et_1,et_2;
	private int photonum = 1;
	private String filename;
	private File photo_file1 = null, photo_file2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_xj_show);
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

		ll_xjnr = (LinearLayout) findViewById(R.id.ll_xjnr);
		ll_show = (LinearLayout) findViewById(R.id.ll_show);
		image_1 = (ImageView) findViewById(R.id.image_1);
		image_2 = (ImageView) findViewById(R.id.image_2);
		et_1 = (EditText) findViewById(R.id.et_1);
		et_2 = (EditText) findViewById(R.id.et_2);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		sblx = itemmap.get("sblx").toString();
		sblx2 = itemmap.get("sblx2").toString();
		((TextView) findViewById(R.id.tv_0)).setText(zbh);
		((TextView) findViewById(R.id.tv_1)).setText(itemmap.get("tv_1")
				.toString());
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("tv_2")
				.toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("tv_3")
				.toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("tv_4")
				.toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("tv_5")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("tv_6")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("tv_7")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("tv_8")
				.toString());

		if(sblx2.equals("1")){
			ll_show.setVisibility(View.VISIBLE);
		}
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
					onBackPressed();
					break;
				case R.id.confirm:
					cs = zbh;
					int num = ll_xjnr.getChildCount();
					cs += "*PAM*";
					cs += num;
					for(int i=0;i<num;i++){
						LinearLayout ll = (LinearLayout) ll_xjnr.getChildAt(i);
						TextView tv = (TextView) ll.getChildAt(0);
						String bm = tv.getTag().toString();
						String name = tv.getText().toString();
						cs += "*PAM*";
						cs += bm;
						if(ll.getChildAt(1) instanceof RadioGroup){
							RadioGroup rg = (RadioGroup) ll.getChildAt(1);
							if(rg.getCheckedRadioButtonId()==R.id.rb_1){
								cs += "*PAM*";
								cs += "是";
							}else if(rg.getCheckedRadioButtonId()==R.id.rb_2){
								cs += "*PAM*";
								cs += "否";
							}else if(rg.getCheckedRadioButtonId()==R.id.rb_3){
								cs += "*PAM*";
								cs += "无该设备";
							}else{
								toastShowMessage("请选择"+name);
								return;
							}
						} else if(ll.getChildAt(1) instanceof EditText){
							EditText et = (EditText) ll.getChildAt(1);
							if(et.getText()==null||"".equals(et.getText().toString())){
								toastShowMessage("请录入"+name);
								return;
							}else{
								cs += "*PAM*";
								cs += et.getText().toString();
							}
						}
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							confirm.setEnabled(true);
							if(sblx2.equals("1")){
								upload();//先长传图片，在上传数据
							}else{
								getWebService("submit");
							}
							
						}
					});
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		
		et_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 1;
				camera();
			}
		});

		et_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 2;
				camera();
			}
		});
	}
	
	private void camera() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			toastShowMessage("没有储存卡，不能拍照");
			return;
		}
		filename = String.valueOf(System.currentTimeMillis()).trim().substring(4);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), filename + ".jpg")));
		startActivityForResult(intent, 1);

	}

	@SuppressLint("ShowToast")
	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// 读pic
			String filepath = Environment.getExternalStorageDirectory().toString() + "/" + filename + ".jpg";
			try {
				File file = new File(filepath);
				if(file==null){
					Toast.makeText(getApplicationContext(), "图片不存在！",1).show();
					return;
				}
				//压缩图片到100K
				ImageUtil.compressAndGenImage(convertBitmap(file, 480),filepath, 200);
				if (photonum == 1) {
					photo_file1 = new File(filepath);
					image_1.setImageBitmap(convertBitmap(photo_file1, 150));
					image_1.setVisibility(View.VISIBLE);
					et_1.setText("重新拍照");
				} else if (photonum == 2) {
					photo_file2 = new File(filepath);
					image_2.setImageBitmap(convertBitmap(photo_file2, 150));
					image_2.setVisibility(View.VISIBLE);
					et_2.setText("重新拍照");
				}
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片尺寸失败，请调低像素后重新拍照",1).show();
			}catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片大小失败，请调低像素后重新拍照",1).show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "拍照失败", 1).show();
		}
	}

	@SuppressLint("InflateParams")
	private void loadData() {
		
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			LinearLayout ll = null;
			TextView tv = null;
			View line = (View) LayoutInflater.from(getApplicationContext()).inflate(R.layout.include_line, null);
			if ("001".equals(map.get("xxjg").toString())) {
				ll = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.include_xj_type_1, null);
				tv = (TextView) ll.findViewById(R.id.tv_name);
			}else if ("002".equals(map.get("xxjg").toString())) {
				ll = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.include_xj_type_2, null);
				tv = (TextView) ll.findViewById(R.id.tv_name);
			}else if ("003".equals(map.get("xxjg").toString())) {
				ll = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.include_xj_type_3, null);
				tv = (TextView) ll.findViewById(R.id.tv_name);
			}else{//"004".equals(map.get("xxjg").toString())
				ll = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.include_xj_type_4, null);
				tv = (TextView) ll.findViewById(R.id.tv_name);
			}
			tv.setText(map.get("xxnr"));
			tv.setTag(map.get("xxbm"));
			ll_xjnr.addView(ll);
			//ll_xjnr.addView(line);
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s)) {
			try {
				String sqlid = "_PAD_XJD_JDWG";
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, sblx, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					data = new ArrayList<Map<String, String>>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("xxbm", temp.getString("xxbm"));
						item.put("xxnr", temp.getString("xxnr"));
						item.put("xxjg", temp.getString("xxjg"));
						data.add(item);
					}
					Message msg = new Message();
					msg.what = Constant.NUM_6;// 成功
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
		} else {
			Message msg = new Message();
			msg.what = FAIL;
			handler.sendMessage(msg);
		}

		if (s.equals("submit")) {// 提交
			try {
				String typeStr = DataCache.getinition().getUserId();
				message = "提交成功！";
				flag = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_XJD_XJBG", cs, typeStr, typeStr,
						"uf_json_setdata2", this).getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}
	
	private void upload() {
		if(photo_file1==null||photo_file2==null){
			Message msg = new Message();
			msg.what = 13;
			handler.sendMessage(msg);
			return;
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					boolean flag = uploadPic("", readJpeg(photo_file1),
							"uf_json_setdata");
					if (flag) {
						flag = uploadPic("", readJpeg(photo_file2),
								"uf_json_setdata");
						if (flag) {
							Message msg = new Message();
							msg.what = Constant.NUM_7;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = Constant.NUM_8;
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = Constant.NUM_8;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = Constant.NUM_8;
					handler.sendMessage(msg);
				}

			}

		});

	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(
					"c#_PAD_ESP_GDCZP", "0001", zbh + "*1", "0001",
					data1, "uf_json_setdata2_p11", getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				loadData();
				break;
			case Constant.NUM_7:
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("submit");
					}
				});
				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交失败,上传图片失败！", null);
				break;
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("请拍照！", null);
				break;
			}
		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
