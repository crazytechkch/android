package com.crazytech.android.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.crazytech.commons.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Image {
	

	private int calculateInSampleSize(
            BitmapFactory.Options options, int maxWidth) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    //Log.d("PubsImgAdapter:Height/maxWidth", width+"/"+maxWidth);
	    if (width > maxWidth) {
	    	inSampleSize = Math.round((float)height / (float)maxWidth);
	        
	    }
	    return inSampleSize;
	}
	
	public void decodeImageViewBitmap(ImageView imView, String file, int maxWidth,
			int maxHeight){
		ImageViewHolder imHolder = new ImageViewHolder(imView, file, null, maxWidth, 
				ImageViewHolder.FLAG_IMAGE_VIEW);
		decodeBitmap(imHolder);
	}
	
	public void saveImage(Context context, Uri imgUri,String imgDir, String filename, 
			int width, CompressFormat format, int quality)throws IOException{
		String[] projection = {MediaStore.Images.ImageColumns.DATA};
		Cursor cursor = context.getContentResolver().query(imgUri, projection, null, null, null);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		String imgPathStr = cursor.getString(index);
		cursor.close();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPathStr, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(imgPathStr, options);
		File dir = new File(imgDir);
		if (!dir.exists()) dir.mkdirs();
		File imgfile = new File(dir, filename);
		FileOutputStream out = new FileOutputStream(imgfile);
		bitmap.compress(format, quality, out);
		out.flush();
		out.close();
	}
	
	public void saveTempImage(String tempImgDir, String imgDir, String filename,
			int width, CompressFormat format, int quality)throws IOException{
		File tempImage = new File(tempImgDir);
		if(tempImage.exists()){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(tempImgDir, options);
			
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, width);
			
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(tempImgDir,options);
			File dir = new File(imgDir);
			if (!dir.exists()) dir.mkdirs();
			File imgfile = new File(dir, filename);
			FileOutputStream out = new FileOutputStream(imgfile);
			bitmap.compress(format, quality, out);
			out.flush();
			out.close();
			tempImage.delete();
		}
	}
	
	public void decodeImageButtonBitmap(ImageButton imButton, String file, int maxWidth,
			int maxHeight){
		ImageViewHolder imHolder = new ImageViewHolder(imButton, file, null, maxWidth, 
				ImageViewHolder.FLAG_IMAGE_BUTTON);
		decodeBitmap(imHolder);
	}
	
	private void decodeBitmap(ImageViewHolder imHolder) {
		new LoadImage().execute(imHolder);
			
	}
	private class LoadImage extends AsyncTask<ImageViewHolder, Integer, Bitmap> {
		ImageViewHolder imHolder;
		
		@Override
		protected Bitmap doInBackground(ImageViewHolder... params) {
			// TODO Auto-generated method stub
			imHolder = params[0];
			String fileDir = imHolder.getFileDir();
			Integer maxWidth = imHolder.getMaxWidth();
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileDir, options);
			
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, maxWidth);
			
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(fileDir, options);
			
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			switch (imHolder.getFlag()) {
			case ImageViewHolder.FLAG_IMAGE_VIEW:
				imHolder.getImageView().setImageBitmap(result);
				break;
			case ImageViewHolder.FLAG_IMAGE_BUTTON:
				imHolder.getImageButton().setImageBitmap(result);
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}
	}
}
