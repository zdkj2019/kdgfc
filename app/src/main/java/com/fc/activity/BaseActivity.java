package com.fc.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fc.R;
import com.fc.cache.DataCache;
import com.fc.utils.MySimpleAdapter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseActivity extends Activity {
	protected ProgressDialog progressDialog;
	protected Window window;
	protected AlertDialog dlg = null;
	protected String checkedtxet, checkedtxet2, selectedId2;
	protected DisplayMetrics dm;
	protected Animation translate_Animation;
	protected Button dlg_confirmb;
	protected Button dlg_cancelb;
	protected TextView dlg_title;
	protected boolean backboolean;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(this.dm);
		this.translate_Animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate);
		dlg = new AlertDialog.Builder(this).create();
		this.window = dlg.getWindow();
		Intent backintent = getIntent();
		backboolean = backintent.getBooleanExtra("fanhui", false);
		initImageLoader();
	}

	protected void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		imageLoader.init(config);
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// Log.e("dd", getClass().getSimpleName()+"    onDestroy");
	// // 结束Activity&从堆栈中移除
	// AppManager.getAppManager().finishActivity(this);
	// }
	/**
	 * Toast显示消息
	 * 
	 * @param text
	 *            需要显示的内容
	 */
	protected void toastShowMessage(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 返回上一个activity
	 * 
	 * @param cls
	 *            需要跳转的activity
	 */
	protected void skipActivity2(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		intent.putExtra("fanhui", true);
		startActivity(intent);
		// finish();
	}

	/**
	 * 跳转到下另外一个activity
	 * 
	 * @param cls
	 *            需要跳转的activity
	 */
	protected void skipActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		// finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.e("dd", "onNewIntent");
		setIntent(intent);
	}

	protected void Call(String tel) {
		Intent phoneIntent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + tel));
		startActivity(phoneIntent);
	}

	protected void dialogShowMessage_P(String message,
			OnClickListener confirmListener) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setMessage(message);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", confirmListener);
		builder.create().show();

	}

	/**
	 * 对话框显示消息
	 * 
	 * @param message
	 *            需要显示的消息
	 * @param confirmListener
	 *            点击确认的事件处理
	 * @param cancelListener
	 *            点击取消的事件处理
	 */
	protected void dialogShowMessage(String message,
			OnClickListener confirmListener, OnClickListener canlListener) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setMessage(message);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", confirmListener);
		builder.setNegativeButton("取消",
				canlListener == null ? new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,
							int paramAnonymous2Int) {
						face.dismiss();
					}
				} : canlListener);
		builder.create().show();

	}

	protected void showProgressDialog() {
		progressDialog = ProgressDialog.show(this, "提示", "正在处理中，请稍后...");
	}

	/**
	 * dlg
	 * 
	 * @param view
	 * @param arrayradiotext
	 */
	protected void defined(final TextView view, String name,
			List<String> arrayradiotext) {

		this.dlg.show();
		this.window.setContentView(R.layout.definedspinner);

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
						+ this.dm.heightPixels / 2));

		// 动态添加radio item text
		RadioGroup localRadioGroup = (RadioGroup) this.window
				.findViewById(R.id.dlg_radioGroup1);
		for (int i = 0; i < arrayradiotext.size(); i++) {

			RadioButton localView = new RadioButton(this);
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
						checkedtxet = localRadioButton.getText().toString();
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

	protected void defined2(final TextView view, String title,
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
		final MySimpleAdapter simpleAdapter = new MySimpleAdapter(
				getApplicationContext(), date, R.layout.definedspinner2_item,
				new String[] { "id", "name" }, new int[] { R.id.tV_number,
						R.id.tV_name });
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setAdapter(simpleAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View cview,
					int position, long id) {

				MySimpleAdapter.mposition = position;
				simpleAdapter.notifyDataSetChanged();// 更新可见viewitem

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

	private EditText searchC;
	protected AlertDialog inputdlg;
	protected Window inputwindow;

	/**
	 * 带EditText的 AlertDialog
	 * 
	 * @param view
	 *            选定内容输出的view
	 * @param name
	 *            title
	 * @param arrayradiotext
	 *            共选择的项（list）
	 * @param edtext
	 *            当选中项内容为edtext，则显示输入框
	 */
	protected void inputdialog(final TextView view, String name,
			List<String> arrayradiotext, final String edtext) {
		inputdlg = new AlertDialog.Builder(this).create();
		// et_search能输入东西
		View inflate = getLayoutInflater().inflate(
				R.layout.definedspinner_edittext, null);

		inputdlg.setView(inflate);

		searchC = (EditText) inflate.findViewById(R.id.searchC);

		searchC.setText("一定要弹出键");

		dlg_confirmb = (Button) inflate.findViewById(R.id.dlg_confirmb);

		inputdlg.show();

		inputwindow = inputdlg.getWindow();

		inputwindow.setContentView(getLayoutInflater().inflate(
				R.layout.definedspinner_edittext, null));

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

			RadioButton localView = new RadioButton(this);
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
						searchC = (EditText) inputwindow
								.findViewById(R.id.searchC);
						searchC.setTextColor(Color.BLACK);
						searchC.setCursorVisible(true);
						checkedtxet = localRadioButton.getText().toString();
						if (edtext.equals(checkedtxet)) {
							searchC.setVisibility(View.VISIBLE);
						} else {
							searchC.setVisibility(View.INVISIBLE);
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
						checkedtxet = searchC.getText().toString();
					}
					view.setText(checkedtxet);
					inputdlg.dismiss();
					break;

				case R.id.dlg_cancelb:
					inputdlg.dismiss();
					break;
				}
			}
		};

		dlg_confirmb.setOnClickListener(onClickListener);
		dlg_cancelb.setOnClickListener(onClickListener);
	}

}
