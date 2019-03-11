package com.fc.activity.esp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.utils.Config;
import com.fc.utils.ImageUtil;

/**
 * 证件照上传
 * 
 * @author cheng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class BusinessAcceptanceSure extends FrameActivity implements
		OnClickListener {

	private TextView photo1, tv_preview1;
	private String zbh;
	private Button confirm, cancel;
	private ImageView imageView1;
	private String fromActivity = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_businessacceptancesure);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	public void onBackPressed() {

		if (file1 != null && file1.exists()) {
			file1.delete();
		}

		super.onBackPressed();
	}

	@Override
	protected void initVariable() {

		photo1 = (TextView) findViewById(R.id.tv_getphoto1);
		tv_preview1 = (TextView) findViewById(R.id.tv_preview1);
		imageView1 = (ImageView) findViewById(R.id.imageView1);

		confirm = (Button) findViewById(R.id.include_sureclane).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_sureclane).findViewById(
				R.id.cancel);
	}

	@Override
	protected void initView() {
		title.setText("拍照");

		Intent intent = getIntent();
		// toastShowMessage(intent.getStringE xtra("zbh"));
		String picPath11 = intent.getStringExtra("picPath1");
		fromActivity = intent.getStringExtra("fromActivity");

		if (!"".equals(picPath11) && picPath11 != null) {
			try {
				file1 = new File(picPath11);
				Bitmap bitmap = convertBitmap(file1, 150);
				imageView1.setImageBitmap(bitmap);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void initListeners() {
		Intent intent = getIntent();
		zbh = intent.getStringExtra("zbh");
		photo1.setOnClickListener(this);
		topBack.setOnClickListener(this);
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		tv_preview1.setOnClickListener(this);
	}

	@Override
	protected void getWebService(String s) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_getphoto1:
			//camera(1);
			break;

		case R.id.bt_topback:
			onBackPressed();
			break;
		case R.id.cancel:
			onBackPressed();
			break;
		case R.id.confirm:
			if (file1 != null) {

				confirmPressed();
			} else {
				toastShowMessage("请拍摄照片");
			}
			break;
		case R.id.tv_preview1:// 预览
			previewPressed(file1);
			break;

		}

	}

	private void previewPressed(File file) {
		if (file != null && file.exists()) {
			// 使用Intent
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// Uri mUri = Uri.parse("file://" +
			// picFile.getPath());Android3.0以后最好不要通过该方法，存在一些小Bug
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			startActivity(intent);
		}
	}

	// 上传照片
	private void confirmPressed() {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				upload();
			}
		});

	}

	private String filename;

	private void camera(int requestCode,int num) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			toastShowMessage("没有储存卡，不能拍照");
			return;
		}
		filename = String.valueOf(System.currentTimeMillis()).trim()
				.substring(4);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), filename + ".jpg")));
		startActivityForResult(intent, requestCode);

	}

	private File file1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				// 读pic
				String filepath = Environment.getExternalStorageDirectory().toString() + "/" + filename + ".jpg";

				// 压缩大小 压缩后生成的图片覆盖原来的图片
				try {
					ImageUtil.compressAndGenImage(convertBitmap(new File(filepath), 480),filepath, 200);
					file1 = new File(filepath);
					imageView1.setImageBitmap(convertBitmap(file1, 150));
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {

				toastShowMessage("拍照失败");
			}
		}
	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) {

		if (data1 != null && orderNumbers != null) {

			try {
				JSONObject json = callWebserviceImp.getWebServerInfo2(
						"_FWBG_1", orderNumbers, data1, methed, this);
				String flag = json.getString("flag");
				if ("1".equals(flag)) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message localMessage1 = new Message();
				localMessage1.what = -3;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(localMessage1);
				return false;
			}

		}
		return false;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -2:
				dialogShowMessage_P("数据获取失败", null);
				break;
			case -1:
				dialogShowMessage_P("获取基础数据失败", null);
				break;
			case 1:
				dialogShowMessage_P("上传完成,单号" + zbh, null);

				break;
			case 0:
				dialogShowMessage_P("提交失败，请检查后重试...错误标识：", null);
				// dialogShowMessage_P("提交失败，请检查后重试...错误标识：" + flag, null);
				break;

			case 6:

				dialogShowMessage_P("上传完成,单号" + zbh,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								if ("ServiceReportsComplete"
										.equals(fromActivity)) {
									Intent intent = getIntent();
									intent.putExtra("flag", "true");
									setResult(21, intent);
									finish();
								} else {
									onBackPressed();
								}

							}
						});

				break;
			}
			progressDialog.dismiss();
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	private void upload() {
		try {
			String picPath1 = file1.getPath();
			if (file1 != null) {
				ImageUtil.compressAndGenImage(convertBitmap(file1, 480),
						picPath1, 200);
				file1 = new File(picPath1);
			}
			byte[] readJpeg1 = readJpeg(file1);
			if (readJpeg1 == null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						toastShowMessage("图片为空,请先拍照");
					}
				});
				progressDialog.dismiss();
				return;
			}
			boolean uploadPic1 = uploadPic(zbh, readJpeg1,
					"uf_json_setdata2_p1");

			if (uploadPic1) {
				File file11 = new File(picPath1);
				if (file11.exists()) {
					file11.delete();
				}
			}
			if (uploadPic1) {

				Message localMessage1 = new Message();
				localMessage1.what = 6;// 图片上传成功
				handler.sendMessage(localMessage1);
			} else {
				Message localMessage1 = new Message();
				localMessage1.what = -3;// 图片上传失败
				handler.sendMessage(localMessage1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message localMessage1 = new Message();
			localMessage1.what = -3;// 图片上传失败
			handler.sendMessage(localMessage1);
		}

	}
}
