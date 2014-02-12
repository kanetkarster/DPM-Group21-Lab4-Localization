/**
 * Localization using the light sensor
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
	private static final int LINE_VALUE = 420;
	private final double d_Light_To_Sensor = 12;
	
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
		// turn on the light
	}
	
	public void doLocalization() {
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		// do trig to compute (0,0) and 0 degrees
		// when done travel to (0,0) and turn to 0 degrees	
	//sets to current time
	double lastLineTime = System.currentTimeMillis();
	//sets up native values for light sensor
	cs.setFloodlight(lejos.robotics.Color.RED);
	cs.calibrateHigh();
	double lv = cs.getNormalizedLightValue() - 125;
	//starts to rotate
	robot.rotate(true);
	//only processes lines while it has rotated < 360
	while (odo.getTheta() * 180 / Math.PI <= 358){
		try {Thread.sleep(50);} catch (InterruptedException e) {}
		//to display on display
		lightValue = cs.getNormalizedLightValue();
		//if it sees a line and it has been 60ms from the last line
		if(cs.getNormalizedLightValue() < lv && ((System.currentTimeMillis() - lastLineTime) > 60)){
			//increments counter (for display)
			counter++;
			//adds angle of lines
			angles.add(odo.getTheta());
			//updates time to abvoid seeing one line twice
			lastLineTime = System.currentTimeMillis();
			}
	}
	//stops robot
	robot.stop();
	//updates X and Y posistions
	odo.setY(-d_Light_To_Sensor * Math.cos((angles.get(2)-angles.get(0))/2));
	odo.setX(-d_Light_To_Sensor * Math.cos((angles.get(3)-angles.get(1))/2));
	
	} 
 }