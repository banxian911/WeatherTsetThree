package com.goodweather.utils.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class DialogUtil {

	/**
	 * 获取一个自定义风格的Dialog
	 * 
	 * @param activity
	 *            上下文对象
	 * @param style
	 *            风格
	 * @param customView
	 *            自定义view
	 * @return dialog
	 */
	public static Dialog getCustomeDialog(Activity activity, int style,
			View customView) {
		Dialog dialog = new Dialog(activity, style);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(customView);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		lp.x = 0;
		lp.y = 0;
		window.setAttributes(lp);
		return dialog;
	}

	public static Dialog getCustomeDialog(Activity activity, int style,
			int customView) {
		Dialog dialog = new Dialog(activity, style);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(customView);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.MATCH_PARENT;
		lp.x = 0;
		lp.y = 0;
		window.setAttributes(lp);
		return dialog;
	}
}
