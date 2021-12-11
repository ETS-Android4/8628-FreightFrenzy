package TestBot.Mechanisms.Auto.TouchSensor;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import TestBot.Mechanisms.FreightBotInfo;

/*
 * This is an example LinearOpMode that shows how to use
 * a REV Robotics Touch Sensor.
 *
 * It assumes that the touch sensor is configured with a name of "sensor_digital".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 */

@Disabled
@Autonomous(name = "TouchSensorAuto", group = "Sensor")

public class TouchSensorAuto extends LinearOpMode {
    /* Copyright (c) 2017 FIRST. All rights reserved.
     *
     * Redistribution and use in source and binary forms, with or without modification,
     * are permitted (subject to the limitations in the disclaimer below) provided that
     * the following conditions are met:
     *
     * Redistributions of source code must retain the above copyright notice, this list
     * of conditions and the following disclaimer.
     *
     * Redistributions in binary form must reproduce the above copyright notice, this
     * list of conditions and the following disclaimer in the documentation and/or
     * other materials provided with the distribution.
     *
     * Neither the name of FIRST nor the names of its contributors may be used to endorse or
     * promote products derived from this software without specific prior written permission.
     *
     * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
     * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
     * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
     * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
     * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
     * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
     * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
     * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
     * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
     * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
     * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
     */

    /**
     * The REV Robotics Touch Sensor
     * is treated as a digital channel.  It is HIGH if the button is unpressed.
     * It pulls LOW if the button is pressed.
     * <p>
     * Also, when you connect a REV Robotics Touch Sensor to the digital I/O port on the
     * Expansion Hub using a 4-wire JST cable, the second pin gets connected to the Touch Sensor.
     * The lower (first) pin stays unconnected.*
     */
    FreightBotInfo robot = new FreightBotInfo();
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 537.6;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    DigitalChannel digitalTouch;  // Hardware Device Object
    int robotPosition = 0;


    @Override
    public void runOpMode() {

        // get a reference to our digitalTouch object.
        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");

        // set the digital channel to input.
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.backRight.getCurrentPosition(),
                robot.backLeft.getCurrentPosition(),
                robot.frontRight.getCurrentPosition(),
                robot.frontLeft.getCurrentPosition());
        telemetry.update();
        telemetry.addData("running route",robotPosition);

        // wait for the start button to be pressed.
        waitForStart();

        //Move servo forward


        // while the op mode is active, loop and read the light levels.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            // send the info back to driver station using telemetry function.
            // if the digital channel returns true it's HIGH and the button is unpressed.
            encoderDrive(DRIVE_SPEED, 16, 16, 3);



            robot.touchServo.setPosition(.5);

                sleep(2000);

          //  robot.touchServo.setPosition(-1);

           // sleep(4000);
            //robot.touchServo.setPosition(0);


            if (digitalTouch.getState() == true) {
                telemetry.addData("Digital Touch", "Is Pressed");
                //Move servo backward
                robot.touchServo.setPosition(0);
                robotPosition = 3;
                //now the robot would go place the block on the right level
                /*
                * GO TO DROP AT LEVEL 1
                * strafe right to line up with scoring place
                * lift xrail
                * forwards to scoring place?
                * flip box if necessary
                * */

                telemetry.addData("Running route", "3");
                encoderDrive(TURN_SPEED, 19,-19,4.0); //strafe left
                encoderDrive(DRIVE_SPEED, 8,8,2);
                liftXRail(-1600);
                //pivot box all the way
                robot.liftServo(0.3); //middle
                liftXRail(-6050);
                robot.liftServo.setPosition(1);
                sleep(800);
                robot.liftServo.setPosition(.3);//middle
                liftXRail(-300);
                robot.liftServo.setPosition(0); //down
                encoderDrive(DRIVE_SPEED, 63,-63, 4);//strafe left
                encoderDrive(DRIVE_SPEED,3,3,3.0);
                sleep(20000);

            } else {
                sleep(3000);
                telemetry.addData("Digital Touch", "not pressed");
                robot.touchServo.setPosition(0);
                encoderDrive(DRIVE_SPEED,-6, 6, 3.0);
                robot.touchServo.setPosition(1);
                digitalTouch.getState();
                if (digitalTouch.getState() == true) {
                    telemetry.addData("Digital Touch", "Is Pressed");
                    //Move servo backward
                    robot.touchServo.setPosition(0);
                    robotPosition = 2;


                    //now the robot would go place the block on the right level

                    telemetry.addData("Running route", "2");
                    encoderDrive(TURN_SPEED, 21,-21,4.0); //strafe left
                    encoderDrive(DRIVE_SPEED, 8,8,2);
                    liftXRail(-1600);
                    //pivot box all the way
                    robot.liftServo(0.3); //middle
                    robot.elevatorServo.setPosition(.7);
                    liftXRail(-5000);//CHANGE
                    robot.liftServo.setPosition(1);
                    sleep(250);
                    robot.liftServo.setPosition(.3);
                    liftXRail(-300);
                    robot.liftServo.setPosition(0); //down
                    encoderDrive(DRIVE_SPEED, 45,-45, 4);
                    encoderDrive(DRIVE_SPEED,6,6,3.0);
                    sleep(20000);
                } else {
                    telemetry.addData("Digital Touch", "not pressed");
                    robotPosition = 1;

                    robot.touchServo.setPosition(0);
                    //now the robot would place the block on the right level

                    telemetry.addData("Running route", "1");
                    encoderDrive(TURN_SPEED, 21,-21,4.0); //strafe left
                    encoderDrive(DRIVE_SPEED, 8,8,2);
                    liftXRail(-1600);
                    //pivot box all the way
                    robot.liftServo(0.7); //middle
                    liftXRail(-5000);//CHANGE
                    robot.liftServo.setPosition(1);
                    sleep(250);
                    robot.liftServo.setPosition(.3);
                    liftXRail(-300);
                    encoderDrive(DRIVE_SPEED, 45,-45, 4);
                    encoderDrive(DRIVE_SPEED,6,6,3.0);
                    sleep(20000);
                }
            }
            telemetry.addData("program over!",robotPosition);
        }


            telemetry.update();
            //Move servo backward
            //servoTest.setPosition(1);
    }

    public void liftXRail(int position){
        if (opModeIsActive()){
            robot.liftMotor.setTargetPosition(position);

            // Turn On RUN_TO_POSITION
            robot.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.liftMotor.setPower(1);
            while (opModeIsActive() &&
                    (runtime.seconds() < 5.0) && (robot.liftMotor.isBusy())) {

                        robot.liftMotor.getCurrentPosition();
                telemetry.update();
            }

            // Stop all motion;
            robot.liftMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newBackRightTarget;
        int newBackLeftTarget;
        int newFrontRightTarget;
        int newFrontLeftTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newBackRightTarget = robot.backRight.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newBackLeftTarget = robot.backLeft.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newFrontLeftTarget = robot.frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newFrontRightTarget = robot.frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            robot.backRight.setTargetPosition(newBackRightTarget);
            robot.backLeft.setTargetPosition(newBackLeftTarget);
            robot.frontRight.setTargetPosition(newFrontRightTarget);
            robot.frontLeft.setTargetPosition(newFrontLeftTarget);

            // Turn On RUN_TO_POSITION
            robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.backRight.setPower(Math.abs(speed));
            robot.backLeft.setPower(Math.abs(speed));
            robot.frontLeft.setPower(Math.abs(speed));
            robot.frontRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newBackLeftTarget, newBackRightTarget, newFrontLeftTarget, newFrontRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.backLeft.getCurrentPosition(),
                        robot.backRight.getCurrentPosition(),
                        robot.frontRight.getCurrentPosition(),
                        robot.frontLeft.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.backRight.setPower(0);
            robot.backLeft.setPower(0);
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}