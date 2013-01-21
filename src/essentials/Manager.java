package essentials;

import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

import village.Cabin;
import village.Road;
import village.VillageGenerator;
import weapons.*;

import entity.Human;
import entity.Player;
import entity.Zombie;

public class Manager {
	private Player man;
	private Human[] human = new Human[100];
	private Zombie[] zombie = new Zombie[100];
	private Bullet[] bullet = new Bullet[100];
	private Gun[] gun = new Gun[100];
	private Knife[] knife = new Knife[100];
	private TimeKeeper timekeeper = new TimeKeeper();
	private VillageGenerator villagegen = new VillageGenerator();
	private int humanNumber = 0;
	private int zombieNumber = 0;
	private int bulletNumber = 0;
	private int gunNumber = 0;
	private int knifeNumber = 0;
	private Random rand = new Random();
	private Benchmarker benchmarker;
	private boolean benchmark = false;
	public void loop(float mouseX, float mouseY){
		if(man == null){
			createNew(ID.PLAYER, -16, -16);
			populateVillage();
			boolean makeGun = rand.nextBoolean();
			if(makeGun)
				createNew(ID.STATICBULLET, rand.nextInt(500) - 250, rand.nextInt(500) - 250);
			if(makeGun)
				createNew(ID.STATICGUN, rand.nextInt(500) - 250, rand.nextInt(500) - 250);
			if(!makeGun)
				createNew(ID.STATICKNIFE, rand.nextInt(500) - 250, rand.nextInt(500) - 250);
			benchmarker = new Benchmarker();
			timekeeper.start();
		}
		if(timekeeper.timeDifference() > 16000){
			makeZombie();
			timekeeper.start();
		}
		zombieLogic();
		humanLogic();
		bulletLogic();
		gunLogic(mouseX, mouseY);
		knifeLogic(mouseX, mouseY);
		detectPlayerCollisions();
		if(benchmark){
			System.out.println(benchmarker.getElapsedTime());
			System.out.println(benchmarker.getMemoryUsage());
			benchmarker.reset();
		}
	}
	public void render(){
		villagegen.loop(man.x, man.y);
		for(int i = 0; i < zombieNumber; i++)
			zombie[i].draw();
		for(int i = 0; i < humanNumber; i++)
			human[i].draw();
		man.draw();
		for(int i = 0; i < bulletNumber; i++)
			if(!bullet[i].done)
				bullet[i].draw();
		for(int i = 0; i < gunNumber; i++)
			gun[i].draw();
		for(int i = 0; i < knifeNumber; i++)
			knife[i].draw();
	}
	public void movePlayer(int direction){
		if(man.dead == false){
			man.oldX = man.x;
			man.oldY = man.y;
			if(direction == Directions.UP)
				man.y -= man.SPEED;
			if(direction == Directions.RIGHT)
				man.x += man.SPEED;
			if(direction == Directions.DOWN)
				man.y += man.SPEED;
			if(direction == Directions.LEFT)
				man.x -= man.SPEED;
			detectPlayerCollisions();
		}
	}
	private void zombieLogic(){
		for(int i = 0; i < zombieNumber; i++){
			if(!zombie[i].dead){
				moveZombie(i);
				for(int u = 0; u < zombieNumber; u++){
					if(i == u){
						u++;
						if(u == zombieNumber)
							break;
					}
					if(!zombie[u].dead)
						if(detectCollision(zombie[i].getRectangle(), zombie[u].getRectangle())){
							zombie[i].returnToPrevious();
							zombie[i].timekeeper.start();
							zombie[i].reset = true;
							zombie[i].direction = rand.nextInt(8);
						}
				}
				for(int u = 0; u < humanNumber; u++){
					if(!human[u].dead && human[u].show){
						human[u].zombieNear = isZombieNear(u);
						if(detectCollision(human[u].getRectangle(), zombie[i].getRectangle())){
							human[u].show = false;
							for(int y = 0; y < knifeNumber; y++)
								if(knife[y].equipped && knife[y].equippedBy == ID.HUMAN)
									if(knife[y].equippedNumber == u)
										knife[y].reset();
							createNew(ID.ZOMBIE, human[u].x, human[u].y);
						}
					}
				}
				for(int y = 0; y < villagegen.cabinNumber; y++){
					if(detectCollision(zombie[i].getRectangle(), villagegen.cabin[y].getRectangle())){
						zombie[i].returnToPrevious();
						zombie[i].timekeeper.start();
						zombie[i].reset = true;
						zombie[i].direction = rand.nextInt(8);
					}
				}
			}
		}
	}
	private void humanLogic(){
		for(int i = 0; i < humanNumber; i++){
			if(!human[i].dead){
				human[i].zombieNear = isZombieNear(i);
				human[i].gunNear = isGunNear(i);
				human[i].knifeNear = isKnifeNear(i);
				human[i].bulletNear = isBulletNear(i);
				if(human[i].zombieNear)
					zombieHumanMovement(i, zombie[human[i].nearestZombie].x, zombie[human[i].nearestZombie].y);
				if(human[i].gunNear)
					zombieHumanMovement(i, gun[human[i].nearestGun].x, gun[human[i].nearestGun].y);
				if(human[i].knifeNear)
					zombieHumanMovement(i, knife[human[i].nearestKnife].x, knife[human[i].nearestKnife].y);
				if(human[i].bulletNear && human[i].hasGun)
					zombieHumanMovement(i, bullet[human[i].nearestBullet].x, bullet[human[i].nearestBullet].y);
				human[i].move();
				for(int y = 0; y < villagegen.cabinNumber; y++){
					if(detectCollision(villagegen.cabin[y].getRectangle(), human[i].getRectangle()))
						human[i].returnToPrevious();
				}
				if(detectCollision(human[i].getRectangle(), man.getRectangle())){
					human[i].returnToPrevious();
					human[i].direction = rand.nextInt(4);
				}
				for(int u = 0; u < humanNumber; u++){
					if(i == u){
						u++;
						if(u == humanNumber)
							break;
					}
					if(detectCollision(human[i].getRectangle(), human[u].getRectangle())){
						human[i].returnToPrevious();
						human[i].direction = rand.nextInt(4);
					}
				}
			}
		}
	}
	private void bulletLogic(){
		for(int i = 0; i < bulletNumber; i++){
			if(!bullet[i].done){
				bullet[i].move();
				if(bullet[i].moving){
					for(int u = 0; u < zombieNumber; u++)
						if(!zombie[u].dead)
							if(detectCollision(zombie[u].getRectangle(), bullet[i].getRectangle())){
								zombie[u].dead = true;
								bullet[i].done = true;
								for(int y = 0; y < rand.nextInt(3); y++)
									createNew(ID.STATICBULLET, zombie[u].x + rand.nextInt(10) - 5, zombie[u].y + rand.nextInt(10) - 5);
								int rareDrop = rand.nextInt(300);
								if(rareDrop == 0)
									createNew(ID.STATICGUN, zombie[u].x + rand.nextInt(10) - 5, zombie[u].y + rand.nextInt(10) - 5);
								if(rareDrop == 1)
									createNew(ID.STATICKNIFE, zombie[u].x + rand.nextInt(10) - 5, zombie[u].y + rand.nextInt(10) - 5);
							}
					for(int u = 0; u < humanNumber; u++)
						if(!human[u].dead && human[u].show)
							if(detectCollision(human[u].getRectangle(), bullet[i].getRectangle())){
								human[u].dead = true;
								System.out.println("human dies via bullet");
								bullet[i].done = true;
								for(int y = 0; y < rand.nextInt(3); y++)
									createNew(ID.STATICBULLET, human[u].x + rand.nextInt(10) - 5, human[u].y + rand.nextInt(10) - 5);
								int rareDrop = rand.nextInt(300);
								if(rareDrop == 0)
									createNew(ID.STATICGUN, human[u].x + rand.nextInt(10) - 5, human[u].y + rand.nextInt(10) - 5);
								if(rareDrop == 1)
									createNew(ID.STATICKNIFE, human[u].x + rand.nextInt(10) - 5, human[u].y + rand.nextInt(10) - 5);
							}
				}
				if(!bullet[i].moving){
					if(detectCollision(man.getRectangle(), bullet[i].getRectangle())){
						bullet[i].done = true;
						man.bullets++;
					}
					for(int y = 0; y < humanNumber; y++)
						if(detectCollision(human[y].getRectangle(), bullet[i].getRectangle())){
							bullet[i].done = true;
							human[y].bullets++;
						}
				}
			}
		}
	}
	private void gunLogic(float mouseX, float mouseY){
		for(int i = 0; i < gunNumber; i++){
			if(!man.dead && gun[i].equippedBy == ID.PLAYER)
				gun[i].move(man.x, man.y, mouseX, mouseY);
			if(gun[i].equippedBy == ID.HUMAN)
				if(!human[gun[i].equippedNumber].dead && human[gun[i].equippedNumber].show){
					float pointX = human[gun[i].equippedNumber].x + (Human.WIDTH * 2), pointY = human[gun[i].equippedNumber].y + Human.HEIGHT;
					if(zombie[human[gun[i].equippedNumber].nearestZombie] != null)
						if(!zombie[human[gun[i].equippedNumber].nearestZombie].dead && human[gun[i].equippedNumber].zombieNear){
							pointX = zombie[human[gun[i].equippedNumber].nearestZombie].x;
							pointY = zombie[human[gun[i].equippedNumber].nearestZombie].y;
						}
					gun[i].move(human[gun[i].equippedNumber].x, human[gun[i].equippedNumber].y, pointX, pointY);
				}
			if(!gun[i].equipped && detectCollision(man.getRectangle(), gun[i].getRectangle())){
				gun[i].equipped = true;
				gun[i].equippedBy = ID.PLAYER;
				man.gun = i;
				man.hasGun = true;
			}
			for(int y = 0; y < humanNumber; y++)
				if(!gun[i].equipped && detectCollision(human[y].getRectangle(), gun[i].getRectangle())){
					human[y].hasGun = true;
					gun[i].equipped = true;
					gun[i].equippedBy = ID.HUMAN;
					gun[i].equippedNumber = y;
				}
		}
	}
	private void knifeLogic(float mouseX, float mouseY){
		for(int i = 0; i < knifeNumber; i++){
			if(!man.dead && knife[i].equippedBy == ID.PLAYER)
				knife[i].move(man.x, man.y, mouseX, mouseY, man.hasGun);
			if(knife[i].equippedBy == ID.HUMAN)
				if(!human[knife[i].equippedNumber].dead && human[knife[i].equippedNumber].show){
					float pointX = human[knife[i].equippedNumber].x + (Human.WIDTH * 2), pointY = human[knife[i].equippedNumber].y + Human.HEIGHT;
					if(zombie[human[knife[i].equippedNumber].nearestZombie] != null)
						if(!zombie[human[knife[i].equippedNumber].nearestZombie].dead && human[knife[i].equippedNumber].zombieNear){
							pointX = zombie[human[knife[i].equippedNumber].nearestZombie].x;
							pointY = zombie[human[knife[i].equippedNumber].nearestZombie].y;
						}
					knife[i].move(human[knife[i].equippedNumber].x, human[knife[i].equippedNumber].y, pointX, pointY, human[knife[i].equippedNumber].hasGun);
				}
			if(!knife[i].equipped && detectCollision(man.getRectangle(), knife[i].getRectangle())){
				man.hasKnife = true;
				knife[i].equipped = true;
				knife[i].equippedBy = ID.PLAYER;
			}
			for(int y = 0; y < humanNumber; y++)
				if(!knife[i].equipped && human[y].show && !human[y].dead && detectCollision(knife[i].getRectangle(), human[y].getRectangle())){
					human[y].hasKnife = true;
					knife[i].equipped = true;
					knife[i].equippedBy = ID.HUMAN;
					knife[i].equippedNumber = y;
				}
			if((knife[i].equipped && knife[i].equippedBy == ID.PLAYER && !man.dead) || (knife[i].equipped && knife[i].equippedBy == ID.HUMAN && !human[knife[i].equippedNumber].dead)){
				for(int u = 0; u < zombieNumber; u++)
					if(!zombie[u].dead)
						if(detectCollision(zombie[u].getRectangle(), knife[i].getRectangle())){
							zombie[u].dead = true;
							System.out.println("zombie died of knife");
							for(int y = 0; y < rand.nextInt(3); y++)
								createNew(ID.STATICBULLET, zombie[u].x + rand.nextInt(10) - 5, zombie[u].y + rand.nextInt(10) - 5);
						}
				for(int u = 0; u < humanNumber; u++){
					if(u == knife[i].equippedNumber)u++;
					if(u == humanNumber)break;
					if(!human[u].dead && human[u].show)
						if(detectCollision(knife[i].getRectangle(), human[u].getRectangle())){
							human[u].dead = true;
							System.out.println("human dies via knife");
							for(int y = 0; y < rand.nextInt(3); y++)
								createNew(ID.STATICBULLET, human[u].x + rand.nextInt(10) - 5, human[u].y + rand.nextInt(10) - 5);
						}
				}
				if(knife[i].equippedBy == ID.HUMAN)
					if(detectCollision(man.getRectangle(), knife[i].getRectangle()))
						man.dead = true;
			}
		}
	}
	public void createZombieSurround(float x, float y, int zombies, float radius){
		int circle = 360, divisor = 1;
		if(zombies > 360){
			circle = 36000;
			divisor = 100;
		}
	    for(int i = 0; i < zombies; i++){
	    	float[] center = new float[2];
	    	center[0] = x;
	    	center[1] = y;
	    	float positionx = (float) ((Math.cos(Math.toRadians(((circle / zombies) * i) / divisor)) * radius) + center[0]);
	    	float positiony = (float) ((Math.sin(Math.toRadians(((circle / zombies) * i) / divisor)) * radius) + center[1]);
	    	createNew(ID.ZOMBIE, positionx, positiony);
	    }
	}
	public void createNew(int object, float x, float y){
		if(object == 0){
			man = new Player(x, y);
		}
		if(object == 1){
			zombie[zombieNumber] = new Zombie(x, y, zombieNumber);
			zombieNumber++;
		}
		if(object == 2){
			human[humanNumber] = new Human(x, y, humanNumber);
			humanNumber++;
		}
		if(object == 3){
			if(man.bullets > 0 && man.hasGun){
				bullet[bulletNumber] = new Bullet(x, y, man.x, man.y, bulletNumber, true);
				man.bullets--;
				bulletNumber++;
			}
		}
		if(object == 4){
			bullet[bulletNumber] = new Bullet(x, y, man.x, man.y, bulletNumber, false);
			bulletNumber++;
		}
		if(object == 5){
			gun[gunNumber] = new Gun(x, y, gunNumber, true);
			gunNumber++;
		}
		if(object == 6){
			gun[gunNumber] = new Gun(x, y, gunNumber, false);
			gunNumber++;
		}
		if(object == 7){
			knife[knifeNumber] = new Knife(x, y, knifeNumber, true);
			knifeNumber++;
		}
		if(object == 8){
			knife[knifeNumber] = new Knife(x, y, knifeNumber, false);
			knifeNumber++;
		}
	}
	public boolean isPlayerAlive(){
		if(man != null)
			return !man.dead;
		else {
			return true;
		}
	}
	private void populateVillage(){
		for(int x = -200, y = 0; x < 200; x += 50){
			x += rand.nextInt(10);
			y += rand.nextInt(50);
			createNew(ID.HUMAN, x, y);
			y = 0;
		}
		for(int i = 0, x = -200, y = 0; i < humanNumber; i++){
			x = (int) human[i].x;
			y -= (Cabin.HEIGHT * Cabin.SCALE) + (Human.HEIGHT * Human.SCALE) + rand.nextInt(10);
			Rectangle rectangle = new Rectangle(x, y, Cabin.WIDTH * Cabin.SCALE, Cabin.HEIGHT * Cabin.SCALE);
			if(rectangle.getCenterX() == x){
				rectangle.setCenterX(x + ((Cabin.WIDTH * Cabin.SCALE) / 2));
				rectangle.setCenterX(y + ((Cabin.HEIGHT * Cabin.SCALE) / 2));
			}
			boolean intersects = false;
			for(int z = 0; z < humanNumber; z++)
				if(rectangle.intersects(human[z].getRectangle()))
					intersects = true;
			if(rectangle.intersects(man.getRectangle()))
				intersects = true;
			for(int z = 0; z < villagegen.cabinNumber; z++)
				if(rectangle.intersects(villagegen.cabin[z].getRectangle()))
					intersects = true;
			if(!intersects){
				villagegen.createCabin(x, y);
				for(int u = 0; u < 2; u++)
					villagegen.createRoad(x + (Road.WIDTH * u), y + (Cabin.HEIGHT * Cabin.SCALE) + rand.nextInt(10), 0);
			}
			y = 0;
		}
	}
	public int idealCameraX(){
		return (int) (man.x + 16);
	}
	public int idealCameraY(){
		return (int) (man.y + 16);
	}
	private void detectPlayerCollisions(){
		for(int i = 0; i < zombieNumber; i++){
			if(!zombie[i].dead)
				if(detectCollision(man.getRectangle(), zombie[i].getRectangle()))
					man.dead = true;
		}
		for(int i = 0; i < humanNumber; i++){
			if(human[i].show && !human[i].dead)
				if(detectCollision(man.getRectangle(), human[i].getRectangle()))
					man.returnToPrevious();
		}
		for(int y = 0; y < villagegen.cabinNumber; y++){
			if(detectCollision(man.getRectangle(), villagegen.cabin[y].getRectangle()))
				man.returnToPrevious();
		}
	}
	private void makeZombie(){
		boolean created = false, canCreate = false;
		for(int i = 0; i < humanNumber; i++){
			if(human[i].show)
				canCreate = true;
		}
		if(canCreate){
			while(!created){
				int which = rand.nextInt(humanNumber);
				if(human[which].show){
					human[which].show = false;
					for(int u = 0; u < knifeNumber; u++)
						if(knife[u].equipped && knife[u].equippedBy == ID.HUMAN)
							if(knife[u].equippedNumber == which)
								knife[u].reset();
					createNew(ID.ZOMBIE, human[which].x, human[which].y);
					created = true;
				}
			}
		}
	}
	private void moveZombie(int zombieID){
		int lowest = (int) Math.sqrt(Math.pow((man.x - zombie[zombieID].x), 2) + Math.pow((man.y - zombie[zombieID].y), 2)), humanID = humanNumber;
		for(int i = 0; i < humanNumber; i++){
			if(human[i].show && !human[i].dead){
				int humanDistance = (int) Math.sqrt(Math.pow((human[i].x - zombie[zombieID].x), 2) + Math.pow((human[i].y - zombie[zombieID].y), 2));
				if(humanDistance < lowest){
					lowest = humanDistance;
					humanID = i;
				}
			}
		}
		if(humanID < humanNumber)
			zombie[zombieID].move(human[humanID].x, human[humanID].y);
		if(humanID == humanNumber)
			zombie[zombieID].move(man.x, man.y);
	}
	private boolean isZombieNear(int humanID){
		boolean zombieNear = false;
		for(int i = 0; i < zombieNumber; i++){
			if(!zombie[i].dead){
				int distance = (int) Math.sqrt(Math.pow((human[humanID].x - zombie[i].x), 2) + Math.pow((human[humanID].y - zombie[i].y), 2));
				if(distance <= 100){
					zombieNear = true;
					human[humanID].nearestZombie = i;
				}
			}
		}
		return zombieNear;
	}
	private boolean isGunNear(int humanID){
		boolean gunNear = false;
		for(int i = 0; i < gunNumber; i++){
			if(!gun[i].equipped){
				int distance = (int) Math.sqrt(Math.pow((human[humanID].x - gun[i].x), 2) + Math.pow((human[humanID].y - gun[i].y), 2));
				if(distance <= 300){
					gunNear = true;
					human[humanID].nearestGun = i;
				}
			}
		}
		return gunNear;
	}
	private boolean isKnifeNear(int humanID){
		boolean knifeNear = false;
		for(int i = 0; i < knifeNumber; i++){
			if(!knife[i].equipped){
				int distance = (int) Math.sqrt(Math.pow((human[humanID].x - knife[i].x), 2) + Math.pow((human[humanID].y - knife[i].y), 2));
				if(distance <= 300){
					knifeNear = true;
					human[humanID].nearestKnife = i;
				}
			}
		}
		return knifeNear;
	}
	private boolean isBulletNear(int humanID){
		boolean bulletNear = false;
		for(int i = 0; i < bulletNumber; i++){
			if(!bullet[i].done){
				int distance = (int) Math.sqrt(Math.pow((human[humanID].x - bullet[i].x), 2) + Math.pow((human[humanID].y - bullet[i].y), 2));
				if(distance <= 900){
					bulletNear = true;
					human[humanID].nearestBullet = i;
				}
			}
		}
		return bulletNear;
	}
	private void zombieHumanMovement(int humanID, float zombieX, float zombieY){
		double angle = Math.atan2(human[humanID].y - zombieY, human[humanID].x - zombieX);
		human[humanID].movementX = (float) (Math.cos(angle) * human[humanID].SPEED);
		human[humanID].movementY = (float) (Math.sin(angle) * human[humanID].SPEED);
	}
	@SuppressWarnings("unused")
	private int mix(int direction1, int direction2){
		int mix = 0;
		if((direction1 == 0 && direction2 == 1) || (direction2 == 0 && direction1 == 1))
			mix = 4;
		if((direction1 == 0 && direction2 == 3) || (direction2 == 0 && direction1 == 3))
			mix = 5;
		if((direction1 == 2 && direction2 == 1) || (direction2 == 2 && direction1 == 1))
			mix = 6;
		if((direction1 == 2 && direction2 == 3) || (direction2 == 2 && direction1 == 3))
			mix = 7;
		return mix;
	}
	public int getBullets(){
		if(man != null)
			return man.bullets;
		else {
			return 0;
		}
	}
	public boolean hasGun(){
		if(man != null)
			return man.hasGun;
		else {
			return false;
		}
	}
	public boolean hasKnife(){
		if(man != null)
			return man.hasKnife;
		else {
			return false;
		}
	}
	private boolean detectCollision(Rectangle rec1, Rectangle rec2){
		if(rec1.intersects(rec2))
			return true;
		else {
			return false;
		}
	}
	@SuppressWarnings("unused")
	private void getNearestZombie(int humanID){
		int maxDistance = Integer.MAX_VALUE;
		for(int i = 0; i < zombieNumber; i++){
			if(!zombie[i].dead){
				int distance = (int) Math.sqrt(Math.pow((human[humanID].x - zombie[i].x), 2) + Math.pow((human[humanID].y - zombie[i].y), 2));
				if(distance < maxDistance){
					human[humanID].nearestZombie = i;
				}
			}
		}
	}
}
