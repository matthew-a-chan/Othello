/**
 * @author Stephen C.
 */
package Genetics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Stephen C.
 *
 */
public class Population {
	
	final int populationSize=10;
	final double mutationRate=0.09;
	int genNumber=0;
	
	Individual[] population;
	
	public static void main(String[] args) {
		new Population();
	}
	
	public Population() {
		
		System.out.println(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"DATA");
		File data=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"DATA");
		
		newGen();
		
		newPopulation();
	}
	
	
	
	public void newGen() {
		population=new Individual[populationSize];
		File genFolder=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"GEN"+genNumber);
		genFolder.mkdir();
		for(int i=0;i<populationSize;i++) {
			File a=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+i);
			FileWriter aw;
			DecimalFormat df = new DecimalFormat("#.###"); 
			df.setRoundingMode(RoundingMode.CEILING); 
			try {
				aw = new FileWriter(a);
				for (int j = 0; j < 2700; j++) //NOTE: this will print a space at the very end of the file
				{
					double random = (double)Math.random() * 10;
					if (Math.random() >= .5)
					{
						random *= -1;
					}
					aw.write(df.format(random));
					aw.write(" ");
				}
				aw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(a.getAbsolutePath());
			population[i]=new Individual(a);
		}
		
		genNumber++;
	}
	
	public void newPopulation()
	{
		Individual[] nextPopulation = new Individual[populationSize];
		double total = 0;
		
		for (int i = 0; i < 10; i++)
		{
			total += population[i].calculateFitness();
		}
		
		population = sort(population);
		
		//best two always get in
		nextPopulation[0] = population[0];
		nextPopulation[1] = population[1];
		
		Individual parent1;
		Individual parent2;
		for (int i = 0; i < 8; i++)
		{
			double rng = (double)Math.random() * total;
			parent1 = whichIndividual(rng);
			rng = (double)Math.random() * total;
			parent2 = whichIndividual(rng);
			
			//crossover
		}
		
	}
	
	private Individual[] sort(Individual[] i) //bubble sort --> make more efficient if you want
	{
		Individual[] sortedPopulation = i;
		for (int j = 0; j < i.length; j++)
		{
			for (int k = 1; k < i.length; k++)
			{
				if (sortedPopulation[k].compareTo(sortedPopulation[k-1]) == 1)
				{
					sortedPopulation[k-1] = i[k];
					sortedPopulation[k] = i[k-1];
					i = sortedPopulation;
				}
			}
		}
		return sortedPopulation;
	}
	
	private Individual whichIndividual(Double d)
	{
		double upperbound = population[0].fitness;
		double lowerbound = 0;
		
		if (d < upperbound)
		{
			return population[0];
		}
		//else...darn
		
		for (int i = 1; i < populationSize; i++)
		{
			lowerbound = upperbound;
			upperbound += population[i].fitness;
			
			if (lowerbound < d && d < upperbound)
			{
				return population[i];
			}
		}
		
		return null; //hopefully doesn't get here
	}
	
	
}
