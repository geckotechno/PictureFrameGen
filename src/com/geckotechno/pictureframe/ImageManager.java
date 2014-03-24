package com.geckotechno.pictureframe;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageManager {
	private List<ImageCallBack> callbacks = new ArrayList<ImageCallBack>(); 
	
	private File sourceDir = null;
	private File destinationDir = null;
	private Dimension imageSize = null;
	private ImagePropertyMap propMap = null;


	public ImageManager(String srcDir, String destDir, Dimension imgSize, ImagePropertyMap props) {
		init(new File(srcDir), new File(destDir));
		this.imageSize  = imgSize;
        this.propMap = props;
	}

	public ImageManager(String srcDir, String destDir, Dimension imgSize) {
		init(new File(srcDir), new File(destDir));
		this.imageSize  = imgSize;
	}


	public void registerCallBack(ImageCallBack imgCallBack) {
		callbacks.add(imgCallBack);
	}
	
	
	private void sendCallBackMessage(String message) {
		for (ImageCallBack cb: callbacks) {
			cb.imageCreationMessage(message);
		}
	}
	
	
	private void init(File srcDir, File destDir) {
		sourceDir = srcDir;
		destinationDir = destDir;
		
		validate("Source", sourceDir);
		validate("Destination", destinationDir, true);
	}


	private void validate(String location, File dir, boolean createDir) {
		if (dir == null) {
			throw new RuntimeException("Error: The " + location + " is not set");
		} else if (dir.exists()) {
			if (! dir.isDirectory()) {
				throw new RuntimeException("Error: The " + location + " is not a directory.  See: " + dir.getAbsolutePath());
			}
		} else {
			// try to create dir
			if (! dir.mkdirs() ) {
				throw new RuntimeException("Error: Was not able to create the " + location + " in the following location: " + dir.getAbsolutePath());
			}
		}
	}


	private void validate(String location, File dir) {
		validate(location, dir,false);
	}

	public File getSrcDir() {
		return sourceDir;
	}

	public File getDestDir() {
		return destinationDir;
	}

	public Dimension getImageSize() {
		return imageSize;
	}


	public void generateImages() {
		try {
			int i = 0;
			for (ImageFileVO img : ImageFileDAO.getImageFiles(getSrcDir())) {
				BufferedImage image = ImageIO.read(img.getFile());
				image = ImageUtils.borderImage(getImageSize(), image);
                String titleImageName = null;
                String titleName = this.propMap.getTitle(img.getFile().getName());
                if (titleName != null) {
                    titleImageName = genImageName(i++);
                }
				File fName = new File(getDestDir(), genImageName(i++));
				ImageIO.write(image, "jpg", fName);
				sendCallBackMessage(fName.getName());
                if (titleImageName != null) {
                    fName = new File(getDestDir(),titleImageName);
                    image = ImageUtils.addTextToImage(image,titleName);
                    ImageIO.write(image, "jpg", fName);
                    sendCallBackMessage(fName.getName());
                }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}



	private String genImageName(int i) {
		return "image-" + i + ".jpg";
	}


	public void cleanDestDir() {
		for (ImageFileVO img : ImageFileDAO.getImageFiles(getDestDir())) {
			img.getFile().delete();
		}
	}
}
