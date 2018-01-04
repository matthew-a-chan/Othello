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


	ArrayList<Individual> population;
	int genNumber;

	public Population(int currentGen) {

		population=new ArrayList<Individual>();
		this.genNumber=currentGen;
		init();
	}

	private void init() {
		for(int i=0;i<Playground.populationSize;i++) {
			addIndividual(new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+i),i);
		}
	}

	private void addIndividual(File f,int i) {
		population.add(new Individual(f,i));
		population.get(i).getPlayer().setName(""+i);
	}

	private void makeChild(File p1,File p2,int ID) {

		File newFile=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
				"AI"+File.separator+"GEN"+(genNumber+1)+File.separator+"GEN"+(genNumber+1)+"-IND"+ID);

		try {
			FileWriter fw = new FileWriter(newFile);

			if(p1.getAbsolutePath().equals(new File("").getAbsolutePath())) {

				System.out.println("ELITISM");
				BufferedReader a=new BufferedReader(new FileReader(p2));
				fw.write(a.readLine());
				a.close();

				fw.flush();

			}
			else {

				String[] parent1Gene;
				String[] parent2Gene;

				BufferedReader a1=new BufferedReader(new FileReader(p1));
				BufferedReader a2=new BufferedReader(new FileReader(p2));


				parent1Gene = a1.readLine().split(" ");
				parent2Gene = a2.readLine().split(" ");

				a1.close();
				a2.close();

				for(int k=0;k<2730;k++) {//parent1Gene.length;k++) {

					DecimalFormat df = new DecimalFormat("#.###"); 
					df.setRoundingMode(RoundingMode.CEILING);

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
			}
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newGen() {
		File genFolder=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"GEN"+(genNumber+1));
		genFolder.mkdir();

		Collections.sort(population);

		int i=0;
		for(;i<Playground.elitism;i++) {
			//Copy best individuals
			makeChild(new File(""),population.get(i).file,i);
		}



		for(;i<Playground.populationSize;i++) {
			int d=(int)(Math.random()*Playground.populationSize*(Playground.populationSize-1)/2);
			File parent1=whichIndividual(d).file;
			d=(int)(Math.random()*Playground.populationSize*(Playground.populationSize-1)/2);
			File parent2=whichIndividual(d).file;

			makeChild(parent1,parent2,i);
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

		System.err.println("REALLY BAD THINGS ARE HAPPENING");
		System.err.println("IN Individual.whichIndividual() -- PLZ FIX");

		return population.get(0); //hopefully doesn't get here
	}


	/*  INIT CODE-- plz no mess with :D
	 * 		System.out.println("WHY");
		int i=0;

		FileWriter fw=null;

		try {
			File newFile=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+0+File.separator+"GEN"+0+"-IND"+i);
			System.out.println(newFile.getAbsolutePath());

			fw = new FileWriter(newFile);

			for(int k=0;k<2730;k++) {

				fw.write(Math.random()-1/2.0+" ");

			}

			fw.flush();
			fw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("K");

		for(i=1;i<1000;i++) {

			File newFile=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+0+File.separator+"GEN"+0+"-IND"+i);

			File f1=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN0"+File.separator+"GEN0-IND0");

			try {
				fw = new FileWriter(newFile);

				BufferedReader a=new BufferedReader(new FileReader(f1));
				System.out.println(fw);

				fw.write(a.readLine());
				a.close();
				fw.flush();
				fw.close();
			}
			catch(Exception e) {e.printStackTrace();}
		}


		System.exit(0);
	 */

}
