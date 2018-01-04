package Genetics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import game.Player;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import othello.Othello;
import othello.OthelloGUI;
import AI.OthelloAI;
import cabinet.GameDisplay;
import cabinet.GameState;
import cabinet.PluginInfo;
import cabinet.PluginManager;

public class Playground {


	//Population
	static final int populationSize=100;
	static final int matchesPlayed=10;
	
	//Mutation
	static final double mutationRate=0.01;
	static final double mutationAmount=0.06;
	static final double disruptiveMutationRate=.0005;
	static final double range=1.0;
	static final double regularization=0.99;
	
	//Evolution
	static final int elitism=5;


	public static final int ply=4;
	int genNumber=0;

	Population currentPop;



	private static int gamesComplete=0;

	public static void main(String[] args) {
		new Playground();
	}

	Playground(){
		start();
	}

	public void start() {
		File data=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"DATA.txt");

		try {
		BufferedReader a=new BufferedReader(new FileReader(data));
		genNumber=Integer.parseInt(a.readLine().split(" ")[0]);
		a.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		currentPop=new Population(genNumber);

		for(int i=0;i<10;i++) {
			Match();
			while(gamesComplete<populationSize*matchesPlayed-1) {try {Thread.sleep(10);} catch (InterruptedException e) {}}
			System.out.println("NEW GEN INCOMING");
			newGen();
		}

	}

	public void Match() {
		for(int i=0;i<populationSize;i++) {
			int startGamesComplete=gamesComplete;
			for(int k=1;k<=matchesPlayed;k++) {
				runGame(currentPop.population.get(i),currentPop.population.get((i+k)%populationSize));
			}
			while(gamesComplete<startGamesComplete+matchesPlayed) 
				{try {Thread.sleep(10);} catch (InterruptedException e) {}}
			System.out.println("FITNESS "+currentPop.population.get(i).getPlayer().getName()+":"+currentPop.population.get(i).getFitness());
		}
	}

	public void runGame(Individual i1,Individual i2) {
		GameState state = new Othello();//WOULD NEED TO MAKE ANOTHER ONE OF THESE FOR EACH GAME

		OthelloAI player1=(OthelloAI) i1.getPlayer();
		state.addPlayer(player1);

		OthelloAI player2=(OthelloAI) i2.getPlayer();
		state.addPlayer(player2);

		state.start();//THEN FINALLY START IT -- IT WILL RETURN INFO UPON COMPLETION
	}		

	public static void gameComplete(Player player1, Player player2, boolean winner) { //True = player1 Wins, False = player2 Wins
		((OthelloAI)player1).getIndividual().inputGameResult(((OthelloAI)player2).getIndividual(),winner);
		((OthelloAI)player2).getIndividual().inputGameResult(((OthelloAI)player1).getIndividual(),!winner);
		if(winner) {
			//System.out.println(player1.getName());
		}
		else {
			//System.out.println(player2.getName());
		}
		//System.out.println(gamesComplete);
		gamesComplete++;
	}


	public void newGen() {
		genNumber++;
		gamesComplete=0;

		if(currentPop==null) {
			currentPop=new Population(genNumber-1);
		}

		currentPop.newGen();
		currentPop=new Population(genNumber);
		
		File data=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"DATA.txt");
		FileWriter fr;
		try {
			fr = new FileWriter(data);
			fr.write(genNumber+"");
			fr.flush();
			fr.close();} 
		catch (IOException e) {e.printStackTrace();}


	}
}
