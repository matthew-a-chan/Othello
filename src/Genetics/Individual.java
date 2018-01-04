/**
 * @authors Stephen C. & Jon Wu
 */

package Genetics;

import java.io.File;

import AI.OthelloAI;
import game.Player;

public class Individual implements Comparable<Individual>{
	
	private int fitness=0;
	boolean[] results=new boolean[Playground.populationSize];
	File file;
	
	protected Player player;
	
	private int ID=0;
	
	
	//Populations have individuals in them
	//Individuals each have ONE AI in them
	//AI's have one heuristic each
	//Each Heuristic has ONE Neural network trained for that AI (so one per)
	
	//Genetic Algorithm
	//AI
	//Heuristic
	//Making da geme werk
	
	//new comment!


	/**
	 * @authors Stephen C. & Jon Wu
	 *
	 * @param file
	 */
	public Individual(File f,int ID) {
		file=f;
		player=new OthelloAI(file,this);
		this.ID=ID;
	}
	
	public Individual() {}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public void resetFitness() {
		fitness=0;
	}
	
	public File getFile() {
		return file;
	}
	
	public void inputGameResult(Individual Other,boolean result) {//True = THIS won, False = Other won
		results[Other.ID]=result;
		if(result)
			fitness++;
	}

	@Override
	public int compareTo(Individual o) {
		if(this.fitness>o.fitness)
			return 1;
		if(this.fitness<o.fitness)
			return -1;
		if(results[o.ID])
			return 1;
		return 0;
	}
	
	
	
	
}
