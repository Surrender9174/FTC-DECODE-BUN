package org.firstinspires.ftc.teamcode.Objects.Intake;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

import java.security.PublicKey;

public class Intake {
    public ActiveIntake activeIntake;
    public Trapa trapa;
    public Spindexer spindexer;
    public ServoIntake servoIntake;

    public enum StateIntake{
        INIT,
        INTAKE,
        OUTTAKE;
    }

    public StateIntake state, laststate;
    public Intake(ActiveIntake activeIntake, Trapa trapa, Spindexer spindexer, ServoIntake servoIntake){
        this.activeIntake = activeIntake;
        this.trapa = trapa;
        this.spindexer = spindexer;
        this.servoIntake = servoIntake;

        state = StateIntake.INIT;
    }

    public void update(){
        if(state != laststate){
            switch (state){
                case INIT:
                    activeIntake.setState(ActiveIntake.ActiveIntakeStates.INIT);
                    trapa.setState(Trapa.StateTrapa.OUTTAKE);
                    servoIntake.setState(ServoIntake.StariServoIntake.OUTTAKE);

                    spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);

                    break;
                case INTAKE:
                    activeIntake.setState(ActiveIntake.ActiveIntakeStates.INTAKE);
                    trapa.setState(Trapa.StateTrapa.INTAKE);
                    servoIntake.setState(ServoIntake.StariServoIntake.INTAKE);

                    spindexer.setState(Spindexer.StateSpindexer.INTAKE);

                    break;
                case OUTTAKE:
                    activeIntake.setState(ActiveIntake.ActiveIntakeStates.OUTTAKE);
                    trapa.setState(Trapa.StateTrapa.INTAKE);
                    servoIntake.setState(ServoIntake.StariServoIntake.OUTTAKE);

                    spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);
                    break;
            }
        }

        telemetry.addData("Entered", 1);
        laststate = state;
    }

    public void setState(StateIntake state) {
        this.state = state;
    }
    public StateIntake getState(){
        return state;
    }
}
