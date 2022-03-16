package frc.robot.commands.SUBSYS;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.robotMap;
import frc.robot.commands.goToAngle;

public class lockOn extends CommandBase{
    private goToAngle target;

    @Override
    public void initialize() {
        // protocal for serial bus
        // set target angle 
        // set global speed
        goToAngle target = new goToAngle(robotMap.jTurnTo, 1);
        target.schedule();
    }
    
    @Override
    public boolean isFinished() {
        return target.isFinished();
    }
}
