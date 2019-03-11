package com.fc.utils;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fc.R;
import com.fc.cache.DataCache;

/**
 * 重手机底部弹出Dialog选择框
 * 
 * @author wlj
 * 
 */
public class MyDialog {

	private String checkedtxet,checkedtxet2, selectedId2;
	private AlertDialog dlg = null;
	private Button dlg_confirmb;
	private Button dlg_cancelb;
	private TextView dlg_title;
	private Animation translate_Animation;
	private Window window;
	private DisplayMetrics dm;
	private Activity activity;
	private String[] cardtype = { "互联互通","未恢复/安装失败" };
//	private MyDialog mMyDialog;
	public MyDialog(Activity activity) {

		this.activity = activity;
		this.dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(this.dm);
		this.dlg = new AlertDialog.Builder(activity).create();
		this.window = dlg.getWindow();
		this.translate_Animation = AnimationUtils.loadAnimation(activity,
				R.anim.translate);
	}

	public interface buttonSure{
		 
		public void onClick(String checkedtxet,Spinner sp); 
	 }; 
//	public sync hronized static MyDialog getInstall(Activity activity) {
//
//		if (mMyDialog == null) {
//			mMyDialog = new MyDialog(activity);
//		}
//		return mMyDialog;
//	}

	/**
	 * (TextView, "派工对象", data, "员工编码", "姓名");
	 * 
	 * @param view
	 * @param title
	 * @param date
	 * @param titleid
	 * @paraarg0m titlename
	 */
	public void defined2(final TextView view, String title,
			List<Map<String, String>> date, String titleid, String titlename) {

		checkedtxet2 = null;
		this.dlg.show();
		this.window.setContentView(R.layout.definedspinner2);

		dlg_confirmb = (Button) window.findViewById(R.id.dlg_confirmb);
		dlg_cancelb = (Button) window.findViewById(R.id.dlg_cancelb);
		dlg_title = (TextView) window.findViewById(R.id.dlg_title);
		dlg_title.setText(title);
		((TextView) window.findViewById(R.id.titleid)).setText(titleid);
		((TextView) window.findViewById(R.id.titlename)).setText(titlename);
		// 动态出现
		((RelativeLayout) this.window.findViewById(R.id.spconent))
				.startAnimation(this.translate_Animation);
		// 空白宽
		((LinearLayout) this.window.findViewById(R.id.LLspinner))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1, -100
						+ this.dm.heightPixels / 2));

		// listview
		final ListView listview = (ListView) window.findViewById(R.id.listView);
		final MySimpleAdapter simpleAdapter = new MySimpleAdapter(activity,
				date, R.layout.definedspinner2_item, new String[] { "id",
						"name" }, new int[] { R.id.tV_number, R.id.tV_name });
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setAdapter(simpleAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View cview,
					int position, long id) {

				MySimpleAdapter.mposition = position;
				simpleAdapter.notifyDataSetChanged();// 更新可见itemView

				TextView tV_number = (TextView) cview
						.findViewById(R.id.tV_number);
				TextView tV_name = (TextView) cview.findViewById(R.id.tV_name);

				checkedtxet2 = tV_name.getText().toString();
				selectedId2 = tV_number.getText().toString();

				view.setText(checkedtxet2);
				view.setTag(selectedId2);
				DataCache.getinition().setSelectedId(selectedId2);
				dlg.dismiss();

				// Log.e("dd", selectedId2);
			}
		});

		View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.dlg_confirmb:
					if (checkedtxet2 == null) {
						// 打开了dlg，但没点选
						dlg.dismiss();
					} else {
						view.setText(checkedtxet2);
						view.setTag(selectedId2);
						DataCache.getinition().setSelectedId(selectedId2);
						dlg.dismiss();
					}
					break;

				case R.id.dlg_cancelb:
					dlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);

	}

	/**
	 * (TextView, "处理方式", clfs_list);
	 * 专为服务评价写的
	 * view.setTag(checkedtxet);
	 * @param view
	 * @param name
	 * @param arrayradiotext
	 */
	public void redioDialog(final TextView view, String name,
			List<String> arrayradiotext) {

		dlg.show();
		window.setContentView(R.layout.definedspinner);

		dlg_confirmb = (Button) window.findViewById(R.id.dlg_confirmb);
		dlg_cancelb = (Button) window.findViewById(R.id.dlg_cancelb);
		dlg_title = (TextView) window.findViewById(R.id.dlg_title);
		dlg_title.setText(name);
		// 动态出现
		((RelativeLayout) this.window.findViewById(R.id.spconent))
				.startAnimation(this.translate_Animation);
		// 空白宽
		((LinearLayout) this.window.findViewById(R.id.LLspinner))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1, -40
						+ this.dm.heightPixels/2));

		// 动态添加radio item text
		RadioGroup localRadioGroup = (RadioGroup) this.window
				.findViewById(R.id.dlg_radioGroup1);
		for (int i = 0; i < arrayradiotext.size(); i++) {

			RadioButton localView = new RadioButton(activity);
			localView.setPadding(140, 5, 5, 0);
			localView.setTextSize(18.0f);
			localView.setButtonDrawable(R.drawable.dlg_radio);
			localView.setGravity(Gravity.CENTER);
			localView.setTextColor(Color.BLACK);
			localView.setText(arrayradiotext.get(i));
			// localView.setSingleLine();
			localRadioGroup.addView(localView);
		}

		localRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					public void onCheckedChanged(RadioGroup Group, int id) {

						RadioButton localRadioButton = (RadioButton) window
								.findViewById(id);
						String checkedtxet = localRadioButton.getText()
								.toString();
						dlg.dismiss();
						view.setTag(checkedtxet);
					}

				});

		View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.dlg_confirmb:

					dlg.dismiss();
					break;

				case R.id.dlg_cancelb:
					dlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);
	}
	
	/**
	 * (TextView, "处理方式", clfs_list);
	 * 专为服务评价写的
	 * view.setTag(checkedtxet);
	 * @param view
	 * @param name
	 * @param arrayradiotext
	 */
	public void redioDialog_fwbg(final TextView view, String name,
			List<String> arrayradiotext) {
		dlg.setView((activity).getLayoutInflater().inflate(R.layout.definedspinner_fwbg, null));
		dlg.show();
		window.setContentView(R.layout.definedspinner_fwbg);

		dlg_confirmb = (Button) window.findViewById(R.id.dlg_confirmb);
		dlg_cancelb = (Button) window.findViewById(R.id.dlg_cancelb);
		dlg_title = (TextView) window.findViewById(R.id.dlg_title);
		dlg_title.setText(name);
		// 动态出现
		((RelativeLayout) this.window.findViewById(R.id.spconent))
				.startAnimation(this.translate_Animation);
		// 空白宽
		((LinearLayout) this.window.findViewById(R.id.LLspinner))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1, -40
						+ this.dm.heightPixels/2));
		final CheckBox localView0 = (CheckBox) window.findViewById(R.id.fwbg_bmy_0);
		localView0.setText(arrayradiotext.get(0));
		final CheckBox localView1 = (CheckBox) window.findViewById(R.id.fwbg_bmy_1);
		localView1.setText(arrayradiotext.get(1));
		final CheckBox localView2 = (CheckBox) window.findViewById(R.id.fwbg_bmy_2);
		localView2.setText(arrayradiotext.get(2));
		final EditText fwbg_bmy_4 = (EditText) window.findViewById(R.id.fwbg_bmy_4);
		
		
		View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.dlg_confirmb:
					
					String value = "";
					if(localView0.isChecked()){
						value+=localView0.getText()+";";
					}
					if(localView1.isChecked()){
						value+=localView1.getText()+";";
					}
					if(localView2.isChecked()){
						value+=localView2.getText()+";";
					}
					value+=fwbg_bmy_4.getText();
					view.setTag(value);
					
					dlg.dismiss();
					break;

				case R.id.dlg_cancelb:
					dlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);
	}

	/**
	 * ((TextView) v, "处理结果", cljg_list, "")
	 * 
	 * @param view
	 * @param title
	 * @param arrayradiotext
	 * @param checked  没用
	 */
	public void redioDialog(final TextView view, String title,
			List<String> arrayradiotext,final String checked) {
		
		this.dlg.show();
		this.window.setContentView(R.layout.definedspinner);

		dlg_confirmb = (Button) window.findViewById(R.id.dlg_confirmb);
		dlg_cancelb = (Button) window.findViewById(R.id.dlg_cancelb);
		dlg_title = (TextView) window.findViewById(R.id.dlg_title);
		dlg_title.setText(title);
		// 动态出现
		((RelativeLayout) this.window.findViewById(R.id.spconent))
				.startAnimation(this.translate_Animation);
		// 空白宽
		((LinearLayout) this.window.findViewById(R.id.LLspinner))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1, -40
						+ this.dm.heightPixels/2));

		// 动态添加radio item text
		RadioGroup localRadioGroup = (RadioGroup) this.window
				.findViewById(R.id.dlg_radioGroup1);
		for (int i = 0; i < arrayradiotext.size(); i++) {

			RadioButton localView = new RadioButton(activity);
			localView.setPadding(140, 5, 5, 0);
			localView.setTextSize(18.0f);
			localView.setButtonDrawable(R.drawable.dlg_radio);
			localView.setGravity(Gravity.CENTER);
			localView.setTextColor(Color.BLACK);
			localView.setText(arrayradiotext.get(i));
			// localView.setSingleLine();
			localRadioGroup.addView(localView);
		}
		//
		localRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					public void onCheckedChanged(RadioGroup Group, int id) {

						RadioButton localRadioButton = (RadioButton) window.findViewById(id);
						String checkedtxet = localRadioButton.getText()
								.toString();
						dlg.dismiss();
						view.setText(checkedtxet);
					}

				});

		View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.dlg_confirmb:

					dlg.dismiss();
					break;

				case R.id.dlg_cancelb:
					dlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);
	
	}
	
	
	private Window inputwindow;
	private Spinner dialog_spinner;
	private RelativeLayout spinner_lay;
	/**
	 * 带EditText的 AlertDialog
	 * 
	 * @param view 选定内容输出的view
	 * @param name title
	 * @param arrayradiotext 共选择的项（list）
	 * @param edtext 当选中项内容为edtext，则显示输入框
	 */
	public void inputdialog(final TextView view, String name,
			List<String> arrayradiotext, final String edtext) {
		
		this.dlg.show();
		inputwindow = dlg.getWindow();
		inputwindow.setContentView(activity.getLayoutInflater().inflate(
				R.layout.definedspinner_spinner, null));

		dlg_confirmb = (Button) inputwindow.findViewById(R.id.dlg_confirmb);
		dlg_cancelb = (Button) inputwindow.findViewById(R.id.dlg_cancelb);
		dlg_title = (TextView) inputwindow.findViewById(R.id.dlg_title);
		dlg_title.setText(name);
		// 动态出现
		((RelativeLayout) inputwindow.findViewById(R.id.spconent))
				.startAnimation(this.translate_Animation);
		// 空白宽
		((LinearLayout) inputwindow.findViewById(R.id.LLspinner))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1, -40
						+ this.dm.heightPixels / 2));

		// 动态添加radio item text
		final RadioGroup localRadioGroup = (RadioGroup) inputwindow
				.findViewById(R.id.dlg_radioGroup);
		for (int i = 0; i < arrayradiotext.size(); i++) {

			RadioButton localView = new RadioButton(activity);
			localView.setPadding(140, 5, 5, 0);
			localView.setTextSize(18.0f);
			localView.setButtonDrawable(R.drawable.dlg_radio);
			localView.setGravity(Gravity.CENTER);
			localView.setTextColor(Color.BLACK);
			localView.setText(arrayradiotext.get(i));
			// localView.setSingleLine();
			localRadioGroup.addView(localView);
		}

		localRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					public void onCheckedChanged(RadioGroup Group, int id) {

						RadioButton localRadioButton = (RadioButton) inputwindow
								.findViewById(id);
						
						spinner_lay = (RelativeLayout) inputwindow.findViewById(R.id.spinner_lay);
						checkedtxet = localRadioButton.getText().toString();
						if (edtext.equals(checkedtxet)) {
							spinner_lay.setVisibility(View.VISIBLE);
							dialog_spinner = (Spinner) inputwindow.findViewById(R.id.dialog_spinner);
							
							SpinnerAdapter adapter = new SpinnerAdapter(activity,
									android.R.layout.simple_spinner_dropdown_item, cardtype);
							dialog_spinner.setAdapter(adapter);
							
						} else {
							spinner_lay.setVisibility(View.INVISIBLE);
						}

						// dlg.dismiss();
						// view.setText(checkedtxet);
					}

				});

		View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.dlg_confirmb:
					if (edtext.equals(checkedtxet)) {
						String tag = dialog_spinner.getSelectedItem().toString();
						view.setTag(tag);
					}else{
						view.setTag("");
					}
					view.setText(checkedtxet);
					dlg.dismiss();
					break;

				case R.id.dlg_cancelb:
					dlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);
	}
	
	/**
	 * 带EditText的 AlertDialog
	 * 
	 * @param name title
	 * @param arrayradiotext 共选择的项（list）
	 * @param edtext 当选中项内容为edtext，则显示输入框
	 * @param spinneritem spinner内容的String数组
	 */
	public void inputdialog(String title,List<String> arrayradiotext, 
			final String edtext,final String[] spinneritem,final buttonSure bs) {
		
		this.dlg.show();
		inputwindow = dlg.getWindow();
		inputwindow.setContentView(activity.getLayoutInflater().inflate(
				R.layout.definedspinner_spinner, null));

		dlg_confirmb = (Button) inputwindow.findViewById(R.id.dlg_confirmb);
		dlg_cancelb = (Button) inputwindow.findViewById(R.id.dlg_cancelb);
		dlg_title = (TextView) inputwindow.findViewById(R.id.dlg_title);
		dlg_title.setText(title);
		// 动态出现
		((RelativeLayout) inputwindow.findViewById(R.id.spconent))
				.startAnimation(this.translate_Animation);
		// 空白宽
		((LinearLayout) inputwindow.findViewById(R.id.LLspinner))
				.setLayoutParams(new RelativeLayout.LayoutParams(-1, -40
						+ this.dm.heightPixels / 2));

		// 动态添加radio item text
		final RadioGroup localRadioGroup = (RadioGroup) inputwindow
				.findViewById(R.id.dlg_radioGroup);
		for (int i = 0; i < arrayradiotext.size(); i++) {

			RadioButton localView = new RadioButton(activity);
			localView.setPadding(140, 5, 5, 0);
			localView.setTextSize(18.0f);
			localView.setButtonDrawable(R.drawable.dlg_radio);
			localView.setGravity(Gravity.CENTER);
			localView.setTextColor(Color.BLACK);
			localView.setText(arrayradiotext.get(i));
			// localView.setSingleLine();
			localRadioGroup.addView(localView);
		}

		localRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					public void onCheckedChanged(RadioGroup Group, int id) {

						RadioButton localRadioButton = (RadioButton) inputwindow
								.findViewById(id);
						
						spinner_lay = (RelativeLayout) inputwindow.findViewById(R.id.spinner_lay);
						checkedtxet = localRadioButton.getText().toString();
						if (edtext.equals(checkedtxet)) {
							spinner_lay.setVisibility(View.VISIBLE);
							dialog_spinner = (Spinner) inputwindow.findViewById(R.id.dialog_spinner);
							
							if(spinneritem.length<1){
							
								Toast.makeText(activity, "故障类别条目为空", Toast.LENGTH_SHORT).show();
								return;
							}
							SpinnerAdapter adapter = new SpinnerAdapter(activity,
									android.R.layout.simple_spinner_dropdown_item, spinneritem);
							dialog_spinner.setAdapter(adapter);
							
						} else {
							spinner_lay.setVisibility(View.INVISIBLE);
						}

						// dlg.dismiss();
						// view.setText(checkedtxet);
					}

				});

		View.OnClickListener onClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.dlg_confirmb:
					bs.onClick(checkedtxet, dialog_spinner);
					dlg.dismiss();
					break;

				case R.id.dlg_cancelb:
					dlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);
	}
	
}
