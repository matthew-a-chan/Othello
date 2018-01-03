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
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Stephen C.
 *
 */
public class Population {

	private final boolean elitism=true;
	
	ArrayList<Individual> population;

	public Population() {
		population=new ArrayList<Individual>();
	}


	public void newGen(int genNumber) {
		File genFolder=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"GEN"+genNumber);
		genFolder.mkdir();
		int i=0;
		if(elitism && genNumber>0) {
			i=2;
			
			//Copy best two individuals
			population.add(new Individual(new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+0),0));
			population.add(new Individual(new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+1),1));
		}
		
		
		Collections.sort(population);
		
		for(;i<Playground.populationSize;i++) {
			File newFile=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+i);
			try {
				FileWriter fw=new FileWriter(newFile);
				int d=(int)(Math.random()*Playground.populationSize*(Playground.populationSize-1)/2);
				File parent1=whichIndividual(d).file;
				d=(int)(Math.random()*Playground.populationSize*(Playground.populationSize-1)/2);
				File parent2=whichIndividual(d).file;
				
				String[] parent1Gene = new BufferedReader(new FileReader(parent1)).readLine().split(" ");
				String[] parent2Gene = new BufferedReader(new FileReader(parent2)).readLine().split(" ");
				
				DecimalFormat df = new DecimalFormat("#.###"); 
				df.setRoundingMode(RoundingMode.CEILING);
				
				for(int k=0;k<parent1Gene.length;k++) 
				{
					
					if(Math.random() < Playground.disruptiveMutationRate) 
					{
						//Disruptive mutation = random number from -10 to 10
						
							double randomGene = (double)Math.random() * 10;
							if (Math.random() >= .5)
							{
								randomGene *= -1;
							}
							fw.write(df.format(randomGene));
							fw.write(" ");
						
					}
					else if (Math.random() < Playground.mutationRate) //pick one allele and change it by ±.2
					{
						double modifier = .2 * Math.random();
						if (Math.random() >= .5)
						{
							modifier *= -1;
						}
						
						if (Math.random() >= .5)
						{
							fw.write(Double.parseDouble(parent1Gene[k])+modifier + " ");
						}
						else
						{
							fw.write(Double.parseDouble(parent2Gene[k])+modifier + " ");
						}		
					}
					else 
					{
						//This averages parent 1's and parent 2's alleles at k --- No mutations yet (Probably could just +- Playground.mutationRate*math random)
						fw.write( (Double.parseDouble(parent1Gene[k])+Double.parseDouble(parent2Gene[k])) / 2 +" ");
					}
				}
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(newFile.getAbsolutePath());
			population.add(new Individual(newFile, i));
		}
	}
	
	
	private Individual whichIndividual(int d)
	{
		double upperbound = population.get(0).getFitness();
		double lowerbound = 0;
		
		if (d < upperbound)
		{
			return population.get(0);
		}
		//else...darn
		for (int i = 1; i < Playground.populationSize; i++)
		{
			lowerbound = upperbound;
			upperbound += population.get(i).getFitness();
			
			if (d < upperbound)
			{
				return population.get(i);
			}
		}
		
		return null; //hopefully doesn't get here
	}




}
