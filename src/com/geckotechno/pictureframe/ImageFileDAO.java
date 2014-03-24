package com.geckotechno.pictureframe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFileDAO {

	// static class
	private ImageFileDAO() {
	}
	
	
	public static List<ImageFileVO> getImageFiles(File sourceDirectory) {
		List<ImageFileVO> lFiles = new ArrayList<>();
		File[] files = sourceDirectory.listFiles();
		for (int i = 0; i < files.length; i++) {
			String fName = files[i].getName().toLowerCase();
			if (fName.endsWith("jpeg") || fName.endsWith("jpg")) {
				lFiles.add(new ImageFileVO(files[i]));
			}
		}
		return lFiles ;
	}

}
