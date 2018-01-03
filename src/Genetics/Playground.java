package Genetics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import game.Player;
import AI.OthelloAI;

public class Playground {
	
	final int populationSize=10;
	final double mutationRate=0.09;
	int genNumber=0;
	
	Population currentPop;
	Population lastPop;
	
	
	
	private int gamesComplete=0;
	
	public void start() {
		File data=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"DATA");
		
		newGen();
	}
	
	public void gameComplete(Player player1, Player player2, boolean winner) { //True = player1 Wins, False = player2 Wins
		((OthelloAI)player1).getIndividual().inputGameResult(((OthelloAI)player2).getIndividual(),winner);
		((OthelloAI)player2).getIndividual().inputGameResult(((OthelloAI)player1).getIndividual(),!winner);
	}
	
	
	public void newGen() {
		lastPop=currentPop;
		currentPop=new Population(populationSize);
		currentPop.newGen(populationSize, genNumber);

		
		genNumber++;
	}
}
