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
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+i+".txt"),i);
		}
	}

	private void addIndividual(File f,int i) {
		population.add(new Individual(f,i));
		population.get(i).getPlayer().setName(""+i);
	}

	private void makeChild(File p1,File p2,int ID) {

		File newFile=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
				"AI"+File.separator+"GEN"+(genNumber+1)+File.separator+"GEN"+(genNumber+1)+"-IND"+ID+".txt");

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

						double randomGene = (double)Math.random() * Playground.range;
						if (Math.random() >= .5)
						{
							randomGene *= -1;
						}
						fw.write(df.format(randomGene));
						fw.write(" ");

					}
					else if (Math.random() < Playground.mutationRate) //pick one allele and change it by +-Playground.mutationAmount
					{
						double modifier = Playground.mutationAmount * Math.random();
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
						double modifier = Playground.mutationAmount * Math.random();
						if (Math.random() >= .5)
						{
							modifier *= -1;
						}
						
						fw.write( (Double.parseDouble(parent1Gene[k])+Double.parseDouble(parent2Gene[k])) / 2 * Playground.regularization + modifier +" ");
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
		for(int i=0;i<Playground.populationSize;i++) {
			System.out.print(population.get(i).getFitness()+" : ");
		}
		System.out.println();
		for(int i=0;i<Playground.populationSize;i++) {
			System.out.print(population.get(i).getPlayer().getName()+" : ");
		}
		System.out.println();
		
		for(int i=0;i<Playground.populationSize;i++) {
			System.out.println(population.get(i).getPlayer().getName()+":::"+population.get(i).getFitness());
		}


		int i=0;
		for(;i<Playground.elitism;i++) {
			//Copy best individuals
			makeChild(new File(""),population.get(i).file,i);
		}



		for(;i<Playground.populationSize;i++) {
			File parent1=whichIndividual().file;
			File parent2=whichIndividual().file;

			makeChild(parent1,parent2,i);
		}
	}


	private Individual whichIndividual()
	{
		int d=(int)(Math.random()*Playground.populationSize*Playground.matchesPlayed);

		double upperbound = population.get(0).getFitness();

		if (d < upperbound)
		{
			return population.get(0);
		}
		//else...darn
		for (int i = 1; i < Playground.populationSize; i++)
		{
			upperbound += population.get(i%((int)(Playground.populationSize*Playground.AICutoff))).getFitness();

			if (d < upperbound)
			{
				return population.get(i%((int)(Playground.populationSize*Playground.AICutoff)));
			}
		}

		System.err.println("REALLY BAD THINGS ARE HAPPENING");
		System.err.println("In Individual.whichIndividual() -- PLZ FIX");

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
