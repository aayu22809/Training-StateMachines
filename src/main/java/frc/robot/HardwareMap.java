package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * HardwareMap provides a centralized spot for constants related to the hardware
 * configuration of the robot.
 */
public final class HardwareMap {
	// Crosswalk button
	public static final int DIO_CROSSWALK_BUTTON_CHANNEL = 1;

	// Addressable LED
	public static final int PWM_ADDRESSABLE_LED_CHANNEL = 9;

	public final class LEDStripIndex {
		public static final int RED_LIGHT = 4;
		public static final int YELLOW_LIGHT = 3;
		public static final int GREEN_LIGHT = 2;

		public static final int WALK_SIGN = 1;
		public static final int HAND_SIGN = 0;
	}

	// Place jumper from DIO pin 9 to GND to indicate this is a test setup
	private static final int DIO_TEST_SETUP_CHANNEL = 9;
	private static DigitalInput testBoardPin = new DigitalInput(HardwareMap.DIO_TEST_SETUP_CHANNEL);
	/**
	 * Check if the current RoboRIO is part of a test setup or real robot.
	 * @return true if the current setup is a test setup
	 */
	public static boolean isTestBoard() {
		return !HardwareMap.testBoardPin.get();
	}
}
