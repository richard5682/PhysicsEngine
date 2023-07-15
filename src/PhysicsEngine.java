import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class PhysicsEngine {
	static int WIDTH = 460;
	static int HEIGHT = 440;
	static double gravity = 980;
	static final boolean DEBUG_MODE = false;
	//TODO: When colliding update velocity
	public static class Particle{
		boolean on_collision = false;
		boolean static_collision = false;
		Vector pos;
		Velocity vel;
		double rad = 50;
		double mass = 1;
		Color color;
		public Particle(int x,int y,double vx, double vy,double rad,Color fill) {
			this.rad = rad;
			this.color = fill;
			this.mass = rad*rad*rad;
			pos = new Vector(x,y);
			this.vel = new Velocity(vx,vy);
		}
		public int[] GetPartitionCoord(int size,int partition){
			int x = (int)Math.floor((double)partition/size*pos.x);
			int y = (int)Math.floor((double)partition/size*pos.y);
			int[] return_val = {Math.max(x,0),Math.max(y,0)};
			return return_val;
		}
		public void Update(ArrayList<Particle> particles,int size,int partition,ArrayList<Particle>[][] screen_particle,double dt) {
			for(int i=0;i<particles.size();i++) {
				on_collision = false;
			}
			int[] coord = GetPartitionCoord(size,partition);
			for(int dx=-1;dx<=1;dx++) {
				for(int dy=-1;dy<=1;dy++) {
					int px = coord[0]+dx;
					int py = coord[1]+dy;
					if(px < 0 || px >= partition || py<0 || py >= partition) break;
					for(int i=0;i<screen_particle[px][py].size();i++) {
						Particle p2 = screen_particle[px][py].get(i);
						CheckBoundaryCollision();
						if(on_collision) break;
						if(p2 != this && CheckCollision(this,p2) && !p2.on_collision){
							Collide(p2);
							on_collision = true;
						}
					}
				}
			}
			for(int v=0;v<5;v++) {
				for(int dx=-1;dx<=1;dx++) {
					for(int dy=-1;dy<=1;dy++) {
						int px = coord[0]+dx;
						int py = coord[1]+dy;
						if(px < 0 || px >= partition || py<0 || py >= partition) break;
						for(int i=0;i<screen_particle[px][py].size();i++) {
							Particle p2 = screen_particle[px][py].get(i);
							CheckBoundaryCollision();
							if(p2 != this && CheckCollision(this,p2)){
								Separate(p2);
							}
						}
					}
				}
			}
			vel.velocity_vec.y += gravity*dt;
			pos = Vector.Add_Vec(pos, Vector.Sca_Vec(vel.velocity_vec, dt));
		}
		public void DrawParticle(Graphics2D g2d) {
			g2d.setColor(this.color);
			g2d.fillOval((int)(pos.x-rad), (int)(pos.y-rad), (int)rad*2, (int)rad*2);
//			g2d.setColor(Color.WHITE);
//			if(on_collision) g2d.setColor(Color.red);
//			if(static_collision) g2d.setColor(Color.GRAY);
//			g2d.drawOval((int)(pos.x-rad), (int)(pos.y-rad), (int)rad*2, (int)rad*2);
		}
		public void Collide(Particle p2) {
			//print_Debug("COLLISION");
			Particle p1 = this;
			if((p1.vel.velocity_vec.GetLength() > 30 || p2.vel.velocity_vec.GetLength() > 30)) {
				static_collision = false;
				//print_Debug("COLLISION_DYNAMIC");
				Vector n = Vector.Sub_Vec(p1.pos, p2.pos).Get_Normal();
				double ff1 = Vector.dot_Vec(n, p1.vel.velocity_vec.Get_Normal());
				double ff2 = Vector.dot_Vec(n, p2.vel.velocity_vec.Get_Normal());
				double ff = Math.max(Math.abs(ff1), Math.abs(ff2));
				if(ff < 0.5) return;
				double v1 = Vector.dot_Vec(n, p1.vel.velocity_vec);
				double v2 = Vector.dot_Vec(n, p2.vel.velocity_vec);
				double[] w = Compute_LinearVelocity(p1.mass,v1,p2.mass,v2);
				Vector w1 = Vector.Sca_Vec(n, -w[0]*ff*0.8);
				Vector w2 = Vector.Sca_Vec(n, -w[1]*ff*0.8);
				this.vel.velocity_vec = w1;
				p2.vel.velocity_vec = w2;
			}else {
				static_collision = true;
			}
		}
		public void Separate(Particle p2) {
			Particle p1 = this;
			Vector posvec = Vector.Sub_Vec(p1.pos, p2.pos);
			Vector n = posvec.Get_Normal();
			double dist = posvec.GetLength();
			double amount_to_move = (p1.rad+p2.rad-dist)*1.3;
			Vector delta_vec = Vector.Sca_Vec(n, amount_to_move/2);
			p1.pos = Vector.Add_Vec(p1.pos, delta_vec);
			p2.pos = Vector.Sub_Vec(p2.pos, delta_vec);
		}
		public void CheckBoundaryCollision() {
			//TOP
			if(this.pos.y - rad < 0) {
				this.vel.velocity_vec.y *= -1;
			}else if(this.pos.y + rad> HEIGHT) {
				this.vel.velocity_vec.y *= -0.8;
				this.vel.velocity_vec.x *= 0.995;
				this.pos.y = HEIGHT-rad;
			}
			if(this.pos.x - rad < 0) {
				this.vel.velocity_vec.x *= -1;
				this.pos.x = rad;
			}else if(this.pos.x + rad> WIDTH) {
				this.vel.velocity_vec.x *= -1;
				this.pos.x = WIDTH-rad;
			}
		}
	}
	public static boolean CheckCollision(Particle p1,Particle p2) {
		if(Vector.Sub_Vec(p1.pos, p2.pos).GetLength() < p1.rad+p2.rad) {
			return true;
		}
		return false;
	}
	public static double[] Compute_LinearVelocity(double m1,double v1,double m2,double v2) {
		//OUTPUT [w1,w2]
		double A = m1*v1 + m2*v2;
		double B = 0.5*(m1*v1*v1+m2*v2*v2);
		double Cx = (A*A)/(2*m1)-B;
		double Bx = (-A*m2)/m1;
		double Ax = (m2*m2)/(2*m1)+(m2/2);
		double[] xn = Solve_QuadraticEqn(Ax,Bx,Cx);
		double w2 = xn[0];
		double w1 = (A-m2*w2)/m1;
		double[] return_val = {w1,w2};
		if(DEBUG_MODE) {
			print_Debug(return_val);
		}
		return return_val;
	}
	public static double[] Solve_QuadraticEqn(double A,double B,double C) {
		double Discriminant = B*B-4*A*C;
		if(Discriminant < 0) System.err.println("ERROR: IMAGINARY");
		double sqrt = Math.sqrt(Discriminant);
		double x1 = (-B+sqrt)/(2*A);
		double x2 = (-B-sqrt)/(2*A);
		double[] return_val = {x1,x2};
		return return_val;
	}
	public static double GetDist(double x,double y) {
		return Math.sqrt(x*x+y*y);
	}
	
	public static class Vector {
		double x,y;
		public Vector(double x,double y) {
			this.x = x;
			this.y = y;
		}
		public double GetLength() {
			return GetDist(x,y);
		}
		public Vector Get_Normal() {
			double length = GetLength();
			return new Vector(x/length,y/length);
		}
		public String toString() {
			return "X: "+x+"   Y: "+y;
		}
		public static double dot_Vec(Vector v1,Vector v2) {
			return v1.x*v2.x+v1.y*v2.y;
		}
		public static Vector Sca_Vec(Vector v1,double s) {
			return new Vector(v1.x*s,v1.y*s);
		}
		public static Vector Sub_Vec(Vector v1,Vector v2) {
			return new Vector(v1.x-v2.x,v1.y-v2.y);
		}
		public static Vector Add_Vec(Vector v1,Vector v2) {
			return new Vector(v1.x+v2.x,v1.y+v2.y);
		}
	}
	public static class Velocity{
		Vector velocity_vec;
		public Velocity(double vx,double vy) {
			velocity_vec = new Vector(vx,vy);
		}
		public String toString() {
			return "Velocity: "+velocity_vec;
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
