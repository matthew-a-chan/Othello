package Genetics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import game.Player;
import othello.Othello;
import cabinet.GameState;


public class Playground {


	//Population
	static final int populationSize=100;
	static final int matchesPlayed=10;
	static final double AICutoff=0.50;

	//Mutation
	static final double mutationRate=0.01;
	static final double mutationAmount=0.06;
	static final double disruptiveMutationRate=.0005;
	static final double range=1.0;
	static final double regularization=0.99;

	//Evolution
	static final int elitism=5;

	private final int refresh=40;

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
			while(gamesComplete<populationSize*matchesPlayed-1) {try {Thread.sleep(refresh);} catch (InterruptedException e) {}}
			if(genNumber%3==0) {
				System.out.println("BENCH TEST");
				benchTest();
			}
			System.out.println("NEW GEN INCOMING");
			newGen();
		}

	}

	public void Match() {
		for(int i=0;i<populationSize;i++) {
			int startGamesComplete=gamesComplete;
			for(int k=1;k<=matchesPlayed;k+=1) { //---------------------------------------------------------
				runGame(currentPop.population.get(i),currentPop.population.get((i+k*5)%populationSize));
			}
			while(gamesComplete<startGamesComplete+matchesPlayed) 
			{try {Thread.sleep(refresh);} catch (InterruptedException e) {}}
			System.out.println("FITNESS "+currentPop.population.get(i).getPlayer().getName()+":"+currentPop.population.get(i).getFitness());
		}
	}

	public void benchTest() {
		gamesComplete=0;
		Collections.sort(currentPop.population);
		for(int i=0;i<50;i++) {
			runGame(currentPop.population.get(0),new Individual());
			runGame(new Individual(),currentPop.population.get(0));
		}
		while(gamesComplete<99)
		{try {Thread.sleep(10);} catch (InterruptedException e) {}}
		FileWriter fr;
		try {
			fr = new FileWriter(new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"BenchTest"+genNumber+".txt"));
			fr.write(currentPop.population.get(0).getFitness()+"");
			fr.flush();
			fr.close();
		} catch (IOException e) {e.printStackTrace();}
		currentPop.population.get(0).resetFitness();

	}

	public void runGame(Individual i1,Individual i2) {
		GameState state = new Othello();//WOULD NEED TO MAKE ANOTHER ONE OF THESE FOR EACH GAME

		Player player1=i1.getPlayer();
		state.addPlayer(player1);

		Player player2=i2.getPlayer();
		state.addPlayer(player2);

		state.start();//THEN FINALLY START IT -- IT WILL RETURN INFO UPON COMPLETION
	}		

	public static void gameComplete(Player player1, Player player2, boolean winner) { //True = player1 Wins, False = player2 Wins
		player1.getIndividual().inputGameResult(player2.getIndividual(),winner);
		player2.getIndividual().inputGameResult(player1.getIndividual(),!winner);
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
