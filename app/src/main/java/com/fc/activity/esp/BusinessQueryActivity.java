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
 * 业务受理查询
 * 
 * @author cheng
 */
public class BusinessQueryActivity extends FrameActivity {

//	private TextView text_zbh, text_djzt, text_sfcs,
//		    text_khxl, 
//			text_bzsj, text_bzr, text_bzrlxdh, text_gzxx, text_kzzd5,
//			text_pgdx, text_cljg, text_clfs, text_fwpj,
//			text_kzzd2, text_ddsj, text_wcsj, text_czy, text_czsj;
	
	private TextView text_pgdh,text_kzzf1,text_lxdhr2,text_lxdhr,
					text_username,text_xqlh,text_azxq,text_tcmc,text_kdlx;
	private Button confirm,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_businessquery);
		initVariable();
		initView();
		initListeners();
		
	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		
		text_kdlx = (TextView) findViewById(R.id.text_kdlx);
		text_tcmc = (TextView) findViewById(R.id.text_tcmc);
		text_azxq = (TextView) findViewById(R.id.text_azxq);
		text_xqlh = (TextView) findViewById(R.id.text_xqlh);
		text_username = (TextView) findViewById(R.id.text_username);
//		text_xb = (TextView) findViewById(R.id.text_xb);
		text_lxdhr = (TextView) findViewById(R.id.text_lxdhr);
		text_lxdhr2 = (TextView) findViewById(R.id.text_lxdhr2);
//		text_sfzh = (TextView) findViewById(R.id.text_sfzh);
//		text_csrq = (TextView) findViewById(R.id.text_csrq);
//		text_zjyxq1 = (TextView) findViewById(R.id.text_zjyxq1);
//		text_zjyxq2 = (TextView) findViewById(R.id.text_zjyxq2);
//		text_zjdz = (TextView) findViewById(R.id.text_zjdz);
		text_pgdh = (TextView) findViewById(R.id.text_pgdh);
		text_kzzf1 = (TextView) findViewById(R.id.text_kzzf1);

	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		text_kdlx.setText(itemmap.get("kdlxname"));         
		text_tcmc.setText(itemmap.get("tcdcname"));            
		text_azxq.setText(itemmap.get("azxqname"));    
		text_xqlh.setText(itemmap.get("azdz"));    
		text_username.setText(itemmap.get("toXin"));
//		text_xb.setText(itemmap.get("xb"));        
		text_lxdhr.setText(itemmap.get("lxdh"));   
		text_lxdhr2.setText(itemmap.get("yhdh2")); 
//		text_sfzh.setText(itemmap.get("sfzh"));    
//		text_csrq.setText(itemmap.get("csrq"));    
//		text_zjyxq1.setText(itemmap.get("zjyxq1"));
//		text_zjyxq2.setText(itemmap.get("zjyxq2"));
//		text_zjdz.setText(itemmap.get("zjdz"));    
		text_pgdh.setText(itemmap.get("zbh"));
		text_kzzf1.setText(itemmap.get("kzzf1"));

		text_lxdhr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent phoneIntent = new Intent(
						"android.intent.action.CALL",

						Uri.parse("tel:" + text_lxdhr.getText()));

				startActivity(phoneIntent);

			}
		});

		CharacterStyle span = new UnderlineSpan();
		SpannableString tel = new SpannableString(itemmap.get("lxdh"));
		tel.setSpan(span, 0, tel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		text_lxdhr.setText(tel);
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
		skipActivity2(BusinessQueryList.class);
	}

}
