package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import com.kao.shapes.PathSG;

//proofread
public class Ship extends Group implements AvatarFactory, Tickable {
	public static boolean POSITION_ORIGIN = false;
	public static boolean ORIENTATION_Z = false;
	public static final long FIRING_INTERVAL = 100000000L;
	private static final Point3D ORIGIN = new Point3D(0, 0, 0);
	private static final boolean CONST_FORCE = false;
	private static final double ROTATE_DEGREES_PER_SECOND = 50;
	private static final boolean LOGGING = false;

	public Group createAvatar() {
		Ship s = new Ship(shipPos, rollPitchYaw, color, this);
		shipAvatars.add(s);
		return s;
	}

	public Group getAvatar(int i) {
		return shipAvatars.get(i);
	}

	private void fireMissle() {
		Missle missle = new Missle(getLocation(), getVelocity(), color,
				rollPitchYaw, this);
		universe.getMissles().add(missle);
	}

	private Ship(Translate shipPos, Affine rollPitchYaw, Color color, Ship ship) {
		this.shipPos = shipPos;
		this.rollPitchYaw = rollPitchYaw;
		this.color = color;
		original = ship;
		getTransforms().addAll(shipPos, rollPitchYaw);
		makeGeometry(color);

	}

	public boolean isAvatar() {
		return original != null;
	}

	public Ship(JoyStick joystick, Color color, Universe universe) {
		this.universe = universe;
		shipNum = shipCount++;
		this.joyStick = joystick;
		this.color = color;
		getTransforms().addAll(shipPos, rollPitchYaw);
		makeGeometry(color);
		resetAll();
		integrate();
		universe.getChildren().addAll(traj);
		// animate();
		GameTime.add(this);
	}

	private void integrate() {
		shipIntegrator = new Integrator() {
			public Point3D acceleration(Point3D rPos, double t) {
				return thrustAndGravityAcceleration(rPos, t);
			}
		};
		trajectoryIntegrator = new Integrator() {
			public Point3D acceleration(Point3D rPos, double t) {
				return gravityAcceleration(rPos, t);
			}
		};
	}

	private void makeGeometry(Color color) {
		geometry = new XWing(color);
		getChildren().addAll(geometry);
	}

	public Point3D getLocation() {
		return new Point3D(shipPos.getX(), shipPos.getY(), shipPos.getZ());
	}

	public void plotTrajectory() {
		long oldTime = System.nanoTime();
		ArrayList<State> states = new ArrayList<State>();
		trajectoryIntegrator.advanceTime(states, new State(getLocation(),
				velocity), SpaceGame.trajectoryLength);
		long calcTime = System.nanoTime();
		if (LOGGING)
			System.out.printf("Integrate Time: %8.4f",
					(calcTime - oldTime) / 1e9);
		traj.clear();
		traj.moveTo(getLocation());
		for (int i = 0; i < states.size(); i += 10) {
			traj.lineTo(states.get(i).pos);
		}
		if (states.size() >= 1) {
			traj.lineTo(states.get(states.size() - 1).pos);
		}

		if (LOGGING)
			System.out.printf("    Draw Time: %8.4f\n",
					(System.nanoTime() - calcTime) / 1e9);
	}

	// private void animate() {
	// new AnimationTimer() {
	// @Override
	// public void handle(long now) {
	// double timeElapsed = now - oldNow;
	// oldNow = now;
	// if (firstIteration) {
	// firstIteration = false;
	// return;// skips rest of code
	// }
	// if (now > lastBulletTime + FIRING_INTERVAL) {
	// if (joyStick instanceof Logitech3DP) {
	// if (((Logitech3DP) joyStick).getTrigger()) {
	// fireMissle();
	// lastBulletTime = now;
	// }
	// }
	// }
	// double rotateDegrees = timeElapsed * ROTATE_DEGREES_PER_SECOND
	// / 1e9;
	//
	// double joyDir = mode == Mode.CAMERA ? -1 : 1;
	// incrementYaw(joyDir * joyStick.getRudder() * rotateDegrees);
	// incrementPitch(joyDir * joyStick.getElevator() * rotateDegrees);
	// incrementRoll(joyDir * joyStick.getAileron() * rotateDegrees);
	//
	// if (mode == Mode.CAMERA) {
	// Point3D shipDir = rollPitchYaw.transform(Rotate.Z_AXIS);
	// setLocation(shipDir.multiply(-cameraDistance));
	// }
	//
	// if (mode == Mode.MOBILE) {
	// State s = shipIntegrator.advanceTime(null, new State(
	// getLocation(), velocity), ((timeElapsed) / 1e9)
	// * SpaceGame.speedUp);
	// if (counter >= 5) {
	// plotTrajectory();
	// counter = 0;
	// }
	// setLocation(s.pos);
	// velocity = s.vel;
	// counter++;
	// }
	// }
	// }.start();
	// }

	public Point3D gravityAcceleration(Point3D rPos, double t) {
		double earthAccel = earthGravAccel(rPos);
		double moonAccel = moonGravAccel(rPos, t);
		Point3D vectEarthAccel = Units.EARTH_LOCATION.subtract(rPos)
				.normalize().multiply(earthAccel);
		Point3D vectMoonAccel = Galaxy.getMoonPos(t).subtract(rPos).normalize()
				.multiply(moonAccel);
		if (LOGGING)
			System.out.printf("Moon Force = %3.3f  Earth Force = %3.3f\n",
					moonAccel, earthAccel);
		return vectEarthAccel.add(vectMoonAccel);
	}

	private double earthGravAccel(Point3D rPos) {
		return (Units.G * Units.EARTH_MASS)
				/ Math.pow(rPos.distance(Units.EARTH_LOCATION), 2);
	}

	private double moonGravAccel(Point3D rPos, double t) {
		return SpaceGame.getEnableMoonGrav() ? (Units.G * Units.MOON_MASS)
				/ Math.pow(rPos.distance(Galaxy.getMoonPos(t)), 2) : 0;
	}

	public Point3D thrustAndGravityAcceleration(Point3D rPos, double t) {
		double thrust = joyStick.getThrottle() * Units.SHIP_THRUST;
		Point3D shipDir = rollPitchYaw.transform(Rotate.Z_AXIS);
		Point3D thrustAcceleration = shipDir.multiply(thrust);
		return thrustAcceleration.add(gravityAcceleration(rPos, t));
	}

	public void setScale(double scale) {
		geometry.setScaleX(scale);
		geometry.setScaleY(scale);
		geometry.setScaleZ(scale);
	}

	public double getLength() {
		return geometry.getLength();
	}

	public void setX(double xPos) {
		shipPos.setX(POSITION_ORIGIN ? 0 : xPos);
	}

	public void setY(double yPos) {
		shipPos.setY(POSITION_ORIGIN ? 0 : yPos);
	}

	public void setZ(double zPos) {
		shipPos.setZ(POSITION_ORIGIN ? 0 : zPos);
	}

	public void setLocation(Point3D pos) {
		shipPos.setX(pos.getX());
		shipPos.setY(pos.getY());
		shipPos.setZ(pos.getZ());
	}

	public double getX() {
		return shipPos.getX();
	}

	public double getY() {
		return shipPos.getY();
	}

	public double getZ() {
		return shipPos.getZ();
	}

	public void setVelocity(Point3D velocity) {
		this.velocity = velocity;
	}

	public void incrementYaw(double degrees) {
		rollPitchYaw.appendRotation(degrees, ORIGIN, Rotate.Y_AXIS);
	}

	public void incrementPitch(double degrees) {
		rollPitchYaw.appendRotation(degrees, ORIGIN, Rotate.X_AXIS);
	}

	public void incrementRoll(double degrees) {
		rollPitchYaw.appendRotation(degrees, ORIGIN, Rotate.Z_AXIS);
	}

	// public void resetRollPitchYaw() {
	// rollPitchYaw.setToIdentity();
	// }

	// private void setXVel(double xVel) {
	// this.xVel = xVel;
	// }
	//
	// private void setYVel(double yVel) {
	// this.yVel = yVel;
	// }
	//
	// private void setZVel(double zVel) {
	// this.zVel = zVel;
	// }
	public Point3D getVelocity() {
		return isAvatar() ? original.getVelocity() : velocity;
	}

	public Color getColor() {
		return color;
	}

	// public void setStationary(boolean stationary) {
	// this.stationary = stationary;
	// }

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public void resetAll() {
		rollPitchYaw.setToIdentity();
		shipPos.setX(0);
		shipPos.setY(0);
		shipPos.setZ(0);
		velocity = ORIGIN;
	}

	public void setCameraDistance(double cameraDistance) {
		this.cameraDistance = cameraDistance;
	}

	public double getCameraDistance() {
		return cameraDistance;
	}

	public void tick(double now, double timeSincePrevious,
			double realTimeSincePrevious) {
		if (now > lastBulletTime + FIRING_INTERVAL) {
			if (joyStick instanceof Logitech3DP) {
				if (((Logitech3DP) joyStick).getTrigger()) {
					fireMissle();
					lastBulletTime = now;
				}
			}
		}
		double rotateDegrees = realTimeSincePrevious
				* ROTATE_DEGREES_PER_SECOND;

		double joyDir = mode == Mode.CAMERA ? -1 : 1;
		incrementYaw(joyDir * joyStick.getRudder() * rotateDegrees);
		incrementPitch(joyDir * joyStick.getElevator() * rotateDegrees);
		incrementRoll(joyDir * joyStick.getAileron() * rotateDegrees);

		if (mode == Mode.CAMERA) {
			Point3D shipDir = rollPitchYaw.transform(Rotate.Z_AXIS);
			setLocation(shipDir.multiply(-cameraDistance));
		}

		if (mode == Mode.MOBILE) {
			State s = shipIntegrator.advanceTime(null, new State(getLocation(),
					velocity), timeSincePrevious);
			if (counter >= 5) {
				plotTrajectory();
				counter = 0;
			}
			setLocation(s.pos);
			velocity = s.vel;
			earthAccel = earthGravAccel(s.pos);
			moonAccel = moonGravAccel(s.pos, timeSincePrevious);
			counter++;
		}
	}

	public double getMoonGravity() {
		return isAvatar() ? original.getMoonGravity() : moonAccel;
	}

	public double getEarthGravity() {
		return isAvatar() ? original.getEarthGravity() : earthAccel;
	}

	public enum Mode {
		STATIONARY, CAMERA, MOBILE
	};
	
	public void setTrajRadius(double r){
		traj.setStrokeWidth(r);
	}
	
	public double getTrajRadius(){
		return traj.getStrokeWidth();
	}
	
	private double moonAccel, earthAccel;
	private Ship original = null;
	private Integrator shipIntegrator, trajectoryIntegrator;
	private XWing geometry;
	private int counter;
	private double cameraDistance = Units.EARTH_RADIUS * 3;
	private Mode mode = Mode.MOBILE;
	private Color color;
	private ArrayList<Ship> shipAvatars = new ArrayList<Ship>();
	private JoyStick joyStick;
	private Translate shipPos = new Translate();
	private Affine rollPitchYaw = new Affine();
	private Point3D velocity = ORIGIN;
	private Universe universe;
	private double lastBulletTime;
	private int shipNum;
	private static int shipCount = 0;
	private PathSG traj = new PathSG(Color.ORANGE, 0);
}
