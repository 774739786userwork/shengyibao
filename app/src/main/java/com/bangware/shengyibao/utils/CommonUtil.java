package com.bangware.shengyibao.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 拍照保存、压缩的处理公共类
 * @author ccssll
 *
 */
public class CommonUtil {
	/**
	 * 获取SD卡路径
	 * @return
	 */
	public static String getSDPath() {
		String sdPath = null;
		// 判断sd卡是否存在
		boolean sdCardExit = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCardExit) {
			// 获取根目录
			sdPath = Environment.getExternalStorageDirectory().toString();
		}
		return sdPath;
	}
	
	/**
	 * 返回32位UUID字符串
	 * @return
	 */
	public static String getUUID32(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
   /**
    * 加载本地图片
    * @param
    * @return
    */
    public static Bitmap getBitmapInLocal(String path) {
         try {
              FileInputStream fis = new FileInputStream(path);
              return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
           } catch (FileNotFoundException e) {
              e.printStackTrace();
              return null;
         }
    }
    
	/**
	 * 删除文件
	 * @param filePath
	 */
	public static void deleteFile(String filePath){
		if(filePath == null || "".equals(filePath)){
			return;
		}
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 图片文件进行压缩处理
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */
	public static boolean dealImage(String sourceFilePath, String targetFilePath){
		try{
			int dh = 1024;
        	int dw = 768;
        	BitmapFactory.Options factory=new BitmapFactory.Options();
            factory.inJustDecodeBounds=true; //当为true时  允许查询图片不为 图片像素分配内存  
            InputStream is = new FileInputStream(sourceFilePath);
			Bitmap bmp = BitmapFactory.decodeStream(is,null,factory);
            int hRatio=(int) Math.ceil(factory.outHeight/(float)dh); //图片是高度的几倍
            int wRatio=(int) Math.ceil(factory.outWidth/(float)dw); //图片是宽度的几倍
            System.out.println("hRatio:"+hRatio+"  wRatio:"+wRatio);
            //缩小到  1/ratio的尺寸和 1/ratio^2的像素  
            if(hRatio>1||wRatio>1){
                if(hRatio>wRatio){
                    factory.inSampleSize=hRatio;   
                }
                else 
                    factory.inSampleSize=wRatio;  
            }
            factory.inJustDecodeBounds=false;
            is.close();
			is = new FileInputStream(sourceFilePath);
            bmp= BitmapFactory.decodeStream(is, null, factory);
            OutputStream outFile = new FileOutputStream(targetFilePath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
			outFile.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**对图片质量进行压缩处理**/
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos =new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG,100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options =100;
		while( baos.toByteArray().length /1024>100) {//循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -=10;//每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm =new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm,null,null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts =new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds =true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
		newOpts.inJustDecodeBounds =false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be =1;//be=1表示不缩放
		if(w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		}else if(w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if(be <=0)
			be =1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		try {
			OutputStream outFile = new FileOutputStream(srcPath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outFile);
			outFile.close();
		}catch (Exception e){

		}
		return bitmap;
	}
}
