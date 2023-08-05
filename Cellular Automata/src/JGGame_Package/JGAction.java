package JGGame_Package;
import java.awt.Graphics2D;

import Interface.JGDrawable;

public abstract class JGAction implements JGDrawable{
	JGFrame frame;
	public abstract void Update();
	public final void Render() {
		frame.main_panel.repaint();
	}
	public abstract void Draw(Graphics2D g2d);
}
