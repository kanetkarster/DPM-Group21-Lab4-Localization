import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class LightLocalizer {
	private Odometer odo;
	private Driver robot;
	private ColorSensor cs;
	private static final int LINE_VALUE = 280;
	
	NXTRegulatedMotor leftMotor = Motor.A;
	NXTRegulatedMotor rightMotor = Motor.B;
	
	public static double lightValue;
	
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
		leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
		
		leftMotor.forward();
		rightMotor.forward();
		lightValue = cs.getNormalizedLightValue();
		while(cs.getNormalizedLightValue() > LINE_VALUE){
			lightValue = cs.getNormalizedLightValue();
		    try {Thread.sleep(50);} catch (InterruptedException e) {}
		}
		robot.stop();
		
	    odo.setY(0);
	    robot.turnTo(90);
	}

}
