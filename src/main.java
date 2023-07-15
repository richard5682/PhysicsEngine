import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class main {
	static Game_Action action = new Game_Action();
	static JGFrame frame1 = new JGFrame(500,500,action);
	
	static Thread update_thread = new Thread(new Update_Runnable()); 
	static final int partition =30;
	//TEST PARTICLE
	static PhysicsEngine.Particle p1 = new PhysicsEngine.Particle(100,110,0,10,50,Color.GREEN);
	static PhysicsEngine.Particle p2 = new PhysicsEngine.Particle(300,200,0,200,10,Color.GREEN);
	static PhysicsEngine.Particle p3 = new PhysicsEngine.Particle(90,30,0,20,30,Color.GREEN);
	
	static ArrayList<PhysicsEngine.Particle> particles_list = new ArrayList<PhysicsEngine.Particle>();
	static ArrayList<PhysicsEngine.Particle>[][] screen_particle = new ArrayList[partition][partition];
	public static void main(String args[]) {
		frame1.setVisible(true);
		PhysicsEngine.Compute_LinearVelocity(20*20*20,10,10,0);
//		particles_list.add(p1);
//		particles_list.add(p2);
//		particles_list.add(p3);
		update_thread.start();
		System.out.println("Main Thread Done.");
	}
	public static class Game_Action extends Action{
		int x=0;
		int v=0,c=0;
		@Override
		public void Draw(Graphics2D g2d) {
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, super.frame.width,1);
			for(int i=0;i<main.particles_list.size();i++) {
				particles_list.get(i).DrawParticle(g2d);
			}
		}
		int vx=-1;
		@Override
		public void Update(double dt) {
			
			if(v==10 && c != 1000) {
				v=0;
				particles_list.add(new PhysicsEngine.Particle(250,20,200*vx,100,8,new Color(250-(c)/4,c/4,250)));
				vx *= -1;
				c++;
			}
			v++;
			for(int x=0;x<partition;x++) {
				for(int y=0;y<partition;y++) {
					screen_particle[x][y] = new ArrayList<PhysicsEngine.Particle>();
				}
			}
			for(int i=0;i<main.particles_list.size();i++) {
				int[] coord = main.particles_list.get(i).GetPartitionCoord(main.frame1.width, main.partition);
				screen_particle[coord[0]][coord[1]].add(main.particles_list.get(i));
			}
			for(int i=0;i<main.particles_list.size();i++) {
				particles_list.get(i).Update(particles_list,main.frame1.width,main.partition,screen_particle,dt);
			}
			
		}
	}
	public static class Update_Runnable implements Runnable{
		static final int UPS = 120;
		@Override
		public void run() {
			System.out.println("Update Thread Running");
			int fps = 0;
			double acc_time = 0;
			final double NANOFRAME = (double)1/UPS;
			long time_start = System.nanoTime();
			long time_past = System.nanoTime();
			double delta_time = 0;
			while(true) {
				long time_now = System.nanoTime();
				acc_time += (double)(time_now-time_past)/1000000000;
				time_past = time_now;
				if(acc_time > NANOFRAME) {
					acc_time -= NANOFRAME;
					//UPDATE HERE
					main.action.Update(NANOFRAME);
					main.action.Render();
					fps++;
				}
				if(time_past-time_start>1000000000) {
					System.out.println("FPS: "+fps);
					time_start = System.nanoTime();
					fps=0;
				}
				
			}
		}
	}
}
