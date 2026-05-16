package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;

import java.util.concurrent.TimeUnit;

public class Transfer {
    public Spindexer spindexer;
    public Trapa trapa;

    public ElapsedTime timer = new ElapsedTime();

    public enum StateTransfer{
        IDLE,
        INIT,
        MOVE,
        FINISH;
    }

    private StateTransfer state, laststate;
    private boolean moving = false;
    public Transfer(Spindexer spindexer, Trapa trapa){
        this.spindexer = spindexer;
        this.trapa = trapa;

        state = StateTransfer.IDLE;
    }

    public void update(){
            switch (state){
                case IDLE:
                    break;
                case INIT:
                    spindexer.setState(Spindexer.StateSpindexer.SHOOTING);
                    trapa.setState(Trapa.StateTrapa.OUTTAKE);

                    timer.reset();
                    state = StateTransfer.MOVE;

                    break;
                case MOVE:
                    //spindexer.setSpeed(-1);
                    if(timer.seconds() > 1.2){
                        spindexer.disableShooting();
                        if(timer.milliseconds() > 1600)
                            state = StateTransfer.FINISH;
                    }
                    break;
                case FINISH:
                    spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);

                    state = StateTransfer.IDLE;
                    break;
            }


        telemetry.addData("Timer", timer.seconds());
        telemetry.addData("State", state);
        telemetry.addData("LastState", laststate);

    }
    public void setState(StateTransfer state){
        this.state = state;
    }
}
