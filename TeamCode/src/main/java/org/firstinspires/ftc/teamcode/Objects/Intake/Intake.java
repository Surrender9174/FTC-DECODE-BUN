package org.firstinspires.ftc.teamcode.Objects.Intake;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

import java.security.PublicKey;

public class Intake {
    public ActiveIntake activeIntake;
    public Trapa trapa;

    public enum StateIntake{
        INIT,
        INTAKE,
        OUTTAKE;
    }

    public StateIntake state, laststate;
    public Intake(ActiveIntake activeIntake, Trapa trapa){
        this.activeIntake = activeIntake;
        this.trapa = trapa;

        state = StateIntake.INIT;
    }
    public void update(){
        if(state != laststate){
            switch (state){
                case INIT:

            }
        }
    }
}
