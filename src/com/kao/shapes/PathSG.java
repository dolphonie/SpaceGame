package com.kao.shapes;

import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class PathSG extends Group {
	public PathSG(Color strokeColor, double strokeWidth) {
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
	}

	public void moveTo(double x, double y, double z) {
		location = new Point3D(x, y, z);
	}

	public void moveTo(Point3D r) {
		moveTo(r.getX(), r.getY(), r.getZ());
	}

	private CylinderP newCylinderP(double strokeWidth, Point3D start,
			Point3D end) {
		if (unusedCylinderIndex >= cylinders.size()) {
			cylinders.add(new CylinderP(strokeWidth, start, end));
		}
		CylinderP c = cylinders.get(unusedCylinderIndex);
		c.setRadius(strokeWidth);
		c.setStart(start);
		c.setEnd(end);
		unusedCylinderIndex++;
		return c;

	}

	public void lineTo(Point3D r) {
		CylinderP step = newCylinderP(strokeWidth, location, r);
		getChildren().add(step);
		moveTo(r);
	}

	public void lineTo(double x, double y, double z) {
		lineTo(new Point3D(x, y, z));
	}

	public void clear() {
		getChildren().clear();
		unusedCylinderIndex = 0;
	}

	public void setStrokeWidth(double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	public double getStrokeWidth(){
		return strokeWidth;
	}
	
	public void setStroke(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	ArrayList<CylinderP> cylinders = new ArrayList<CylinderP>();
	int unusedCylinderIndex = 0;
	Point3D location = new Point3D(0, 0, 0);
	double strokeWidth = 1;
	Color strokeColor = Color.ORANGE;
}
