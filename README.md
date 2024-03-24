# Training: State Machines

In this exercise, we will build a more complex state machine to operate a traffic light. To access this lesson, clone this project using git:
`git clone https://github.com/Tino-FRC-2473/Training-StateMachines.git`

After this lesson you should known how to:
* Design a complex state machine
* Use [Timer](https://github.wpilib.org/allwpilib/docs/release/java/edu/wpi/first/wpilibj/Timer.html) to track time
* Read switches with [DigitalInput](https://github.wpilib.org/allwpilib/docs/release/java/edu/wpi/first/wpilibj/DigitalInput.html)
* Control LED strips with [AddressableLED](https://github.wpilib.org/allwpilib/docs/release/java/edu/wpi/first/wpilibj/AddressableLED.html) and [AddressableLEDBuffer](https://github.wpilib.org/allwpilib/docs/release/java/edu/wpi/first/wpilibj/AddressableLEDBuffer.html)
* Post debug information to [NetworkTables](https://docs.wpilib.org/en/stable/docs/software/networktables/index.html)

## Introduction

The goal of this exercise is to create a basic traffic light including a crosswalk signal. When a button is pressed, the traffic light will cycle from green, to yellow, then to red with correct timings. Then, the walk sign and raised hand will illuminate and flash while the crosswalk is active.

Some skeleton code has been written for you in `TrafficLightSystem.java`. Right now only two states are implemented, one representing a red light and one representing a green light. When the button is pressed, the light switches to red for five seconds before reverting to green light.

## Hardware Definition

The hardware definitions have already been completed for you in `HardwareMap.java`. Notice that we have a button connected to a digital input that will represent the crosswalk request button. This is a normally open button, so the input will read high (true) when the button is not preseed, and low (false) when the button is pressed. All of the lights in the traffic light are wired as a signal addressable LED strip. The LED strip is controlled from a PWM port on the RoboRIO. Please open up `HardwareMap.java` and verify your button and addressable LED strip connections match the values defined in our code.

## Simulation

After cloning the repo, open the project in VSCode and run the "WPILib: Simulate Robot Code" command to build and run the project in the robot simulator. Select "Sim GUI" and hit OK to start the simulation.

You should see the Simulation GUI appear. Under the "Hardware" menu, make sure the "Addressable LED" and "DIO" panels are open. These will let you see the state of the LED's and simulate a button press in the simulator.

Switch the "Robot State" to "Teleoperated" to start running the teleop code. You should see the green light turn on in the Addressable LED panel. Next, click on the dropdown in the DIO panel to change the state of the input to `0 (low)` briefly, before switching it back to `1 (high)`. This simulates presing the button. Notice how the addressable LED light immediately switches to red. Make sure you un-press the button by switching the input back to `1 (high)`. Five seconds after the button was pressed, you should see the LED change back to green.
