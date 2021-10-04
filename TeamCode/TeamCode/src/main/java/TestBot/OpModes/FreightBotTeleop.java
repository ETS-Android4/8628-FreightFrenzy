package TestBot.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import TestBot.Mechanisms.FreightBotInfo;
import TestBot.Mechanisms.MecanumDriveBase;

@TeleOp
public class FreightBotTeleop extends OpMode {
    FreightBotInfo robot = new FreightBotInfo();

    @Override
    public void init(){
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        telemetry.addData("Our Heading", robot.getHeading(AngleUnit.DEGREES));
        telemetry.addData("front left Motor Rotations", robot.getFLMotorRotations());
        telemetry.addData("front right Motor Rotations", robot.getFRMotorRotations());
        telemetry.addData("back left Motor Rotations", robot.getBLMotorRotations());
        telemetry.addData("back right Motor Rotations", robot.getBRMotorRotations());

        //mecanum drive
        robot.mecanumDrive(gamepad1.right_stick_y, -gamepad1.right_stick_x, -gamepad1.left_stick_x);

        //ducky mover
        if(gamepad1.a){
            robot.duckyMover.setPower(0.5);
        }


    }
}



