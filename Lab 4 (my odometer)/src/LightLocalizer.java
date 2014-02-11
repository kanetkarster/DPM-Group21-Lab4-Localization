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
		/*leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
		
		leftMotor.forward();
		rightMotor.forward();
		lightValue = cs.getNormalizedLightValue();
		while(cs.getNormalizedLightValue() > LINE_VALUE){
			lightValue = cs.getNormalizedLightValue();
		    try {Thread.sleep(40);} catch (InterruptedException e) {}
		}
		robot.stop(); 
		
	    odo.setY(0);
	    robot.turnTo(90);
	    
	    leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
	    leftMotor.forward();
		rightMotor.forward();
	    
	    while(cs.getNormalizedLightValue() > LINE_VALUE){
			lightValue = cs.getNormalizedLightValue();
		    try {Thread.sleep(40);} catch (InterruptedException e) {}
		}
         robot.stop();
		
	    odo.setX(0);
	    robot.turnTo(-90);
	    robot.turnTo(360);*/		
/*		while(true ){
			
			try {Thread.sleep(50);} catch (InterruptedException e) {}
		
			
		if(cs.getNormalizedLightValue() < LINE_VALUE){
			if(counter >= 3){
				robot.stop(); 
				break;
			}
			//theta1 = 180/Math.PI * odo.getTheta();
		 	 line[counter] = 180/Math.PI * odo.getTheta(); 
			 //theta = 180/Math.PI * odo.getTheta();
			counter ++;
			
		}
		else if (180/Math.PI * odo.getTheta() >= 360){
			robot.stop();
			break;
		}
		else{
			leftMotor.setSpeed(100);
			rightMotor.setSpeed(100);
		    leftMotor.forward();
			rightMotor.backward();
			robot.rotate(true);

		}
		
	}*/
	double lastLineTime = System.currentTimeMillis();
	cs.setFloodlight(lejos.robotics.Color.RED);
	cs.calibrateHigh();
	double lv = cs.getNormalizedLightValue() - 125;
	robot.rotate(true);
	while (odo.getTheta() * 180 / Math.PI <= 358){
		try {Thread.sleep(50);} catch (InterruptedException e) {}
		lightValue = cs.getNormalizedLightValue();
		if(cs.getNormalizedLightValue() < lv && ((System.currentTimeMillis() - lastLineTime) > 60)){
				counter++;
				angles.add(odo.getTheta());
				lastLineTime = System.currentTimeMillis();
			}
	}

	robot.stop();

	odo.setY(-d_Light_To_Sensor * Math.cos((angles.get(2)-angles.get(0))/2));
	odo.setX(-d_Light_To_Sensor * Math.cos((angles.get(3)-angles.get(1))/2));
	
	} 
 }