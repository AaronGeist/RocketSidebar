package com.panda.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class InvisibleView extends ViewGroup {

	private ViewGroup vg = this;
	private GestureDetector mGestureDetector = null;
	private Context mContext = null;
	private Sidebar mSidebar = null;

	private final int invisibleTriggerWidth = 5;
	private final int invisibleTriggerHeight = 100;

	public InvisibleView(Context context) {
		super(context);
		mContext = context;

		// this.setBackgroundColor(Color.YELLOW);

		mGestureDetector = new GestureDetector(context,
				new GestureListner(this));

		MyBroadcastReceiver br = new MyBroadcastReceiver(mSidebar);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("dismiss");
		context.registerReceiver(br, intentFilter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// // there's no onUp method in SimpleOnGestureListener, so we have
			// to handle it here
			// if(mSidebar != null) {
			// //((WindowManager)
			// mContext.getSystemService(Context.WINDOW_SERVICE)).removeView(mSidebar);
			// this.removeView(mSidebar);
			// //Toast.makeText(getContext(), "remove Sidebar",
			// Toast.LENGTH_SHORT).show();
			// mSidebar = null;
			// } else {
			// Toast.makeText(getContext(), "Sidebar is null",
			// Toast.LENGTH_SHORT).show();
			// }
			mGestureDetector.onTouchEvent(event);
			break;
		default:
			mGestureDetector.onTouchEvent(event);
			break;
		}
		return true;
	}

	class GestureListner extends SimpleOnGestureListener {

		ViewGroup mViewGroup = null;

		public GestureListner(ViewGroup viewGroup) {
			mViewGroup = viewGroup;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			if (e.getX() == 0 & e.getY() > mViewGroup.getY()
					& e.getY() < (mViewGroup.getY() + mViewGroup.getHeight())) {
				// Toast.makeText(getContext(), "onDown",
				// Toast.LENGTH_SHORT).show();

				WindowManager.LayoutParams params = new WindowManager.LayoutParams(
						WindowManager.LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
								| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
								| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
						PixelFormat.TRANSLUCENT);
				params.gravity = Gravity.LEFT | Gravity.TOP;

				if (mSidebar == null) {
					mSidebar = new Sidebar(mContext);
					mViewGroup.addView(mSidebar, params);
				}
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			if (e.getX() == 0 & e.getY() > mViewGroup.getY()
					& e.getY() < (mViewGroup.getY() + mViewGroup.getHeight())) {
				// Toast.makeText(getContext(), "onShowPress",
				// Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() == 0 & e1.getY() > mViewGroup.getY()
					& e1.getY() < (mViewGroup.getY() + mViewGroup.getHeight())) {
				// Toast.makeText(getContext(), "onFling",
				// Toast.LENGTH_SHORT).show();
			}
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			// Toast.makeText(getContext(), "onSingleTapUp",
			// Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int cCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;

		int height = 0;

		for (int i = 0; i < cCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();

			System.out.println("child=" + i + ", cWidth=" + cWidth);

			int cl = 0, ct = 0, cr = 0, cb = 0;
			cl = (int) this.getX();
			cr = cl + cWidth;
			ct = height;
			cb = height + cHeight;
			height = cb;
			childView.layout(cl, ct, cr, cb);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int width = 0;
		int height = 0;

		int cCount = getChildCount();

		int cWidth = 0;
		int cHeight = 0;

		// here we set the initial size for the hook
		if (cCount == 0) {
			width = invisibleTriggerWidth;
			height = invisibleTriggerHeight;
		} else {
			for (int i = 0; i < cCount; i++) {
				View childView = getChildAt(i);
				cWidth = childView.getMeasuredWidth();
				cHeight = childView.getMeasuredHeight();

				width = width > cWidth ? width : cWidth;
				height += cHeight;
			}
		}

		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
				: width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
				: height);
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		View mView = null;

		public MyBroadcastReceiver(View view) {
			mView = view;
		}

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			// Toast.makeText(getContext(), "Destory View",
			// Toast.LENGTH_SHORT).show();
			vg.removeView(mSidebar);
			mSidebar = null;
		}

	}
}
