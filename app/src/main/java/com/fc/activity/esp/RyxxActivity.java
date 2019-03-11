package com.fc.activity.esp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.w.QyxxActivity;
import com.fc.activity.w.XxglActivity;
import com.fc.activity.w.YHKInfoActivity;
import com.fc.activity.w.YhkxxActivity;
import com.fc.cache.DataCache;
import com.jauker.widget.BadgeView;

public class RyxxActivity extends FrameActivity {

	private LinearLayout jcxx_qyxx, jcxx_jbxx, jcxx_yhkxx,jcxx_xxgl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_ryxx);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {
		title.setText("基础信息");
		jcxx_qyxx = (LinearLayout) findViewById(R.id.jcxx_qyxx);
		jcxx_jbxx = (LinearLayout) findViewById(R.id.jcxx_jbxx);
		jcxx_yhkxx = (LinearLayout) findViewById(R.id.jcxx_yhkxx);
		jcxx_xxgl = (LinearLayout) findViewById(R.id.jcxx_xxgl);
		
		if(DataCache.getinition().getZzxx_num()>0){
			BadgeView badgeView = new BadgeView(getApplicationContext());
			badgeView.setTargetView(jcxx_xxgl);
			badgeView.setText(DataCache.getinition().getZzxx_num()+"");
		}
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
		
		jcxx_qyxx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				skipActivity(QyxxActivity.class);
			}
		});
		
		jcxx_jbxx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				skipActivity(JbxxActivity.class);
			}
		});
		
		jcxx_xxgl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				skipActivity(XxglActivity.class);
			}
		});
		
		jcxx_yhkxx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean hasYHK = DataCache.getinition().isHasYHK();
				if (hasYHK) {
					skipActivity(YHKInfoActivity.class);
				} else {
					dialogShowMessage("银行卡信息未完善，去完善 ？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									Intent intent = new Intent(
											getApplicationContext(),
											YhkxxActivity.class);
									startActivityForResult(intent, 1);
								}
							}, null);
				}

			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:
			DataCache.getinition().setHasYHK(true);
			break;
		default:
			break;
		}
	}

	@Override
	protected void getWebService(String s) {

	}

}
