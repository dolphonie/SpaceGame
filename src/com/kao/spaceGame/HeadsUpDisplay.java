package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import com.kao.shapes.CylinderP;

// ----------------------------- HeadsUpDisplay ---------------------------
public class HeadsUpDisplay extends Group {
	private static final int TEXT_SIZE = 20;
	private static final double CROSS_SIZE = 40;
	private final Color TEXT_COLOR = Color.WHITE;
	private static final double SEC_PER_NANOSEC = 1e-9;
	private static final double POINTER_LENGTH = 100;
	private static final double POINTER_THICKNESS = 10;
	private static Point3D POINTER_ORIGIN = new Point3D(0, 200, 150);
	private static final double POINTER_BALL_RADIUS = POINTER_THICKNESS * 1.5;
	

	public HeadsUpDisplay(DoubleExpression displayWid,
			DoubleExpression displayHei, Ship ship, JoyStick joyStick,
			ArrayList<Ship> enemies) {
		this.ship = ship;
		this.joyStick = joyStick;
		this.enemies = enemies;
		addCrossHairs(displayWid, displayHei);
		addGauges();
		addRadar(displayWid);
		animate();
	}

	private void addGauges() {
		gauges = new Text();
		Font font = new Font(TEXT_SIZE);
		gauges.setFont(font);
		gauges.setFill(TEXT_COLOR);
		gauges.setY(font.getSize());
		getChildren().add(gauges);
	}

	private void addCrossHairs(DoubleExpression displayWid,
			DoubleExpression displayHei) {
		Line horiCross = new Line();
		horiCross.setStroke(Color.WHITE);

		horiCross.startXProperty().bind(
				displayWid.divide(2).subtract(CROSS_SIZE));
		horiCross.endXProperty().bind(displayWid.divide(2).add(CROSS_SIZE));
		horiCross.startYProperty().bind(displayHei.divide(2));
		horiCross.endYProperty().bind(displayHei.divide(2));
		horiCross.setStroke(Color.WHITE);

		Line vertCross = new Line();
		vertCross.startXProperty().bind(displayWid.divide(2));
		vertCross.endXProperty().bind(displayWid.divide(2));
		vertCross.startYProperty().bind(
				displayHei.divide(2).subtract(CROSS_SIZE));
		vertCross.endYProperty().bind(displayHei.divide(2).add(CROSS_SIZE));
		vertCross.setStroke(Color.WHITE);

		getChildren().addAll(horiCross, vertCross);
	}

	private void addRadar(DoubleExpression displayWid) {
		POINTER_ORIGIN = new Point3D( displayWid.getValue()- POINTER_LENGTH * 1.5, POINTER_ORIGIN.getY(), POINTER_ORIGIN.getZ());
		PointLight point = new PointLight();
		getChildren().add(point);
		point.setLayoutX(POINTER_ORIGIN.getX());
		point.setLayoutY(POINTER_ORIGIN.getY());

		for (int i = 0; i < enemies.size(); i++) {
			
			CylinderP pointer = new CylinderP(POINTER_THICKNESS,
					new Point3D(POINTER_ORIGIN.getX(), POINTER_ORIGIN.getY(),POINTER_ORIGIN.getZ()), Rotate.X_AXIS /* RANDOM POINT */);
			pointer.setMaterial(new PhongMaterial(Color.ORANGE));
			Sphere tip = new Sphere(POINTER_BALL_RADIUS);
			Circle radarScreen = new Circle(POINTER_LENGTH * 1.2);
			radarScreen.setFill(Color.TRANSPARENT);
			radarScreen.setStrokeWidth(10);
			radarScreen.setStroke(Color.GREEN);
			radarScreen.setTranslateX(POINTER_ORIGIN.getX());
			radarScreen.setTranslateY(POINTER_ORIGIN.getY());
			radarScreen.setTranslateZ(POINTER_ORIGIN.getZ());
			tip.setMaterial(new PhongMaterial(enemies.get(i).getColor()));
			tips.add(tip);
			pointers.add(pointer);
			point.getScope().addAll(pointer,tip);
			getChildren().addAll(pointer,tip,radarScreen);
		}
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				frameCount++;
				if (frameCount % 10 == 0) {
					updateGauges();
				}
				for (int i = 0; i < enemies.size(); i++) {
					Point3D enemyLocation = enemies.get(i).getLocation();
					Point3D relativeLocation = ship.sceneToLocal(enemyLocation);
					Point3D normal = relativeLocation.normalize();
					Point3D scaled = normal.multiply(POINTER_LENGTH);
					Point3D translated = scaled.add(POINTER_ORIGIN);
					CylinderP pointer = pointers.get(i);
					pointer.setEnd(translated);
					Sphere tip = tips.get(i);
					tip.setTranslateX(translated.getX());
					tip.setTranslateY(translated.getY());
					tip.setTranslateZ(translated.getZ());
					double zRel = scaled.getZ();
					if (zRel>0) {
						getChildren().remove(pointer);
						getChildren().add(pointer);
					} else {
						getChildren().remove(tip);
						getChildren().add(tip);
					}
				}
			}
		}.start();
	}

	private void updateGauges() {
		gauges.setText(String.format(
				"Thrust: %+3.3f"
						+ "\nLocation (m): (%+3.0f, %+3.0f, %+3.0f)"
						+ "\nVelocity (m/s): %3.1f"
						+ "\nEarth's Gravitational Acceleration (m/s^2):  %3.5f"
						+ "\nMoon's Gravitational Acceleration (m/s^2): %3.5f"
						+ "\nSpeedUp: %3.1f"
						// + "\nPitch: %+3.1f\nYaw: %+3.1f"
//						+ "\nX Velocity: %+3.1f\nY Velocity: %+3.1f\nZ Velocity: %+3.1f"
						//+ "\nElevator Throw: %+3.3f\nRudder Throw: %+3.3f \nAileron Throw: %+3.3f"
						+ "\nFPS: %+3.1f"
						+"\nTrajectory Length (s): %3d"
						+"\nSimulation Time (s): %3.0f", 
				
				joyStick.getThrottle(),		
				ship.getX(),
				ship.getY(),
				ship.getZ(),
				ship.getVelocity().magnitude(),
				ship.getEarthGravity(),
				ship.getMoonGravity(),
				GameTime.getSpeedUp(),
//				 ship.getPitch(), ship.getYaw(),
//				ship.getXVel(), ship.getYVel(), ship.getZVel(),
				//joyStick.getElevator(), joyStick.getRudder(),joyStick.getAileron(), 
				GameTime.getFPS(),
				SpaceGame.trajectoryLength,
				GameTime.getGameTime()));
	}

	private ArrayList<CylinderP> pointers = new ArrayList<CylinderP>();
	private ArrayList<Sphere> tips = new ArrayList<Sphere>();
	private ArrayList<Ship> enemies;
	private Ship ship;
	private JoyStick joyStick;
	private long frameCount = 0;
	private Text gauges;
}
