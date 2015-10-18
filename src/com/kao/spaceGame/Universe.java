package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.scene.Group;
//proofread
public class Universe extends Group {
	public Universe(int nbAvatars) {
		this();
		for(int i = 0; i < nbAvatars; i++){
			avatars.add(new Universe());
		}
	}
	
	private Universe(){
		getChildren()
				.addAll(shipsGroup, galaxiesGroup, botsGroup, misslesGroup);
	}
	
	public UniverseList<Ship> getShips() {
		return ships;
	}

	public UniverseList<Galaxy> getGalaxies() {
		return galaxies;
	}

	public UniverseList<Bot> getBots() {
		return bots;
	}

	public UniverseList<Missle> getMissles() {
		return missles;
	}

	public Universe getAvatar(int i) {
		return avatars.get(i);
	}

	private ArrayList<Universe> avatars = new ArrayList<Universe>();
	private Group shipsGroup = new Group();
	private Group galaxiesGroup = new Group();
	private Group botsGroup = new Group();
	private Group misslesGroup = new Group();

	private final UniverseList<Ship> ships = new UniverseList<Ship>(
			shipsGroup.getChildren(), avatars);
	private final UniverseList<Galaxy> galaxies = new UniverseList<Galaxy>(
			galaxiesGroup.getChildren(), avatars);
	private final UniverseList<Bot> bots = new UniverseList<Bot>(
			botsGroup.getChildren(), avatars);
	private final UniverseList<Missle> missles = new UniverseList<Missle>(
			misslesGroup.getChildren(), avatars);
}
