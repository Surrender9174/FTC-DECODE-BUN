package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;

import java.util.concurrent.TimeUnit;

public class Transfer {
    public Spindexer spindexer;
    public Trapa trapa;

    public ActiveIntake activeIntake;
    public ElapsedTime timer = new ElapsedTime();

    private double speed = -12;

    public enum StateTransfer{
        IDLE,
        INIT,
        MOVE,
        FINISH;
    }

    private StateTransfer state, laststate;
    private boolean moving = false;
    public Transfer(Spindexer spindexer, Trapa trapa, ActiveIntake activeIntake){
        this.spindexer = spindexer;
        this.trapa = trapa;
        this.activeIntake = activeIntake;

        state = StateTransfer.IDLE;
    }

    public void update(){
            switch (state){
                case IDLE:
                    break;
                case INIT:
                    spindexer.setTransferSpeed(speed / battery);
                    spindexer.setState(Spindexer.StateSpindexer.SHOOTING);
                    trapa.setState(Trapa.StateTrapa.OUTTAKE);
                    //activeIntake.setState(ActiveIntake.ActiveIntakeStates.INTAKE);

                    timer.reset();
                    state = StateTransfer.MOVE;

                    break;
                case MOVE:
                    //spindexer.setSpeed(-1);
                    if(timer.seconds() < 0.8) break;
                    state = StateTransfer.FINISH;

                    break;
                case FINISH:
                    //spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);
                    spindexer.setTransferSpeed(0);

                    //activeIntake.setState(ActiveIntake.ActiveIntakeStates.INIT);

                    state = StateTransfer.IDLE;
                    break;
            }

    }
    public void setState(StateTransfer state){
        this.state = state;
    }

    /*public boolean CasianSafeProff(){
        if(spindexer.SafeForCasian()) return true;
        return false;
    }*/
}
