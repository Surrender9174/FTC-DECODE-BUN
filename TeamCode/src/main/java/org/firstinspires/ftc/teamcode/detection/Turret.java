package org.firstinspires.ftc.teamcode.detection;

import static com.pedropathing.math.MathFunctions.clamp;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Turret {
    private DcMotorEx motor;
    private ElapsedTime timer = new ElapsedTime();
    public static double kp = 0.0342, kd = 0.00285, ki = 0, ks = 1.369;
    private boolean usePIDF, useKs;

    private double currentposition, lastposition, error;
    private double currentspeed;
    private double power;
    private static double targetposition = 0;


    public Turret(RobotHardware robot){
        motor = robot.motorTurret;

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        currentposition = 0;
        usePIDF = true;
    }
    public void update(){
        if(!usePIDF){
            currentposition = motor.getCurrentPosition();
            currentspeed = motor.getVelocity();

            motor.setPower(power);
            return;
        }
        currentposition = motor.getCurrentPosition();
        currentspeed = motor.getVelocity();

        error = targetposition - currentposition;

        power = kp * error + (-currentspeed) * kd;

        if(Math.abs(error) >= 4) useKs = true;
        if(Math.abs(error) <= 2) useKs = false;

        if(useKs) power = power + Math.signum(error) * ks;


        power = clamp(power/battery, -1, 1);
        if(targetposition > 170)
            targetposition = -165;
        if(targetposition < -175)
            targetposition = 160;

        motor.setPower(power);

        if(lastposition != currentposition)
            timer.reset();
        lastposition = currentposition;

        telemetry.addData("pozitie", motor.getCurrentPosition());
        telemetry.addData("power", power/battery);

        telemetry.update();
    }

    public void setTargetposition(){
        Turret.targetposition = (int) (targetposition);
        usePIDF = false;
    }
}
