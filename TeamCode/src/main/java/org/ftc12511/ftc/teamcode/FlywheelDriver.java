package org.ftc12511.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Created by lucas on 4/14/2017.
 */

public class FlywheelDriver {
    private DcMotor Left_Flywheel = null;
    private DcMotor Right_Flywheel = null;
    Telemetry telemetry;


    public void init(Telemetry telemetry, HardwareMap hardwareMap) {
        telemetry.addData("Status", "Initialized");

        Left_Flywheel = hardwareMap.dcMotor.get("Left_Flywheel");
        Right_Flywheel = hardwareMap.dcMotor.get("Right_Flywheel");

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

    public void Shoot(float left_power, float right_power) {
        Left_Flywheel.setPower(left_power);
        Right_Flywheel.setPower(right_power);

    }
}
