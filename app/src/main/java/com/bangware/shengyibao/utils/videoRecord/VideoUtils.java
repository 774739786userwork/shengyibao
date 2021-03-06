package com.bangware.shengyibao.utils.videoRecord;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Surface;
import android.widget.Toast;

import com.bangware.shengyibao.config.Constants_Camera;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_highgui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateFileCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;

/**
 * 视频录制公共方法调用类
 * Created by bangware on 2017/2/9.
 */

public class VideoUtils {
    public static ContentValues videoContentValues = null;
    public static String getRecordingTimeFromMillis(long millis)
    {
        String strRecordingTime = null;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;

        if(hours >= 0 && hours < 10)
            strRecordingTime = "0" + hours + ":";
        else
            strRecordingTime = hours + ":";

        if(hours > 0)
            minutes = minutes % 60;

        if(minutes >= 0 && minutes < 10)
            strRecordingTime += "0" + minutes + ":";
        else
            strRecordingTime += minutes + ":";

        seconds = seconds % 60;

        if(seconds >= 0 && seconds < 10)
            strRecordingTime += "0" + seconds ;
        else
            strRecordingTime += seconds ;

        return strRecordingTime;

    }


    public static int determineDisplayOrientation(Activity activity, int defaultCameraId) {
        int displayOrientation = 0;
        if(Build.VERSION.SDK_INT >  Build.VERSION_CODES.FROYO)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(defaultCameraId, cameraInfo);

            int degrees  = getRotationAngle(activity);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                displayOrientation = (cameraInfo.orientation + degrees) % 360;
                displayOrientation = (360 - displayOrientation) % 360;
            } else {
                displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
            }
        }
        return displayOrientation;
    }

    public static int getRotationAngle(Activity activity)
    {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees  = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    public static int getRotationAngle(int rotation)
    {
        int degrees  = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    public static String createImagePath(Context context){
        long dateTaken = System.currentTimeMillis();
        String title = Constants_Camera.FILE_START_NAME + dateTaken;
        String filename = title + Constants_Camera.IMAGE_EXTENSION;

        String dirPath = Environment.getExternalStorageDirectory()+"/Android/data/" + context.getPackageName()+"/video";
        File file = new File(dirPath);
        if(!file.exists() || !file.isDirectory())
            file.mkdirs();
        String filePath = dirPath + "/" + filename;
        return filePath;
    }

    public static String createFinalPath(Context context)
    {
        long dateTaken = System.currentTimeMillis();
        String title = Constants_Camera.FILE_START_NAME + dateTaken;
        String filename = title + Constants_Camera.VIDEO_EXTENSION;
        String filePath = genrateFilePath(context,String.valueOf(dateTaken), true, null);

        ContentValues values = new ContentValues(7);
        values.put(MediaStore.Video.Media.TITLE, title);
        values.put(MediaStore.Video.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Video.Media.DATE_TAKEN, dateTaken);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/3gpp");
        values.put(MediaStore.Video.Media.DATA, filePath);
        videoContentValues = values;

        return filePath;
    }

    public static void deleteTempVideo(Context context){
        final String dirPath = Environment.getExternalStorageDirectory()+"/Android/data/" + context.getPackageName()+"/video";
        new Thread(new Runnable() {

            @Override
            public void run() {
                File file = new File(dirPath);
                if(file != null && file.isDirectory()){
                    for(File file2 :file.listFiles()){
                        file2.delete();
                    }
                }
            }
        }).start();
    }

    private static String genrateFilePath(Context context,String uniqueId, boolean isFinalPath, File tempFolderPath)
    {
        String fileName = Constants_Camera.FILE_START_NAME + uniqueId + Constants_Camera.VIDEO_EXTENSION;
        String dirPath = Environment.getExternalStorageDirectory()+"/Android/data/" + context.getPackageName()+"/video";
        if(isFinalPath)
        {
            File file = new File(dirPath);
            if(!file.exists() || !file.isDirectory())
                file.mkdirs();
        }
        else
            dirPath = tempFolderPath.getAbsolutePath();
        String filePath = dirPath + "/" + fileName;
        return filePath;
    }
    public static String createTempPath(Context context,File tempFolderPath )
    {
        long dateTaken = System.currentTimeMillis();
        String filePath = genrateFilePath(context,String.valueOf(dateTaken), false, tempFolderPath);
        return filePath;
    }



    public static File getTempFolderPath()
    {
        File tempFolder = new File(Constants_Camera.TEMP_FOLDER_PATH +"_" +System.currentTimeMillis());
        return tempFolder;
    }


    public static List<Camera.Size> getResolutionList(Camera camera)
    {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();


        return previewSizes;
    }

    public static RecorderParameters getRecorderParameter(int currentResolution)
    {
        RecorderParameters parameters = new RecorderParameters();
        if(currentResolution ==  Constants_Camera.RESOLUTION_HIGH_VALUE)
        {
            parameters.setAudioBitrate(128000);
            parameters.setVideoQuality(0);
        }
        else if(currentResolution ==  Constants_Camera.RESOLUTION_MEDIUM_VALUE)
        {
            parameters.setAudioBitrate(128000);
            parameters.setVideoQuality(5);
        }
        else if(currentResolution == Constants_Camera.RESOLUTION_LOW_VALUE)
        {
            parameters.setAudioBitrate(96000);
            parameters.setVideoQuality(20);
        }
        return parameters;
    }

    public static int calculateMargin(int previewWidth, int screenWidth)
    {
        int margin = 0;
        if(previewWidth <= Constants_Camera.RESOLUTION_LOW)
        {
            margin = (int) (screenWidth*0.12);
        }
        else if(previewWidth > Constants_Camera.RESOLUTION_LOW && previewWidth <= Constants_Camera.RESOLUTION_MEDIUM)
        {
            margin = (int) (screenWidth*0.08);
        }
        else if(previewWidth > Constants_Camera.RESOLUTION_MEDIUM && previewWidth <= Constants_Camera.RESOLUTION_HIGH)
        {
            margin = (int) (screenWidth*0.08);
        }
        return margin;


    }

    public static int setSelectedResolution(int previewHeight)
    {
        int selectedResolution = 0;
        if(previewHeight <= Constants_Camera.RESOLUTION_LOW)
        {
            selectedResolution = 0;
        }
        else if(previewHeight > Constants_Camera.RESOLUTION_LOW && previewHeight <= Constants_Camera.RESOLUTION_MEDIUM)
        {
            selectedResolution = 1;
        }
        else if(previewHeight > Constants_Camera.RESOLUTION_MEDIUM && previewHeight <= Constants_Camera.RESOLUTION_HIGH)
        {
            selectedResolution = 2;
        }
        return selectedResolution;


    }

    public static class ResolutionComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size size1, Camera.Size size2) {
            if(size1.height != size2.height)
                return size1.height -size2.height;
            else
                return size1.width - size2.width;
        }
    }


    public static void concatenateMultipleFiles(String inpath, String outpath)
    {
        File Folder = new File(inpath);
        File files[];
        files = Folder.listFiles();

        if(files.length>0)
        {
            for(int i = 0;i<files.length; i++){
                Reader in = null;
                Writer out = null;
                try {
                    in =   new FileReader(files[i]);
                    out = new FileWriter(outpath , true);
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String getEncodingLibraryPath(Context paramContext)
    {
        return paramContext.getApplicationInfo().nativeLibraryDir + "/libencoding.so";
    }

    private static HashMap<String, String> getMetaData()
    {
        HashMap<String, String> localHashMap = new HashMap<String, String>();
        localHashMap.put("creation_time", new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSZ").format(new Date()));
        return localHashMap;
    }

    public static int getTimeStampInNsFromSampleCounted(int paramInt)
    {
        return (int)(paramInt / 0.0441D);
    }



    public static void saveReceivedFrame(SavedFrames frame)
    {
        File cachePath = new File(frame.getCachePath());
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(cachePath));
            if(bos != null)
            {
                bos.write(frame.getFrameBytesData());
                bos.flush();
                bos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            cachePath = null;
        } catch (IOException e) {
            e.printStackTrace();
            cachePath = null;
        }
    }

    public static Toast showToast(Context context, String textMessage, int timeDuration)
    {
        if (null == context)
        {
            return null;
        }
        textMessage = (null == textMessage ? "Oops! " : textMessage.trim());
        Toast t = Toast.makeText(context, textMessage, timeDuration);
        t.show();
        return t;
    }

    public opencv_core.IplImage getFrame(String filePath){
        opencv_highgui.CvCapture capture = cvCreateFileCapture(filePath);
        opencv_core.IplImage image = cvQueryFrame(capture);
        return image;
    }
}
