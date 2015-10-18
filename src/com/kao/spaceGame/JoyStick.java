package com.kao.spaceGame;

import java.util.ArrayList;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public abstract class JoyStick {
	public abstract double getElevator();
	public abstract double getRudder();
	public abstract double getThrottle();
	public abstract double getAileron();
	
	public static ArrayList<JoyStick> findJoySticks() {
		ArrayList<JoyStick> joySticks = new ArrayList<JoyStick>();

		ControllerEnvironment env = ControllerEnvironment
				.getDefaultEnvironment();
		Controller[] conts = env.getControllers();
		System.out.println("Finding controllers");
		for (Controller cont : conts) {
			String name = cont.getName();
			Controller.Type type = cont.getType();
			int nbComponents = cont.getComponents().length;
			int nbControllers = cont.getControllers().length;

			System.out.println("Controller: " + name + ", type: "
					+ cont.getType() + ", port#: " + cont.getPortNumber()
					+ ", # components: " + nbComponents + ", # controllers: "
					+ nbControllers);
			
			if (type==Controller.Type.STICK) {
				if(name.equals("InterLink")){
					joySticks.add(new InterLink(cont));
				}else if(name.equals("Logitech Extreme 3D")){
					joySticks.add(new Logitech3DP(cont));
				}else {
					System.out.println("Unrecognized Contoller: "+name);
				}
				

			}
		}
		return joySticks;
	}
}
