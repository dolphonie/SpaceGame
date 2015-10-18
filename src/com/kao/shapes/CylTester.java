package com.kao.shapes;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class CylTester extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		try {
			myStart(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void myStart(Stage primaryStage) {
		primaryStage.setTitle("Cylinder Tester(Only $99999999999999.99)");
		Group root = new Group();
		Scene scene = new Scene(root, 800, 800, true);
		scene.setFill(Color.BLACK);
//		Camera cam = new ParallelCamera();
//		Camera cam = new PerspectiveCamera(false);
//		cam.setFarClip(00);
//		cam.setNearClip(100000);
//		scene.setCamera(cam);
		primaryStage.setScene(scene);
		cyl = new CylinderP(10, new Point3D(400, 400, 100), new Point3D(300,
				300, -100));
		cyl.setMaterial(new PhongMaterial(Color.BLUE));
		sphere = new Sphere(10);
		sphere.setMaterial(new PhongMaterial(Color.RED));
//		AmbientLight ambient = new AmbientLight();
//		ambient.setColor(Color.WHITE);
		PointLight point = new PointLight();
		point.setColor(Color.WHITE);
		point.getScope().addAll(cyl, sphere);
		point.setLayoutX(400);
		point.setLayoutY(400);
		
		root.getChildren().addAll(sphere, cyl, point);
		primaryStage.show();
		animate();
	}

	private double degToRad(double deg) {
		return deg * 2 * Math.PI / 360;
	}

	private void animate() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (increase) {
					z += .2;
					if (z>=200) increase = false;
				} else {
					z -= .2;
					if (z<=-200) increase = true;
				}
				
				
				angle= (angle + 1)%360;
				double x = 100* Math.cos(degToRad(angle)) + 400;
				double y = 100* Math.sin(degToRad(angle)) + 400;
				double zz = z + 100;
				Point3D end = new Point3D(x,y,zz);
				cyl.setEnd(end);
				sphere.setTranslateX(x);
				sphere.setTranslateY(y);
				sphere.setTranslateZ(zz);
			}
		}.start();
	}

	CylinderP cyl;
	Sphere sphere;
	int angle = 0;
	double z = 0;
	boolean increase = false;
}
