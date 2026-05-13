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
    private double kp = 0.03, kd = 0.0021, ki = 0, ks = 0.01;
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
            currentposition = dcMotor.getTargetPosition();
            currentspeed = dcMotor.getVelocity();

            dcMotor.setPower(power);
            return;
        }
        error = targetposition - currentposition;
        if(error >= 4) useKs = true;
        if(error < 4) useKs = false;
        if(useKs) power = power + (-currentspeed) * ks;

        power = kp * error + (-currentspeed) + kd;
        power = clamp(power/battery, -1, 1);
        dcMotor.setPower(power);

        if(lastposition != currentposition)
            timer.reset();
        lastposition = currentposition;
    }
}
