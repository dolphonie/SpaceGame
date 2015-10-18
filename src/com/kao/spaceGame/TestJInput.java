package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;


public class TestJInput extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
    public void start(Stage stage) {
        stage.setTitle("TestJInput");
        stage.setScene(makeScene());
        stage.show();
        animate();
    }
	
	private Scene makeScene() {
    	findJoysticks(joySticks);
    	updateLabel();
		return new Scene(new Group(label));
	}
	
	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				frameCnt++;
				if (frameCnt % 6 ==0) {
					updateLabel();
				}
			}
		}.start();
	}
	private void updateLabel() {
		label.setText(showJoyStickData());
	}
		
	private String showJoyStickData() {
		if (joySticks.size()==0) {
			System.out.println("No Joysticks found");
			System.exit(1);
		}
		StringBuffer sb = new StringBuffer();
		for (Controller joyStick : joySticks) {
			joyStick.poll();
			sb.append("--------"+joyStick.getName()+"--------\n");
			for (Component comp : joyStick.getComponents()) {
				sb.append(comp.getName() + " = " + comp.getPollData()+ "\n");
			}
		}
		return sb.toString();
	}
	
	private void findJoysticks(ArrayList<Controller> joySticks) {
		ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
		Controller[] conts = env.getControllers();
		System.out.println("Finding controllers");
		for (Controller cont : conts) {
			String name = cont.getName();
			Controller.Type type = cont.getType();
			int nbComponents = cont.getComponents().length;
			int nbControllers = cont.getControllers().length;
					
			System.out.println("Controller: " + name +
					", type: " + type + 
					", port#: " + cont.getPortNumber() +
					", # components: " + nbComponents+
					", # controllers: " + nbControllers);

			listComponents(cont);

			if (type==Controller.Type.STICK) {
				joySticks.add(cont);
			}
		}
	}
	private void listComponents(Controller cont) {
		Component[] comps = cont.getComponents();
		for (Component comp : comps) {
			System.out.printf(
					"  name: %1$-15s , ID: %2$-15s, %3$-7s, %4$-9s, deadZone: %5$10f\n", 
					comp.getName(), 
					comp.getIdentifier(),
					(comp.isAnalog() ? "analog" : "digital"),
					(comp.isRelative() ? "relative" : "absolute"),
					comp.getDeadZone());
		}
	}
	long frameCnt = 0;
	ArrayList<Controller> joySticks = new ArrayList<Controller>();
	Label label = new Label();

}
