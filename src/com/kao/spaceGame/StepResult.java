package com.kao.spaceGame;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class StepResult {
	public StepResult(Point3D pNext, Point3D vNext, Point3D aNext) {
		this.pNext = pNext;
		this.vNext = vNext;
		this.aNext = aNext;
	}
	
	public Point3D pNext, vNext, aNext;
}
