package org.firstinspires.ftc.teamcode.Objects.Drivetrain;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Chassis {
    private DcMotorEx motorFrontRight, motorFrontLeft, motorBackLeft, motorBackRight;

    private double vx, vy, w;
    private double fr, fl, bl, br;
    private double power, theta, sin, cos;
    private double maxx;

    public Chassis(RobotHardware robot) {
        this.motorFrontRight = robot.motorFrontRight;
        this.motorFrontLeft = robot.motorFrontLeft;
        this.motorBackLeft = robot.motorBackLeft;
        this.motorBackRight = robot.motorBackRight;

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void updateFieldCentric() {
        power = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
        theta = Math.atan2(vy, vx) - Math.PI / 4 - (Math.toRadians(robotH) - Math.toRadians(90));

        sin = Math.sin(theta); cos = Math.cos(theta);
        maxx = Math.max(Math.abs(sin), Math.abs(cos));

        fr = power * sin / maxx + w;
        fl = power * cos / maxx - w;
        bl = power * sin / maxx - w;
        br = power * cos / maxx + w;

        maxx = power + Math.abs(w);

        if (maxx > 1) {
            fr /= maxx; fl /= maxx;
            bl /= maxx; br /= maxx;
        }

//        if (maxx > 0.6) {
//            fr = fr / maxx * 0.6; fl = fl / maxx * 0.6;
//            bl = bl / maxx * 0.6; br = br / maxx * 0.6;
//        }

        motorFrontLeft.setPower(fl);
        motorFrontRight.setPower(fr);
        motorBackLeft.setPower(bl);
        motorBackRight.setPower(br);

        telemetry.addData("motorFrontLeft", motorFrontLeft.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("motorFrontRight", motorFrontRight.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("motorBackLeft", motorBackLeft.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("motorBackRight", motorBackRight.getCurrent(CurrentUnit.MILLIAMPS));
    }

    public void updateRobotCentric() {
        power = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
        theta = Math.atan2(vy, vx) - Math.PI / 4;

        sin = Math.sin(theta); cos = Math.cos(theta);
        maxx = Math.max(Math.abs(sin), Math.abs(cos));

        fr = power * sin / maxx - w;
        fl = power * cos / maxx + w;
        bl = power * sin / maxx + w;
        br = power * cos / maxx - w;

        maxx = power + Math.abs(w);

        if (maxx > 1) {
            fr /= maxx; fl /= maxx;
            bl /= maxx; br /= maxx;
        }

        motorFrontLeft.setPower(fl);
        motorFrontRight.setPower(fr);
        motorBackLeft.setPower(bl);
        motorBackRight.setPower(br);
    }


    public void setMovement(double vx, double vy, double w) {
        this.vx = vx;
        this.vy = vy;
        this.w = w;
    }
}
