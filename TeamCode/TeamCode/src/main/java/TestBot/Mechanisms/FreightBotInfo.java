package TestBot.Mechanisms;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.net.PortUnreachableException;


public class FreightBotInfo {

    //define motors

    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor frontLeft;
    public DcMotor frontRight;


    //modules
    public DcMotor duckyMover;
    public Servo liftServo;
    public DcMotor intakeMotor;
    public DcMotor liftMotor;
    public Servo elevatorServo;
    public Servo touchServoLeft;
    public Servo touchServoRight;


    private double backLeftTicksPerRev;
    private double backRightTicksPerRev;
    private double frontLeftTicksPerRev;
    private double frontRightTicksPerRev;

    public DigitalChannel digitalTouchLeft;  // Hardware Device Object
    public DigitalChannel digitalTouchRight;  // Hardware Device Object
    //imu
    private BNO055IMU imu;

    public double LEFT_DOWN = 0.5;
    public double RIGHT_DOWN = 0;

    public double LEFT_UP = 0;
    public double RIGHT_UP = 0.5;

    public void init(HardwareMap hwMap) {

  //      mecanumDriveBase = new MecanumDriveBase(frontLeft, frontRight, backLeft, backRight);
        //drivetrain systems

        // get a reference to our digitalTouch object.
        digitalTouchLeft = hwMap.get(DigitalChannel.class, "digitalTouchLeft");

        // set the digital channel to input.
        digitalTouchLeft.setMode(DigitalChannel.Mode.INPUT);
        // get a reference to our digitalTouch object.
        digitalTouchRight = hwMap.get(DigitalChannel.class, "digitalTouchRight");

        // set the digital channel to input.
        digitalTouchRight.setMode(DigitalChannel.Mode.INPUT);

        backLeft = hwMap.get(DcMotor.class, "lb");
        backRight = hwMap.get(DcMotor.class, "rb");
        frontLeft = hwMap.get(DcMotor.class, "lf");
        frontRight = hwMap.get(DcMotor.class, "rf");



        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);



        backLeftTicksPerRev = backLeft.getMotorType().getTicksPerRev();
        backRightTicksPerRev = backRight.getMotorType().getTicksPerRev();
        frontLeftTicksPerRev = frontLeft.getMotorType().getTicksPerRev();
        frontRightTicksPerRev = frontRight.getMotorType().getTicksPerRev();


        //modules
        duckyMover = hwMap.get(DcMotor.class,"duckyMover");
        duckyMover.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        duckyMover.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // get servo
        touchServoLeft = hwMap.get(Servo.class, "touchServoLeft");
        touchServoRight = hwMap.get(Servo.class, "touchServoRight");

        liftServo = hwMap.get(Servo.class, "liftServo" );

        intakeMotor = hwMap.get(DcMotor.class,"intakeMotor");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        liftMotor = hwMap.get(DcMotor.class,"liftMotor");
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        elevatorServo = hwMap.get(Servo.class, "elevatorServo");





        //imu
        imu = hwMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        //change to default set of parameters go here
        imu.initialize(params);


    }

    //mecanum
    public void mecanumDrive(double forward, double sideways, double rotation){

        // Set the power of each motor
        frontLeft.setPower((forward + sideways + rotation) * 1.0);
        frontRight.setPower((forward - sideways) - rotation * 1.0);
        backLeft.setPower((forward - sideways) + rotation * 1.0);
        backRight.setPower(forward + (sideways - rotation) * 1.0);

    }

    public void presetThree(int midPosition, int finalPosition, boolean pressed){

            if (pressed){
                liftMotor.setTargetPosition(midPosition);

                // Turn On RUN_TO_POSITION

                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                liftMotor.setPower(1);
                while (liftMotor.isBusy() && liftMotor.getCurrentPosition()<midPosition) {

                    liftMotor.getCurrentPosition();
                }

                // Stop all motion;
                liftMotor.setPower(0);

                // Turn off RUN_TO_POSITION
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                if(liftMotor.getCurrentPosition()>=(midPosition)){
                    liftServo.setPosition(.4);

                    liftMotor.setTargetPosition(finalPosition);

                    // Turn On RUN_TO_POSITION

                    liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    liftMotor.setPower(1);
                    while (liftMotor.isBusy()&&liftMotor.getCurrentPosition()<finalPosition) {

                        liftMotor.getCurrentPosition();
                    }

                    // Stop all motion;
                    liftMotor.setPower(0);

                    // Turn off RUN_TO_POSITION
                    liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                }
            }

    }


    public void timedDucky(double timeout, double currentTime, double startTime, double power){
        while(timeout<currentTime-startTime){
        duckyMover.setPower(power);
        }
        duckyMover.setPower(0);
    }
//lift servo test
    public void liftServo(double position){
        liftServo.setPosition(position);
    }

    public double getTicksPerRev(){
        return frontLeftTicksPerRev;
    }


    public double getHeading(AngleUnit angleUnit) {
        Orientation angles = imu.getAngularOrientation(AxesReference.EXTRINSIC,
                AxesOrder.ZYX,
                angleUnit);
        return angles.firstAngle;
    }




}
