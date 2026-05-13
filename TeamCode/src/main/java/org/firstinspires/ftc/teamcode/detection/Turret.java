package org.firstinspires.ftc.teamcode.detection;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Turret {
    private DcMotor dcMotor;
    private double kp = 0, kd = 0, ki = 0, ks = 0;
    private boolean useEncoder, useKs;

    public Turret(RobotHardware robot){
        dcMotor = robot.motorTurret;

        dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void init(){

    }
    public void update(){

    }
}
