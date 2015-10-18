package com.kao.spaceGame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
//proofread
public class Missle extends Group implements AvatarFactory {
	private static final int MISSLE_WIDTH =  10;
	private static final int NUM_DIVISIONS = 10;
	private static final int BULLET_VELOCITY = 1000;
	public Missle(Point3D muzzleLocation,Point3D velocity, Color color, Affine rollPitchYaw, Ship fromShip) {
		this.fromShip = fromShip;
		m = new PhongMaterial(color);
		makeGeometry(m);
		pos = new Translate(muzzleLocation.getX(),muzzleLocation.getY(),muzzleLocation.getZ());
		getTransforms().addAll(pos);
		Point3D bulletVelocity = rollPitchYaw.transform(0,0,BULLET_VELOCITY);
		Vx = bulletVelocity.getX()+velocity.getX();
		Vy = bulletVelocity.getY()+velocity.getY();
		Vz = bulletVelocity.getZ()+velocity.getZ();
		animate();
	}

	private Missle(Transform pos, Material material, Missle m) {
		makeGeometry(material);
		original = m;
		getTransforms().addAll(pos);
	}

	private void animate() {
		new AnimationTimer() {
			public void handle(long now) {
				pos.setX(pos.getX() + Vx);
				pos.setY(pos.getY() + Vy);
				pos.setZ(pos.getZ() + Vz);
			}
		}.start();
	}
	
	public boolean isAvatar(){
		return original!=null;
	}
	
	private void makeGeometry(Material m) {
		Sphere missle = new Sphere(MISSLE_WIDTH, NUM_DIVISIONS);
		missle.setMaterial(m);
		getChildren().add(missle);
	}

	public Point3D getLocation() {
		return new Point3D(pos.getX(), pos.getY(), pos.getZ());
	}

	public Group createAvatar() {
		Missle ma = new Missle(pos, m,this);
		missleAvatars.add(ma);
		return ma;
	}

	public Group getAvatar(int i) {
		return missleAvatars.get(i);
	}

	public Ship getFromShip(){
		return fromShip;
	}
	
	private ArrayList<Missle> missleAvatars = new ArrayList<Missle>();
	
	private Missle original;
	private Ship fromShip;
	private Material m;
	private Translate pos;
	private double Vx,Vy,Vz;
}
