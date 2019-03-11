package com.fc.activity.esp;

import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.cache.ServiceReportCache;

/**
 * 派工单明细查询 显示
 * 
 * @author cheng
 */
public class NotificationOrderActivity extends FrameActivity {

	private TextView text_bzr, text_bzsj, text_gzxx,text_bzrlxdh, 
			text_ydbzsj, text_gdyysj, text_txbs, text_zbh,
			text_dqsj, text_khbm, text_kzzd5, text_kdzh,
			text_khxxdz, text_txzd;
	private Button confirm,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_notificationmxcx);
		initVariable();
		initView();
		initListeners();
		
	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		
		text_bzr = (TextView) findViewById(R.id.text_fy1);
		text_bzsj = (TextView) findViewById(R.id.text_fy5);
		text_gzxx = (TextView) findViewById(R.id.text_jkje);
		text_bzrlxdh = (TextView) findViewById(R.id.text_fy2);
		text_ydbzsj = (TextView) findViewById(R.id.text_ydbzsj);
		text_gdyysj = (TextView) findViewById(R.id.text_gdyysj);
		
		text_txbs = (TextView) findViewById(R.id.text_txbs);
		text_zbh = (TextView) findViewById(R.id.text_mxh);
		text_dqsj = (TextView) findViewById(R.id.text_dqsj);
		text_khbm = (TextView) findViewById(R.id.text_khbm);
		text_kzzd5 = (TextView) findViewById(R.id.text_dqlj);
		text_kdzh = (TextView) findViewById(R.id.text_kdzh);
		text_khxxdz = (TextView) findViewById(R.id.text_khxxdz);
		text_txzd = (TextView) findViewById(R.id.text_txzd);

	}

	@Override
	protected void initView() {
		title.setText("工单详情");
		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		
		text_bzr.setText(itemmap.get("bzr"));
		text_bzsj.setText(itemmap.get("bzsj"));
		text_gzxx.setText(itemmap.get("gzxx"));
		text_bzrlxdh.setText(itemmap.get("bzrlxdh"));
		text_ydbzsj.setText(itemmap.get("kzzd8"));
		text_gdyysj.setText(itemmap.get("gdyysj"));
		
		text_txbs.setText(itemmap.get("txbs"));
		text_zbh.setText(itemmap.get("zbh"));
		text_dqsj.setText(itemmap.get("dqsj"));
		text_khbm.setText(itemmap.get("khbm"));
		text_kzzd5.setText(itemmap.get("kzzd5"));
		text_kdzh.setText(itemmap.get("kdzh"));
		text_khxxdz.setText(itemmap.get("khxxdz"));
		text_txzd.setText(itemmap.get("txzd"));
		
		text_bzrlxdh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent phoneIntent = new Intent(
						"android.intent.action.CALL",Uri.parse("tel:" + text_bzrlxdh.getText()));

				startActivity(phoneIntent);

			}
		});

		CharacterStyle span = new UnderlineSpan();
		SpannableString tel = new SpannableString(itemmap.get("bzrlxdh"));
		tel.setSpan(span, 0, tel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text_bzrlxdh.setText(tel);
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
					onBackPressed();
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

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		finish();
		skipActivity2(NotificationOrderList.class);
	}

}
