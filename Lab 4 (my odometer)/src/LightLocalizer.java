/*
 * lightlocalizer.java
 * 
 * Satyajit Kanetkar
 * Sean Wolfe
 */
import java.util.ArrayList;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class LightLocalizer {
	private Odometer odo;
	private Driver robot;
	private ColorSensor cs;
	private final double D_LIGHT_TO_CENTER = 12;
	
	NXTRegulatedMotor leftMotor = Motor.A;
	NXTRegulatedMotor rightMotor = Motor.B;
	
	public static int counter = 0;

	ArrayList<Double> angles = new ArrayList<Double>();
	
	public static double lightValue;
	public static double theta;
	
	public LightLocalizer(Odometer odo, Driver driver, ColorSensor cs) {
		this.odo = odo;
		this.robot = driver;
		this.cs = cs;
		LCD.clear();
	}
/**
 * does light sensor localiation
 * 
 * @return robot knowing its X and Y coordinates
 */
	public void doLocalization() {
	//stores current time, in order to not measure same line twice
	double lastLineTime = System.currentTimeMillis();
	//red floodlight for consistent values
	cs.setFloodlight(lejos.robotics.Color.RED);
	cs.calibrateHigh();
	//sets value for darkline
	double lv = cs.getNormalizedLightValue() - 125;
	robot.rotate(true);
	//rotates the robot 360 degrees
	while (odo.getTheta() * 180 / Math.PI <= 359){
		try {Thread.sleep(50);} catch (InterruptedException e) {}
/*		//updates light value to display, debugging
		lightValue = cs.getNormalizedLightValue();*/
		//if it sees a black line and it has been 60ms from the last black line:
		if(cs.getNormalizedLightValue() < lv && ((System.currentTimeMillis() - lastLineTime) > 60)){
			/*debugging
			counter++;*/
			
			//adds current angle to arraylist
			angles.add(odo.getTheta());
			//updates time of last crossing
			lastLineTime = System.currentTimeMillis();
			}
	}
	//stops the robot
	robot.stop();

	//updates X and Y locations
	odo.setY(-D_LIGHT_TO_CENTER * Math.cos((angles.get(2)-angles.get(0))/2));
	odo.setX(-D_LIGHT_TO_CENTER * Math.cos((angles.get(3)-angles.get(1))/2));
	
	//travels to center
	robot.travel(0, 0);
	//turns to face 0 degrees
	robot.turnTo(Math.toDegrees(-odo.getTheta()));
	//lightsensor fixes heading
	robot.turnTo(270 - (angles.get(3)-angles.get(1))/2);
	} 
 }