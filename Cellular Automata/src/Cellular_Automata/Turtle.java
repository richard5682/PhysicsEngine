package Cellular_Automata;

import JGNeuralNetwork.JGNet;

public class Turtle {
	//THIS IS THE AI
	//INPUT
	//	- TIME
	//	- SIGHT
	//OUTPUT
	//	- MOVEMENT
	int[] layers_size = {2,20,15,2};
	JGNet neural_net = new JGNet(layers_size);
	
	double pos_x,pos_y;
	double target_x,target_y;
	double goal_x,goal_y;
	double vel = 0.2;
	Grid grid;
	
	double input_fitness = 0;
	double input_time = 0;
	
	public Turtle(int x,int y,Grid grid) {
		this.pos_x = x;
		this.pos_y = y;
		this.grid = grid;
	}
	public void Move() {
		if(pos_y > target_y) {
			pos_y -= vel;
		}else if(pos_y < target_y){
			pos_y += vel;
		}
		if(pos_x > target_x) {
			pos_x -= vel;
		}else if(pos_x < target_x){
			pos_x += vel;
		}
	}
	public void Update() {
		double spatial_x = (double)(goal_x)/grid.size;
		double spatial_y = (double)(goal_y)/grid.size;
		input_time += 0;
		neural_net.SetInput(new double[] {spatial_x,spatial_y});
		double[] output = neural_net.ComputeOutput();
		target_x = output[0]*grid.size;
		target_y = output[1]*grid.size;
		Move();
	}
}
