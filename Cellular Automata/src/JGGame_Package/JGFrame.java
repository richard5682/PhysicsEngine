package JGGame_Package;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JGFrame extends JFrame{
	static final GraphicsEnvironment ENV = GraphicsEnvironment.getLocalGraphicsEnvironment();
	static final DisplayMode GD = ENV.getDefaultScreenDevice().getDisplayMode();
	static final int WIDTH = GD.getWidth();
	static final int HEIGHT = GD.getHeight();
	
	JGPanel main_panel;
	
	public JGFrame(int w,int h,JGAction action) {
		this.setSize(w, h);
		main_panel  = new JGPanel(action);
		action.frame = this;
		this.setLocation((WIDTH-w)/2, (HEIGHT-h)/2);
		this.add(main_panel);
		main_panel.repaint();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public static class JGPanel extends JPanel{
		JGAction action;
		public JGPanel(JGAction action) {
			this.action = action;
		}
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			action.Draw(g2d);
		}
	}
}
