package com.bangware.shengyibao.config;

import android.os.Environment;

/**
 * 拍照常量设置
 * @author ccssll
 *
 */
public class Constants_Camera {
	public static final int MAX_PHOTO_SIZE = 3; //单次拍摄照片最大数
	public static final int MAX_NUM_COLUMNS = 3; // GridView展示照片列数
	public static final int MAX_PRACTICAL=9;    //案例最大拍摄的照片

	/***
	 * 视频拍摄常量设置
	 */
	public final static String METADATA_REQUEST_BUNDLE_TAG = "requestMetaData";
	public final static String FILE_START_NAME = "VMS_";
	public final static String VIDEO_EXTENSION = ".mp4";
	public final static String IMAGE_EXTENSION = ".jpg";
	public final static String DCIM_FOLDER = "/DCIM";
	public final static String CAMERA_FOLDER = "/video";
	public final static String TEMP_FOLDER = "/Temp";
	public final static String CAMERA_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + Constants_Camera.DCIM_FOLDER + Constants_Camera.CAMERA_FOLDER;
	public final static String TEMP_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + Constants_Camera.DCIM_FOLDER + Constants_Camera.CAMERA_FOLDER + Constants_Camera.TEMP_FOLDER;
	public final static String  VIDEO_CONTENT_URI = "content://media/external/video/media";

	public final static String KEY_DELETE_FOLDER_FROM_SDCARD = "deleteFolderFromSDCard";

	public final static String  RECEIVER_ACTION_SAVE_FRAME = "com.javacv.recorder.intent.action.SAVE_FRAME";
	public final static String  RECEIVER_CATEGORY_SAVE_FRAME = "com.javacv.recorder";
	public final static String  TAG_SAVE_FRAME = "saveFrame";

	public final static int RESOLUTION_HIGH = 1300;
	public final static int RESOLUTION_MEDIUM = 500;
	public final static int RESOLUTION_LOW = 180;

	public final static int RESOLUTION_HIGH_VALUE = 2;
	public final static int RESOLUTION_MEDIUM_VALUE = 1;
	public final static int RESOLUTION_LOW_VALUE = 0;
}
