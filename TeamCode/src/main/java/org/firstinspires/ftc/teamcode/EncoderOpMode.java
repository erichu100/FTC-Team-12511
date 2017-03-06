package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by lucas on 2/26/2017.
 */
@TeleOp(name="EncoderOpMode", group="Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class EncoderOpMode extends OpMode
{
    // Declare motors
    DcMotor Left_Motor = null;
    DcMotor Right_Motor = null;


    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    // private DcMotor leftMotor = null;
    // private DcMotor rightMotor = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        //Initialize motors
        Left_Motor = hardwareMap.dcMotor.get("Left_Motor");
        Right_Motor = hardwareMap.dcMotor.get("Right_Motor");
        Left_Motor.setDirection(DcMotorSimple.Direction.REVERSE);
        Left_Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Right_Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        // leftMotor  = hardwareMap.dcMotor.get("left motor");
        // rightMotor = hardwareMap.dcMotor.get("right motor");

        // eg: Set the drive motor directions:
        // Reverse the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        // telemetry.addData("Status", "Initialized");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

        runtime.reset();
        Left_Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Right_Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        Left_Motor.setTargetPosition(1440);
        Right_Motor.setTargetPosition(1440);

        //Set to RUN_TO_POSITION mode
        Left_Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Right_Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Set drive power
        Left_Motor.setPower(0.5);
        Right_Motor.setPower(0.5);

        while(Left_Motor.isBusy() && Right_Motor.isBusy())
        {
            //Wait until target position is reached
        }

        //Stop and change modes back to normal
        Left_Motor.setPower(0);
        Right_Motor.setPower(0);
        Left_Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Right_Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());

        // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
        // leftMotor.setPower(-gamepad1.left_stick_y);
        // rightMotor.setPower(-gamepad1.right_stick_y);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}