package com.fc.activity.esp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fc.R;
import com.fc.activity.FrameActivity;
import com.fc.activity.js.FyqrActivity;
import com.fc.activity.js.LiuShuiZhanglist;
import com.fc.activity.js.YingShouKuanList;
import com.fc.activity.kc.KccxActivity;
import com.fc.activity.kc.WbdbckActivity;
import com.fc.activity.kc.WbdbrkListActivity;
import com.fc.activity.login.LoginActivity;
import com.fc.activity.w.ChangePasswordActivity;
import com.fc.activity.w.XxglActivity;
import com.fc.activity.w.YhkxxActivity;
import com.fc.cache.DataCache;
import com.fc.definition.FloatingFunc;
import com.fc.dodowaterfall.ImageLoaderTask;
import com.fc.dodowaterfall.LazyScrollView;
import com.fc.dodowaterfall.MarqueeTextView;
import com.fc.dodowaterfall.TaskParam;
import com.fc.dodowaterfall.LazyScrollView.OnScrollListener;
import com.fc.service.NumberService;
import com.fc.utils.Config;
import com.jauker.widget.BadgeView;

public class MainMenuActivity extends FrameActivity {

	private LazyScrollView waterfall_scroll;
	private LinearLayout waterfall_container, tab_bottom_esp, tab_bottom_kdg;
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;
	private AssetManager assetManager;
	private List<String> menu,image_filenames;
	private final String image_path = "imagess";
	private int itemWidth;
	private int column_count = 2;// 显示列数
	private int page_count = 12;// 每次加载15张图
	private int current_page = 0;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private TextView tishi;
	private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点
	private TextView tv_title;
	private MarqueeTextView tv_marquee;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	private String ts_msg = "";
	private ImageView img_ry;
	private BadgeView badgeView;
	private Long mExitTime = -2000l;
	private int currType = 1; //当前类型：1.esp 2.快递柜
	private String[] menusoure,menusoure_esp,menusoure_kdg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_);
		display = this.getWindowManager().getDefaultDisplay();
		itemWidth = display.getWidth() / column_count;// 根据屏幕大小计算每列大小
		assetManager = this.getAssets();
		InitLayout();
		initViewPagerActivity();
		//
		Intent intent = new Intent(getApplicationContext(), NumberService.class);
		// 设置一个PendingIntent对象，发送广播
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 5 * 1000,
				5 * 60 * 1000, pi);

		img_ry = (ImageView) findViewById(R.id.img_ry);
		img_ry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				badgeView.setVisibility(View.GONE);
				skipActivity(RyxxActivity.class);
			}
		});
		badgeView = new BadgeView(getApplicationContext());
		badgeView.setTargetView(img_ry);
		badgeView.setWidth(12);
		badgeView.setHeight(12);

		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				//getWebService("getTsxx");
			}
		});

	}

	private void InitLayout() {

		initView();

		waterfall_scroll = (LazyScrollView) findViewById(R.id.waterfall_scroll);
		waterfall_scroll.getView();
		waterfall_scroll.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onTop() {
				// 滚动到最顶端
				// 初始化数据后调用，scrollView.scrollTo(0, 1)，避免4.2版本滑不动问题
				// 一定要有刷新延迟否则scrollTo失效
				waterfall_scroll.postDelayed(new Runnable() {
					@Override
					public void run() {
						waterfall_scroll.scrollTo(0, 1);
					}
				}, 300);
			}

			@Override
			public void onScroll() {
				// 滚动中
			}

			@Override
			public void onBottom() {
				// 滚动到最低端
				AddItemToContainer(++current_page, page_count);
			}
		});

		waterfall_container = (LinearLayout) this
				.findViewById(R.id.waterfall_container);
		waterfall_items = new ArrayList<LinearLayout>();

		for (int i = 0; i < column_count; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
					itemWidth, LayoutParams.WRAP_CONTENT);
			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			waterfall_items.add(itemLayout);
			waterfall_container.addView(itemLayout);
		}
		// 第一次加载
		AddItemToContainer(current_page, page_count);

		// 初始化数据后调用，scrollView.scrollTo(0, 1)，避免4.2版本滑不动问题
		// 一定要有刷新延迟否则scrollTo失效
		waterfall_scroll.postDelayed(new Runnable() {
			@Override
			public void run() {
				waterfall_scroll.scrollTo(0, 1);
			}
		}, 300);

		tv_marquee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogShowMessage_P(tv_marquee.getText().toString(), null);
			}
		});

	}

	private void initViewPagerActivity() {
		imageResId = new int[] { R.drawable.a, R.drawable.b };
		titles = new String[imageResId.length];
		titles[0] = "";
		titles[1] = "";

		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(titles[0]);//

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	protected void initView() {

		tab_bottom_esp = (LinearLayout) findViewById(R.id.tab_bottom_esp);
		tab_bottom_kdg = (LinearLayout) findViewById(R.id.tab_bottom_kdg);

		((TextView) findViewById(R.id.textView1)).setText(DataCache
				.getinition().getUsername());
		tv_marquee = (MarqueeTextView) findViewById(R.id.tv_marquee);
		tishi = (TextView) findViewById(R.id.tishi);
		image_filenames = new ArrayList<String>();
		menu = DataCache.getinition().getMenu();
		if (menu == null || menu.isEmpty()) {
			toastShowMessage("没有菜单权限，请联系管理员");
			skipActivity(LoginActivity.class);
			return;
		}

		// 模块权限名称，模块的显示顺序
		menusoure_esp = new String[] { "m_pad_yytbz", // 故障申报
				"m_pad_gdqd", // 电子抢单
				"m_pad_jdxy",// 接单相应
				"m_pad_jjjd",// 拒绝接单
				"m_pad_smddqr", // 上门定位
				"m_pad_fwbg", // 服务报告
				"m_pad_zzfwbg", // 纸质报告
				"m_pad_sblr", // 设备录入
				"m_pad_pgdcx",// 近期工单查询
				"m_pad_dqyskcx",// 应收款
				"m_pad_yslszcx",// 流水账
				"m_pad_srqr", // 费用确认
				"m_pad_zyxx", // 重要消息
				"m_pad_ckdqkc",// 当前库存
				"m_pad_wbdbrk",// 外部调拨入库
				"m_pad_wbdbck",// 外部调拨出库
				"m_pad_xgmm"// 修改密码
		};
		// 对应menusoure的图片
		Integer[] picname1 = new Integer[] { 11, 12, 13, 131, 15, 16, 17, 18,
				19, 190, 191, 1911, 1912, 1913, 1914, 1915, 192 };// 小图
		Integer[] picname2 = new Integer[] { 21, 22, 23, 231, 25, 26, 27, 28,
				29, 290, 291, 2911, 2912, 2913, 2914, 2915, 292 };// 大图

		int size = 0;

		for (int i = 0; i < menusoure_esp.length; i++) {
			String string = menusoure_esp[i];
			for (int k = 0; k < menu.size(); k++) {
				if (string.equals(menu.get(k))) {
					size++;
					break;
				}
			}
		}

		List<Integer> chooseList = chooseList(size);

		int j = 0;
		for (int i = 0; i < menusoure_esp.length; i++) {
			String string = menusoure_esp[i];// m_pad_js
			for (int k = 0; k < menu.size(); k++) {
				if (string.equals(menu.get(k))) {
					if (chooseList.get(j) == 1) {
						image_filenames.add(picname1[i] + ".png");
					} else {
						image_filenames.add(picname2[i] + ".png");
					}
					j++;
					break;
				}
			}
		}
	}

	private void AddItemToContainer(int pageindex, int pagecount) {
		int j = 0;
		int imagecount = image_filenames.size();
		for (int i = pageindex * pagecount; i < pagecount * (pageindex + 1)
				&& i < imagecount; i++) {
			j = j >= column_count ? j = 0 : j;
			AddImage(image_filenames.get(i), j++);
		}
	}

	private void AddImage(String filename, int columnIndex) {
		ImageView item = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.waterfallitem, null);
		waterfall_items.get(columnIndex).addView(item);

		TaskParam param = new TaskParam();
		param.setAssetManager(assetManager);
		param.setFilename(image_path + "/" + filename);
		param.setItemWidth(itemWidth);
		ImageLoaderTask task = new ImageLoaderTask(item);
		task.execute(param);
		String[] split = filename.split("\\.");
		if (split.length > 1) {
			item.setTag(split[0]);
		}
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (v.getTag() == null) {
					Toast.makeText(MainMenuActivity.this, "您点击了" + v.getTag(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Integer tag = Integer.valueOf(v.getTag().toString());
				if (tag == 11 || tag == 21) {
					// 报障 set-2/
					skipActivity(YytReportedActivity.class);
					DataCache.getinition().setTitle("故障申报");
					return;
				} else if (tag == 12 || tag == 22) {
					// 电子抢单
					skipActivity(InformationReceivinglist_Dzqd.class);
					DataCache.getinition().setTitle("电子抢单");
					return;
				} else if (tag == 13 || tag == 23) {
					// 工程师接收-1 set 1
					skipActivity(InformationReceivinglist.class);
					DataCache.getinition().setTitle("接单响应");
					return;
				} else if (tag == 131 || tag == 231) {
					// 工程师接收get 1 set 1.5
					skipActivity(InformationReceivingRefuseList.class);
					DataCache.getinition().setTitle("拒绝接单");
					return;
				} else if (tag == 15 || tag == 25) {
					// 信息到达 query1 set2
					skipActivity(ArrivalConfirmationActivity.class);
					return;
				} else if (tag == 16 || tag == 26) {
					// 服务报告query2 set 3
					skipActivity(ServiceReportslist.class);
					DataCache.getinition().setBs(1);// 当电子服务报告的标志?
					DataCache.getinition().setTitle("电子服务报告");
					return;
				} else if (tag == 17 || tag == 27) {
					// 纸质报告上传
					skipActivity(ZZServiceReportslist.class);
					DataCache.getinition().setTitle("纸质服务报告");
					return;

				} else if (tag == 19 || tag == 29) {
					Intent intent = new Intent(getApplicationContext(),
							PGDCaseActivity.class);
					intent.putExtra("time", "2013-6-28*2013-7-13");
					startActivity(intent);
					DataCache.getinition().setTitle("近期工单查询");
					return;
				} else if (tag == 190 || tag == 290) {
					// 应收款
					skipActivity(YingShouKuanList.class);
					return;
				} else if (tag == 191 || tag == 291) {
					// 流水账
					skipActivity(LiuShuiZhanglist.class);
					return;
				} else if (tag == 192 || tag == 292) {
					// 修改密码
					skipActivity(ChangePasswordActivity.class);
					return;
				} else if (tag == 1911 || tag == 2911) {
					// 费用确认
					skipActivity(FyqrActivity.class);
					DataCache.getinition().setTitle("费用确认");
					return;
				} else if (tag == 18 || tag == 28) {
					// 设备信息录入
					skipActivity(SheBeiPDActivity.class);
					DataCache.getinition().setTitle("设备信息录入");
					return;
				} else if (tag == 1912 || tag == 2912) {
					// 重要消息
					skipActivity(XxglActivity.class);
					DataCache.getinition().setTitle("重要消息");
					return;
				} else if (tag == 1913 || tag == 2913) {
					// 当前库存查询
					skipActivity(KccxActivity.class);
					DataCache.getinition().setTitle("当前库存查询");
					return;
				} else if (tag == 1914 || tag == 2914) {
					// 当前库存查询
					skipActivity(WbdbrkListActivity.class);
					DataCache.getinition().setTitle("外部调拨入库");
					return;
				} else if (tag == 1915 || tag == 2915) {
					// 当前库存查询
					skipActivity(WbdbckActivity.class);
					DataCache.getinition().setTitle("外部调拨出库");
					return;
				}

			}

		});
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tab_bottom_esp:
					currType = 1;
					setImages();
					break;
				case R.id.tab_bottom_kdg:
					currType = 2;
					setImages();
					break;
				default:
					break;
				}

			}
		};

		tab_bottom_esp.setOnClickListener(onClickListener);
		tab_bottom_kdg.setOnClickListener(onClickListener);
	}
	
	private void setImages(){
		
	}

	@Override
	protected void getWebService(String s) {

		if ("getTsxx".equals(s)) {
			try {

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SJXX_ZDKJ", "", "uf_json_getdata", this);
				String flag2 = jsonObject.getString("flag");
				if (Integer.parseInt(flag2) > 0) {
					JSONArray array = jsonObject.getJSONArray("tableA");
					JSONObject temp = array.getJSONObject(0);
					ts_msg = temp.getString("ts_msg");
					Message msg = Message.obtain();
					msg.what = 1;// 失败
					handlernet.sendMessage(msg);
				} else {
					Message msg = Message.obtain();
					msg.what = 1;// 失败
					handlernet.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = Message.obtain();
				msg.what = NETWORK_ERROR;// 网络不通
				handlernet.sendMessage(msg);
			}
		}

		if ("getYHKxx".equals(s)) {
			try {
				// 查询英航卡信息是否完善
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YHK_CX", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				jsonObject = (JSONObject) jsonArray.get(0);
				if (!"1".equals(jsonObject.getString("yhk"))) {
					DataCache.getinition().setHasYHK(true);
				} else {
					DataCache.getinition().setHasYHK(false);
					Message msg = Message.obtain();
					msg.what = 2;// 失败
					handlernet.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	private List<Integer> chooseList(int size) {
		Integer[] guize = new Integer[] { 2, 1, 1, 2 };
		int yu = size % guize.length;
		int chu = size / guize.length;

		// 大(2)小 (1)图list
		List<Integer> chooseList = new LinkedList<Integer>();
		for (int i = 0; i < chu; i++) {

			for (Integer integer : guize) {
				chooseList.add(integer);
			}
		}
		// yu=1
		if (yu == 1) {
			chooseList.add(1);// 小
		} else if (yu == 2) {
			chooseList.add(1);// 小
			chooseList.add(1);// 小
		} else if (yu == 3) {
			chooseList.add(1);// 小
			chooseList.add(2);// 大
			chooseList.add(1);// 小
		}
		return chooseList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0, 1, 1, "切换用户");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			skipActivity(LoginActivity.class);
		}
		return super.onOptionsItemSelected(item);
	}

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	/**
	 * 重写回退键
	 */
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			FloatingFunc.close(getApplicationContext());
			finish();
		}
	}

	private Handler handlernet = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case FAIL:
				dialogShowMessage_P("获取数据失败", null);
				break;
			case 1:
				tv_marquee.setText(ts_msg);
				Config.getExecutorService().execute(new Runnable() {
					@Override
					public void run() {
						getWebService("getYHKxx");
					}
				});

				break;
			case 2:
				dialogShowMessage("银行卡信息未完善，建议立即完善（您也可以在人员基础信息界面中完善）。去完善？",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								Intent intent = new Intent(
										getApplicationContext(),
										YhkxxActivity.class);
								startActivityForResult(intent, 1);
							}
						}, null);
				break;
			case 5:

				break;
			}

		}

	};
}