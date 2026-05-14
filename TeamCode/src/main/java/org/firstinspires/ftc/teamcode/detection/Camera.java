package org.firstinspires.ftc.teamcode.detection;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Camera {
    private Limelight3A limelight3A;
    private double x, y, lastX, lastY;
    private int ID;

    private boolean detected = false;

    public Camera(RobotHardware robot) {
        limelight3A = robot.limelight;
    }

    public void init() {
        limelight3A.setPollRateHz(100);
        limelight3A.start();
        goal();
    }

    public void update() {
        if(detected) {
            LLResult result = limelight3A.getLatestResult();
            if (result != null && result.isValid()) {
                for (LLResultTypes.FiducialResult fiducialResult : result.getFiducialResults()) {
                    ID = fiducialResult.getFiducialId();
                }
                x = result.getTx();
                y = result.getTy();
                telemetry.addData("Target X camera", x);
                telemetry.addData("Target Y camera", y);
            }
        }
    }

    public void goal(){
        if(alliance == 1)
            limelight3A.pipelineSwitch(1);
        else
            limelight3A.pipelineSwitch(0);
    }

    public void detected(){
        if(detected == true){
            return;
        }
        detected = true;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
