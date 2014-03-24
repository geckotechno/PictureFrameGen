package com.geckotechno.pictureframe;

import java.io.File;
import java.util.Date;

public class ImageFileVO {
	
	private File file = null;
	private String title = null;
	private Date order = null;
	
	public ImageFileVO(File file) {
		super();
		this.file = file;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getOrder() {
		return order;
	}
	
	public void setOrder(Date order) {
		this.order = order;
	}

	public void setOrderNow() {
		this.order = new Date();
	}

	public File getFile() {
		return file;
	}
	
	

}
