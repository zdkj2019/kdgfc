package com.fc.activity.esp;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.cache.DataCache;
import com.fc.utils.Config;

public class JbxxActivity extends FrameActivity {

	private TextView tv_jlmc;
	private String jlmc = "";
	private String lxdh = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_jbxx);
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

	}

	@Override
	protected void initView() {
		title.setText("基本信息");
		tv_jlmc = (TextView) findViewById(R.id.tv_jlmc);
	}

	@Override
	protected void initListeners() {
		topBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				}
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				JSONObject object = callWebserviceImp.getWebServerInfo(
						"_PAD_YGPX_DH", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				if (Integer.parseInt(object.getString("flag")) > 0) {
					JSONArray array = object.getJSONArray("tableA");
					JSONObject temp = array.getJSONObject(0);
					jlmc = temp.getString("xm");
					lxdh = temp.getString("lxdh");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 1;
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
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				dialogShowMessage("查询失败，系统错误！", null, null);
				break;
			case 1:
				tv_jlmc.setText(jlmc + "（" + lxdh + "）");
				break;
			default:
				break;
			}
		};
	};
}
