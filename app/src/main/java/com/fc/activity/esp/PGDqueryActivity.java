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
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;

/**
 * 派工单明细查询 显示
 * 
 * @author cheng
 */
public class PGDqueryActivity extends FrameActivity {

	private TextView text_zbh, text_djzt, text_sfcs,
//		    text_khxl, 
			text_bzsj, text_bzr, text_bzrlxdh, text_gzxx, text_kzzd5,
			text_pgdx, text_cljg, text_clfs, text_fwpj,
			text_kzzd2, text_ddsj, text_wcsj, text_czy, text_czsj,text_kzzd6;
	private Button confirm,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_pgdmxcx);
		initVariable();
		initView();
		initListeners();
		
	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		
		text_zbh = (TextView) findViewById(R.id.text_mxh);
		text_djzt = (TextView) findViewById(R.id.text_sr1);
		text_sfcs = (TextView) findViewById(R.id.text_sr2);
//		text_khxl = (TextView) findViewById(R.id.text_khxl);
		text_bzsj = (TextView) findViewById(R.id.text_fy5);
		text_bzr = (TextView) findViewById(R.id.text_fy1);
		text_bzrlxdh = (TextView) findViewById(R.id.text_fy2);
		text_gzxx = (TextView) findViewById(R.id.text_jkje);
		text_kzzd5 = (TextView) findViewById(R.id.text_dqlj);
		text_pgdx = (TextView) findViewById(R.id.text_pgdx);
		text_cljg = (TextView) findViewById(R.id.text_cljg);
		text_clfs = (TextView) findViewById(R.id.text_clfs);
		text_fwpj = (TextView) findViewById(R.id.text_fwpj);
		text_kzzd2 = (TextView) findViewById(R.id.text_kzzd2);
		text_ddsj = (TextView) findViewById(R.id.text_ddsj);
		text_wcsj = (TextView) findViewById(R.id.text_wcsj);
		text_czy = (TextView) findViewById(R.id.text_czy);
		text_czsj = (TextView) findViewById(R.id.text_czsj);
		text_kzzd6 = (TextView) findViewById(R.id.text_kzzd6);

	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		
		text_zbh.setText(itemmap.get("zbh"));
		text_djzt.setText(itemmap.get("djzt"));
		text_sfcs.setText(itemmap.get("sfcs"));
//		text_khxl.setText(itemmap.get("khxl"));
		text_bzsj.setText(itemmap.get("bzsj"));
		text_bzr.setText(itemmap.get("bzr"));
//		text_bzrlxdh.setText(itemmap.get("bzrlxdh"));
		text_gzxx.setText(itemmap.get("gzxx"));
		text_kzzd5.setText(itemmap.get("kzzd5"));
		text_pgdx.setText(itemmap.get("pgdx"));
		text_kzzd6.setText(itemmap.get("kzzd6"));
		String cljg = itemmap.get("cljg");
		
		if("1".equals(cljg)){
			cljg = "完工";
		}else if("2".equals(cljg)){
			cljg = "未完工";
		}
		text_cljg.setText(cljg);
		text_clfs.setText(itemmap.get("clfs"));
		text_fwpj.setText(itemmap.get("fwpj"));
		text_kzzd2.setText(itemmap.get("kzzd2"));
		text_ddsj.setText(itemmap.get("ddsj"));
		text_wcsj.setText(itemmap.get("wcsj"));
		text_czy.setText(itemmap.get("czy"));
		text_czsj.setText(itemmap.get("czsj"));

//		((TextView) findViewById(R.id.tv_suxq)).setText(itemmap.get("khbm"));
//		((TextView) findViewById(R.id.tv_supq)).setText(itemmap.get("ds"));
//		((TextView) findViewById(R.id.tv_kdzh)).setText(itemmap.get("kdzh"));
//		((TextView) findViewById(R.id.tv_yuyuetime)).setText(itemmap.get("cfsj"));
//		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("khxxdz"));
		
		text_bzrlxdh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent phoneIntent = new Intent(
						"android.intent.action.CALL",

						Uri.parse("tel:" + text_bzrlxdh.getText()));

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
		skipActivity2(PGDquerychooseActivity.class);
		finish();
	}

}
