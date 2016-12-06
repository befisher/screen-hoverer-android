package com.fisher.hoverer.pop;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fisher.hoverer.R;
import com.fisher.hoverer.launcher.ServicePopWindow;
import com.fisher.utils.ConsoleUtil;
import com.fisher.utils.DensityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class FloatView {

	private final ServicePopWindow context;
	@BindView(R.id.rl_panel)
	RelativeLayout mRlPanel;
	@BindView(R.id.ib_drag_center)
	ImageButton mIbDragCenter;

	@BindView(R.id.ib_alpha_darker)
	ImageButton mIbAlphaDarker;
	@BindView(R.id.ib_alpha_lighter)
	ImageButton mIbAlphaLighter;
	@BindView(R.id.ib_alpha_fast_darker)
	ImageButton mIbAlphaFastDarker;
	@BindView(R.id.ib_alpha_fast_lighter)
	ImageButton mIbAlphaFastLighter;

	@BindView(R.id.ib_resize_left_top)
	ImageButton mIbDragLeftTop;
	@BindView(R.id.ib_resize_right_top)
	ImageButton mIbDragRightTop;
	@BindView(R.id.ib_resize_right_bottom)
	ImageButton mIbDragRightBottom;

	private long mLastTimeOptionsPanelClicked = 0;
	private long mLastTimeHovererClicked = 0;

	@BindView(R.id.et_alpha)
	EditText mEtAlpha;

	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWmParams;
	private LinearLayout mFloatLayout;
	private long touchTime;
	private Handler handler;
	private FloatViewImage floatViewImageSetting;
	private int mSystemStatusBar;
	private ViewPositionParam mViewPosition = new ViewPositionParam(100, 250, 250, 480);
	private float mDragSizeCenter;

	public FloatView(ServicePopWindow servicePopWindow) {
		this.context = servicePopWindow;
	}

	public FloatView init() {
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		floatViewImageSetting = FloatViewImage.getInstance();
		mSystemStatusBar = DensityUtils.getSystemStatusBarHeight(context);
		mDragSizeCenter = context.getResources().getDimensionPixelSize(R.dimen.pop_icon_resize_size) / 2;
		mViewPosition.setMin(70, 20);

		mWmParams = new WindowManager.LayoutParams();
		mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mWmParams.format = PixelFormat.RGBA_8888;
		mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

		mFloatLayout = (LinearLayout) View.inflate(context, R.layout.module_pop_window, null);
		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		ButterKnife.bind(this, mFloatLayout);
		// FIXME By Fisher; Here MAY(some condition) crash if the user didn't grant permission to show floating window.
		mWindowManager.addView(mFloatLayout, mWmParams);
		mIbDragCenter.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						return false;
					case MotionEvent.ACTION_MOVE:
						float x = event.getRawX();
						float y = event.getRawY() - mSystemStatusBar;
						mViewPosition.setCenter(x, y);
						updateLayout();
						return false;
					case MotionEvent.ACTION_UP:
						return false;
				}
				return false;
			}
		});
		mIbDragLeftTop.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						return false;
					case MotionEvent.ACTION_MOVE:
						mViewPosition.setX1(event.getRawX() - mDragSizeCenter);
						mViewPosition.setY1(event.getRawY() - mDragSizeCenter - mSystemStatusBar);
						updateLayout();
						return false;
					case MotionEvent.ACTION_UP:
						return false;
				}
				return false;
			}
		});
		mIbDragRightBottom.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						return false;
					case MotionEvent.ACTION_MOVE:
						mViewPosition.setX2(event.getRawX() + mDragSizeCenter);
						mViewPosition.setY2(event.getRawY() + mDragSizeCenter - mSystemStatusBar);
						updateLayout();
						return false;
					case MotionEvent.ACTION_UP:
						return false;
				}
				return false;
			}
		});
		handler = new Handler(context.getMainLooper()) {
			@Override
			public void handleMessage(Message inputMessage) {
				updateLayout();
			}
		};
		updateLayout();
		log("The pop window is initialized!");
		return this;
	}

	public void fnDestroyFloatView() {
		if (mWindowManager != null && mFloatLayout != null) {
			mWindowManager.removeView(mFloatLayout);
			handler = null;
		}
		log("The pop window is destroyed!");
	}


	@OnLongClick({R.id.rl_panel, R.id.ll_panel})
	public boolean onBtnLongClick(View view) {
		switch (view.getId()) {
			case R.id.rl_panel:
				Toast.makeText(context, R.string.pop_toast_long_click_to_show_options_panel, Toast.LENGTH_SHORT).show();
				mRlPanel.setVisibility(View.GONE);
				break;
			case R.id.ll_panel:
				mRlPanel.setVisibility(View.VISIBLE);
				break;
		}
		return true;
	}

	@OnClick({R.id.ib_resize_right_top, R.id.rl_panel, R.id.ll_panel})
	public void onBtnClick(View view) {
		switch (view.getId()) {
			case R.id.rl_panel:
				long time = System.currentTimeMillis();
				if (Constants.INTERVAL_DOUBLE_CLICK < System.currentTimeMillis() - mLastTimeOptionsPanelClicked) {
					mLastTimeOptionsPanelClicked = time;
					break;
				}
			case R.id.ib_resize_right_top:
				Toast.makeText(context, R.string.pop_toast_long_click_to_show_options_panel, Toast.LENGTH_SHORT).show();
				mRlPanel.setVisibility(View.GONE);
				break;
			case R.id.ll_panel:
				time = System.currentTimeMillis();
				if (Constants.INTERVAL_DOUBLE_CLICK < System.currentTimeMillis() - mLastTimeHovererClicked) {
					mLastTimeHovererClicked = time;
				} else {
//					mRlPanel.setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	@OnClick({R.id.ib_alpha_darker, R.id.ib_alpha_lighter, R.id.ib_alpha_fast_darker, R.id.ib_alpha_fast_lighter})
	public void onAlphaClick(View view) {
		float alpha = Float.parseFloat(mEtAlpha.getText().toString());
		switch (view.getId()) {
			case R.id.ib_alpha_darker:
				alpha += 0.01;
				break;
			case R.id.ib_alpha_fast_darker:
				alpha += 0.1;
				break;
			case R.id.ib_alpha_lighter:
				alpha -= 0.01;
				break;
			case R.id.ib_alpha_fast_lighter:
				alpha -= 0.1;
				break;
		}
		if (1 <= alpha) {
			alpha = 1;
			mIbAlphaDarker.setEnabled(false);
			mIbAlphaFastDarker.setEnabled(false);
		} else {
			mIbAlphaDarker.setEnabled(true);
			mIbAlphaFastDarker.setEnabled(true);
		}
		if (0 >= alpha) {
			alpha = 0;
			mIbAlphaLighter.setEnabled(false);
			mIbAlphaFastLighter.setEnabled(false);
		} else {
			mIbAlphaLighter.setEnabled(true);
			mIbAlphaFastLighter.setEnabled(true);
		}
		mEtAlpha.setText(String.format("%.2f", alpha));
		mFloatLayout.setAlpha(alpha);
	}

	private int whatever = 0;

	private void updateLayout() {
		if (whatever++ == 4) {
			whatever = 0;
		} else {
			return;
		}
		mWmParams.width = (int) mViewPosition.getWidth();
		mWmParams.height = (int) mViewPosition.getHeight();
		mWmParams.x = (int) mViewPosition.getX1();
		mWmParams.y = (int) mViewPosition.getY1();
		mWindowManager.updateViewLayout(mFloatLayout, mWmParams);
	}

	public String log(String msg) {
		return ConsoleUtil.console(msg);
	}
}
