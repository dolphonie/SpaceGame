package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Logitech3DP extends JoyStick {
	private static final int ELEV_IDX = 0;
	private static final int AIL_IDX = 1;
	private static final int HAT_IDX = 2;
	private static final int RUDD_IDX = 3;
	private static final int BUTTON_IDX = 4;
	private static final int THROT_IDX = 12;
	
	private static final int TRIG_IDX = 1;
	private static final double DEAD_ZONE = 0.4;

	public Logitech3DP(Controller controller) {
		logitech3DP = controller;
		animate();
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				frameCnt++;
				if (frameCnt % 6 == 0) {
					setLogitech3DPData();
				}
			}
		}.start();
	}

	private void setLogitech3DPData() {
		logitech3DP.poll();
		Component[] comps = logitech3DP.getComponents();
		rudd = comps[RUDD_IDX].getPollData();
		throt = (-comps[THROT_IDX].getPollData() + 1) / 2;
		elev = comps[ELEV_IDX].getPollData();
		hat = comps[HAT_IDX].getPollData();
		ail = comps[AIL_IDX].getPollData();
		trig = comps[BUTTON_IDX].getPollData();
		for(int i = 0; i <buttons.length; i++){
			buttons[i] = comps[BUTTON_IDX].getPollData();
		}

	}

	public boolean getTrigger() {
		return getButton(TRIG_IDX);
	}
	
	public boolean getButton(int buttonNumber){
		return buttons[buttonNumber-1] == 1?true: false;
	}
	
	public double getRudder() {
		if (Math.abs(rudd) <= DEAD_ZONE) {
			return 0;
		}
		return rudd;
	}

	public double getHat() {
		return hat;
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

	int frameCnt;
	double rudd, knob, throt, elev, ail, hat, trig;
	double[] buttons = new double[12];
	private Controller logitech3DP;
}
