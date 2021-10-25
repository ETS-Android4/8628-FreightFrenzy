package TestBot.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import TestBot.Mechanisms.FreightBotInfo;
import TestBot.Mechanisms.MecanumDriveBase;

@TeleOp(name = "FreightBotTeleop")
public class FreightBotTeleop extends OpMode {
    FreightBotInfo robot = new FreightBotInfo();

    @Override
    public void init(){
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("some numbrs", robot.getTicksPerRev());
        telemetry.addData("Our Heading", robot.getHeading(AngleUnit.DEGREES));
        telemetry.addData("front left Motor Rotations", robot.getFLMotorRotations());
        telemetry.addData("front right Motor Rotations", robot.getFRMotorRotations());
        telemetry.addData("back left Motor Rotations", robot.getBLMotorRotations());
        telemetry.addData("back right Motor Rotations", robot.getBRMotorRotations());

        //mecanum drive
        robot.mecanumDrive(gamepad1.right_stick_y, -gamepad1.right_stick_x, -gamepad1.left_stick_x);

        //ducky mover
        if(gamepad1.a) {
            robot.duckyMover.setPower(-1);
        }else if(gamepad1.b){
            robot.duckyMover.setPower(1);
        } else {
            robot.duckyMover.setPower(0);
        }

        //alistair's lifting module motor
        if (gamepad1.x) {
            robot.moduleA.setPower(1.0);
        } else if (gamepad1.y){
            robot.moduleA.setPower(-1.0);
        } else {
            robot.moduleA.setPower(0);
        }

        //alistair's lifting module servo
        if (gamepad1.left_bumper) {
            robot.liftServo(1); //closed
        } else if (gamepad1.right_bumper) {
            robot.liftServo(0.0);
        }
        //jonah's intake servo
        if (gamepad2.x) {
            robot.intakeMotor.setPower(1);
        } else if (gamepad2.y){
            robot.intakeMotor.setPower(-1);
        } else {
            robot.intakeMotor.setPower(0);
        }

        //jonah's lift motor
        if (gamepad2.left_stick_y>0) {
            robot.liftMotor.setPower(1);
        } else if (gamepad2.left_stick_y<0) {
            robot.liftMotor.setPower(-1);
        } else {
            robot.liftMotor.setPower(0);
        }
    }

}





