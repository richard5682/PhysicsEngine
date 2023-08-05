package JGGame_Package;

public class JGGameLoop implements Runnable{
	static final int NANOSEC = 1000000000;
	JGAction action;
	int FPS = 60;
	double FACTOR = FPS/NANOSEC;
	public Thread main_thread;
	public JGGameLoop(JGAction action, int FPS) {
		this.action = action;
		this.FPS = FPS;
		FACTOR = (double)FPS/NANOSEC;
		main_thread = new Thread(this);
	}
	public void UpdateFPS(int FPS) {
		this.FPS = FPS;
		FACTOR = (double)FPS/NANOSEC;
	}
	public void Start() {
		main_thread.start();
	}
	@Override
	public void run() {
		int fps_counter = 0;
		double acc_time = 0;
		long time_past = System.nanoTime();
		long time_milli = System.currentTimeMillis();
		while(true) {
			long time_now = System.nanoTime();
			acc_time += FACTOR*(time_now-time_past);
			time_past = time_now;
			if(acc_time >= 1) {
				acc_time--;
				fps_counter++;
				action.Update();
				action.Render();
				if(System.currentTimeMillis()-time_milli >= 1000) {
					acc_time = 0;
					time_milli = System.currentTimeMillis();
					System.out.println("FPS: "+fps_counter);
					fps_counter = 0;
				}
			}
		}
	}

}
