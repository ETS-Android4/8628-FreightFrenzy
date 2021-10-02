/*package TestBot.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import UltimateBot.Mechanisms.ClairesUltimateBotInfo;
import UltimateBot.Mechanisms.MecanumDriveBase;

@TeleOp
public class movementTest extends OpMode {
    ClairesUltimateBotInfo robot = new ClairesUltimateBotInfo();

    boolean xPressed=false;
    @Override
    public void init(){
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Our Heading", robot.getHeading(AngleUnit.DEGREES));

        //robot.getMotorRevolutions();
        //mecanum drive
        robot.mecanumDrive(gamepad1.right_stick_y, -gamepad1.right_stick_x, -gamepad1.left_stick_x);

        //precision speed
        if(gamepad1.left_trigger>=0.01){
            robot.mecanumDrive(0.15*gamepad1.right_stick_y,-0.15*gamepad1.right_stick_x, -0.15*gamepad1.left_stick_x);
        }

        robot.launcherServoRange(0.1,0.9);
        robot.wobbleServoRange(0.0,1.0);

        //shooter spinner thingy motor
        if (gamepad1.left_bumper || gamepad2.a) {
            robot.setSpinnerSpeed(0.85 );
            //TO DO: figure out ideal speed for spinner!!
        } else{ robot.setSpinnerSpeed(0);}

        //servo that moves the rings towards the spinner
        //robot.launcherServoPosition(0.0);
        if (gamepad1.a) {
            robot.launcherServoPosition(0.1); //out
        } else if (gamepad1.b) {
            robot.launcherServoPosition(0.6); //in
        }

        //wobble goal motor
        robot.moveWobble(0);
        if (gamepad2.right_bumper) {
            robot.moveWobble(0.7); //down
        } else if(gamepad2.left_bumper) {
            robot.moveWobble(-0.7); //up
        } else{
            robot.moveWobble(0);
        }

        //wobble servo
        if (gamepad2.x) {
            robot.grabWobbleGoal(0.6); //closes
        } else if (gamepad2.y) {
            robot.grabWobbleGoal(0.0); //opens
        }

        //intake motor
        if (gamepad1.x) {
            robot.runIntake(1.0);
        } else if(gamepad1.y){
            robot.runIntake(0.0); //open
        }

    }
}
*/
