package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.geometry.Point3D;

import com.kao.spaceGame.SpaceGame;
import com.kao.spaceGame.State;
import com.kao.spaceGame.StepResult;

public abstract class Integrator {
	private static final double DT = 100;
	private static final double R = 200;
	private static final double CIRC_V = 10;
	private static final double A = CIRC_V * CIRC_V / R;
	private static final double ELIP_V = CIRC_V * Math.sqrt(2) - .0001;
	private static final boolean LOGGING = false;

	public State advanceTime(ArrayList<State> intermediateStates, State start,
			double timeAdvanced) {
		Point3D pos = start.pos;
		Point3D vel = start.vel;
		Point3D accel = null;
		accel = acceleration(pos, 0);
		double t;
		for (t = 0; t < timeAdvanced; t += dt) {

			dt *= 2;
			dt = Math.min(dt, timeAdvanced - t);
			if (LOGGING) {
				System.out.printf("T=%10.4f\n", t);
			}
			StepResult nextStep;
			while (true) {
				nextStep = timeStep(pos, vel, accel,t, dt);
				if (nextStep != null) {
					double diff = normalizedMagnitudeDifference(nextStep.aNext,
							accel);
					if (diff <= .01)
						break;
				}
				dt /= 2;
			}
			if (LOGGING) {
				System.out.printf("  dt = %10.5f\n", dt);
			}
			pos = nextStep.pNext;
			vel = nextStep.vNext;
			accel = nextStep.aNext;
			if (LOGGING) {
				System.out.println("pos=" + pos);
				System.out.println("vel=" + vel);
			}
			if (intermediateStates != null) {
				intermediateStates.add(new State(pos, vel));
			}
		}
		if(LOGGING)
			System.out.printf("Difference: %3.6f\n", timeAdvanced - t);
		return new State(pos, vel);
	}

	private StepResult timeStep(Point3D pos, Point3D vel, Point3D accel, double t,
			double dt) {
		Point3D aEff = accel;
		double oldError = Double.MAX_VALUE;
		while (true) {
			StepResult nextStep = relaxStep(pos, vel, aEff, t, dt);
			Point3D actualAccel = average(nextStep.aNext, accel);
			double error = normalizedMagnitudeDifference(actualAccel,
					aEff);
			if (LOGGING) {
				System.out.printf("  error = %11.7f\n", error);
			}
			if (error < 1e-6) {
				return nextStep;
			}
			if (oldError <= error) {
				if (LOGGING) {
					System.out.printf("Relaxation Not Converging!\n");
				}
				return null;
			}
			aEff = actualAccel;
			oldError = error;
		}
	}

	private Point3D average(Point3D p1, Point3D p2) {
		return p1.midpoint(p2);
	}

	private StepResult relaxStep(Point3D pOld, Point3D vOld, Point3D aEff,
			double t, double dt) {
		Point3D nextVel = vOld.add(aEff.multiply(dt));
		Point3D velAverage = nextVel.midpoint(vOld);
		Point3D nextPos = pOld.add(velAverage.multiply(dt));
		Point3D nextAccel = acceleration(nextPos, t + dt);
		return new StepResult(nextPos, nextVel, nextAccel);
	}

	private double normalizedMagnitudeDifference(Point3D p1, Point3D p2) {
		return p1.distance(p2) / p1.magnitude();
	}

	public abstract Point3D acceleration(Point3D rPos, double t);

	double dt = DT;
}
