import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class test extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public test() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSpaceInvaders = new JLabel("Space Invaders", SwingConstants.CENTER);
		lblSpaceInvaders.setFont(new Font("Lucida Console", Font.BOLD, 70));
		lblSpaceInvaders.setForeground(Color.GREEN);
		lblSpaceInvaders.setBounds(10, 200, 714, 84);
		contentPane.add(lblSpaceInvaders);
		
		JLabel lblHighScore = new JLabel("High Score:");
		lblHighScore.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		lblHighScore.setForeground(Color.GREEN);
		lblHighScore.setBounds(10, 11, 149, 30);
		contentPane.add(lblHighScore);
		
		JLabel lblScore = new JLabel("score", SwingConstants.RIGHT);
		lblScore.setForeground(Color.GREEN);
		lblScore.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		lblScore.setBounds(128, 20, 118, 14);
		contentPane.add(lblScore);
		
		JLabel lblPressPTo = new JLabel("press P to play", SwingConstants.CENTER);
		lblPressPTo.setFont(new Font("Lucida Console", Font.PLAIN, 29));
		lblPressPTo.setForeground(Color.GREEN);
		lblPressPTo.setBounds(10, 308, 714, 46);
		contentPane.add(lblPressPTo);
	}
}
