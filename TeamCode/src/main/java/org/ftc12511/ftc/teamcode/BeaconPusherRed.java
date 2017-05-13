package org.ftc12511.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 3/25/2017.
 */
@Autonomous(name="BeaconPusherRed", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class BeaconPusherRed extends LinearOpMode
{
    EncoderBasedNavigator navigator;


    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    byte[] colorCcache;

    I2cDevice colorC;
    I2cDeviceSynch colorCreader;
    Servo servo;
    double servoPosition = 0.0;
    DcMotor leftMotor;
    DcMotor rightMotor;
    double power = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        navigator= new EncoderBasedNavigator();
        navigator.Init(this.telemetry, this.hardwareMap);

        colorC = hardwareMap.i2cDevice.get("cc");
        colorCreader = new I2cDeviceSynchImpl(colorC, I2cAddr.create8bit(0x3c), false);
        colorCreader.engage();
        servo = hardwareMap.servo.get("servo");
        servo.setPosition(servoPosition);
        waitForStart();
        runtime.reset();

        Start();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Loop();
            telemetry.update();
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop

        }
    }



    public void Start() {
        runtime.reset();

        int leftPosition = 1440, rightPosition = 1440;
        double power = 0.5;
        double TicksPerRevolution = 1440;
        double WheelDiameterInches = 3.75;
        double Gearing = 1.0/3.0;
        double pi = 3.14159265358978;
        double Circumfrence = WheelDiameterInches*pi;
        double WheelDistance = 16.0;
        boolean correct = false;
        // Forward
        navigator.DriveByEncoder((int)(1440*3.5), (int)(+1440*3.5), 0.5);
        // Turn.
        navigator.DriveByEncoder((int)(1440*1.8),- (int)(+1440*1.8), 0.5);
        //Detect which color the beacon is
        colorCcache = colorCreader.read(0x04, 1);
        //while there is no power, drive forward
        while (colorCcache[0]==0 || colorCcache[0]==16){
            leftMotor.setPower(power);
            rightMotor.setPower(power);
        }
        //when a color is detected, stop
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        //display values
        telemetry.addData("2 #C", colorCcache[0] & 0xFF);
        //If the beacon is blue, extend servo and change correct to false
        if (colorCcache[0] < 3 && colorCcache[0] > 0 ){
            double servoPosition=1.0;
            servo.setPosition(servoPosition);
            correct = false;
        }
        //If the beacon is red, retract the servo and change correct to true
        else{
            double servoPosition=0.0;
            servo.setPosition(servoPosition);
            correct = true;
        }
        //Drive forward
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        //If correct is true, then wait until the beacon is not red, then stop, extend the servo
        //then continue moving until no color is detected
        if (correct){
            while (colorCcache[0] > 9){

            }
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            double servoPosition=1.0;
            leftMotor.setPower(power);
            rightMotor.setPower(power);
            while (colorCcache[0]!=0 || colorCcache[0]!=16){

            }
        }
        //If correct is false, while the sensor detects blue, drive forward.
        //When it stops, stop and retract the servo
        //Drive forward until no color is detected
        if  (correct = false){
            while (colorCcache[0] < 3){

            }
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            double servoPosition=0.0;
            leftMotor.setPower(power);
            rightMotor.setPower(power);
            while (colorCcache[0]!=0 || colorCcache[0]!=16){

            }
        }
        //Stop
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        // DriveByEncoder((int)(1440*3.14), (int)(+1440*3.14), 0.5);
    }



    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void Loop() {

        // not sensitive.
        if (Math.abs(gamepad1.left_stick_y) < 0.1 && Math.abs(gamepad1.right_stick_y) < 0.1)
        {
            //telemetry.addData("Left Position", leftMotor.getCurrentPosition());
            //telemetry.addData("Right Position", rightMotor.getCurrentPosition());
            return;
        }

        int x = (int)(3* 1440 * -gamepad1.left_stick_y);
        int y = (int)(3* 1440 * -gamepad1.right_stick_y);

        navigator.DriveByEncoder(x, y, 1);
        // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
        telemetry.addData("Status", "Running: " + runtime.toString());


    }


}

