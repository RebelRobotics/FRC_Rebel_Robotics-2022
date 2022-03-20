package frc.robot.commands.SUBSYS;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.robotMap;

public class spinIntake extends SubsystemBase {
    public void spin() {
        robotMap.shooterIntake.set(0.4);
    }
}
