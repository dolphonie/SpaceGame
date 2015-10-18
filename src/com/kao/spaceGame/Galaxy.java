package com.kao.spaceGame;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

//proofread
public class Galaxy extends Group implements AvatarFactory, Tickable {
	private static final boolean TEST_SPHERES = false;
	private static final boolean SHOW_AXIS = true;
	private static final int STAR_COUNT = 0;
	private static final double STAR_RADIUS = 1e6;
	private final Random random = new Random();
	private static final int TEST_CIRCLE_SIZE = 100;

	private void init(int seed) {
		random.setSeed(seed);
		if (TEST_SPHERES) {
			Sphere cMark = new Sphere(TEST_CIRCLE_SIZE, 10);
			cMark.setMaterial(new PhongMaterial(Color.ORANGE));

			Sphere xMark = new Sphere(TEST_CIRCLE_SIZE, 10);
			xMark.setMaterial(new PhongMaterial(Color.RED));
			xMark.setTranslateX(500);

			Sphere yMark = new Sphere(TEST_CIRCLE_SIZE, 10);
			yMark.setMaterial(new PhongMaterial(Color.GREEN));
			yMark.setTranslateY(500);

			Sphere zMark = new Sphere(TEST_CIRCLE_SIZE, 10);
			zMark.setMaterial(new PhongMaterial(Color.BLUE));
			zMark.setTranslateZ(500);

			getChildren().addAll(cMark, xMark, yMark, zMark);
		}
		if (SHOW_AXIS) {
			Line xAxis = new Line(0, 0, SpaceGame.GALAXY_WIDTH, 0);
			xAxis.setStroke(Color.RED);
			Line yAxis = new Line(0, 0, 0, SpaceGame.GALAXY_WIDTH);
			yAxis.setStroke(Color.GREEN);
			Line zAxis = new Line(0, 0, 0, SpaceGame.GALAXY_WIDTH);
			zAxis.setStroke(Color.BLUE);
			zAxis.setRotationAxis(Rotate.X_AXIS);
			zAxis.setRotate(90);
			getChildren().addAll(xAxis, yAxis, zAxis);
		}
		earth = new Sphere(Units.EARTH_RADIUS * .99);
		Image diffuseMap = new Image(Galaxy.class.getResource(
				"earth_flat_map.jpg").toExternalForm());
		Material earthMat = new PhongMaterial(Color.WHITE, diffuseMap, null,
				null, null);
		earth.setMaterial(earthMat);
		earth.getTransforms().add(earthRotation);
		earthRotation.setAxis(Rotate.X_AXIS);
		earthRotation.setAngle(90);
		earth.setRotationAxis(Rotate.Z_AXIS);
		Circle moonOrbit = new Circle(Units.MOON_DIST_FROM_EARTH_CENTER,
				Color.TRANSPARENT);
		moonOrbit.setStrokeWidth(7e5);
		moonOrbit.setStroke(Color.ORANGE);
		moon = new Sphere(Units.MOON_RADIUS);
		Image moonMap = new Image(Galaxy.class.getResource("moon.jpg")
				.toExternalForm());
		Material moonMat = new PhongMaterial(Color.WHITE, moonMap, null, null,
				null);
		moon.setMaterial(moonMat);
		moon.setTranslateX(Units.MOON_DIST_FROM_EARTH_CENTER);
		getChildren().addAll(earth, moon, moonOrbit);
		GameTime.add(this);
		// animate();
		for (int i = 0; i < STAR_COUNT; i++) {
			Sphere star = new Sphere(STAR_RADIUS, 10);
			Point3D starPos = generateRandomStarPosition();
			star.setTranslateX(starPos.getX());
			star.setTranslateY(starPos.getY());
			star.setTranslateZ(starPos.getZ());
			getChildren().add(star);
		}
	}

	private Point3D generateRandomStarPosition() {
		double starVoid = Units.EARTH_RADIUS * 10;
		Point3D starPos = new Point3D(genRandStarCoord(), genRandStarCoord(),
				genRandStarCoord());
		if (starPos.distance(Units.EARTH_LOCATION) <= starVoid) {
			starPos = starPos.add(starPos.normalize().multiply(starVoid));
		}
		return starPos;
	}

	private double genRandStarCoord() {
		return (random.nextDouble() - .5) * SpaceGame.GALAXY_WIDTH;
	}

	public Galaxy() {
		seed = random.nextInt();
		init(seed);
	}

	private Galaxy(int seed, Galaxy galaxy) {
		original = galaxy;
		init(seed);
	}
	
	public boolean isAvatar(){
		return original!=null;
	}
	
	public Group createAvatar() {
		Galaxy g = new Galaxy(seed, this);
		galaxyAvatars.add(g);
		return g;
	}

	public Group getAvatar(int i) {
		return galaxyAvatars.get(i);
	}

	public static Point3D getMoonPos(double timeSincePrevious) {
		double angle = -(Units.MOON_ANGULAR_VELOCITY * (moonTime + timeSincePrevious))+moonPosIncrement;
		double x = Math.cos(Math.toRadians(angle)) * Units.MOON_DIST_FROM_EARTH_CENTER;
		double y = Math.sin(Math.toRadians(angle)) * Units.MOON_DIST_FROM_EARTH_CENTER;
		return new Point3D(x, y, 0);
	}
	
	public static void setMoonPosIncrement(double moonPos){
		moonPosIncrement = moonPos;
	}
	
	public static double getMoonPosIncrement(){
		return moonPosIncrement;
	}
	
	public void setEarthVisibility(boolean visible){
		earth.setVisible(visible);
	}
	
	public boolean getEarthVisibility(){
		return earth.isVisible();
	}
	
	public void tick(double now, double timeSincePrevious, double realTimeSincePrevious) {
		earth.setRotate(-Units.DEGREES_PER_SECOND * now);
		moonTime = now;
		Point3D moonPos = getMoonPos(0);
		moon.setTranslateX(moonPos.getX());
		moon.setTranslateY(moonPos.getY());
	}
	
	public void setMoonScale(double scale) {
		moon.setScaleX(scale);
		moon.setScaleY(scale);
		moon.setScaleZ(scale);
	}
	
	private static double moonPosIncrement = 83;
	private Galaxy original;
	private static double moonTime = 0;
	private Rotate earthRotation = new Rotate();
	private Sphere earth, moon;
	private ArrayList<Galaxy> galaxyAvatars = new ArrayList<Galaxy>();
	private int seed;
}
