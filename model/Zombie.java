package model;

/**
 * The Zombie class, an abstract super class for zombies
 * @author Abhinav Thukral
 * @version 1.0
 *
 */

public abstract class Zombie extends Actor {
	protected String crackedSprite;

	public Zombie(int maxHealth, int level, String string, String sprite, String crackedSprite) {
		super(maxHealth, level, string, false, sprite);
		this.crackedSprite = crackedSprite;
	}
	
	
	/**
	 * Attacks the Actor object passed causing damage
	 * @param actor
	 */
	protected abstract void attack(Actor actor);
	
	/**
	 * move() method moves the zombie to the left in the grid
	 */
	 protected int move(LevelData grid) {
		//zombies walk to the left
		 if(grid.inBounds(x-1, y)){
			if(grid.plantAt(this.x - 1, this.y)){
				return 0;
			}
			else {
				//grid.getActorsAt(this.x, this.y).remove(this);
				this.x = this.x -1;
				//grid.getActorsAt(this.x, this.y).add(this);
				return 1;
			}
		 }
		 else{
			 return -1;
		 }
	}
	/**
	 * @returns a cracked sprite if the health has fallen below 40% of max health
	 */
	 public String getSprite(){
		 if(super.isCracked()){
			 return crackedSprite;
		 }
		 else{
			 return super.sprite;
		 }
		 
	 }
	 
	 public Object clone()throws CloneNotSupportedException{
		 Zombie clone = (Zombie)super.clone();
		 clone.crackedSprite = this.crackedSprite;
		 return clone;
	 }
	
}