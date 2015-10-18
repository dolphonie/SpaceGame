package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;

public class GameTime {
	private GameTime() {
		animate();
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				long timeElapsed = now - oldNow;
				oldNow = now;
				if (firstIteration) {
					firstIteration = false;
					return;// skips rest of code
				}
				double fps = 1 / (timeElapsed / 1e9);
				avgFPS = .05 * fps + .95 * avgFPS;

				gameTime += (timeElapsed * speedUp) / 1e9;
				for (Ship s : ships) {
					s.tick(gameTime, timeElapsed * speedUp/ 1e9,timeElapsed/1e9);
				}
				for (Galaxy g : galaxies) {
					g.tick(gameTime, timeElapsed * speedUp / 1e9,timeElapsed/1e9);
				}

			}
		}.start();
	}

	public static void add(Ship s) {
		ships.add(s);
	}

	public static void add(Galaxy g) {
		galaxies.add(g);
	}
	
	public static void setSpeedUp(double sp){
		speedUp = sp;
	}
	
	public static double getSpeedUp(){
		return speedUp;
	}
	
	public static double getGameTime(){
		return gameTime;
	}
	
	public static void reset(){
		gameTime = 0;
		firstIteration = true;
	}
	public static double getFPS() {
		return avgFPS;
	}
	private static double speedUp = 1;
	private static ArrayList<Ship> ships = new ArrayList<Ship>();
	private static ArrayList<Galaxy> galaxies = new ArrayList<Galaxy>();
	private static long oldNow;
	private static boolean firstIteration = true;
	private static GameTime gt = new GameTime();
	private static double gameTime = 0;
	private static double avgFPS = 0;
}
