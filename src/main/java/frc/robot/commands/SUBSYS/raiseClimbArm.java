package frc.robot.commands.SUBSYS;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.robotMap;

public class raiseClimbArm extends CommandBase {
    @Override
    public void execute() {
        robotMap.climber.set(ControlMode.PercentOutput, 0.05);
    }

    @Override
    public boolean isFinished() {return robotMap.climber.getSelectedSensorPosition()/2048 >= 50;}

}
