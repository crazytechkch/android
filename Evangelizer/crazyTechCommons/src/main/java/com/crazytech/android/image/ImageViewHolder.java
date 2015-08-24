package com.crazytech.android.image;

import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageViewHolder {
	private ImageView imageView;
	private ImageButton imageButton;
	private String fileDir;
	private Integer maxHeight, maxWidth, flag;
	public static final int FLAG_IMAGE_VIEW = 0;
	public static final int FLAG_IMAGE_BUTTON = 1;
	
	public ImageViewHolder(ImageView imageView, String fileDir,
			Integer maxHeight, Integer maxWidth, Integer flag) {
		super();
		this.imageView = imageView;
		this.fileDir = fileDir;
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		this.flag = flag;
	}

	public ImageViewHolder(ImageButton imageButton, String fileDir,
			Integer maxHeight, Integer maxWidth, Integer flag) {
		super();
		this.imageButton = imageButton;
		this.fileDir = fileDir;
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		this.flag = flag;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public ImageButton getImageButton() {
		return imageButton;
	}

	public String getFileDir() {
		return fileDir;
	}

	public Integer getMaxHeight() {
		return maxHeight;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public Integer getFlag() {
		return flag;
	}

}
