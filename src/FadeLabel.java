import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class FadeLabel extends JLabel {
	private float alpha;
	
	FadeLabel(String text, int horizontalAlignment) {
		setText(text);
		setHorizontalAlignment(horizontalAlignment);
		setAlpha(1f);
	}
	
	void setAlpha(float value) {
        if (alpha != value) {
            float old = alpha;
            alpha = value;
            firePropertyChange("alpha", old, alpha);
            repaint();
        }
    }

    float getAlpha() {
        return alpha;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
        super.paint(g2d);
        g2d.dispose();
    }
}
