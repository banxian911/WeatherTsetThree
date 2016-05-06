package com.goodweather.util;

import com.goodweather.activity.MainActivity;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class SpeechSynthesisUtils {
	private static String TAG = "speech";
	// 语音合成对象
	public static SpeechSynthesizer mTts;
	// 引擎类型
	private static String mEngineType = SpeechConstant.TYPE_CLOUD;

	// 默认发音人
	private static String voicer = "xiaoyan";

	private static String speed = "50";// 默认语速
	private static String pitch = "50";// 默认语调
	private static String volume = "50";// 默认音量
	private static String stream = "3";// 默认音频流类型

	// 缓冲进度
	private static int mPercentForBuffering = 0;
	// 播放进度
	private static int mPercentForPlaying = 0;

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public static void setParam(Context context) {
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(context, null);
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
	
		// 设置在线合成发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
		// 设置合成语速
		mTts.setParameter(SpeechConstant.SPEED, speed);
		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, pitch);
		// 设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME, volume);

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, stream);
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}

	/**
	 * 合成回调监听。
	 */
	public static SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
			// showTip("开始播放");
			Log.d(TAG, "yunlong--->开始播放");
		}

		@Override
		public void onSpeakPaused() {
			// showTip("暂停播放");
			Log.d(TAG, "yunlong--->暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			// showTip("继续播放");
			Log.d(TAG, "yunlong--->继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
			// 合成进度
			mPercentForBuffering = percent;
			// showTip(String.format(getString(R.string.tts_toast_format),
			// mPercentForBuffering, mPercentForPlaying));
			Log.d(TAG, "yunlong--->合成进度" + mPercentForBuffering +" ./mPercentForPlaying-->"+mPercentForPlaying);
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			mPercentForPlaying = percent;
			// showTip(String.format(getString(R.string.tts_toast_format),
			// mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				// showTip("播放完成");
			} else if (error != null) {
				// showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

}
