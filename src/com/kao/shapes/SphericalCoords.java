package com.kao.shapes;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class SphericalCoords {
	public SphericalCoords(Point3D p){
		radius  = Math.sqrt(Math.pow(p.getX(),2)+Math.pow(p.getY(),2)+Math.pow(p.getZ(),2));
		azimuth = radiansToDegrees(Math.atan2(p.getY(),p.getX()));
		elevation = radiansToDegrees(Math.asin(p.getZ()/radius));
		
	}
	
	private double radiansToDegrees(double radians){
		return radians*(360/(Math.PI*2));
	}
	
	public double getRadius(){
		return radius;
	}
	
	public double getAzimuth(){
		return azimuth;
	}
	
	public double getElevation(){
		return elevation;
	}
	
	double radius;
	double azimuth;
	double elevation;
}
