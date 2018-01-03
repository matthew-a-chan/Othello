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

/**
 * @author Stephen C.
 *
 */
public class Population {


	Individual[] population;

	public Population(int popSize) {
		population=new Individual[popSize];

	}


	public void newGen(int populationSize,int genNumber) {
		File genFolder=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"GEN"+genNumber);
		genFolder.mkdir();
		for(int i=0;i<populationSize;i++) {
			File a=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+i);
			FileWriter aw;
			try {
				aw = new FileWriter(a);
				aw.write("HELLO");
				aw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(a.getAbsolutePath());
			population[i]=new Individual(a);
		}
	}




}
