package org.ftc12511.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 3/25/2017.
 */
@TeleOp(name="BeaconPusher", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class BeaconPusher extends LinearOpMode
{
    EncoderBasedNavigator navigator;


    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() throws InterruptedException {
        navigator= new EncoderBasedNavigator();
        navigator.Init(this.telemetry, this.hardwareMap);
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
        double WheelDiameterInches = 4.0;
        double Gearing = 1.0/3.0;
        double pi = 3.14159265358978;
        double Circumfrence = WheelDiameterInches*pi;
        double WheelDistance = 16.0;

        // Forward
        navigator.DriveByEncoder((int)(1440*3.5), (int)(+1440*3.5), 0.5);
        // Turn.
        navigator.DriveByEncoder((int)(1440*1.8),- (int)(+1440*1.8), 0.5);

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

