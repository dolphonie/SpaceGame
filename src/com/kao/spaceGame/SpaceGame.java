package com.kao.spaceGame;

import java.util.ArrayList;
import java.util.Random;

import com.kao.spaceGame.Ship.Mode;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;


/*VM arguments required:
 * -Djava.library.path="C:\Program Files (x86)\Java\JInput\dist"
 * -Xms1g -Xmx1g
 */
public class SpaceGame extends Application {
	public static final boolean SPACE_PLANNER_MODE = true;
	public static final double GALAXY_WIDTH = Units.EARTH_RADIUS * 300;// 6E5;
	public static final double SCROLL_DISTANCE = Units.EARTH_RADIUS;
	private static final Color[] SHIP_COLORS = { Color.RED, Color.CADETBLUE,
			Color.ORANGE, Color.AQUA, Color.GREENYELLOW };
	private static final int EXPLODE_DISTANCE = 250;
	private static final int SHIP_SPAWN_DISTANCE = 30;
	public static final boolean DESIGNER_MODE = false;
	private Random random = new Random();

	// private final Random random = new Random();

	@Override
	public void start(Stage primaryStage) {
		try {
			myStart(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void myStart(Stage primaryStage) {
		primaryStage.setTitle("Space Simulator (Only $999999.99!)");
		ArrayList<JoyStick> joySticks = JoyStick.findJoySticks();
		if (joySticks.size() == 0) {
			System.out.println("NO INTERLINK FOUND");
			System.exit(1);
		}
		int numAvatars = joySticks.size() - 1;
		universe = new Universe(numAvatars);
		Galaxy galaxy = new Galaxy();
		universe.getGalaxies().add(galaxy);

		for (int i = 0; i < joySticks.size(); i++) {
			JoyStick joyStick = joySticks.get(i);
			Ship ship = new Ship(joyStick, SHIP_COLORS[i], universe);
			universe.getShips().add(ship);
		}
		// randomReset();
		scienceReset();
		double stageX = 0;
		UniverseList<Ship> ships = universe.getShips();
		EventHandler<KeyEvent> keyHandler = (new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				handleKeyEvent(ke);
			}
		});
		for (int i = 0; i < joySticks.size(); i++) {
			Stage stage = i == 0 ? primaryStage : new Stage();
			stage.setX(stageX);
			stage.setY(0);
			stage.setWidth(screenBounds.getWidth() / joySticks.size());
			stage.setHeight(screenBounds.getHeight());
			stageX += screenBounds.getWidth() / joySticks.size();
			ArrayList<Ship> enemies = new ArrayList<Ship>();
			for (int j = 0; j < ships.size(); j++) {
				if (i != j) {
					enemies.add(ships.get(j));
				}
			}
			if (i == 0) {
				new CockpitView(stage, ships.get(i), universe,
						joySticks.get(i), enemies);
			} else {
				new CockpitView(stage, (Ship) ships.get(i).getAvatar(i - 1),
						universe.getAvatar(i - 1), joySticks.get(i), enemies);
			}
			Scene scene = stage.getScene();
			scene.setOnKeyPressed(keyHandler);
		}

		new AnimationTimer() {
			@Override
			public void handle(long now) {
				frameCount++;
				if (frameCount % 10 == 0)
					return;
				detectCollision();
			}
		}.start();
	}

	private void handleKeyEvent(KeyEvent ke) {
		try {
			KeyCode kc = ke.getCode();
			switch (kc) {
			// case R:
			// randomReset();
			// break;
			case E:
				universe.getGalaxies()
						.get(0)
						.setEarthVisibility(
								!universe.getGalaxies().get(0)
										.getEarthVisibility());
				break;
			case F:
				Galaxy.setMoonPosIncrement(Galaxy.getMoonPosIncrement() + 1);
				break;
			case V:
				Galaxy.setMoonPosIncrement(Galaxy.getMoonPosIncrement() - 1);
				break;
			case D:
				trajectoryLength += 1000;
				break;
			case C:
				trajectoryLength -= 1000;
				break;
			case L:
				laGrangeReset();
				break;
			case S:
				scienceReset();
				break;
			case M:
				moonReset();
				break;
			case G:
				enableMoonGrav = !enableMoonGrav;
				break;
			case LESS:
			case COMMA:
				GameTime.setSpeedUp(Math.max(1, GameTime.getSpeedUp() / 2));
				break;
			case GREATER:
			case PERIOD:
				GameTime.setSpeedUp(Math.min(2097152, GameTime.getSpeedUp() * 2));
				break;
			case A:
				Ship s = universe.getShips().get(0);
				s.setCameraDistance(s.getCameraDistance() - SCROLL_DISTANCE);
				break;
			case Z:
				Ship sh = universe.getShips().get(0);
				sh.setCameraDistance(sh.getCameraDistance() + SCROLL_DISTANCE);
				break;
			case T:
				universe.getShips().get(1).plotTrajectory();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void randomReset() {
		restoreShips();
		randomPosition();
	}

	private void designerReset() {
		restoreShips();
		designerPosition();
	}

	private void moonReset() {
		restoreShips();
		moonPosition();
	}

	private void scienceReset() {
		restoreShips();
		sciencePosition();

	}

	private void laGrangeReset() {
		restoreShips();
		laGrangePosition();
	}

	private void restoreShips() {
		for (Ship s : destroyedShips) {
			universe.getShips().add(s);
		}
		destroyedShips.clear();
		for (int i = 0; i < universe.getShips().size(); i++) {
			universe.getShips().get(i).resetAll();
		}
	}

	private void randomPosition() {
		for (int i = 0; i < universe.getShips().size(); i++) {
			Ship ship = universe.getShips().get(i);
			ship.setX((random.nextDouble() - .5) * SpaceGame.GALAXY_WIDTH
					/ SHIP_SPAWN_DISTANCE);
			ship.setY((random.nextDouble() - .5) * SpaceGame.GALAXY_WIDTH
					/ SHIP_SPAWN_DISTANCE);
			ship.setZ((random.nextDouble() - .5) * SpaceGame.GALAXY_WIDTH
					/ SHIP_SPAWN_DISTANCE);
		}
	}

	private void designerPosition() {
		for (int i = 0; i < universe.getShips().size(); i++) {
			Ship ship = universe.getShips().get(i);
			if (i == 0) {
				ship.setZ(-1000);
			} else {
				ship.incrementYaw(180);
			}
			ship.setMode(Mode.STATIONARY);
		}
	}

	private void sciencePosition() {
		universe.getGalaxies().get(0).setEarthVisibility(true);
		universe.getGalaxies().get(0).setMoonScale(1);
		GameTime.reset();
		GameTime.setSpeedUp(1);
		trajectoryLength = 5900;
		Galaxy.setMoonPosIncrement(83);
		enableMoonGrav = true;
		universe.getShips().get(1).setTrajRadius(Units.EARTH_RADIUS / 100);
		for (int i = 0; i < universe.getShips().size(); i++) {
			Ship ship = universe.getShips().get(i);
			if (i == 0) {
				// observer
				Circle orbit = new Circle(Units.EARTH_RADIUS * 1.1);
				orbit.setFill(Color.TRANSPARENT);
				orbit.setStrokeWidth(Units.ORBIT_WIDTH);
				orbit.setStroke(Color.ORANGE);
				ship.setMode(Mode.CAMERA);
				ship.setCameraDistance(Units.EARTH_RADIUS * 3);
				// universe.getChildren().add(orbit);
			} else {
				// orbiter
				double shipAltitude = Units.EARTH_RADIUS
						+ Units.SPACE_STATION_ALTITUDE;// * 1.2;
				ship.setY(shipAltitude);
				ship.setScale(1e4);
				double velocity = earthSateliteVelocity(shipAltitude);
				velocity *= 1;
				ship.setVelocity(new Point3D(velocity, 0, 0));
				ship.plotTrajectory();
			}
		}
	}

	private void moonPosition() {
		universe.getGalaxies().get(0).setEarthVisibility(true);
		universe.getGalaxies().get(0).setMoonScale(3);
		GameTime.reset();
		GameTime.setSpeedUp(1);
		Galaxy.setMoonPosIncrement(83);
		enableMoonGrav = true;
		trajectoryLength = 1592000;
		universe.getShips().get(1).setTrajRadius(Units.EARTH_RADIUS / 10);
		for (int i = 0; i < universe.getShips().size(); i++) {
			Ship ship = universe.getShips().get(i);
			if (i == 0) {
				// observer
				Circle orbit = new Circle(Units.EARTH_RADIUS * 1.1);
				orbit.setFill(Color.TRANSPARENT);
				orbit.setStrokeWidth(Units.ORBIT_WIDTH);
				orbit.setStroke(Color.ORANGE);
				ship.setMode(Mode.CAMERA);
				ship.setCameraDistance(Units.MOON_DIST_FROM_EARTH_CENTER * 2);
				// universe.getChildren().add(orbit);
			} else {
				// orbiter
				double shipAltitude = Units.EARTH_RADIUS
						+ Units.MOON_DIST_FROM_EARTH_CENTER;// * 1.2;
				ship.setY(shipAltitude);
				ship.setScale(1e5);
				double velocity = earthSateliteVelocity(shipAltitude);
				velocity *= 1;
				ship.setVelocity(new Point3D(velocity, 0, 0));
				ship.plotTrajectory();

				// set ship angle so we get a cool view of moon and earth
				ship.incrementPitch(90);
				ship.incrementYaw(-45);
			}
		}

	}

	private void laGrangePosition() {
		universe.getGalaxies().get(0).setEarthVisibility(true);
		universe.getGalaxies().get(0).setMoonScale(3);
		GameTime.reset();
		GameTime.setSpeedUp(1);
		Galaxy.setMoonPosIncrement(0);
		enableMoonGrav = true;
		trajectoryLength = 1592000;
		universe.getShips().get(1).setTrajRadius(Units.EARTH_RADIUS / 10);
		for (int i = 0; i < universe.getShips().size(); i++) {
			Ship ship = universe.getShips().get(i);
			if (i == 0) {
				// observer
				Circle orbit = new Circle(Units.EARTH_RADIUS * 1.1);
				orbit.setFill(Color.TRANSPARENT);
				orbit.setStrokeWidth(Units.ORBIT_WIDTH);
				orbit.setStroke(Color.ORANGE);
				ship.setMode(Mode.CAMERA);
				ship.setCameraDistance(Units.MOON_DIST_FROM_EARTH_CENTER * 2);
				// universe.getChildren().add(orbit);
			} else {
				// orbiter
				double shipAltitude = Units.L1_POS;// * 1.2;
				ship.setX(shipAltitude);
				ship.setScale(1e5);
				double velocity = Units.VELOCITY_AT_L1;
				velocity *= 1;
				ship.setVelocity(new Point3D(0, -velocity, 0));
				ship.plotTrajectory();

				// set ship angle so we get a cool view of moon and earth
				ship.incrementPitch(90);
				ship.incrementYaw(-45);
			}
		}

	}

	public static double earthSateliteVelocity(
			double sateliteDistanceToEarthCenter) {
		double earthSateliteVelocity = Math.sqrt((Units.G * Units.EARTH_MASS)
				/ sateliteDistanceToEarthCenter);
		return earthSateliteVelocity;
	}

	private void detectCollision() {
		for (int i = 0; i < universe.getMissles().size(); i++) {
			Missle m = universe.getMissles().get(i);
			Ship fromShip = m.getFromShip();
			Point3D ml = m.getLocation();
			for (int j = 0; j < universe.getShips().size(); j++) {
				Ship s = universe.getShips().get(j);
				if (ml.distance(s.getLocation()) <= EXPLODE_DISTANCE
						&& fromShip != s) {
					destroyedShips.add(s);
					universe.getShips().remove(j);
					universe.getMissles().remove(i);
					i--;
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static boolean getEnableMoonGrav() {
		return enableMoonGrav;
	}

	private static boolean enableMoonGrav = true;
	public static long trajectoryLength;
	private long frameCount = 0;
	Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	private ArrayList<Ship> destroyedShips = new ArrayList<Ship>();
	private Universe universe;
	// private Set<KeyCode> downKeys = new HashSet<KeyCode>();
}
