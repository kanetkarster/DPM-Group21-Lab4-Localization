/*
 * USlocalizer.java
 * 
 * Satyajit kanetkar
 * Sean Wolfe
 */
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };	
	
	public static final double WALL_DISTANCE = 30;
	public static final double NOISE = 5;
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;

	NXTRegulatedMotor leftMotor = Motor.A;
	NXTRegulatedMotor rightMotor = Motor.B;
	
	public static double distance, angleA, angleB, errorAngle; 
	public static String doing = "";
	private Odometer odo;
	private Driver robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	
	public USLocalizer(Odometer odo, Driver driver, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = driver;
		this.us = us;
		this.locType = locType;
		
		// switch off the ultrasonic sensor
		us.off();
	}
/**
 *	has the robot start localizing with the US
 *
 * @return robot facing approx. 45 degrees
 */
	public void doLocalization() {
		double [] pos = new double [3];
		//uses falling endge localizations
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
			rotateFromWall(true);
			//rotates 25 degrees to avoid seeing a wall twice
			robot.turnTo(25);
			// keep rotating until the robot sees a wall, then latch the angle
			rotateToWall(true);
			angleA = odo.getTheta();
			//rotates 25 degrees to avoid seeing a wall twice
			robot.turnTo(-25);
			// switch direction and wait until it sees no wall
			rotateFromWall(false);
			// keep rotating until the robot sees a wall, then latch the angle
			rotateToWall(false);
			angleB = odo.getTheta();
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			errorAngle = getAngle(angleA, angleB);
			//turns to face about 45 deg
			robot.turnTo(errorAngle + 45);
			//updates odometer
			odo.setPosition(new double [] {0.0, 0.0, Math.toRadians(45)}, new boolean [] {true, true, true});
			//allows LightSensor to see all 4 lines
			robot.goForward(12);
			//uses rising edge localizations
		} else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			//finds wall
			rotateToWall(true);
			//goes to end of wall
			rotateFromWall(true);
			angleA = odo.getTheta();
			//rotates 15 degrees to avoid seeing a wall twice
			robot.turnTo(15);
			rotateToWall(false);			
			angleB = odo.getTheta();
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			errorAngle = getAngle(angleA, angleB);
			//turns to face about 45 deg
			robot.turnTo(errorAngle + 45);
			//updates odometer
			odo.setPosition(new double [] {0.0, 0.0, Math.toRadians(45)}, new boolean [] {true, true, true});
			//allows LightSensor to see all 4 lines
		}
	}
 /**Has the robot turn until it sees no wall
  * 
  * @param direction true is clockwise, false is counterclockwise rotation
  */
	 private void rotateFromWall(boolean direction){
		 //direction to rotate in
		robot.rotate(direction);
		//keeps rotating until the distance is less than ~35
		while(distance < (WALL_DISTANCE + NOISE)){
			distance = getFilteredData();	//debugging, don't care about collissions
		}
		robot.stop();
	}
 /**
  * Has the robot turn until it sees a wall
  * 
  * @param direction true is clockwise, false is counterclockwise rotation
  */
	private void rotateToWall(boolean direction){
		 //direction to rotate in
		robot.rotate(direction);
		//keeps rotating until the distance is less than ~35
		while(distance > (WALL_DISTANCE - NOISE)){
			distance = getFilteredData(); //debugging
		}
		robot.stop();
	}
/**
 * Tells the robot how many degrees to turn to face 0 degrees
 * 
 * @param alpha first error angle
 * @param beta second error angle
 * @return Degrees to turn to face 0 degrees
 */
	private double getAngle(double alpha, double beta){
		//calculates angle to turn to
		 return (alpha > beta) ? (45 - (alpha + beta)/2) : (225 - (alpha + beta)/2);
		}
	private int getFilteredData() {
		int dist;
		
		// do a ping
		us.ping();
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		// there will be a delay here
		dist = us.getDistance();
		//filters bad values
		if(dist > 50)
			dist = 50;
		return dist;
	}

}
