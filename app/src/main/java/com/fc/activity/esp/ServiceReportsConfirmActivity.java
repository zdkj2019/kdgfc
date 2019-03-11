package com.fc.activity.esp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.cache.DataCache;

/**
 * 确认报告信息
 * 
 * @author cheng
 * 
 */
@SuppressLint("HandlerLeak")
public class ServiceReportsConfirmActivity extends FrameActivity {

	private TextView tv_pgdh, tv_fwbgbm, tv_fgs, tv_yyt, tv_bzr, tv_bzrlxdh,
			tv_bzsj, tv_gzxx, tv_sblx, tv_sbbm, tv_fwlx, tv_fwry, tv_fwfylx,
			tv_ddsj, tv_cljg, tv_rgfy, tv_gzlx;
	private Button confirm, cancel;
	private String rgfy;
	private Intent intent;
	private ArrayList<String> hpsql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_servicereportsconfirm);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		tv_fwbgbm = (TextView) findViewById(R.id.tv_fwbgbm);
		tv_fgs = (TextView) findViewById(R.id.tv_fgs);
		tv_yyt = (TextView) findViewById(R.id.tv_yyt);
		tv_bzr = (TextView) findViewById(R.id.tv_bzr);
		tv_bzrlxdh = (TextView) findViewById(R.id.tv_bzrlxdh);
		tv_bzsj = (TextView) findViewById(R.id.tv_bzsj);
		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		tv_sblx = (TextView) findViewById(R.id.tv_sblx);
		tv_sbbm = (TextView) findViewById(R.id.tv_sbbm);
		// tv_sgsbbm = (TextView) findViewById(R.id.tv_sgsbbm);
		tv_fwlx = (TextView) findViewById(R.id.tv_fwlx);
		tv_fwry = (TextView) findViewById(R.id.tv_fwry);
		tv_fwfylx = (TextView) findViewById(R.id.tv_fwfylx);
		tv_ddsj = (TextView) findViewById(R.id.tv_ddsj);
		tv_cljg = (TextView) findViewById(R.id.tv_cljg);
		tv_rgfy = (TextView) findViewById(R.id.tv_rgfy);
		// tv_bxlx = (TextView) findViewById(R.id.tv_bxlx);
		tv_gzlx = (TextView) findViewById(R.id.tv_gzlx);

		hpsql = this.getIntent().getStringArrayListExtra("货品明细sql");

	}

	@Override
	protected void initView() {

		title.setText("服务报告确认");

		intent = this.getIntent();

		tv_pgdh.setText(intent.getStringExtra("派工单号"));
		tv_fwbgbm.setText(intent.getStringExtra("服务报告编码"));
		tv_fgs.setText(intent.getStringExtra("分公司"));
		tv_yyt.setText(intent.getStringExtra("营业厅"));
		tv_bzr.setText(intent.getStringExtra("报障人"));
		tv_bzrlxdh.setText(intent.getStringExtra("报障人联系电话"));
		tv_bzsj.setText(intent.getStringExtra("报障时间"));
		tv_gzxx.setText(intent.getStringExtra("故障现象"));
		tv_sblx.setText(splitString(intent.getStringExtra("设备类型")));
		// tv_bxlx.setText(intent.getStringExtra("保修类型"));

		tv_sbbm.setText(splitString(intent.getStringExtra("设备编码")));
		// tv_sgsbbm.setText(intent.getStringExtra("手工设备编码"));
		tv_fwlx.setText(intent.getStringExtra("服务类型"));
		tv_fwry.setText(intent.getStringExtra("服务人员"));
		tv_fwfylx.setText(intent.getStringExtra("服务费用类型"));
		tv_ddsj.setText(intent.getStringExtra("到达时间"));
		tv_cljg.setText(intent.getStringExtra("处理结果"));
		tv_rgfy.setText(intent.getStringExtra("人工费用"));
		tv_gzlx.setText(splitString(intent.getStringExtra("故障类型")));

		if (!"".equals(intent.getStringExtra("人工费用"))
				&& intent.getStringExtra("人工费用") != null) {
			rgfy = intent.getStringExtra("人工费用");
		} else {
			rgfy = "0";
		}

	}

	@Override
	protected void initListeners() {

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {

				case R.id.confirm:
					sure();
					break;

				case R.id.cancel:
					skipActivity(ServiceReportsComplete.class);
					break;

				case R.id.bt_topback:
					skipActivity(ServiceReportslist.class);
					break;

				// case R.id.bt_toplogout:
				// skipActivity(LoginActivity.class);
				// break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		// topLogout.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);

	}

	@Override
	protected void getWebService(String s) {

	}

	private String splitString(String s) {
		if (!"".equals(s) && s != null) {
			String[] s1 = s.split(",");
			String temp = s1[1];
			return temp.substring(6, temp.length() - 1);
		}
		return "";
	}

	protected void sure() {
		Intent localIntent = new Intent(getApplicationContext(),
				ServiceReportsAppraiseActivity.class);
		localIntent.putExtra("xmmc", getId(intent.getStringExtra("设备类型")));
		localIntent.putExtra("ydbm", this.tv_sbbm.getText());
		localIntent.putExtra("sbbm", getId(intent.getStringExtra("设备编码")));
		localIntent.putExtra("gzbm1", getId(intent.getStringExtra("故障类型")));
		localIntent.putExtra("zbh", this.tv_pgdh.getText());
		localIntent.putExtra("sgdh", this.tv_fwbgbm.getText());
		localIntent.putExtra("gzxx", this.tv_gzxx.getText());
		localIntent.putExtra("bzr", this.tv_bzr.getText());
		localIntent.putExtra("bzrlxdh", this.tv_bzrlxdh.getText());
		localIntent.putExtra("fwfylx", intent.getStringExtra("服务费用类型"));
		// localIntent.putExtra("fwfylx", localIntent.getStringExtra("保修类型ID"));
		// localIntent.putExtra("kzzd6",
		// localIntent.getStringExtra("服务费用类型ID"));
		localIntent.putExtra("cljg", this.tv_cljg.getText());
		localIntent.putExtra("ddsj", "to_date('" + this.tv_ddsj.getText()
				+ "','yyyy-mm-dd hh24:mi:ss')");
		localIntent.putExtra("kzzd3", DataCache.getinition().getUserId());
		localIntent.putExtra("rgfy", this.rgfy);
		localIntent.putStringArrayListExtra("货品明细sql", hpsql);
		startActivity(localIntent);
		finish();
	}

	@Override
	public void onBackPressed() {

		skipActivity(ServiceReportslist.class);
	}
}
