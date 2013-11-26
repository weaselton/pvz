/**
 * 
 */
package model;

/**
 * @author Abhinav Thukral
 * ExplosiveZombie creates a zombie which has double the attack power of DefZombie and when the Zombie dies, it also kills the next plant in line
 *
 */
public class ExplosiveZombie extends Zombie {
	// Default Health Factor multiplies with level to increase max health
	private static final int HF = 50; 
	// Default Damage Factor multiplies with level to increase damage 
	private static final int DF = 20;
	// Default Sprite for the Zombie
	private static final String DEFSPRITE = "images/HealthyExplosiveZombie.jpg";
	// Cracked Sprite for the Zombie
	private static final String CRACKEDSPRITE = "images/damagedExplosiveZombie.png";
	/**
	 * @param level
	 */
	public ExplosiveZombie(int level) {
		super((HF * level), level, "ZE", DEFSPRITE, CRACKEDSPRITE);
	}

	/* (non-Javadoc)
	 * @see model.Zombie#attack(model.Actor)
	 */
	@Override
	protected void attack(Actor actor) {
		actor.takeDamage(DF * super.level);
	}

	/** 
	 * Act method for this class
	 * @returns 0 for no movement, 1 for movement and 2 for successful attack
	 */
	@Override
	public int act() {
		int move = super.move();
		if(move == 0){
			Actor actor = tile.getLeft().getOccupant();
			if (actor instanceof Zombie) {
				return 0;
			}
			attack(actor);
			return 2;	
		}
		else{
			return move;
		}
	}
	
	/**
	 * overriding the Actor's take damage to cause an explosion when it dies.
	 */
	public int takeDamage(int damage){
		Tile tile = super.tile;
		super.takeDamage(damage);
		if(!isAlive()){
			explode(tile);
		}
		return super.currHealth;
	}
	
	/**
	 * causes Actor to explode
	 * @param tempTile
	 */
	private void explode(Tile tempTile){
		while(tempTile != null){
			tempTile = tempTile.getLeft();
			if(tempTile != null && tempTile.isOccupied()){
				Actor actor = tempTile.getOccupant();
				if (actor instanceof Plant) {
					actor.takeDamage(1000);
					return;
				}
			}
				
		}
	}
}
