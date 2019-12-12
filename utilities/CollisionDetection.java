import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CollisionDetection {
	public static boolean overlaps(double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		boolean a_to_left_of_b = (a_right < b_left);
		boolean a_to_right_of_b = (a_left > b_right);
		boolean horizontal_overlap = !(a_to_left_of_b || a_to_right_of_b);

		boolean a_above_b = (a_bottom < b_top);
		boolean a_below_b = (a_top > b_bottom);
		boolean vertical_overlap = !(a_above_b || a_below_b);

		return (horizontal_overlap && vertical_overlap);
	}

	public static boolean inside(double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		boolean x_inside = ((b_left <= a_left) && (a_right <= b_right));
		boolean y_inside = ((b_top <= a_top) && (a_bottom <= b_bottom));
		if (x_inside && y_inside) {
			return true;
		} else {
			return false;	    	
		}
	}
	
	public static boolean pixelBasedOverlaps(Sprite spriteA, Sprite spriteB) {
		if (overlaps(spriteA.getMinX(), spriteA.getMinY(), spriteA.getMaxX(), spriteA.getMaxY(), 
				spriteB.getMinX(), spriteB.getMinY(), spriteB.getMaxX(), spriteB.getMaxY()) == false) {
			return false;
		}
		
		BufferedImage bufferedA = (BufferedImage) spriteA.getImage();
		BufferedImage bufferedB = (BufferedImage) spriteB.getImage();
		
		int offsetX = (int) (spriteB.getMinX() - spriteA.getMinX());
		int offsetY = (int) (spriteB.getMinY() - spriteA.getMinY());
		
		int left = Math.max(0, (int) (offsetX));
		int top =  Math.max(0, (int) (offsetY));
		int right = (int) (spriteA.getWidth() - Math.max(0, spriteA.getMaxX() - spriteB.getMaxX()));
		int bottom = (int) (spriteA.getHeight() - Math.max(0, spriteA.getMaxY() - spriteB.getMaxY()));
		
		double scaleXA = bufferedA.getHeight() / (float)spriteA.getWidth();
		double scaleYA = bufferedA.getHeight() / (float)spriteA.getHeight();
		double scaleXB = bufferedB.getHeight() /  (float)spriteB.getWidth();
		double scaleYB = bufferedB.getHeight() /  (float)spriteB.getHeight();

		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				int xA = (int)(x * scaleXA);
				int yA = (int)(y * scaleYA);				
				int xB = (int) ((x - offsetX) * scaleXB);
				int yB = (int) ((y - offsetY) * scaleYB);
				if ((xB >= 0) && (yB >= 0) && (yB < bufferedB.getWidth()) && (yB < bufferedB.getHeight())) {
					int pixelA = bufferedA.getRGB(xA, yA);
					int pixelB = bufferedB.getRGB(xB, yB);
					if ((pixelA>>>24 > 0x00) && (pixelB>>>24 > 0x00)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public static boolean covers (double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		return inside(b_left, b_top, b_right, b_bottom, a_left, a_top, a_right, a_bottom);
	}
}
