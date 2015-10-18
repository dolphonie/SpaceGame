package com.kao.spaceGame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class XWing extends Group {
	private static final double RADIANS_PER_DEGREE = 2 * Math.PI / 360; 
	private static final double WING_ROTATION = 17;
	private static final int WING_SPAN = 160;
	private static final int WING_LENGTH = (int) (WING_SPAN / Math.cos(WING_ROTATION * RADIANS_PER_DEGREE));
	private static final int WING_THICKNESS = 2;
	private static final int WING_OFFSET = -10;
	private static final int WING_CHORD = 40;

	private static final int REAR_FUSE_DIAM = 30;
	private static final int REAR_FUSE_LENGTH = 55;
	private static final int MID_FUSE_DIAM = 29;
	private static final int MID_FUSE_LENGTH = 57;
	private static final int FRONT_FUSE_DIAM = 14;
	private static final int FRONT_FUSE_LENGTH = 47;

	private static final int FRONT_ENG_DIAM = 13;
	private static final int FRONT_ENG_LENGTH = 33;
	private static final int FRONT_ENG_OFFSET = -5;
	private static final int REAR_ENG_DIAM = 10;
	private static final int REAR_ENG_LENGTH = 32;

	private static final int REAR_GUN_DIAM = 6;
	private static final int REAR_GUN_LENGTH = 30;
	private static final int GUN_BARREL_DIAM = 3;
	private static final int GUN_BARREL_LENGTH = 62;

	private static final int MID_FUSE_CENTER_Z = 0,
			MID_FUSE_FRONT_Z = MID_FUSE_CENTER_Z + MID_FUSE_LENGTH / 2,
			MID_FUSE_REAR_Z = MID_FUSE_CENTER_Z - MID_FUSE_LENGTH / 2,
			FRONT_ENG_FRONT_Z = MID_FUSE_REAR_Z + FRONT_ENG_OFFSET,
			FRONT_ENG_REAR_Z = FRONT_ENG_FRONT_Z - FRONT_ENG_LENGTH,
			WING_FRONT_Z = MID_FUSE_REAR_Z + WING_OFFSET,
			REAR_GUN_FRONT_Z = WING_FRONT_Z;

	public XWing(Color color) {
		material = new PhongMaterial(color);
		getChildren().addAll(makeWings(), makeFuse());
		setTranslateY(MID_FUSE_DIAM / 2);
	}

	private Group makeGun() {
		Group gun = new Group();
		Cylinder gunBase = new Cylinder(REAR_GUN_DIAM / 2, REAR_GUN_LENGTH);
		gunBase.setMaterial(material);
		gunBase.setRotationAxis(Rotate.X_AXIS);
		gunBase.setRotate(90);
		gunBase.setTranslateZ(REAR_GUN_FRONT_Z - REAR_GUN_LENGTH / 2);

		Cylinder gunBarrel = new Cylinder(GUN_BARREL_DIAM / 2,
				GUN_BARREL_LENGTH);
		gunBarrel.setMaterial(new PhongMaterial(Color.GREY));
		gunBarrel.setRotationAxis(Rotate.X_AXIS);
		gunBarrel.setRotate(90);
		gunBarrel.setTranslateZ(REAR_GUN_FRONT_Z + GUN_BARREL_LENGTH / 2);

		gun.getChildren().addAll(gunBase, gunBarrel);
		return gun;

	}

	private Group makeEngine() {
		Group engine = new Group();
		Cylinder front = new Cylinder(FRONT_ENG_DIAM / 2, FRONT_ENG_LENGTH);
		front.setMaterial(material);
		front.setRotationAxis(Rotate.X_AXIS);
		front.setRotate(90);
		front.setTranslateZ(FRONT_ENG_FRONT_Z - FRONT_ENG_LENGTH / 2);

		Cylinder rear = new Cylinder(REAR_ENG_DIAM / 2, REAR_ENG_LENGTH);
		rear.setMaterial(material);
		rear.setRotationAxis(Rotate.X_AXIS);
		rear.setRotate(90);
		rear.setTranslateZ(FRONT_ENG_REAR_Z - REAR_ENG_LENGTH / 2);

		engine.getChildren().addAll(front, rear);
		return engine;
	}

	private Group makeWings() {
		Group wings = new Group();

		Group clockWing = makeWing(true);
		clockWing.setRotationAxis(Rotate.Z_AXIS);
		clockWing.setRotate(WING_ROTATION);

		Group countWing = makeWing(false);
		countWing.setRotationAxis(Rotate.Z_AXIS);
		countWing.setRotate(-WING_ROTATION);

		wings.getChildren().addAll(clockWing, countWing);
		return wings;
	}

	private Group makeWing(boolean clock) {
		Group wing = new Group();
		Box w = new Box(WING_LENGTH, WING_CHORD, WING_THICKNESS);
		w.setMaterial(material);
		w.setRotationAxis(Rotate.X_AXIS);
		w.setRotate(90);
		w.setTranslateZ(WING_FRONT_Z - WING_CHORD / 2);
		if (clock) {
			Group leftEng = makeEngine();
			leftEng.setTranslateX(-REAR_FUSE_DIAM / 2 - FRONT_ENG_DIAM / 2);
			leftEng.setTranslateY(-WING_THICKNESS / 2 - FRONT_ENG_DIAM / 2);
			Group rightEng = makeEngine();
			rightEng.setTranslateX(REAR_FUSE_DIAM / 2 + FRONT_ENG_DIAM / 2);
			rightEng.setTranslateY(WING_THICKNESS / 2 + FRONT_ENG_DIAM / 2);

			Group leftGun = makeGun();
			leftGun.setTranslateX(-WING_LENGTH / 2);
			leftGun.setTranslateY(-WING_THICKNESS / 2 - REAR_GUN_DIAM / 2);
			Group rightGun = makeGun();
			rightGun.setTranslateX(WING_LENGTH / 2);
			rightGun.setTranslateY(WING_THICKNESS / 2 + REAR_GUN_DIAM / 2);

			wing.getChildren().addAll(w, leftEng, rightEng, leftGun, rightGun);

		} else {
			Group leftEng = makeEngine();
			leftEng.setTranslateX(-REAR_FUSE_DIAM / 2 - FRONT_ENG_DIAM / 2);
			leftEng.setTranslateY(WING_THICKNESS / 2 + FRONT_ENG_DIAM / 2);
			Group rightEng = makeEngine();
			rightEng.setTranslateX(REAR_FUSE_DIAM / 2 + FRONT_ENG_DIAM / 2);
			rightEng.setTranslateY(-WING_THICKNESS / 2 - FRONT_ENG_DIAM / 2);

			Group leftGun = makeGun();
			leftGun.setTranslateX(-WING_LENGTH / 2);
			leftGun.setTranslateY(WING_THICKNESS / 2 + REAR_GUN_DIAM / 2);
			Group rightGun = makeGun();
			rightGun.setTranslateX(WING_LENGTH / 2);
			rightGun.setTranslateY(-WING_THICKNESS / 2 - REAR_GUN_DIAM / 2);

			wing.getChildren().addAll(w, leftEng, rightEng, leftGun, rightGun);
		}

		return wing;
	}

	private Group makeFuse() {
		Group fuse = new Group();
		Cylinder rearFuse = new Cylinder(REAR_FUSE_DIAM / 2, REAR_FUSE_LENGTH);
		rearFuse.setMaterial(material);
		rearFuse.setRotationAxis(Rotate.X_AXIS);
		rearFuse.setRotate(90);
		rearFuse.setTranslateZ(MID_FUSE_REAR_Z - REAR_FUSE_LENGTH / 2);
		
		Cylinder midFuse = new Cylinder(MID_FUSE_DIAM / 2, MID_FUSE_LENGTH);
		midFuse.setMaterial(material);
		midFuse.setRotationAxis(Rotate.X_AXIS);
		midFuse.setRotate(90);
		midFuse.setTranslateZ(MID_FUSE_CENTER_Z);
		
		Cylinder frontFuse = new Cylinder(FRONT_FUSE_DIAM / 2,
				FRONT_FUSE_LENGTH);
		frontFuse.setMaterial(material);
		frontFuse.setRotationAxis(Rotate.X_AXIS);
		frontFuse.setRotate(90);
		frontFuse.setTranslateZ(MID_FUSE_FRONT_Z + FRONT_FUSE_LENGTH / 2);
		fuse.getChildren().addAll(rearFuse, midFuse, frontFuse);
		return fuse;
	}
	
	public double getLength(){
		return REAR_FUSE_LENGTH + MID_FUSE_LENGTH + FRONT_FUSE_LENGTH;
	}
	private void makeGeometry(Color color) {
		// Cylinder fuse = new Cylinder(SHIP_SIZE,SHIP_SIZE*3,10);
		// fuse.setRotationAxis(Rotate.X_AXIS);
		// fuse.setRotate(90);
		// fuse.setMaterial(new PhongMaterial(color));
		//
		// Rectangle wing1 = new Rectangle(SHIP_SIZE/2,SHIP_SIZE*10,color);
		// wing1.setRotationAxis(Rotate.Z_AXIS);
		// wing1.setRotate(30);
		// wing1.setRotationAxis(Rotate.X_AXIS);
		// wing1.setRotate(90);
		// //wing1.setTranslateX(wng)
		//
		//
		// Rectangle wing2 = new Rectangle(SHIP_SIZE/2,SHIP_SIZE*10,color);
		// wing2.setRotationAxis(Rotate.Z_AXIS);
		// wing2.setRotate(-30);
		// wing2.setRotationAxis(Rotate.X_AXIS);
		// wing2.setRotate(90);
		//
		// getChildren().addAll(fuse,wing1,wing2);
	}

	private Material material;
}
