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

    @Override
    public void runOpMode() throws InterruptedException {
        navigator= new EncoderBasedNavigator();
        navigator.Init(this.telemetry, this.hardwareMap);

        colorC = hardwareMap.i2cDevice.get("cc");
        colorCreader = new I2cDeviceSynchImpl(colorC, I2cAddr.create8bit(0x3c), false);
        colorCreader.engage();
        servo = hardwareMap.servo.get("servo");
        servo.setPosition(servoPosition);
        leftMotor = hardwareMap.dcMotor.get("Left_Motor");
        rightMotor = hardwareMap.dcMotor.get("Right_Motor");
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
        double Gearing = 1.0 / 3.0;
        double pi = 3.14159265358978;
        double Circumfrence = WheelDiameterInches * pi;
        double WheelDistance = 16.0;

        colorCcache = colorCreader.read(0x04, 1);
        servoPosition = 1.0;
        servo.setPosition(servoPosition);
        telemetry.addData("Initialize 2 #C", colorCcache[0] & 0xFF);
        telemetry.update();
        //while there is no color, drive forward
        while (colorCcache[0] == 0 || colorCcache[0] == 16) {
            colorCcache = colorCreader.read(0x04, 1);
            leftMotor.setPower(power);
            rightMotor.setPower(power);
            telemetry.addData("Driveforward 2 #C", colorCcache[0] & 0xFF);
            telemetry.update();
        }
        //when a color is detected, stop
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        //display values
        telemetry.addData("BeginDetection 2 #C", colorCcache[0] & 0xFF);
        telemetry.update();
        //while a color is detected...
        while (colorCcache[0] != 0 || colorCcache[0] != 16) {
            telemetry.addData("Relocate 2 #C", colorCcache[0] & 0xFF);
            telemetry.update();
            //If the beacon is blue, extend servo and change correct to false
            if (colorCcache[0] < 4 && colorCcache[0] > 0 && servoPosition != 1.0) {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                double servoPosition = 1.0;
                servo.setPosition(servoPosition);
                telemetry.addData("Ascend 2 #C", colorCcache[0] & 0xFF);
                telemetry.update();
                sleep(3000);
            }
            //If the beacon is red, retract the servo and change correct to true
            if (colorCcache[0]>9 && servoPosition != 0.0){
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                double servoPosition = 0.0;
                servo.setPosition(servoPosition);
                telemetry.addData("Descend 2 #C", colorCcache[0] & 0xFF);
                telemetry.update();
                sleep(3000);

            }

            //Drive forward
            leftMotor.setPower(power);
            rightMotor.setPower(power);
            colorCcache = colorCreader.read(0x04, 1);


        }
        //when no color detected, stop
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void Loop() {
        telemetry.addData("End 2 #C", colorCcache[0] & 0xFF);
        telemetry.update();



    }

}

