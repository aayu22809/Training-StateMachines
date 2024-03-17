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

## Hardware Definition

The hardware definitions have already been completed for you in `HardwareMap.java`. Notice that we have a normally open button connected to a digital input that we will represent the crosswalk request button. All of the lights in the traffic light are wired as a signal addressable LED strip. The LED strip is controlled from a PWM port on the RoboRIO. Please open up `HardwareMap.java` and verify your button and addressable LED strip connections match the values defined in our code.



