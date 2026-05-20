package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static androidx.core.math.MathUtils.clamp;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Hood {
    private Servo servoHood;
    public static double position;
    private final double upperLimit = 0.91, lowerLimit = 0.13;
    private boolean istarcking;

    public Hood(RobotHardware robot){
        servoHood = robot.servoHood;
        servoHood.setPosition(lowerLimit);
    }

   public void setPosition(double position) {
        this.position = clamp(position, lowerLimit, upperLimit);
        servoHood.setPosition(this.position);
   }
}
