package org.firstinspires.ftc.teamcode.basic_functions;

import static androidx.core.math.MathUtils.clamp;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotVelocity;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotVelocityAngle;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;

@Configurable
public class Outtake{
    private Turret turret;
    private Shoot shooter;
    private Hood hood;
    private Camera camera;
    private Spindexer spindexer;
    private Trapa trapa;

    private double constant_hood;

    private double turretAngle, cameraAngle, absoluteAngle;

    public static double shooterSpeed = 0, anglePosition = 0.13;
    public static double KangleAdjustment = 0;

    private double goalAngle, goalDistance;

    private double imaginaryX, imaginaryY, imaginaryDistance;

    private boolean transferMode = false;

    private ElapsedTime timer = new ElapsedTime();

    public Outtake(Turret turret, Shoot shooter, Hood hood, Spindexer spindexer, Trapa trapa, Camera camera) {
        this.turret = turret;
        this.shooter = shooter;
        this.hood = hood;
        this.spindexer = spindexer;
        this.trapa = trapa;
        this.camera = camera;
    }

    public void update() {
        if (goalX == 0 && goalY == 0)
            return;

        goalDistance = Math.sqrt((Math.pow(goalX - robotX, 2) + Math.pow(goalY - robotY, 2)));
        goalAngle = Math.toDegrees(Math.atan2(goalY - robotY, goalX - robotX));

        //imaginaryDistance = robotVelocity * getTime(goalDistance);
        //imaginaryX = goalX + Math.cos(robotVelocityAngle + Math.PI) * imaginaryDistance;
        //imaginaryY = goalY + Math.sin(robotVelocityAngle + Math.PI) * imaginaryDistance;

        //goalDistance = Math.sqrt(Math.pow(imaginaryX - robotX, 2) + Math.pow(imaginaryY - robotY, 2));

       //GoalAngle = Math.toDegrees(Math.atan2(imaginaryY - robotY, imaginaryX - robotX));

        turretAngle = goalAngle - robotH + 180;

        if (turretAngle > 180) turretAngle = turretAngle - 360;
        if (turretAngle < -180) turretAngle = turretAngle + 360;

       // turretAngle = clamp(turretAngle, -160, 160);

        if (goalX != 0 && goalY != 0) {
            if (!camera.isTrackingMotif())
                turret.setTargetPosition(turretAngle);
            //if (transferMode) //shooter.setTargetSpeed(getShooterSpeed(goalDistance));
            //else //shooter.setTargetSpeed(2050);

            //hood.setPosition(getShooterSpeed(goalDistance));

            shooter.setTargetSpeed(shooterSpeed);
            hood.setPosition(anglePosition - KangleAdjustment * shooter.getSpeedDifference());
        }

        telemetry.addData("Turret", turretAngle);

        telemetry.addData("Goal X", goalX);
        telemetry.addData("Goal Y", goalY);
        telemetry.addLine("");

        telemetry.addData("Goal Distance", goalDistance);
        telemetry.addLine("");
    }

    private double getShooterSpeed(double x) {
        return 0;
    }
    private double getAngle(double x) {
        return 0;
    }
    private double getTime(double x) {
        return 0.9;
    }
    public void initiateTransfer() {
        transferMode = true;
    }
    public void endTransfer() {
        transferMode = false;
    }
}
