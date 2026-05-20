package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.*;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Camera{
    private Limelight3A limelight;

    public static double cameraAngle = 0;

    private boolean activated, detected;

    private double x = 0, y = 0, z = 0, lastX, lastY, lastZ, last_lastX, last_lastY, last_lastZ;

    private Pose3D position;

    private int ID;

    private boolean trackingMotif = false;
    private boolean dead = false;

    public Camera(RobotHardware robot) {
        limelight = robot.limelight;

        searchGoal();

        limelight.start();
        activated = false;
    }

    public void detectGoal() {
        if (trackingMotif) return;

        detected = false;

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            detected = true;

            for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
                position = fiducial.getTargetPoseCameraSpace();

                x = position.getPosition().z * Math.cos(Math.toRadians(cameraAngle)) + position.getPosition().y * Math.sin(Math.toRadians(cameraAngle));
                y = -position.getPosition().x;
                z = position.getPosition().z * Math.sin(Math.toRadians(cameraAngle)) - position.getPosition().y * Math.cos(Math.toRadians(cameraAngle));

                x = x * 100; y = y * 100; z = z * 100;

                if (x == lastX && y == lastY && z == lastZ) {
                    dead = true;
                }
                else {
                    dead = false;
                }

                lastX = x; lastY = y; lastZ = z;

            }
        }
    }

    public void update() {
        if (trackingMotif) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {

                for (LLResultTypes.FiducialResult fr : result.getFiducialResults()) {
                    ID = fr.getFiducialId();
                }
            }
        }

        telemetry.addData("ID", ID);
        telemetry.addData("detected", detected);

        telemetry.addData("camreaX", x);
        telemetry.addData("cameraY", y);
        telemetry.addData("cameraZ", z);

        telemetry.addLine("");

    }

    public void searchForMotif() {
        trackingMotif = true;

        limelight.pipelineSwitch(2);
    }

    public void searchGoal() {
        trackingMotif = false;

        if (alliance == -1) limelight.pipelineSwitch(1);
        else limelight.pipelineSwitch(0);
    }

    public boolean isTrackingMotif() {
        return (trackingMotif);
    }

    public int getID() {
        return ID;
    }
    public void resetID() {
        ID = 0;
    }

    public double getGoalX() {
        return x;
    }

    public double getGoalY() {
        return y;
    }
    public double getGoalHeight() {
        return z;
    }

    public boolean detected() {
        return detected;
    }
    public boolean isDead() {
        return dead;
    }

    public void start() {
        //limelight.start();
        activated = true;
    }
    public void stop() {
        //limelight.stop();
        activated = false;
    }

    public void resetDetection() {
        x = 0; y = 0; z = 0;
        lastX = 0; lastY = 0; lastZ = 0;
        last_lastX = 0; last_lastY = 0; last_lastZ = 0;
        goalX = 0; goalY = 0;
    }
}
