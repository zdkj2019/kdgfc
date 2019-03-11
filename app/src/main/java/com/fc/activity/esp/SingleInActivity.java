package com.fc.activity.esp;

import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.main.MainActivity;
import com.fc.utils.DateTimePickerDialog;
/**
 * 签退
 * @author cheng
 *
 */
public class SingleInActivity extends FrameActivity{
	private TextView sp_yytbz;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_singlein);
		initVariable();
		initView();
		initListeners();
		
	}

	@Override
	protected void initVariable() {
		sp_yytbz = (TextView)findViewById(R.id.sp_yytbz);
	}

	@Override
	protected void initView() {
		
		title.setText("签退");
		//初始化
		Calendar calendar = Calendar.getInstance();
		String time = calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"+calendar.get(Calendar.SECOND);
		sp_yytbz.setText(time);
	}

	@Override
	protected void initListeners() {
		
		sp_yytbz.setOnClickListener(new DateTimeOnClick());
		topBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onBackPressed();
			}
		});
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		skipActivity2(MainActivity.class);
		
	}
	private final class DateTimeOnClick implements OnClickListener {
		public void onClick(View v) {
			
			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(SingleInActivity.this);
			dateTimePicKDialog.dateTimePicKDialog(sp_yytbz, 0);
		}
	}
	
}
