package TestBot.Mechanisms;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


public class FreightBotInfo {

    //define motors

    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    //modules!


    //encoders
    private double backLeftTicksPerRev;
    private double backRightTicksPerRev;
    private double frontLeftTicksPerRev;
    private double frontRightTicksPerRev;

    //imu
    private BNO055IMU imu;

    //mecanum?
    public MecanumDriveBase mecanumDriveBase;


    public void init(HardwareMap hwMap) {

        mecanumDriveBase = new MecanumDriveBase(frontLeft, frontRight, backLeft, backRight);
        //drivetrain systems

        backLeft = hwMap.get(DcMotor.class, "lb");
        backRight = hwMap.get(DcMotor.class, "rb");
        frontLeft = hwMap.get(DcMotor.class, "lf");
        frontRight = hwMap.get(DcMotor.class, "rf");

        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

      /*  backLeftTicksPerRev = backLeft.getMotorType().getTicksPerRev();
        backRightTicksPerRev = backRight.getMotorType().getTicksPerRev();
        frontLeftTicksPerRev = frontLeft.getMotorType().getTicksPerRev();
        frontRightTicksPerRev = frontRight.getMotorType().getTicksPerRev();
*/

        //modules
        //launcher module stuff


        //imu
        imu = hwMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        //change to default set of parameters go here
        imu.initialize(params);


    }

    //test mecanum
    public void mecanumDrive(double forward, double sideways, double rotation){

        // Set the power of each motor
        frontLeft.setPower((forward + sideways + rotation) * 1.0);
        frontRight.setPower((forward - sideways) - rotation * 1.0);
        backLeft.setPower((forward - sideways) + rotation * 1.0);
        backRight.setPower(forward + (sideways - rotation) * 1.0);


    }

    public void mecanumTargetPos(int horizontalPos, int verticalPos){
        // Set the position of each motor
        frontLeft.setTargetPosition((verticalPos + horizontalPos + 0));
        frontRight.setTargetPosition((verticalPos - horizontalPos) - 0);
        backLeft.setTargetPosition((verticalPos - horizontalPos) + 0 );
        backRight.setTargetPosition(verticalPos + (horizontalPos - 0));
    }




    public double getHeading(AngleUnit angleUnit) {
        Orientation angles = imu.getAngularOrientation(AxesReference.EXTRINSIC,
                AxesOrder.ZYX,
                angleUnit);
        return angles.firstAngle;
    }




}
