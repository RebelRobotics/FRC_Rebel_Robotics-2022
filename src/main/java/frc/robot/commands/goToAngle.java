package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.robotMap;
import java.time.LocalTime;

public class goToAngle extends CommandBase {

    private double Lspeed;
    private double Rspeed;
    private double currentAngle;
    private double error;
    private double ANGLE;
    private double p;
    private double direction;
    private double Lrpm;
    private double Rrpm;
    private double avgRPM;
    

    public goToAngle(double angle, double P) {
        direction = (angle/Math.abs(angle));
        Lspeed = -direction*p;
        Rspeed = direction*p;
        ANGLE = angle;
        p = P; // P should be about 12/180
    }
    
    @Override
    public void initialize() {
      
    }

    @Override
    public void execute() {
      LocalTime time = LocalTime.now();

      currentAngle = robotMap.imu.getAngle();
      error = ANGLE + currentAngle;

      Lrpm = robotMap.LDrive1.getSelectedSensorVelocity()*10*60/2048;
      Rrpm = robotMap.RDrive2.getSelectedSensorVelocity()*10*60/2048;
      avgRPM = Math.abs(Lrpm) + Math.abs(Rrpm);

      Lspeed = -direction+(error*p);
      Rspeed = direction+(error*p);
      
      System.out.println();

      robotMap.RDrive2.set(ControlMode.PercentOutput, Rspeed);
      robotMap.LDrive1.set(ControlMode.PercentOutput, Lspeed);
    }

    @Override 
    public void end(boolean interrupted) {
      robotMap.RDrive2.set(ControlMode.PercentOutput, 0);
      robotMap.LDrive1.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public boolean isFinished() {
      return (Math.abs(error) <= 1 && avgRPM < 350);
    }
    
    public void getDebug() {
      System.out.println("CURRENT ANGLE = " + currentAngle + "\n"+
                         "Erorr = " + error + "\n"+
                         "Lspeed = "+Lspeed+"\n"+
                         "Rspeed = "+Rspeed+"\n"+
                         "DIRECTION = "+direction+"\n"+
                         "FINISHED = "+this.isFinished()+"\n"+
                         "avg RPM = "+avgRPM
                         );
    }
}
