package org.firstinspires.ftc.teamcode.detection;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Camera {
    private Limelight3A limelight3A;

    public Camera(RobotHardware robot) {
        limelight3A = robot.limelight;
        limelight3A.setPollRateHz(100);
    }

    public void init() {
        limelight3A.start();
    }

    public void update() {
        LLResult result = limelight3A.getLatestResult();
        if (result != null && result.isValid()) {
            double tx = result.getTx();
            double ty = result.getTy();
            double ta = result.getTa();

            telemetry.addData("Target X", tx);
            telemetry.addData("Target Y", ty);
            telemetry.addData("Target Area", ta);
        }
    }
}
