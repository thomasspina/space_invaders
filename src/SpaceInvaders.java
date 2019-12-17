import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class SpaceInvaders extends JFrame {
	
	final public static int FRAMES_PER_SECOND = 60;
	final public static int SCREEN_HEIGHT = 600;
	final public static int SCREEN_WIDTH = 750;
	final private static String HIGH_SCORE_FILE = "score.txt";
	
	private int xpCenter = SCREEN_WIDTH / 2;
	private int ypCenter = SCREEN_HEIGHT / 2;
	
	private double scale = 1;
	private double xCenter = 0;		
	private double yCenter = 0;
	
	private JPanel panel = null;
	private JLabel lblScore;
	
	private static Thread game;
	
	private long current_time = 0;
	private long next_refresh_time = 0;
	private long last_refresh_time = 0;
	private long minimum_delta_time = 1000 / FRAMES_PER_SECOND;
	private long actual_delta_time = 0;
	private long elapsed_time = 0;
	private boolean isPaused = false;
	private float fadeDirection = -0.07f;
	private String score;
	
	private ArrayList<ActiveSprite> activeSprites = null;
	private ArrayList<StaticSprite> staticSprites = null;
	
	private KeyboardInput keyboard = new KeyboardInput();
	private Serializer serializer = new Serializer();
	private Screen screen = null;
	
	public SpaceInvaders() {
		super();
		setFocusable(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		
		try {
			score = (String) serializer.deserialize(HIGH_SCORE_FILE);
		} catch (Exception e) {
			score = "0";
		}
		
		Container cp = getContentPane();
		cp.setBackground(Color.BLACK);
		cp.setLayout(null);

		panel = new DrawPanel();
		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblSpaceInvaders = new JLabel("Space Invaders", SwingConstants.CENTER);
		lblSpaceInvaders.setFont(new Font("Lucida Console", Font.BOLD, 70));
		lblSpaceInvaders.setForeground(Color.WHITE);
		lblSpaceInvaders.setBounds(10, 200, 714, 84);
		getContentPane().add(lblSpaceInvaders);
		getContentPane().setComponentZOrder(lblSpaceInvaders, 0);
		
		JLabel lblHighScore = new JLabel("High Score:");
		lblHighScore.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		lblHighScore.setForeground(Color.WHITE);
		lblHighScore.setBounds(10, 11, 149, 30);
		getContentPane().add(lblHighScore);
		getContentPane().setComponentZOrder(lblHighScore, 0);
		
		lblScore = new JLabel(score, SwingConstants.RIGHT);
		lblScore.setForeground(Color.WHITE);
		lblScore.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		lblScore.setBounds(100, 20, 100, 14);
		getContentPane().add(lblScore);
		getContentPane().setComponentZOrder(lblScore, 0);
		
		FadeLabel lblPressPToPlay = new FadeLabel("press P to play", SwingConstants.CENTER);
		lblPressPToPlay.setFont(new Font("Lucida Console", Font.PLAIN, 29));
		lblPressPToPlay.setForeground(Color.WHITE);
		lblPressPToPlay.setBounds(10, 308, 714, 46);
		getContentPane().add(lblPressPToPlay);
		getContentPane().setComponentZOrder(lblPressPToPlay, 0);
		
		Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float alpha = lblPressPToPlay.getAlpha();
                alpha += fadeDirection;
                if (alpha < 0) {
                    alpha = 0;
                    fadeDirection = 0.07f;
                } else if (alpha > 1) {
                    alpha = 1;
                    fadeDirection = -0.07f;
                }
                lblPressPToPlay.setAlpha(alpha);
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();
    }
	
	public static void main(String[] args) {
		SpaceInvaders spaceInvaders = new SpaceInvaders();
		spaceInvaders.setVisible(true);
		
		game = new Thread() {
			public void run() {
				spaceInvaders.animationLoop();
			}
		};
		
		game.start();
	}
	
	public void animationLoop() {
		screen = new MenuScreen();
		staticSprites = screen.getStaticSprites();
		activeSprites = screen.getActiveSprites();
		
		while(true) {
			last_refresh_time = System.currentTimeMillis();
			next_refresh_time = current_time + minimum_delta_time;

			while (current_time < next_refresh_time) {
				Thread.yield();

				try {
					Thread.sleep(1);
				}
				catch(Exception e) {    					
				} 

				current_time = System.currentTimeMillis();
			}
			
			keyboard.poll();
			handleKeyboardInput();
			updateTime();
			screen.update(keyboard, actual_delta_time);
			repaint();
		}
	}
	
	private void handleKeyboardInput() {
		if (keyboard.keyDown(80)) {
			// play the game
		}
	}
	
	private void updateTime() {
		current_time = System.currentTimeMillis();
		actual_delta_time = (isPaused ? 0 : current_time - last_refresh_time);
		last_refresh_time = current_time;
		elapsed_time += actual_delta_time;
	}
	
	class DrawPanel extends JPanel {

		public void paintComponent(Graphics g) {	
			if (screen == null) {
				return;
			}
			
			
			for (StaticSprite staticSprite : staticSprites) {
				if (staticSprite.getShowImage()) {
					if (staticSprite.getImage() != null) {
						g.drawImage(staticSprite.getImage(), translateX(staticSprite.getMinX()), translateY(staticSprite.getMinY()), scaleX(staticSprite.getWidth()), scaleY(staticSprite.getHeight()), null);
					} else {
						g.setColor(Color.RED);
						g.fillRect(translateX(staticSprite.getMinX()), translateY(staticSprite.getMinY()),scaleX(staticSprite.getWidth()), scaleY(staticSprite.getHeight()));					
					}
				}
			}

			for (ActiveSprite activeSprite : activeSprites) {
				if (activeSprite.getImage() != null) {
					g.drawImage(activeSprite.getImage(), translateX(activeSprite.getMinX()), translateY(activeSprite.getMinY()), scaleX(activeSprite.getWidth()), scaleY(activeSprite.getHeight()), null);
				} else {
					g.setColor(Color.BLUE);
					g.fillRect(translateX(scale * (activeSprite.getMinX())), translateY(activeSprite.getMinY()), scaleX(activeSprite.getWidth()), scaleY(activeSprite.getHeight()));					
				}
			}

		}
		
		private int translateX(double x) {
			return xpCenter + scaleX(x - xCenter);
		}
		
		private int scaleX(double x) {
			return (int) Math.round(scale * x);
		}
		private int translateY(double y) {
			return ypCenter + scaleY(y - yCenter);
		}		
		private int scaleY(double y) {
			return (int) Math.round(scale * y);
		}
	}
	
	protected void this_windowClosing(WindowEvent e) {
		try {
			serializer.serialize(score, HIGH_SCORE_FILE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		dispose();	
	}
}
