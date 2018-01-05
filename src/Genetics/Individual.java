/**
 * @authors Stephen C. & Jon Wu
 */

package Genetics;

import java.io.File;

import AI.BenchmarkAI;
import AI.OthelloAI;
import game.Player;

/**
 * @author Jon Wu & Stephen Chern
 * 
 * represents an individual (AI) in a population (collection of AIs)
 * contains AI-specific heuristic file
 */
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
	 * Initializes an Individual with an OthelloAI
	 *
	 * @param file
	 */
	public Individual(File f,int ID) {
		file=f;
		player=new OthelloAI(file,this);
		this.ID=ID;
	}
	
	/**
	 * @author Jon Wu & Stephen Chern
	 * 
	 * Initializes an Individual with a BenchmarkAI 
	 * 
	 * "Yes, I already know this is bad coding practice. If I wasn't so lazy I would have done it correctly." -The dev
	 */
	public Individual() {
		player=new BenchmarkAI(this);
	}
	
	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @return This individual's player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @return This individual's fitness
	 */
	public int getFitness() {
		return fitness;
	}
	
	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @param fitness Resets fitness to fitness
	 */
	public void resetFitness(int fitness) {
		this.fitness=fitness;
	}
	
	/**
	 * @author Jon Wu & Stephen Chern
	 *
	 * @return This individual's file
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * @author Jon Wu & Stephen Chern
	 * 
	 * adds 1 to AI's fitness counter if it won the game
	 */
	public void inputGameResult(Individual Other,boolean result) {//True = THIS won, False = Other won
		results[Other.ID]=result;
		if(result)
			fitness++;
	}

	@Override
	public int compareTo(Individual o) {
		if(this.fitness>o.fitness)
			return -1;
		if(this.fitness<o.fitness)
			return 1;
		if(results[o.ID])
			return -1;
		return 0;
	}
	
	
	
	
}
