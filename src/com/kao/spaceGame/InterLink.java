package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
//actually proofread
public class InterLink extends JoyStick {
	private static final int RUDD_IDX = 1;
	private static final int KNOB_IDX = 2;
	private static final int THROT_IDX = 3;
	private static final int ELEV_IDX = 4;
	private static final int AIL_IDX = 5;
	private static final int RATE_IDX = 6;
	private static final int HOLD_IDX = 7;
	private static final int RESET_IDX = 8;
	private static final int IDLE2_IDX = 9;
	private static final int NORM_IDX = 10;
	private static final double DEAD_ZONE = 0.03;

	public InterLink(Controller controller) {
		interLink = controller;
		animate();
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				frameCnt++;
				if (frameCnt % 6 == 0) {
					setInterLinkData();
				}
			}
		}.start();
	}

	private void setInterLinkData() {
		interLink.poll();
		Component[] comps = interLink.getComponents();
		rudd = comps[RUDD_IDX].getPollData();
		knob = comps[KNOB_IDX].getPollData();
		throt = (-comps[THROT_IDX].getPollData() + 1) / 2;
		elev = comps[ELEV_IDX].getPollData();
		ail = comps[AIL_IDX].getPollData();
		rate = comps[RATE_IDX].getPollData();
		hold = comps[HOLD_IDX].getPollData();
		reset = comps[RESET_IDX].getPollData();
		idle2 = comps[IDLE2_IDX].getPollData();
		norm = comps[NORM_IDX].getPollData();

	}

	

	public double getRudder() {
		if (Math.abs(rudd) <= DEAD_ZONE) {
			return 0;
		}
		return rudd;
	}

	public double getKnob() {
		return knob;
	}

	public double getThrottle() {
		if (Math.abs(throt) <= DEAD_ZONE) {
			return 0;
		}
		return throt;
	}

	public double getElevator() {
		if (Math.abs(elev) <= DEAD_ZONE) {
			return 0;
		}
		return elev;
	}

	public double getAileron() {
		if (Math.abs(ail) <= DEAD_ZONE) {
			return 0;
		}
		return ail;
	}

	public boolean getRate() {
		return rate == 1.0 ? true : false;
	}

	public boolean getHold() {
		return hold == 1.0 ? true : false;
	}

	public boolean getReset() {
		return reset == 1.0 ? true : false;
	}

	public boolean getIdle2() {
		return idle2 == 1.0 ? true : false;
	}

	public boolean getNormal() {
		return norm == 1.0 ? true : false;
	}

	int frameCnt;
	double rudd, knob, throt, elev, ail, rate, hold, reset, idle2, norm;
	private Controller interLink;
}
