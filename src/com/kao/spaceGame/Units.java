package com.kao.spaceGame;

import javafx.geometry.Point3D;

//UNITS USED BY GAME ARE METERS,KILOGRAMS, AND SECONDS(MKS[METRIC SYSTEM])
public class Units {
	public static final double LB_PER_TON = 2000;//short ton
	public static final double LB_PER_KG = 2.2;
	public static final double EARTH_MASS = 5.97219e24;//kg
	public static final double MOON_MASS = 7.3477e22;
	public static final double MOON_RADIUS = 1737e3;
	public static final double MOON_DIST_FROM_EARTH_CENTER =  384405e3;
	private static final double SHIP_MASS_TONS = 2030;//tons
	public static final double SHIP_MASS = SHIP_MASS_TONS*LB_PER_TON/LB_PER_KG;
	public static final double G = 6.674E-11;//N M^2 / KG^2
	public static final Point3D EARTH_LOCATION = new Point3D(0,0,0);
	public static final double EARTH_RADIUS = 6771e3;//m
	public static final double ORBIT_WIDTH = EARTH_RADIUS/10;
	public static final double SPACE_STATION_ALTITUDE = 230 * 1609.34;//meters
	public static final double SHIP_THRUST = 5.5e2;
	public static final double DEGREES_PER_DAY = 360;
	public static final double SECONDS_PER_DAY = 24*60*60;
	public static final double DEGREES_PER_NANOSECOND = DEGREES_PER_DAY/SECONDS_PER_DAY/1E9;
	public static final double DEGREES_PER_SECOND = DEGREES_PER_DAY/SECONDS_PER_DAY;
	public static final double MOON_VELOCITY = SpaceGame.earthSateliteVelocity(MOON_DIST_FROM_EARTH_CENTER);
	public static final double MOON_PERIOD = 2*Math.PI*MOON_DIST_FROM_EARTH_CENTER/MOON_VELOCITY;
	public static final double MOON_ANGULAR_VELOCITY = 360/MOON_PERIOD; 
	public static final double L1_POS = 3.263178e8;
	public static final double VELOCITY_AT_L1 = L1_POS*MOON_VELOCITY/MOON_DIST_FROM_EARTH_CENTER;
}
