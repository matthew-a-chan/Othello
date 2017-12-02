/**
 * @author Stephen C.
 */
package NeuralNetwork;

/**
 * @author Stephen C.
 *
 */
public class Network {

	final int InputNeuronCount=64;
	final int HiddenNeuronCount=42;
	final int HiddenLayerCount=1;



	Node[] InputLayer;
	Node[][] HiddenLayers;
	Node OutNode;

	Connection[] Connections;



	public Network() {
		InputLayer=new Node[InputNeuronCount];

		//Input -> Hidden
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i]=new Node(HiddenNeuronCount);
			InputLayer[i].x=100;
			InputLayer[i].y=50+i*900/InputNeuronCount;
		}

		HiddenLayers=new Node[HiddenLayerCount][HiddenNeuronCount];

		//Hidden -> Hidden
		for(int k=0;k<HiddenLayerCount-1;k++) {
			for(int i=0;i<HiddenNeuronCount;i++) {
				HiddenLayers[k][i]=new Node(HiddenNeuronCount);
				HiddenLayers[k][i].x=700;
				HiddenLayers[k][i].y=100+i*800/HiddenNeuronCount;
			}
		}

		//Last Hidden -> Output
		for(int i=0;i<HiddenNeuronCount;i++) {
			HiddenLayers[HiddenLayerCount-1][i]=new Node(1);
			HiddenLayers[HiddenLayerCount-1][i].x=950;
			HiddenLayers[HiddenLayerCount-1][i].y=100+i*800/HiddenNeuronCount;
		}


		OutNode=new Node(0);
		OutNode.x=1400;
		OutNode.y=500;


		//Neurons=new Node[InputNeuronCount+HiddenNeuronCount*HiddenLayerCount+1];
		Connections=new Connection[InputNeuronCount*HiddenNeuronCount+HiddenNeuronCount*HiddenNeuronCount*(HiddenLayerCount-1)+HiddenNeuronCount];

		for(int i=0;i<InputNeuronCount;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				Connection connection=InputLayer[i].addConnection(HiddenLayers[0][k]);
				Connections[connection.getID()]=connection;
			//	System.out.println("ADDING CONNECTION "+connection.getID());
			}
		}
		for(int i=0;i<HiddenLayerCount-1;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				for(int j=0;j<HiddenNeuronCount;j++) {
					Connection connection=HiddenLayers[i][k].addConnection(HiddenLayers[i+1][j]);
					Connections[connection.getID()]=connection;
			//		System.out.println("ADDING CONNECTION "+connection.getID());
				}
			}
		}
		for(int i=0;i<HiddenNeuronCount;i++) {
			Connection connection=HiddenLayers[HiddenLayerCount-1][i].addConnection(OutNode);
			Connections[connection.getID()]=connection;
			//System.out.println("ADDING CONNECTION "+connection.getID());
		}


		int[] a={	
				1,-1,0,0,0,0,0,1,
				0,1,-1,0,0,0,-1,0,
				0,0,0,-1,0,0,1,0,
				0,1,0,-1,0,-1,0,0,
				0,1,0,-1,0,0,1,0,
				0,1,0,0,0,-1,0,0,
				0,1,-1,1,-1,1,0,0,
				0,0,-1,1,0,0,0,1
		};
		System.out.println(calculate(a));
	}

	public void retrain(double[] weights) {
		for(int i=0;i<Connections.length;i++) {
			Connections[i].setWeight(weights[i]);
		}
	}

	public double calculate(int[] Inputs) {

		//Input -> Hidden
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i].clear();
		}

		//Hidden -> Hidden
		for(int i=0;i<HiddenLayerCount;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				HiddenLayers[i][k].clear();
			}
		}

		OutNode.clear();
		
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i].add(Inputs[i]);
		}
		
		//Input -> Hidden
		for(int i=0;i<InputNeuronCount;i++) {
			InputLayer[i].propagate();
		}

		//Hidden -> Hidden
		for(int i=0;i<HiddenLayerCount;i++) {
			for(int k=0;k<HiddenNeuronCount;k++) {
				HiddenLayers[i][k].propagate();;
			}
		}
		

		return OutNode.getValue();
	}

	public static void main(String[] args) {
		Network network=new Network();
		new DrawingPane(network);
	}
}
