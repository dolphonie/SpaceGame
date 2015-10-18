package com.kao.spaceGame;

public interface Tickable {
	public void tick(double simTime, double simTimeSincePrevious, double realTimeSincePrevious);
}
