package AI;
import java.io.File;
import java.util.List;

import Genetics.Individual;
import cabinet.GameState;
import cabinet.Plugin;
import cabinet.PluginInfo;
import game.*;

public class OthelloAI extends Player{
	
	File f;
	Individual i=null;
	String name="DaOthelloAI";

	MinMaxShrubbery MMtree;
	
	public static PluginInfo getInfo() {
		PluginInfo pi = new PluginInfo() {

			@Override
			public String name() {
				return "OthelloAI";
			}

			@Override
			public String description() {
				return "An AI for Othello";
			}

			@Override
			public Class<? extends Plugin> type() {
				return Player.class;
			}

			@Override
			public List<Class<? extends GameState>> supportedGames() {
				return null;
			}
			
		};
		return pi;
	}
	
	public OthelloAI() {
		MMtree=new MinMaxShrubbery(this);
	}

	public OthelloAI(File file) {
		MMtree=new MinMaxShrubbery(file,(Player)this);
	}
	
	public OthelloAI(File file,Individual indiv) {
		this(file);
		this.i=indiv;
	}
	
	public Individual getIndividual() {
		return i;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public void makeMove(GameState gs,List<Move> m){
		if(gs.getValidMoves().size()==0) {
			return;
		}
		Move move=null;
		m.add(move=new Move());
		MMtree.getMove(gs,move);
		
		//printGS(gs);
		
		if(move.to.x<0||move.to.y<0) {
			move.to.x=gs.getValidMoves().get(0).to.x;
			move.to.y=gs.getValidMoves().get(0).to.y;
			//System.err.println("COULDNT SEARCH:: RANDOM MOVE--"+gs.isGameOver());
		}

	}
	
	private void printGS(GameState gs) {
		System.err.println("TEAM:"+gs.getPlayers()[0].contains(this));
		System.out.println("MOVES AVAILABLE::"+gs.getValidMoves().size());
		Location location=new Location();
		for(int r=0;r<8;r++) {
			for(int c=0;c<8;c++) {
				location.x=c;
				location.y=r;
				if(gs.getOwner(location).isEmpty()) {//If unowned
					System.out.print(0);
				}
				else if(gs.getOwner(location).get(0).get(0)==gs.getPlayers()[0].get(0)) {//If owned by AI -- happy
					System.out.print("Y");
				}
				else {//If owned by non-AI -- sad
					System.out.print("N");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
}