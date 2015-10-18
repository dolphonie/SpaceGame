package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CockpitView {

	public CockpitView(Stage windShield, Ship ship, Universe universe,
			JoyStick joyStick, ArrayList<Ship> enemies) {
		this.joyStick = joyStick;
		primaryWindow(windShield, ship, universe, joyStick, enemies);
	}

	private void primaryWindow(Stage windShield, Ship ship, Universe universe,
			JoyStick joyStick, ArrayList<Ship> enemies) {
		root = new Group();
		scene2D = new Scene(root, windShield.getWidth(), windShield.getHeight(), false);
		//scene2D.setFill(Color.BLACK);
		windShield.setScene(scene2D);

		camera = new PerspectiveCamera(true);
		camera.setNearClip(ship.getLength());
		camera.setFarClip(Units.EARTH_RADIUS*1000);
		((PerspectiveCamera) camera).setFieldOfView(60);
		((PerspectiveCamera) camera).setVerticalFieldOfView(false);
		camera.setRotationAxis(Rotate.Y_AXIS);
		scene3D = new SubScene(universe, windShield.getWidth(), windShield.getHeight(), true,
				SceneAntialiasing.BALANCED);
		scene3D.setFill(Color.BLACK);
		scene3D.widthProperty().bind(scene2D.widthProperty());
		scene3D.heightProperty().bind(scene2D.heightProperty());
		scene3D.setCamera(camera);
		hud = new HeadsUpDisplay(scene2D.widthProperty(),
				scene2D.heightProperty(), ship, joyStick, enemies);
		root.getChildren().addAll(scene3D, hud);
		ship.getChildren().add(camera);
		animate();
		windShield.show();
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (joyStick instanceof Logitech3DP) {
					Logitech3DP log = (Logitech3DP) joyStick;
					if (log.getHat() != 0) {
						camera.setRotate((log.getHat() - 0.25) * 360);
					} else {
						camera.setRotate(0);
					}
				}

			}
		}.start();

	}

	private HeadsUpDisplay hud;
	private JoyStick joyStick;
	private Camera camera;
	private Scene scene2D;
	private Group root;
	private SubScene scene3D;
}
