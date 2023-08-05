package JGNeuralNetwork;

public class JGNet{
	// NODES HAVE CONNECTIONS
	// WHEN BREEDING
	
	Layer[] layers;
	int[] layer_size;
	int input_size;
	public JGNet(int[] layers_size) {
		this.layer_size = layers_size;
		layers  = new Layer[layers_size.length];
		input_size = layers_size[0];
		for(int i=layers_size.length-1;i>=0;i--) {
			if(i==layers_size.length-1) {
				layers[i] = new Layer(layers_size[i],null);
				continue;
			}
			layers[i] = new Layer(layers_size[i],layers[i+1]);
		}
	}
	public JGNet Copy(){
		JGNet new_copy = new JGNet(layer_size);
		for(int i=0;i<layers.length;i++) {
			Node[] orig_nodes = layers[i].nodes;
			Node[] new_nodes = new_copy.layers[i].nodes;
			for(int v=0;v<new_nodes.length;v++) {
				new_nodes[v].bias = orig_nodes[v].bias;
				if(new_nodes[v].weights == null) continue;
				for(int u=0;u<new_nodes[v].weights.length;u++) {
					new_nodes[v].weights[u] = orig_nodes[v].weights[u];
				}	
			}
		}
		return new_copy;
	}
	public static double Random(double offset) {
		return 0.01*(Math.random()+offset);
	}
	public void Randomize() {
		for(int i=0;i<layers.length;i++) {
			Node[] nodes = layers[i].nodes;
			for(int v=0;v<nodes.length;v++) {
				Node node = nodes[v];
				node.bias += Random(-0.5);
				if(node.weights == null) continue;
				for(int u=0;u<node.weights.length;u++) {
					node.weights[u] += Random(-0.5);
				}
			}
		}
	}
	public void Mutate(double factor) {
		for(int i=0;i<layers.length;i++) {
			Node[] nodes = layers[i].nodes;
			for(int v=0;v<nodes.length;v++) {
				Node node = nodes[v];
				node.bias += Random(-0.5)*factor;
				if(node.weights == null) continue;
				for(int u=0;u<node.weights.length;u++) {
					node.weights[u] += Random(-0.5)*factor;
				}
			}
		}
	}
	public void SetInput(double[] node_values) {
		if(node_values.length != input_size) {
			System.err.println("INVALID INPUT SIZE");
			return;
		}
		for(int i=0;i<node_values.length;i++) {
			layers[0].nodes[i].val = node_values[i];
		}
	}
	public double[] ComputeOutput() {
		layers[0].ForwardPropagate();
		Node[] output_node = layers[layers.length-1].nodes;
		double[] returnval = new double[output_node.length];
		for(int i=0;i<output_node.length;i++) {
			returnval[i] = output_node[i].val;
		}
		return returnval;
	}
	public static class Node{
		double val;
		double bias;
		double weights[];
		public Node(int next_size) {
			bias = Random(-0.5);
			weights = new double[next_size];
			for(int i=0;i<weights.length;i++) {
				weights[i] = Random(-0.5);
			}
		}
		public Node() {
			bias = Random(-0.5);
		}
	}
	public static class Layer{
		Node[] nodes;
		Layer next_layer = null;
		int size;
		public Layer(int size,Layer next_layer) {
			this.next_layer = next_layer;
			this.size = size;
			nodes = new Node[size];
			if(next_layer == null) {
				for(int i=0;i<nodes.length;i++) {
					nodes[i] = new Node();
				}
				return;
			}
			for(int i=0;i<nodes.length;i++) {
				nodes[i] = new Node(next_layer.size);
			}
		}
		public void ForwardPropagate() {
			if(next_layer != null) {
				Node[] next_nodes = next_layer.nodes;
				for(int i=0;i<next_nodes.length;i++) {
					next_nodes[i].val = next_nodes[i].bias;
					for(int v=0;v<nodes.length;v++) {
						next_nodes[i].val += nodes[v].val*nodes[v].weights[i];
					}
					next_nodes[i].val = Activation(next_nodes[i].val);
				}
				next_layer.ForwardPropagate();
			}
		}
		public double Activation(double val) {
			return (val>0)?val:val/100;
		}
	}
}
