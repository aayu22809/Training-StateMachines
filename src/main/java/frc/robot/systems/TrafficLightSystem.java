package frc.robot.systems;

// WPILib Imports
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// Robot Imports
import frc.robot.TeleopInput;
import frc.robot.HardwareMap;
import frc.robot.systems.AutoHandlerSystem.AutoFSMState;

/**
 * 
 * TrafficLightSystem is a finite state machine that controls the traffic light
 * system. The system has two parallel state machines in one loop: VEHICLE_GREEN, VEHICLE_YELLOW and VEHICLE_RED; PEDESTRIAN_HALT, PEDESTRIAN_WALK, PEDESTRIAN_CHANGE. The system
 * transitions between these states based on the state of the crosswalk button.
 * The system also has a timer that tracks how long the vehice lights and crosswalk lights have been on
 * and transitions states based on set time intervals.
 */


public class TrafficLightSystem {
	/* ======================== Constants ======================== */
	// FSM state definitions
	public enum FSMState {
		GREEN,
		YELLOW,
		RED
	}

	// How long to keep the red light on before resetting to green
	private static final double YELLOW_LIGHT_TIME_SEC = 3.0;
	private static final double RED_LIGHT_TIME_SEC = 11.0;

	/* ======================== Private variables ======================== */
	private FSMState currentState;
	private Timer timer;

	// Hardware devices should be owned by one and only one system. They must
	// be private to their owner system and may not be used elsewhere.
	private AddressableLED ledStrip;
	private AddressableLEDBuffer ledBuffer;

	// Normally open button. This means input is high when button is not pressed.
	private DigitalInput crosswalkButton;

	/* ======================== Constructor ======================== */
	/**
	 * Create FSMSystem and initialize to starting state. Also perform any
	 * one-time initialization or configuration of hardware required. Note
	 * the constructor is called only once when the robot boots.
	 */
	public TrafficLightSystem() {
		// Perform hardware init
		crosswalkButton = new DigitalInput(HardwareMap.DIO_CROSSWALK_BUTTON_CHANNEL);

		ledStrip = new AddressableLED(HardwareMap.PWM_ADDRESSABLE_LED_CHANNEL);
		ledBuffer = new AddressableLEDBuffer(HardwareMap.ADDRESSABLE_LED_COUNT);
		ledStrip.setLength(ledBuffer.getLength());
		ledStrip.setData(ledBuffer);
		ledStrip.start();

		timer = new Timer();

		// Reset state machine
		reset();
	}

	/* ======================== Public methods ======================== */
	/**
	 * Return current FSM state.
	 * @return Current FSM state
	 */
	public FSMState getCurrentState() {
		return currentState;
	}
	/**
	 * Reset this system to its start state. This may be called from mode init
	 * when the robot is enabled.
	 *
	 * Note this is distinct from the one-time initialization in the constructor
	 * as it may be called multiple times in a boot cycle,
	 * Ex. if the robot is enabled, disabled, then reenabled.
	 */
	public void reset() {
		currentState = FSMState.GREEN;

		timer.stop();
		timer.reset();

		// Call one tick of update to ensure outputs reflect start state
		update(null);
	}

	/**
	 * Update FSM based on new inputs. This function only calls the FSM state
	 * specific handlers.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	public void update(TeleopInput input) {
		switch (currentState) {
			case GREEN:
				handleGreenLightState(input);
				break;

			case RED:
				handleRedLightState(input);
				break;

			case YELLOW:
			handleYellowLightState(input);
			break;

			default:
				throw new IllegalStateException("Invalid state: " + currentState.toString());
		}
		currentState = nextState(input);

		// Publish current state for debugging
		SmartDashboard.putString("Traffic Light State", currentState.toString());
		SmartDashboard.putBoolean("Button", crosswalkButton.get());
		SmartDashboard.putNumber("Timer", timer.get());
	}

	/**
	 * Performs specific action based on the autoState passed in.
	 * @param autoState autoState that the subsystem executes.
	 * @return if the action carried out in this state has finished executing
	 */
	public boolean updateAutonomous(AutoFSMState autoState) {
		update(null);
		return false;
	}

	/* ======================== Private methods ======================== */
	/**
	 * Decide the next state to transition to. This is a function of the inputs
	 * and the current state of this FSM. This method should not have any side
	 * effects on outputs. In other words, this method should only read or get
	 * values to decide what state to go to.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 * @return FSM state for the next iteration
	 */
	private FSMState nextState(TeleopInput input) {
		// Stay in start state until input is available
		if (input == null) {
			return FSMState.GREEN;
		}
		switch (currentState) {
			case GREEN:
				if (!crosswalkButton.get()) {
					// Start timer tracking how long red light is on
					timer.restart();
					return FSMState.YELLOW;
				} else {
					return FSMState.GREEN;
				}

			case YELLOW:
				if (timer.get() > YELLOW_LIGHT_TIME_SEC) {
					timer.restart();
					return FSMState.RED;
				} else {
					return FSMState.YELLOW;
				}	
			case RED:
				if (timer.get() > RED_LIGHT_TIME_SEC) {
					timer.restart();
					return FSMState.GREEN;
				} else if (timer.get() > 3) {
					handlePedestrianChangeState(input);
					return FSMState.RED;
				} else {
					handlePedestrianWalkState(input);
					return FSMState.RED;
				}
			

			default:
				throw new IllegalStateException("Invalid state: " + currentState.toString());
		}
	}

	/* ------------------------ FSM state handlers ------------------------ */
	/**
	 * Handle behavior in GREEN.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleGreenLightState(TeleopInput input) {
		// Turn off all lights
		fillColor(Color.kBlack);

		// Turn on green light
		ledBuffer.setLED(HardwareMap.LEDStripIndex.GREEN_LIGHT, Color.kGreen);
		ledStrip.setData(ledBuffer);
	}
	/**
	 * Handle behavior in RED.
	 * @param input Global TeleopInput if robot in teleop mode or null if
	 *        the robot is in autonomous mode.
	 */
	private void handleRedLightState(TeleopInput input) {
		// Turn off all lights
		fillColor(Color.kBlack);

		// Turn on red light
		ledBuffer.setLED(HardwareMap.LEDStripIndex.RED_LIGHT, Color.kRed);
		ledStrip.setData(ledBuffer);
	}

	private void handleYellowLightState(TeleopInput input) {
		// Turn off all lights
		fillColor(Color.kBlack);

		// Turn on red light
		ledBuffer.setLED(HardwareMap.LEDStripIndex.YELLOW_LIGHT, Color.kYellow);
		ledStrip.setData(ledBuffer);
	}
	
	private void handlePedestrianWalkState(TeleopInput input) {
		// Turn off all lights
		ledBuffer.setLED(HardwareMap.LEDStripIndex.HAND_SIGN, Color.kBlack);

		//Turn on walk light
		ledBuffer.setLED(HardwareMap.LEDStripIndex.WALK_SIGN, Color.kWhite);
		ledStrip.setData(ledBuffer);

	}

	private void handlePedestrianHaltState(TeleopInput input) {
		// Turn off all lights
		ledBuffer.setLED(HardwareMap.LEDStripIndex.WALK_SIGN, Color.kBlack);
		
		// Turn on hand sign
		ledBuffer.setLED(HardwareMap.LEDStripIndex.HAND_SIGN, Color.kOrange);
		ledStrip.setData(ledBuffer);
	}

	private void handlePedestrianChangeState(TeleopInput input) {
		// Turn off all lights
		ledBuffer.setLED(HardwareMap.LEDStripIndex.WALK_SIGN, Color.kBlack);
		
		// Turn on hand sign
		for (int i=0; i < 4; i++) {
			ledBuffer.setLED(HardwareMap.LEDStripIndex.HAND_SIGN, Color.kOrange);
			ledStrip.setData(ledBuffer);
			wait(1000);
			ledBuffer.setLED(HardwareMap.LEDStripIndex.HAND_SIGN, Color.kBlack);
			ledStrip.setData(ledBuffer);
			wait(1000);
		}
	}

	/* ------------------------ LED Helpers ------------------------ */
	/**
	 * Set color of all LEDs.
	 * @param color Color to set LEDs to
	 */
	private void fillColor(Color color) {
		for (int i = 0; i < ledBuffer.getLength(); i++) {
			ledBuffer.setLED(i, color);
		}
	}

	private void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

