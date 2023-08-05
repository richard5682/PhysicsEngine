package Cellular_Automata;

import JGNeuralNetwork.JGNet;

public abstract class TestCase {
	int epoch=0;
	
	int no_tick=20;
	int no_trial=10;
	int tick=0,trial=0;
	int survivor_length=10;
	
	double mutation_factor = 0.1; //MUTATION FACTOR
	double random_factor = 0.2; //PERCENT OF WHICH TURTLES NUMBER WILL RANDOMIZE
	
	int fittest=0;
	double max_fit=0;
	
	Turtle[] turtles;
	double[] fitness;
	int[] survivor_index;
	public TestCase(Turtle[] turtles) {
		this.turtles = turtles;
		fitness = new double[turtles.length];
		survivor_index = new int[survivor_length];
		for(int i=0;i<survivor_length;i++) {
			survivor_index[i] = -1;
		}
	}
	public void SetTick(int tick) {
		this.no_tick = tick;
	}
	public void SetMutationFactor(double factor) {
		mutation_factor = factor;
	}
	public void SetRandomFactor(double factor) {
		random_factor = factor;
	}
	public void SetTrial(int trial) {
		this.no_trial = trial;
	}
	public void AppendSurvivor(int index) {
		for(int i=survivor_length-1;i>0;i--) {
			survivor_index[i] = survivor_index[i-1];
		}
		survivor_index[0] = index;
	}
	public void GetFittest() {
		int fittest = 0;
		double max_fit=0;
		for(int i=0;i<fitness.length;i++) {
			double fitness = this.fitness[i]/this.no_trial;
			if(i==0) {
				max_fit = fitness;
				continue;
			}
			if(fitness > max_fit) {
				fittest = i;
				max_fit = fitness;
			}
		}
		this.fittest = fittest;
		this.max_fit = max_fit;
	}
	public boolean CheckIfNotSurvivor(int index) {
		for(int i=0;i<survivor_index.length;i++) {
			if(survivor_index[i] == index) {
				return false;
			}
		}
		return true;
	}
	public void Update() {
		if(tick > no_tick) {
			if(trial > no_trial) {
				epoch++;
				trial = 0;
				GetFittest();
				AppendSurvivor(fittest);
				System.out.print("EPOCH : "+epoch);
				System.out.print("  FITNESS : "+max_fit);
				System.out.println("  FITTEST : "+fittest);
				JGNet fittest_net = turtles[fittest].neural_net;
				int mate = 0;
				for(int i=0;i<turtles.length;i++) {
					if(CheckIfNotSurvivor(i) && mate > turtles.length*(1-random_factor/epoch)) {
						turtles[i].neural_net.Randomize();
					}else if(CheckIfNotSurvivor(i)) {
						JGNet fittest_clone = fittest_net.Copy();
						fittest_clone.Mutate(mutation_factor*i);
						turtles[i].neural_net = fittest_clone;
						mate++;
					}
					fitness[i] = 0;
				}
			}else {
				for(int i=0;i<turtles.length;i++) {
					fitness[i] += this.GetFitness(i);
				}
				trial++;
			}
			ResetTest();
			tick = 0;
		}
		tick++;
	}
	public abstract double GetFitness(int i);
	public abstract void ResetTest();
}
