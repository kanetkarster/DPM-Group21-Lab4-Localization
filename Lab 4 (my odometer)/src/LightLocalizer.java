import lejos.nxt.LightSensor;

public class LightLocalizer {
	private Odometer odo;
	private Driver robot;
	private LightSensor ls;
	
	public LightLocalizer(Odometer odo, Driver driver, LightSensor ls) {
		this.odo = odo;
		this.robot = driver;
		this.ls = ls;
		
		// turn on the light
		ls.setFloodlight(true);
	}
	
	public void doLocalization() {
		// drive to location listed in tutorial
		// start rotating and clock all 4 gridlines
		// do trig to compute (0,0) and 0 degrees
		// when done travel to (0,0) and turn to 0 degrees
	}

}
