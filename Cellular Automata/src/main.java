import java.awt.Color;
import java.awt.Graphics2D;

import Cellular_Automata.Grid;
import JGGame_Package.JGAction;
import JGGame_Package.JGFrame;
import JGGame_Package.JGGameLoop;
import JGNeuralNetwork.JGNet;

public class main {
	final static int TickPerSecond = 1000;
	
	static JGAction frame1_action = new frame1_action();
	static JGFrame frame1 = new JGFrame(600,620,frame1_action);
	static JGGameLoop loop1 = new JGGameLoop(frame1_action,TickPerSecond);
	static Grid grid1 = new Grid(30,frame1,loop1);
	
	public static void main(String args[]) {
		loop1.Start();
	}

	public static class frame1_action extends JGAction{
		int v=0;
		@Override
		public void Update() {
			// TODO Auto-generated method stub
			grid1.Update();
//			if(v>=100000) {
//				loop1.UpdateFPS(10);
//			}else {
//				v++;
//			}
		}
		@Override
		public void Draw(Graphics2D g2d) {
			// TODO Auto-generated method stub
			grid1.Draw(g2d);
		}
	}
	public static void print_Debug(String text) {
		String current_line = Thread.currentThread().getStackTrace()[2].getMethodName();
		String obj_type = "String";
		System.out.println("Debug Output....@"+current_line+"...."+obj_type+"="+text);
	}
	public static void print_Debug(double[] numbers) {
		String current_line = Thread.currentThread().getStackTrace()[2].getMethodName();
		String obj_type = "Double["+numbers.length+"]";
		System.out.print("Debug Output....@"+current_line+"...."+obj_type+"= [");
		for(int v=0;v<numbers.length;v++) {
			System.out.printf("%.2f,",numbers[v]);
		}
		System.out.println("]");
	}
}
