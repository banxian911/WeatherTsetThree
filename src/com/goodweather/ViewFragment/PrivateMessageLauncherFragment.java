package com.goodweather.ViewFragment;



import com.goodweather.activity.R;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


/**
 * 天气预报
 */
public class PrivateMessageLauncherFragment extends LauncherBaseFragment{
	private ImageView ivWeatherGood,ivTomorrow,ivWeatherLook,ivHowKnow;
	
	private Animation likeAnimation,thinkAnimation,watchAnimation,thisWeekAnimation;
	
	private boolean started;//是否开启动画
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.fragment_private_message_launcher, null);
		
		ivWeatherGood=(ImageView) rooView.findViewById(R.id.iv_private_message_weather_good);
		ivTomorrow=(ImageView) rooView.findViewById(R.id.iv_private_message_weather_tomorrow);
		ivHowKnow=(ImageView) rooView.findViewById(R.id.iv_private_message_weather_howknow);
		ivWeatherLook=(ImageView) rooView.findViewById(R.id.private_message_weather_look);
		return rooView;
	}
	
	public void stopAnimation(){
		//动画开启标示符设置成false   
		started=false;
		/**
		 * 清空所有控件上的动画
		 */
		ivWeatherGood.clearAnimation();
		ivTomorrow.clearAnimation();
		ivHowKnow.clearAnimation();
		ivWeatherLook.clearAnimation();
	}
	
	
	public void startAnimation(){
		started=true;
		
		/**
		 * 每次开启动画前先隐藏控件
		 */
		ivWeatherGood.setVisibility(View.GONE);
		ivTomorrow.setVisibility(View.GONE);
		ivHowKnow.setVisibility(View.GONE);
		ivWeatherLook.setVisibility(View.GONE);
		
		new Handler().postDelayed(new Runnable() {//延时0.5秒之后开启喜欢视频动画
			@Override
			public void run(){
				if(started)
					likeVideoAnimation();
			}
		},500);
	}
	
	/**
	 * 今天天气不错
	 */
	private void likeVideoAnimation(){
		ivWeatherGood.setVisibility(View.VISIBLE);
		
		likeAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.private_message_launcher);
		ivWeatherGood.startAnimation(likeAnimation);//开启动画
		likeAnimation.setAnimationListener(new AnimationListener(){  
            @Override  
            public void onAnimationStart(Animation animation) {}  
            @Override  
            public void onAnimationRepeat(Animation animation) {}  
            @Override  
            public void onAnimationEnd(Animation animation) {//监听动画结束
	            	if(started)
	            		thinkReward();
            }  
        }); 
	}
	
	/**
	 *明天天气更好
	 */
	private void thinkReward(){
		ivTomorrow.setVisibility(View.VISIBLE);
		thinkAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.private_message_launcher);
		ivTomorrow.startAnimation(thinkAnimation);
		thinkAnimation.setAnimationListener(new AnimationListener(){  
            @Override  
            public void onAnimationStart(Animation animation) {}  
            @Override  
            public void onAnimationRepeat(Animation animation) {}  
            @Override  
            public void onAnimationEnd(Animation animation) {
            	if(started)
            		watchMovie();
            }  
        }); 
	}
	
	/**
	 * 你是怎么知道的
	 */
	private void watchMovie(){
		ivHowKnow.setVisibility(View.VISIBLE);
		watchAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.private_message_launcher);
		ivHowKnow.startAnimation(watchAnimation);
		watchAnimation.setAnimationListener(new AnimationListener(){  
            @Override  
            public void onAnimationStart(Animation animation) {}  
            @Override  
            public void onAnimationRepeat(Animation animation) {}  
            @Override  
            public void onAnimationEnd(Animation animation) {
            	if(started)
            		thisWeek();
            }  
        }); 
	}
	
	/**
	 * 看goodweather
	 */
	private void thisWeek(){
		ivWeatherLook.setVisibility(View.VISIBLE);
		thisWeekAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.private_message_launcher);  
		ivWeatherLook.startAnimation(thisWeekAnimation);
	}
}
