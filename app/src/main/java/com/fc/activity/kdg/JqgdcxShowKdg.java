package com.fc.activity.kdg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.cache.DataCache;
import com.fc.cache.ServiceReportCache;
/**
 * 快递柜-近期工单查询-数据展示
 * @author zdkj
 *
 */
public class JqgdcxShowKdg extends FrameActivity {

	private Button confirm,cancel;
	private String flag,zbh,type="1",msgStr,ddh,lxdh;
	private ImageView iv_telphone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jqgdcxshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
		
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		ddh = itemmap.get("ddh").toString();
		lxdh = itemmap.get("lxdh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("axdh").toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("sx").toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("qy").toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("ddh_mc").toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("bzsj").toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("yqsx").toString());
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("lxdh").toString());
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("dygdh1_mc").toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("dygdh2_mc").toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("kzzf6").toString());
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("kzzf7").toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("kzzf8").toString());
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("gzxx").toString());
		((TextView) findViewById(R.id.tv_17)).setText(itemmap.get("kzsz1").toString());
		((TextView) findViewById(R.id.tv_18)).setText(itemmap.get("bz").toString());
		((TextView) findViewById(R.id.tv_jddz)).setText(itemmap.get("jddz").toString());
		
		if("1".equals(ddh)){
			findViewById(R.id.ll_1).setVisibility(View.VISIBLE);
		}
		if("3".equals(ddh)){
			findViewById(R.id.ll_2).setVisibility(View.VISIBLE);
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
					onBackPressed();
					break;
				default:
					break;
				}
				
			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
		
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(lxdh)) {
					toastShowMessage("请选择联系电话！");
					return;
				}
				Call(lxdh);
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		
	
	}
//
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		finish();
//	}

}
