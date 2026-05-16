package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static androidx.core.math.MathUtils.clamp;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Hood {
    private Servo servoHood;
    public static double position;
    private static double upperLimit, lowerLimit;
    private boolean istarcking;

    public Hood(RobotHardware robot){
        servoHood = robot.servoHood;
        servoHood.setPosition(position);
    }

    public void update(){
        position = clamp(position, -1, 1);
        servoHood.setPosition(position);
    }

   public void upperLimit(double position){

   }
}
