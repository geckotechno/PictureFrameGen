package com.geckotechno.pictureframe;

import java.awt.Dimension;
import java.io.*;
import java.util.Properties;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Dimension imgSize = new Dimension(800,600);
		System.out.println("Picture Frame Manager");
		String srcDir = "C:\\Users\\hale\\PhotoFrame";
        String destDir = srcDir + "-gen";

		if (args.length == 1) { // set destination
			destDir = args[0];
		} else if (args.length == 2) {
			srcDir = args[0];
			destDir = args[1];
		} else {
			System.out.println("Arg count = " + args.length);
		}
        final Properties props;
        File fProps = new File(srcDir,"file.properties");
        ImagePropertyMap map = null;
        if (fProps.exists() && fProps.isFile()) {
            props = new Properties();
            props.load(new BufferedReader(new InputStreamReader(new FileInputStream(fProps), "UTF-16")));
            props.size();
            map = new ImagePropertyMap(props);
        }


		
		ImageManager iMgr = new ImageManager(srcDir ,destDir , imgSize, map);

		iMgr.registerCallBack(new ImageCallBack() {

			@Override
			public void imageCreationMessage(String message) {
				System.out.println("Writing File = " + message);
			}
		});


		iMgr.cleanDestDir();
		iMgr.generateImages();
		
	}

}
