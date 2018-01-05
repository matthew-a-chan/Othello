package Genetics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * used to specifically create the 0th generation's heuristics
 */
public class Gen0Init {
	public static void main(String[] args) {

		for(int i=0;i<Playground.populationSize;i++){
			File newFile=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN0"+File.separator+"GEN0"+"-IND"+i+".txt");

			try {
				FileWriter fw = new FileWriter(newFile);
				
				for(int k=0;k<2730;k++) {

					fw.write(Math.random()-1/2.0+" ");

				}
				fw.flush();
				fw.close();

			}
			catch(Exception e) {}
		}
		
	}
	
}