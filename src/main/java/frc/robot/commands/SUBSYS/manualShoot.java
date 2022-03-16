package frc.robot.commands.SUBSYS;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.robotMap;


public class manualShoot extends CommandBase{
    
    private double speed;

    public manualShoot(Double target) {
        speed = target;
    }
    @Override
    public void initialize() {
        robotMap.shooter.set(ControlMode.PercentOutput, speed);
        
    }

    @Override
    public void execute() {
        if (robotMap.shooter.getMotorOutputPercent()/speed > 0.95) {
            robotMap.feeder.set(0.4);
        }
    }
    @Override
    public void end(boolean interupted) {
        robotMap.feeder.set(0);
        robotMap.shooter.set(ControlMode.PercentOutput, 0.3);
    }
    @Override 
    public boolean isFinished() {
        return (robotMap.feeder.getEncoder().getVelocity()>=4500);
    }
}
