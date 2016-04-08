package com.goodweather.ViewFragment;



import com.goodweather.activity.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


/**
 * 欢迎页面
 */
public class RewardLauncherFragment extends LauncherBaseFragment{
	private ImageView ivReward;
	
	private static final float ZOOM_MAX = 1.0f;
	private static final  float ZOOM_MIN = 0.8f;
	private boolean started;//是否开启动画(ViewPage滑动时候给这个变量赋值)
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.fragment_reward_launcher, null);
	
		ivReward=(ImageView) rooView.findViewById(R.id.iv_reward);
	
		startAnimation();
		return rooView;
	}
	
	public void startAnimation(){
		started=true;
		if (started) {
			ivReward.setVisibility(View.VISIBLE);
			//开启缩放动画
	        Animation anim=AnimationUtils.loadAnimation(getActivity(),R.anim.reward_launcher);  
	        ivReward.startAnimation(anim);
	        anim.setAnimationListener(new AnimationListener(){
	            @Override  
	            public void onAnimationStart(Animation animation) {}  
	            @Override  
	            public void onAnimationRepeat(Animation animation) {}  
	            @Override  
	            public void onAnimationEnd(Animation animation) {
	            		/*//缩放动画结束 开启改变透明度动画
	            		AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
	            		alphaAnimation.setDuration(1000);
	            		ivReward.startAnimation(alphaAnimation);
	            		alphaAnimation.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {}
							@Override
							public void onAnimationRepeat(Animation animation) {}
							@Override
							public void onAnimationEnd(Animation animation) {
								//透明度动画结束隐藏图片
								//ivReward.setVisibility(View.GONE);
							}
					});*/
	            	/**
	        		 * 缩小动画
	        		 */
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(ZOOM_MAX, ZOOM_MIN, ZOOM_MAX,ZOOM_MIN, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(0.8f, 1.0f));
                animationSet.setDuration(1000);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                 // 实现心跳的View
                ivReward.startAnimation(animationSet);
	     
	            }
	        });
	        // 实现心跳的View
            ivReward.startAnimation(anim);
		}
		
		
	}
	
	@Override
	public void stopAnimation(){
		started=false;//结束动画时标示符设置为false
		ivReward.clearAnimation();//清空view上的动画
	}
}
