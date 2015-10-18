package com.kao.spaceGame;

import javafx.geometry.Point3D;

public class State {
	public State(Point3D pos,Point3D vel){
		this.pos = pos;
		this.vel = vel;
	}
	public Point3D pos,vel;
}
