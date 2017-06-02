package org.firstinspires.ftc.teamcode;

/*
Modern Robotics Color Sensor Example with color number
Created 9/29/2016 by Colton Mehlhoff of Modern Robotics using FTC SDK 2.2
Reuse permitted with credit where credit is due

Configuration:
I2CDevice "cc" (MRI Color Sensor with default I2C address 0x3c (0x1e 7-bit)

ModernRoboticsI2cColorSensor class is not being used because it can not access color number.
ColorSensor class is not being used because it can not access color number.

To change color sensor I2C Addresses, go to http://modernroboticsedu.com/mod/lesson/view.php?id=96
Support is available by emailing support@modernroboticsinc.com.
*/

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "FlipRed", group = "MRI")
// @Autonomous(...) is the other common choice
//@Disabled
public class FlipRed extends OpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    byte[] colorCcache;

    I2cDevice colorC;
    I2cDeviceSynch colorCreader;
    Servo servo;
    double servoPosition = 0.0;



    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        //the below lines set up the configuration file
        colorC = hardwareMap.i2cDevice.get("cc");
        colorCreader = new I2cDeviceSynchImpl(colorC, I2cAddr.create8bit(0x3c), false);
        colorCreader.engage();
        servo = hardwareMap.servo.get("servo");
        servo.setPosition(servoPosition);
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


    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());

        //The below two if() statements ensure that the mode of the color sensor is changed only once each time the touch sensor is pressed.
        //The mode of the color sensor is saved to the sensor's long term memory. Just like flash drives, the long term memory has a life time in the 10s or 100s of thousands of cycles.
        //This seems like a lot but if your program wrote to the long term memory every time though the main loop, it would shorten the life of your sensor.



        colorCcache = colorCreader.read(0x04, 1);

        //display values
        telemetry.addData("2 #C", colorCcache[0] & 0xFF);
        if (colorCcache[0] < 3 && colorCcache[0] >0){
            double servoPosition=1.0;
            servo.setPosition(servoPosition);
        }
        else{
            double servoPosition=0.5;
            servo.setPosition(servoPosition);
        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}