import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JGFrame extends JFrame{
	static final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	static final DisplayMode dm = env.getDefaultScreenDevice().getDisplayMode();
	static final int screen_w = dm.getWidth();
	static final int screen_h = dm.getHeight();
	Action action;
	JGPanel main_panel;
	int width,height;
	public JGFrame(int w,int h,Action action) {
		this.width = w;
		this.height = h;
		this.action = action;
		this.action.SetFrame(this);
		main_panel = new JGPanel(w,h,action);
		this.add(main_panel);
		this.setLocation((screen_w-w)/2, (screen_h-h)/2);
		this.setSize(w, h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		main_panel.repaint();
	}
	public static class JGPanel extends JPanel{
		Action action;
		public JGPanel(int width,int height,Action action) {
			this.action = action;
			this.setSize(width, height);
			this.setLocation(0,0);
		}
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			action.Draw(g2d);
		}
	}
}
