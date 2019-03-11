package com.fc.activity.w;

import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.cache.ServiceReportCache;

public class GlzdShowActivity extends FrameActivity {
	
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_xxglshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		final int index = ServiceReportCache.getIndex();
		final Map<String, String> itemmap = ServiceReportCache.getData().get(index);
		webview = (WebView) findViewById(R.id.webview);
		webview.loadUrl(itemmap.get("bz"));
	}

	@Override
	protected void initView() {
		title.setText("管理制度");
	}

	@Override
	protected void initListeners() {

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;

				}
			}
		};
		topBack.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {

	}

	@Override
	public void onBackPressed() {
		finish();
	}


	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case SUCCESSFUL:
				
				break;

			case FAIL:
				
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
