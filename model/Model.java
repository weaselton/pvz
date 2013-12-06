package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Observable;

public class Model extends Observable {
	public static final int MAX_ROWS = 6;
	public static final int MAX_COLS = 12;
	private PlayerData currPlayer;
	private LevelData currLevel;

	/**
	 * 
	 * @param level determines the game level and difficulty of the game generated. only 1 level is implemented currently
	 */
	public Model(int level){
		currPlayer = new PlayerData(level);
		currLevel = new LevelData(level);		
		this.setChanged();
	}
	

	/**
	 * simulates the game system, updates the model. Every moving part moves.
	 */
	public void update(){	
		Random generator = new Random();
		
		for(Actor a: currLevel.getActorList()){	
			if(a.isAlive()){
				if(a.act(currLevel) == 5){			//sunflowers will have act(){return 5} unless anyone can think of a better way to do this?
					currPlayer.solarPower+= 5;
				}
			}
		}
		if(generator.nextInt(100) > 50){
			addZombie();
		}
		this.setChanged();
		notifyObservers();
	}

	
	/**
	 * Moves a zombie from the waiting area onto the map. If there is no place to put a zombie, the zombie is returned.
	 * @return True if a zombie was added, false otherwise
	 */
	private boolean addZombie(){				
		if(currLevel.getWaitingZombiesList().size() > 0){
			int endOfList = currLevel.getWaitingZombiesList().size() - 1;
			Actor newZombie = currLevel.getWaitingZombiesList().get(endOfList);
			currLevel.getWaitingZombiesList().remove(newZombie);
			Random generator = new Random();
			int y;
			int tries = 0;
			y = generator.nextInt(MAX_ROWS);
			ArrayList<Actor> destination = LevelData.getGrid()[y][MAX_COLS];
			while(tries < 5 && destination != null){			//if the spot is occupied, choose another
				if(!currLevel.actorAt(MAX_COLS, y)){
					return (currLevel.addActor(newZombie, MAX_COLS, y));
				}
				y = (y + 1) % MAX_COLS;
				tries++;
			}
			currLevel.getWaitingZombiesList().add(newZombie);		//zombie goes back in line
		}
		return false;						 	//all rows are blocked
	}

	
	/**
	 * Attempts to purchase a plant. Decreases solar reserves by the cost of the plant if successful.
	 * @param type the kind of plant to be purchased
	 * @return The newly purchased plant, or null if it is unaffordable
	 */
	private Plant purchasePlant(String type){
		Plant plant = currPlayer.getPlant(type);
		if(plant != null){
			currPlayer.solarPower -= plant.getCost();
			return plant;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Puts a plant on the map, given a co-ordinate pair, and the name of a plant type.
	 * @param x destination co-ordinate for the plant
	 * @param y destination co-ordinate for the plant
	 * @param type the type of plant to be placed
	 * @return True if the plant was placed, false otherwise;
	 */
	public boolean placePlant(int x, int y, String type){
		//TODO check bounds of x y
			if(!currLevel.plantAt(x, y)){
				Actor newPlant = purchasePlant(type);			//this decreases your solarPower. we should split it into createPlant() and payForPlant() methods. 
				if (newPlant != null) {							//otherwise there will be times where we will want to refund the player if they screw up.
					return(currLevel.addActor(newPlant, x, y));
				}
			}
		return false;		
	}
	
	/**
	 * Checks the game state for a win or a loss.
	 * Win if there are no zombies on the field, and no zombies waiting.
	 * Loss if there is a zombie in the first column.
	 * @return -1 if the player lost, 1 if they won, 0 otherwise
	 */
	public int state(){
		for(int y = 0; y <= MAX_ROWS; y++){
			if(currLevel.zombieAt(0, y)){ 
				return -1; 								//game loss if there is a zombie in the first column
			}
		}
		if(currLevel.getWaitingZombiesList().isEmpty()){
			for(Actor a: currLevel.getActorList()){
				if(!a.isFriendly() && a.isAlive()){								
					return 0;
				}
			}
			return 1;										//game win if there are no zombies on the field, and no zombies waiting
		}
		return 0;
	}
	
	/**
	 * Primitive display method. A view system will be responsible for all of this in later versions.
	 *//*
	public void printGrid(){
		for (int y = 0; y < MAX_ROWS; y++){
			Tile tempTile = gameGrid.get(y);
			while(tempTile != null){
				System.out.print(tempTile.toString());
				tempTile = tempTile.getRight();
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	*/
	public ArrayList<Actor> getZombies(){
		
		return this.currLevel.getWaitingZombiesList();
	}


	//TODO reaching through is... bad
	public ArrayList<Actor>[][] getGameGrid() {
		return currLevel.getGrid();
	}


	public int getSolarPower() {
		return currPlayer.solarPower;
	}


	public int getSolarRate() {
		return currPlayer.solarRate;
	}


//	public int getLevel() {
//		return currLevel.level;
//	}
//
//
//	public ArrayList<Actor> getActorList() {
//		return currLevel.actorList;
//	}
//
//
//	public void setActorList(ArrayList<Actor> actorList) {
//		this.currLevel.actorList = actorList;
//	}
//
//
//	public ArrayList<Actor> getWaitingZombiesList() {
//		return currLevel.waitingZombiesList;
//	}
//
//
//	public void setWaitingZombiesList(ArrayList<Actor> waitingZombiesList) {
//		this.currLevel.waitingZombiesList = waitingZombiesList;
//	}
	
	/**
	 * Returns the choice
	 * @return String - the string representation of the choice
	 */
	public String getChoice()
	{
		return currPlayer.getChoice();
	}
	
	/**
	 *  Sets the choice 
	 * @param choosen -the choice the user has choosen
	 */
	public void setChoice(String choosen)
	{
		this.setChanged();
		currPlayer.setChoice(choosen);
		notifyObservers();
	}
}