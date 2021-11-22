package TestBot.Mechanisms.Auto.TouchSensor;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

@Autonomous(name = "RedTouchSensorWarehouse", group = "Sensor")

public class RedTouchSensor extends LinearOpMode {
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
        robot.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
            digitalTouch.setState(true);
            encoderDrive(DRIVE_SPEED, -10,10,3.0);
            encoderDrive(DRIVE_SPEED, 15.5, 15.5, 3); //strafe right to line up touch sensor
            robot.touchServo.setPosition(.5);
            sleep(2000);


            if(digitalTouch.getState() == false){
                //    at position 3, run top one
                telemetry.addData("Digital Touch", "Is Pressed");
                telemetry.update();
                //Move servo backward
                robot.touchServo.setPosition(0);
                robotPosition = 3;
                runThree();
                sleep(20000);
            } else{
                //    move right 6 and test that box
                sleep(3000);
                telemetry.addData("Digital Touch", "not pressed");
                telemetry.update();
                robot.touchServo.setPosition(0);
                encoderDrive(DRIVE_SPEED,-6, -6, 3.0); //forward
                encoderDrive(DRIVE_SPEED,10, -10, 3.0); //LEFT
                encoderDrive(DRIVE_SPEED,6, 6, 3.0); //back
                digitalTouch.setMode(DigitalChannel.Mode.INPUT);
                digitalTouch.setState(true);
                robot.touchServo.setPosition(.5);
                sleep(2000);
                if(digitalTouch.getState() == false){
                    //       at position 2, run middle one
                    telemetry.addData("Digital Touch", "Is Pressed");
                    telemetry.update();
                    //Move servo backward
                    robot.touchServo.setPosition(0);
                    robotPosition = 2;
                    runTwo();
                    sleep(20000);
                } else{
                    //       at position 1, run bottom one
                    telemetry.addData("Digital Touch", "not pressed");
                    telemetry.update();
                    robotPosition = 1;

                    robot.touchServo.setPosition(0);
                    runOne();

                    sleep(20000);
                }
            }

            telemetry.update();
        }

    }

    public void runOne(){
        telemetry.addData("Running route", "1");
        encoderDrive(TURN_SPEED, -10,10,4.0); //strafe right
        encoderDrive(DRIVE_SPEED, 8,8,2);
        liftXRail(-1600);
        //pivot box all the way
        robot.liftServo.setPosition(0.3); //middle
        sleep(800);
        liftXRail(-2500);//CHANGE
        sleep(800);
        robot.liftServo.setPosition(1);
        sleep(1000);
        robot.liftServo.setPosition(.3);
        sleep(1000);
        liftXRail(-1600);
        turnRight(TURN_SPEED,.9); //turn 90 degrees right
        encoderDrive(DRIVE_SPEED,-36,36,3.0); //strafe right 36 inches
        encoderDrive(DRIVE_SPEED,-36,-36,4.0); //forward 36 inches into warehouse
        encoderDrive(DRIVE_SPEED,12,-12,3.0); //strafe left 12 inches
    }
    public void runTwo(){

        robot.elevatorServo.setPosition(.5);
        telemetry.addData("Running route", "2");
        telemetry.update();
        encoderDrive(TURN_SPEED, -10,10,4.0); //strafe right
        encoderDrive(DRIVE_SPEED, 6,6,2);
        liftXRail(-1600);
        //pivot box all the way
        robot.liftServo.setPosition(0.3); //middle
        sleep(800);
        robot.elevatorServo.setPosition(.7);
        sleep(1000);
        liftXRail(-2500);//CHANGE
        sleep(800);
        robot.liftServo.setPosition(1);
        sleep(1000);
        robot.liftServo.setPosition(.3);
        sleep(1000);
        liftXRail(-1600);
        robot.liftServo.setPosition(0); //down
        turnRight(TURN_SPEED,.9); //turn 90 degrees right
        encoderDrive(DRIVE_SPEED,-36,36,3.0); //strafe right 36 inches
        encoderDrive(DRIVE_SPEED,-36,-36,4.0); //forward 36 inches into warehouse
        encoderDrive(DRIVE_SPEED,12,-12,3.0); //strafe left 12 inches
    }
    public void runThree(){
        telemetry.addData("Running route", "3");

        encoderDrive(TURN_SPEED, -4,4,4.0); //strafe right
        encoderDrive(DRIVE_SPEED, 8,8,2);
        turnLeft(TURN_SPEED,.4);
        encoderDrive(DRIVE_SPEED,6,6,2.0);
        liftXRail(-1600);
        //pivot box all the way
        robot.liftServo(0.3); //middle
        sleep(800);
        liftXRail(-4900);
        sleep(1000);
        robot.liftServo.setPosition(1); //dump box
        sleep(800);
        robot.liftServo.setPosition(.3);//middle
        sleep(1000);
        liftXRail(-1600);
        robot.liftServo.setPosition(0); //down
        turnRight(TURN_SPEED,.9); //turn 90 degrees right
        encoderDrive(DRIVE_SPEED,-36,36,3.0); //strafe right 36 inches
        encoderDrive(DRIVE_SPEED,-36,-36,4.0); //forward 36 inches into warehouse
        encoderDrive(DRIVE_SPEED,12,-12,3.0); //strafe left 12 inches
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
    public void turnLeft (double power, double timeout){
        if (opModeIsActive()) {

            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < timeout) ){
                robot.backRight.setPower(power);
                robot.frontRight.setPower(power);
                robot.backLeft.setPower(-power);
                robot.frontLeft.setPower(-power);
            }
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);
            robot.backLeft.setPower(0);
            robot.backRight.setPower(0);
        }
    }
    public void turnRight (double power, double timeout){
        if (opModeIsActive()) {

            robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < timeout) ){
                robot.backRight.setPower(-power);
                robot.frontRight.setPower(-power);
                robot.backLeft.setPower(power);
                robot.frontLeft.setPower(power);
            }
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);
            robot.backLeft.setPower(0);
            robot.backRight.setPower(0);
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
