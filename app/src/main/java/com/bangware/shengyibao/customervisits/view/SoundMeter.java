package com.bangware.shengyibao.customervisits.view;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 语音播放文件
 */
public  class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/shengyibao_voice");
			if(!file.exists()){
				file.mkdir();
			}
			mRecorder.setOutputFile(Environment.getExternalStorageDirectory()+"/shengyibao_voice/"+name);
			try {
				mRecorder.prepare();
				mRecorder.start();
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
