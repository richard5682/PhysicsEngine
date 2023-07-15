import java.awt.Graphics2D;

public abstract class Action {
	JGFrame frame;
	public void SetFrame(JGFrame frame) {
		this.frame = frame;
	}
	public abstract void Draw(Graphics2D g2d);
	public void Render() {
		frame.main_panel.repaint();
	}
	public abstract void Update(double dt);
}
