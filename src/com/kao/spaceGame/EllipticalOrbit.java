package com.kao.spaceGame;

public class EllipticalOrbit {
	public EllipticalOrbit(double vPeriapsis, double rPeriapsis, double gm) {
		this.rPeriapsis = rPeriapsis;
		this.gm = gm;
		double numerator = Math.pow(vPeriapsis*rPeriapsis, 2); 
		double denominator = 2*gm - vPeriapsis*vPeriapsis*rPeriapsis;
		rApoapsis = numerator / denominator;
	}
	public double semiMajorAxisLength() {
		return (rPeriapsis + rApoapsis)/2;
	}
	public double semiMinorAxisLength() {
		return Math.sqrt(rPeriapsis*rApoapsis);
	}
	public double focusToPeriapsis() {
		return rPeriapsis;
	}
	public double focusToApoapsis() {
		return rApoapsis;
	}
	public double focusToCenter() {
		return semiMajorAxisLength() - rPeriapsis;
	}
	public double period() {
		return 2*Math.PI*Math.sqrt(Math.pow(semiMajorAxisLength(),3)/gm);
	}
	private double rPeriapsis;
	private double rApoapsis; 
	private double gm;
}

