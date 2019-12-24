public class CollisionDetection {
	static boolean overlaps(double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		boolean a_to_left_of_b = (a_right < b_left);
		boolean a_to_right_of_b = (a_left > b_right);
		boolean horizontal_overlap = !(a_to_left_of_b || a_to_right_of_b);

		boolean a_above_b = (a_bottom < b_top);
		boolean a_below_b = (a_top > b_bottom);
		boolean vertical_overlap = !(a_above_b || a_below_b);

		return (horizontal_overlap && vertical_overlap);
	}

	private static boolean inside(double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		boolean x_inside = ((b_left <= a_left) && (a_right <= b_right));
		boolean y_inside = ((b_top <= a_top) && (a_bottom <= b_bottom));

		return x_inside && y_inside;
	}

	public static boolean covers (double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		return inside(b_left, b_top, b_right, b_bottom, a_left, a_top, a_right, a_bottom);
	}
}
