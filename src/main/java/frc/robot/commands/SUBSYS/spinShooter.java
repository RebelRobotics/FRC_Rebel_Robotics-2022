package frc.robot.commands.SUBSYS;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.robotMap;

public class spinShooter extends SubsystemBase {
    public void spin() {
        robotMap.jevois.updateVision();
        robotMap.shooter.set(ControlMode.PercentOutput, robotMap.jevois.getDistance());
    }
    public void stop() {robotMap.shooter.set(ControlMode.PercentOutput, 0.3);}
}
