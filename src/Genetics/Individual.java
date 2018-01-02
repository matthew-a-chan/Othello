/**
 * @authors Stephen C. & Jon Wu
 */

package Genetics;

import java.io.File;

public class Individual implements Comparable<Individual>{
	
	double fitness;
	File file;
	
	
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
	public Individual(File f) {
		file=f;
	}
	
	public File getFile() {
		return file;
	}


	@Override
	public int compareTo(Individual o) {
		if(this.fitness>o.fitness)
			return 1;
		if(this.fitness<o.fitness)
			return -1;
		return 0;
	}
	
	
	
	
}
