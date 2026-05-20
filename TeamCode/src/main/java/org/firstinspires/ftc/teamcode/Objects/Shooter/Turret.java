package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static com.pedropathing.math.MathFunctions.clamp;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.lastgamepad;
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

    public ElapsedTime stabletimer = new ElapsedTime();

    private boolean usePIDF, useKs;

    private double K = 4.166666666667;

    private double currentPosition, lastPosition, error;
    private double currentSpeed;
    private double power;
    private static double targetPosition = 0;


    public Turret(RobotHardware robot){
        motor = robot.motorTurret;

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        currentPosition = 0;
        targetPosition = 0;
        usePIDF = true;
    }
    public void update(){
        if(!usePIDF){
            currentPosition = motor.getCurrentPosition();
            currentSpeed = motor.getVelocity();

            motor.setPower(power);
            return;
        }
        currentPosition = motor.getCurrentPosition();
        currentSpeed = motor.getVelocity();

        /*if(currentPosition > 180 * K) {
            currentPosition = currentPosition - 360 * K;
            targetPosition = targetPosition - 360 * K;
        }

        else if (currentPosition < -180*K) {
            currentPosition = currentPosition + 360 * K;
            targetPosition = targetPosition + 360 * K;
        }*/

        error = targetPosition - currentPosition;

        /*if (error > 180 * K) error = error - 360 * K;
        if (error < -180 * K) error = error + 360 * K;*/

        power = kp * error + (-currentSpeed) * kd;

        if(Math.abs(error) >= 4) useKs = true;
        if(Math.abs(error) <= 2) useKs = false;

        if(useKs) power = power + Math.signum(error) * ks;

        power = clamp(power/battery, -1, 1);
        motor.setPower(power);

        //motor.setPower(0);

        if(currentPosition != lastPosition) stabletimer.reset();

        lastPosition = currentPosition;

        telemetry.addData("turretAngle", getTurretAngle());
        telemetry.addLine("");
        telemetry.addData("current pos", currentPosition);

    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition * K;
    }

    public double getTurretAngle() {
        return (currentPosition / K);
    }

    public boolean isStable(){
        return (stabletimer.seconds() > 0.3);
    }
}
