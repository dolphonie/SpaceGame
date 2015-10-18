package com.kao.shapes;

import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class CylinderP extends Cylinder {
	
	public CylinderP(double radius, Point3D start, Point3D end) {
		//super(radius,0,3);
		setRadius(radius);
		this.start = start;
		this.end = end;
		getTransforms().add(transform);
		reshape();
	}

	public void setStart(Point3D start) {
		this.start = start;
		reshape();
	}

	public void setEnd(Point3D end) {
		this.end = end;
		reshape();
	}

	private void reshape() {
		Point3D relativePoint = new Point3D(end.getX() - start.getX(),
				end.getY() - start.getY(), end.getZ() - start.getZ());
		SphericalCoords coord = new SphericalCoords(relativePoint);
		setHeight(coord.getRadius());
		Point3D origin = new Point3D(0, 0, 0);
		transform.setToIdentity();
		transform.prependTranslation(0, getHeight() / 2, 0);
		transform.prependRotation(-90, origin, Rotate.Z_AXIS);
		transform.prependRotation(-coord.getElevation(), origin, Rotate.Y_AXIS);
		transform.prependRotation(coord.getAzimuth(), origin, Rotate.Z_AXIS);
		transform.prependTranslation(start.getX(), start.getY(), start.getZ());
	}

	Point3D start, end;
	Affine transform = new Affine();
}
