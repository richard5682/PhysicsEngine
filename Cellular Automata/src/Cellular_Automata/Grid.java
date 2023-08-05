package Cellular_Automata;

import java.awt.Color;
import java.awt.Graphics2D;

import Interface.JGDrawable;
import JGGame_Package.JGFrame;
import JGGame_Package.JGGameLoop;
import JGNeuralNetwork.JGNet;

public class Grid implements JGDrawable{
	final int OFFSET = 20;
	final int SIZE_OFFSET = 5;
	Cell[][] cells;
	int size,frame_size;
	float cell_size;
	JGFrame frame;
	
	static final int INITIAL_POPULATION = 100;
	
	Turtle[] turtles = new Turtle[INITIAL_POPULATION];
	int no_tick = 0;
	
	Test_Movement test1;
	
	double testx=0,testy=0;
	
	JGGameLoop loop;
	public Grid(int size,JGFrame frame,JGGameLoop loop) {
		this.loop = loop;
		this.size = size;
		this.frame = frame;
		for(int i=0;i<turtles.length;i++) {
			turtles[i] = new Turtle(0,0,this);
		}
		test1  = new Test_Movement(turtles);
		test1.SetTick(100);
		test1.SetMutationFactor(0.1);
		test1.SetRandomFactor(0.7);
		
		this.frame_size = frame.getWidth()-size*3;
		this.cell_size = (float)this.frame_size/size;
		cells = new Cell[size][size];
		//Initialize Cells
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				cells[x][y] = new Cell();
			}
		}
	}
	
	@Override
	public void Update() {
		test1.Update();
		if(test1.epoch%50 == 0 && test1.epoch!=0) {
			this.loop.UpdateFPS(30);
		}else {
			this.loop.UpdateFPS(1000);
		}
		for(int i=0;i<turtles.length;i++) {
			turtles[i].Update();
		}
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				//UPDATE CELLS HERE
				//cells[x][y].state = States.FOOD;
			}
		}
	}

	@Override
	public void Draw(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		g2d.fillRect(OFFSET+(int)(testx*cell_size), 
				OFFSET+(int)(testy*cell_size),
				(int)(cell_size), (int)(cell_size));
		g2d.setColor(new Color(0,250,0,50));
		for(int i=0;i<turtles.length;i++) {
			g2d.fillRect(OFFSET+(int)(turtles[i].pos_x*cell_size), 
					OFFSET+(int)(turtles[i].pos_y*cell_size),
					(int)(cell_size), (int)(cell_size));
		}
		g2d.setColor(new Color(0,0,250,250));
		g2d.fillRect(OFFSET+(int)(turtles[test1.fittest].pos_x*cell_size), 
				OFFSET+(int)(turtles[test1.fittest].pos_y*cell_size),
				(int)(cell_size), (int)(cell_size));
		
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				if(cells[x][y].GetColor() == null) continue;
				g2d.setColor(cells[x][y].GetColor());
				g2d.fillRect((int)(OFFSET+(x*cell_size))+SIZE_OFFSET, 
							(int)(OFFSET+(y*cell_size)+SIZE_OFFSET), 
							(int)cell_size-SIZE_OFFSET*2, (int)cell_size-SIZE_OFFSET*2);
			}
		}
		g2d.setColor(Color.DARK_GRAY);
		for(int x=0;x<=frame_size;x+=cell_size) {
			g2d.drawLine(OFFSET+x, OFFSET, OFFSET+x, frame_size+OFFSET);
		}
		for(int y=0;y<=frame_size;y+=cell_size) {
			g2d.drawLine(OFFSET, y+OFFSET, frame_size+OFFSET, y+OFFSET);
		}
	}
	
	public class Test_Movement extends TestCase{

		public Test_Movement(Turtle[] turtles) {
			super(turtles);
		}

		@Override
		public double GetFitness(int i) {
			Turtle t = turtles[i];
			testx = t.goal_x;
			testy = t.goal_y;
			double dy = Math.abs(testy-t.pos_y);
			double dx = Math.abs(testx-t.pos_x);
			return -(dy*dy+dx*dx);
		}

		@Override
		public void ResetTest() {
			int tx = (int)(Math.random()*size);
			int ty = (int)(Math.random()*size);
			for(int i=0;i<turtles.length;i++) {
				Turtle t = turtles[i];
				t.goal_x = tx;
				t.goal_y = ty;
				t.pos_x = 14;
				t.pos_y = 14;
			}
		}
		
	}
}
