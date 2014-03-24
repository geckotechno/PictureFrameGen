package com.geckotechno.pictureframe;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageUtils {
	private static final String NO_IMAGE = "No Image";

	private ImageUtils() {
	}

	public static BufferedImage genNoImage(Dimension image) {
		GraphicsConfiguration gc = getDefaultConfiguration();
		BufferedImage result = gc.createCompatibleImage((int) image.getWidth(),
				(int) image.getHeight(), Transparency.OPAQUE);
		Graphics2D g = result.createGraphics();
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		resizeFontToWidth(g, (int) (image.getWidth() * 0.9), NO_IMAGE);

		// g.setFont(getMaxFont(g.getFont().deriveFont(Font.BOLD),
		// (int)(image.getWidth() * 0.9), NO_IMAGE));

		int sW = g.getFontMetrics().stringWidth(NO_IMAGE);
		int sH = g.getFontMetrics().getHeight();
		// System.out.println(" sw = " + sW + "  sh = " + sH);
		// g.scale(image.getWidth() / (sW * 0.1), image.getHeight() / (sH *
		// 0.1));
		g.drawString(NO_IMAGE, (float) (image.getWidth() / 2.0f) - (sW / 2.0f),
				(float) ((image.getHeight() / 2.0f)) + (sH / 4.0f));
		g.dispose();

		return result;
	}

	public static BufferedImage genTextImage(Dimension image, String message) {
		GraphicsConfiguration gc = getDefaultConfiguration();
		BufferedImage result = gc.createCompatibleImage((int) image.getWidth(),
                (int) image.getHeight(), Transparency.OPAQUE);
		Graphics2D g = result.createGraphics();
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		resizeFontToFit(g, image, 10, message);

		// g.setFont(getMaxFont(g.getFont().deriveFont(Font.BOLD),
		// (int)(image.getWidth() * 0.9), NO_IMAGE));

		int sW = g.getFontMetrics().stringWidth(message);
		int sH = g.getFontMetrics().getHeight();
		// System.out.println(" sw = " + sW + "  sh = " + sH);
		// g.scale(image.getWidth() / (sW * 0.1), image.getHeight() / (sH *
		// 0.1));
		g.drawString(message, (float) (image.getWidth() / 2.0f) - (sW / 2.0f),
				(float) ((image.getHeight() / 2.0f)) + (sH / 4.0f));
		g.dispose();

		return result;
	}

	public static BufferedImage borderImage(Dimension outImageSize,
			BufferedImage inImage) {
		GraphicsConfiguration gc = getDefaultConfiguration();
		BufferedImage result = gc.createCompatibleImage(
				(int) outImageSize.getWidth(), (int) outImageSize.getHeight(),
				Transparency.OPAQUE);
		Graphics2D g = result.createGraphics();
		BufferedImage temp = resizeToFit(inImage, outImageSize);
		AffineTransform atLarge = new AffineTransform();
		atLarge.setToTranslation(
				(outImageSize.getWidth() - temp.getWidth()) / 2,
				(outImageSize.getHeight() - temp.getHeight()) / 2);
		g.drawRenderedImage(temp, atLarge);
		g.dispose();

		return result;
	}


    public static BufferedImage addTextToImage(BufferedImage inImage, String text) {
        GraphicsConfiguration gc = getDefaultConfiguration();
      		Graphics2D g = inImage.createGraphics();
      		g.setFont(g.getFont().deriveFont(Font.BOLD));

      		resizeFontToFit(g, new Dimension(inImage.getWidth(), inImage.getHeight()), 10, text);

      		// g.setFont(getMaxFont(g.getFont().deriveFont(Font.BOLD),
      		// (int)(image.getWidth() * 0.9), NO_IMAGE));

      		int sW = g.getFontMetrics().stringWidth(text);

            FontRenderContext frc = g.getFontRenderContext();
            TextLayout layout = new TextLayout(text, g.getFont(), frc);
//      		int sH = (int) (g.getFontMetrics().getHeight() -  layout.getBounds().getHeight());
      		int sH =  offsetDecent(text, g.getFontMetrics().getDescent()) + 5;
      		// System.out.println(" sw = " + sW + "  sh = " + sH);
      		// g.scale(image.getWidth() / (sW * 0.1), image.getHeight() / (sH *
      		// 0.1));
//      		g.drawString(text, (float) (inImage.getWidth() / 2.0f) - (sW / 2.0f),
//      				(float) (inImage.getHeight() - sH));

            AffineTransform transform = new AffineTransform();
            Shape outline = layout.getOutline(null);
            Rectangle outlineBounds = outline.getBounds();
            transform = g.getTransform();
//            transform.translate(inImage.getWidth() / 2 - (outlineBounds.width / 2), inImage.getHeight() / 2 + (outlineBounds.height / 2));
            transform.translate(inImage.getWidth() / 2 - (outlineBounds.width / 2), (inImage.getHeight() - sH));
            g.transform(transform);
            g.setColor(Color.white);
            g.fill(outline);
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.black);
            g.draw(outline);
            g.setClip(outline);

      		g.dispose();

      		return inImage;
    }

    private static int offsetDecent(String text, int descent) {
        return (text.matches(".*[gjpqy]+.*")) ? descent: 0 ;
    }

    private static void resizeFontToWidth(Graphics2D g, int maxW, String noImage) {
		int fontSize = g.getFont().getSize();
		while (maxW > g.getFontMetrics().stringWidth(noImage)) {
			g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize + 1));
			fontSize = g.getFont().getSize();
			// System.out.println("font size = " + g.getFont().getSize());
		}
		g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize - 1));
	}

	private static void resizeFontToFit(Graphics2D g, Dimension imgDim,
			int border, String message) {
		int fontSize = g.getFont().getSize();
		while (((imgDim.getWidth() - border) > g.getFontMetrics().stringWidth(
				message))
				&& ((imgDim.getHeight() - border) > g.getFontMetrics()
						.getHeight())) {
			g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize + 1));
			fontSize = g.getFont().getSize();
		}
		System.out.println("font size = " + g.getFont().getSize());
		g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize - 1));
	}

	private static GraphicsConfiguration getDefaultConfiguration() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		return gd.getDefaultConfiguration();
	}

	private static BufferedImage resize(BufferedImage image, int width,
			int height) {
		BufferedImage resizedImage = new BufferedImage(width, height,
				BufferedImage.TRANSLUCENT);
		Graphics2D g = resizedImage.createGraphics();
		g.setColor(g.getColor().darker());
		g.drawImage(image, 0, 0, width, height, null);

		g.dispose();
		return resizedImage;
	}

	private static BufferedImage resizeToFit(BufferedImage image,
			Dimension screenSize) {
		float widthResize = getResizeFactor(image.getWidth(),
				(int) screenSize.getWidth());
		float heightResize = getResizeFactor(image.getHeight(),
				(int) screenSize.getHeight());
		float resize = (widthResize < heightResize) ? widthResize
				: heightResize;
		int height = (int) (image.getHeight() * resize);
		int width = (int) (image.getWidth() * resize);
		return resize(image, width, height);
	}

	private static float getResizeFactor(int imgLength, int screenLength) {
		return (((float) screenLength / (float) imgLength));
	}


}
