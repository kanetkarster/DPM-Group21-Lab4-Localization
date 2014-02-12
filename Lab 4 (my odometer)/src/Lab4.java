/**
 * Main java class file for localization
 * Satyajit Kanetkar
 * Sean Wolfe
 */
import lejos.nxt.*;

public class Lab4 {

	public static void main(String[] args) {
		// setup the odometer, display, and ultrasonic and light sensors
		Odometer odo = new Odometer();
		Driver driver = new Driver(odo);
		OdometryDisplay lcd = new OdometryDisplay(odo);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S2);
		ColorSensor cs = new ColorSensor(SensorPort.S1);
		
		odo.start();
		lcd.start();
		
		Button.waitForAnyPress();
		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odo, driver, us, USLocalizer.LocalizationType.FALLING_EDGE);
		usl.doLocalization();
				
		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odo, driver, cs);
		lsl.doLocalization();

		driver.travel(0, 0);
		driver.turnTo(Math.toDegrees(-odo.getTheta()));

		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}

}
