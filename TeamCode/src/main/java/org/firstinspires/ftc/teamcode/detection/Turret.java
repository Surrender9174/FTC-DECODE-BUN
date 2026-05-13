package org.firstinspires.ftc.teamcode.detection;

import static com.pedropathing.math.MathFunctions.clamp;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Turret {
    private DcMotorEx dcMotor;
    private ElapsedTime timer = new ElapsedTime();
    private double kp = 0.01, kd = 0.00021, ki = 0, ks = 0.01;
    private boolean usePIDF, useKs;

    private double currentposition, lastposition, error;
    private double currentspeed;
    private double power;
    private static double targetposition = 0;


    public Turret(RobotHardware robot){
        dcMotor = robot.motorTurret;

        dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        currentposition = 0;
        usePIDF = true;
    }
    public void update(){
        if(!usePIDF){
            currentposition = dcMotor.getCurrentPosition();
            currentspeed = dcMotor.getVelocity();

            dcMotor.setPower(power);
            return;
        }
        currentposition = dcMotor.getCurrentPosition();
        currentspeed = dcMotor.getVelocity();
        error = targetposition - currentposition;
        power = kp * error + (-currentspeed) + kd;
        if(error >= 4) useKs = true;
        if(error < 4) useKs = false;
        if(useKs) power = power + (-currentspeed) + ks;
        power = clamp(power/battery, -1, 1);
        dcMotor.setPower(power);

        if(lastposition != currentposition)
            timer.reset();
        lastposition = currentposition;
    }
}
