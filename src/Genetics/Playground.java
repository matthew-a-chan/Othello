package Genetics;

import java.io.File;
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
	
	
	
	static final int populationSize=10;
	static final double mutationRate=0.1;
	static final double disruptiveMutationRate=.05;
	public static final int ply=5;
	int genNumber=0;
	
	Population currentPop;
	Population lastPop;
	
	
	
	private int gamesComplete=0;
	
	public static void main(String[] args) {
		new Playground();
	}
	
	Playground(){
		start();
	}
	
	public void start() {
		File data=new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+"AI"+File.separator+"DATA");
		
		runGame();
		
		//newGen();
	}
	
	public void runGame() {
		GameState state = new Othello();//WOULD NEED TO MAKE ANOTHER ONE OF THESE FOR EACH GAME
		
		for(int i=0;i<2;i++){
			Individual ind=new Individual(new File(System.getProperty("user.home")+File.separator+"Desktop"+File.separator+
					"AI"+File.separator+"GEN"+genNumber+File.separator+"GEN"+genNumber+"-IND"+i),i);//REPLACE THIS WITH OTHELLO AI.NEW INSTANCE()
			OthelloAI play=(OthelloAI) ind.getPlayer();
			play.setName("PLAYER"+i);
			state.addPlayer(play);
		}
		state.start();//THEN FINALLY START IT -- IT WILL RETURN INFO UPON COMPLETION
	}		
	
	public static void gameComplete(Player player1, Player player2, boolean winner) { //True = player1 Wins, False = player2 Wins
		((OthelloAI)player1).getIndividual().inputGameResult(((OthelloAI)player2).getIndividual(),winner);
		((OthelloAI)player2).getIndividual().inputGameResult(((OthelloAI)player1).getIndividual(),!winner);
	}
	
	
	public void newGen() {
		lastPop=currentPop;
		currentPop=new Population();
		currentPop.newGen(genNumber);

		
		genNumber++;
	}
}
