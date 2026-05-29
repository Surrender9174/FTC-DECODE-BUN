package org.firstinspires.ftc.teamcode.basic_functions;

import static androidx.core.math.MathUtils.clamp;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.PanelTelemetry;
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
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
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
    private Transfer transfer;
    private double constant_hood;

    private double turretAngle, cameraAngle, absoluteAngle;

    public static double shooterSpeed = 0, anglePosition = 0.11;
    public static double KangleAdjustment = 0;

    private double goalAngle, goalDistance, GoalAngle;

    private double imaginaryX, imaginaryY, imaginaryDistance;

    private boolean transferMode = false;

    private ElapsedTime timer = new ElapsedTime();

    public Outtake(Turret turret, Shoot shooter, Hood hood, Camera camera) {
        this.turret = turret;
        this.shooter = shooter;
        this.hood = hood;
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

//       goalDistance = Math.sqrt(Math.pow(imaginaryX - robotX, 2) + Math.pow(imaginaryY - robotY, 2));

//       goalAngle = Math.toDegrees(Math.atan2(imaginaryY - robotY, imaginaryX - robotX));

        turretAngle = goalAngle - robotH + 180;

        if (turretAngle > 180) turretAngle = turretAngle - 360;
        if (turretAngle < -180) turretAngle = turretAngle + 360;

       // turretAngle = clamp(turretAngle, -160, 160);

        if (goalX != 0 && goalY != 0) {
            if (!camera.isTrackingMotif())
                turret.setTargetPosition(turretAngle);
            if (transferMode) shooter.setTargetSpeed(getShooterSpeed(goalDistance));
            else shooter.setTargetSpeed(1400);

            //shooter.setTargetSpeed(getShooterSpeed((goalDistance)));
            hood.setPosition(getAngle(goalDistance) - KangleAdjustment * shooter.getSpeedDifference());
            //shooter.setTargetSpeed(shooterSpeed);
            //hood.setPosition(anglePosition - KangleAdjustment * shooter.getSpeedDifference());
        }

        telemetry.addData("Turret", turretAngle);

        telemetry.addData("Goal X", goalX);
        telemetry.addData("Goal Y", goalY);
        telemetry.addLine("");

        telemetry.addData("Goal Distance", goalDistance);
        telemetry.addLine("");

        PanelTelemetry.addData("HoodPos", anglePosition - KangleAdjustment * shooter.getSpeedDifference());
    }

    private double getShooterSpeed(double x) {
        return 0.0000000731412 * Math.pow(x, 4) - 0.0000893328 * Math.pow(x, 3) + 0.0399948 * Math.pow(x, 2) - 5.92603 * x + 1154.75858;
    }
    private double getAngle(double x) {
        return 0.00000000198847 * Math.pow(x, 4) - 0.00000230068 * Math.pow(x, 3)  + 0.00095348 * Math.pow(x, 2) - 0.163724 * x + 9.97497;
    }
    private double getTime(double x) {
        return 0.5;
    }
    public void initiateTransfer() {
        transferMode = true;
    }
    public void endTransfer() {
        transferMode = false;
    }
}
