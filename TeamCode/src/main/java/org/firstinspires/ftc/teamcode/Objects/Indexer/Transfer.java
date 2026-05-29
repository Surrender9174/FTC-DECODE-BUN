package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.basic_functions.Outtake;

import java.util.concurrent.TimeUnit;

public class Transfer {
    public Spindexer spindexer;
    public Trapa trapa;
    private Shoot shooter;
    public ActiveIntake activeIntake;

    public Outtake outtake;
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
    public Transfer(Spindexer spindexer, Trapa trapa, ActiveIntake activeIntake, Outtake outtake, Shoot shooter){
        this.spindexer = spindexer;
        this.trapa = trapa;
        this.activeIntake = activeIntake;
        this.outtake = outtake;
        this.shooter = shooter;

        state = StateTransfer.IDLE;
    }

    public void update(){
            switch (state){
                case IDLE:
                    break;
                case INIT:
                    outtake.initiateTransfer();

                    if (Math.abs(shooter.getSpeedDifference()) > 20) break;

                    spindexer.setTransferSpeed(-1);
                    spindexer.setState(Spindexer.StateSpindexer.SHOOTING);
                    trapa.setState(Trapa.StateTrapa.OUTTAKE);
                    //activeIntake.setState(ActiveIntake.ActiveIntakeStates.INTAKE);

                    timer.reset();
                    state = StateTransfer.MOVE;

                    break;
                case MOVE:
                    //spindexer.setSpeed(-1);
                    if(timer.seconds() < 0.5) break;
                    state = StateTransfer.FINISH;

                    break;
                case FINISH:
                    //spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);
                    spindexer.setTransferSpeed(0);
                    outtake.endTransfer();
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
