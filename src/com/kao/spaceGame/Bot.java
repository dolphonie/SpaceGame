package com.kao.spaceGame;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bot extends Group{
	private static final double GALAXY_WIDTH = SpaceGame.GALAXY_WIDTH;
	private static final double BOT_SPEED = 50;
	private static final double BOT_MISSLE_SPEED = 1000;
	private static final double BOT_WIDTH = 50;
	private static final int BOT_FIRING_FREQUENCY = 5 * 60;
	private final Random random = new Random();

	public Bot(double shipX, double shipY, double shipZ) {
		bot = new Rectangle(BOT_WIDTH, BOT_WIDTH);
		bot.setFill(Color.BLUE);
		bot.setTranslateX(random.nextDouble()*GALAXY_WIDTH - GALAXY_WIDTH / 2);
		bot.setTranslateY(random.nextDouble()*GALAXY_WIDTH - GALAXY_WIDTH / 2);
		bot.setTranslateZ(random.nextDouble()*GALAXY_WIDTH - GALAXY_WIDTH / 2);
		setVariables(shipX, shipY, shipZ, BOT_SPEED);
	}

//	public Missle tick(double shipX, double shipY, double shipZ) {
//		setVariables(shipX, shipY, shipZ, BOT_SPEED);
//		bot.setTranslateX(bot.getTranslateX() + vx);
//		bot.setTranslateY(bot.getTranslateY() + vy);
//		bot.setTranslateZ(bot.getTranslateZ() + vz);
//		if (timeCounter >= timeFire) {
//			timeFire += random.nextInt(BOT_FIRING_FREQUENCY);
//			return fire(shipX,shipY,shipZ);
//		}
//		timeCounter++;
//		for (int i = 0; i < missles.size(); i++) {
//			missles.get(i).tick();
//		}
//		return null;
//	}

	public Missle fire(double shipX, double shipY, double shipZ) {
		setVariables(shipX, shipY, shipZ, BOT_MISSLE_SPEED);
//		Missle missle = new Missle(bot.getTranslateX(), bot.getTranslateY(),
//				bot.getTranslateZ(), vx, vy, vz, Color.GREEN);
//		missles.add(missle);
		Missle missle = null;
		return missle;
	}

	public void setVariables(double shipX, double shipY, double shipZ,
			double speed) {
		deltaX = shipX - bot.getTranslateX();
		deltaY = shipY - bot.getTranslateY();
		deltaZ = shipZ - bot.getTranslateZ();
		distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ);
		time = distance / speed;
		vx = deltaX / time;
		vy = deltaY / time;
		vz = deltaZ / time;
	}

	public Rectangle getBot() {
		return bot;
	}
	
	public Point3D getLocation(){
		return new Point3D(bot.getTranslateX(),bot.getTranslateY(),bot.getTranslateZ());
	}
	
	private long timeCounter = 0;
	private long timeFire = random.nextInt(BOT_FIRING_FREQUENCY);
	private double deltaX, deltaY, deltaZ, time, distance, vx, vy, vz;
	ArrayList<Missle> missles = new ArrayList<Missle>();
	Rectangle bot;
}
