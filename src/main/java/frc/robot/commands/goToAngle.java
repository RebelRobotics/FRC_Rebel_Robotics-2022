package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.robotMap;

public class goToAngle extends CommandBase {

    private double Lspeed;
    private double Rspeed;
    //private double angleSign;
    private double currentAngle;
    //private double angleAjusted;
    private double error;
    private double ANGLE;
    private double SPEED;
    private double direction;
    private double Lrpm;
    private double Rrpm;
    private double avgRPM;
    

    public goToAngle(double angle, double speed) {
        direction = (angle/Math.abs(angle));
        Lspeed = -direction*speed;
        Rspeed = direction*speed;
        ANGLE = angle;
        SPEED = speed;
        //angleAjusted = Math.abs(angle)-(9);
    }
    //@Override
    //public void initialize() {
    //  robotMap.imu.reset();
    //}
    
    @Override
    public void execute() {
      currentAngle = robotMap.imu.getAngle();
      error = ANGLE + currentAngle;


      Lrpm = robotMap.LDrive1.getSelectedSensorVelocity()*10*60/2048;
      Rrpm = robotMap.RDrive2.getSelectedSensorVelocity()*10*60/2048;
      avgRPM = Math.abs(Lrpm) + Math.abs(Rrpm);

      Lspeed = -direction*SPEED*error;
      Rspeed = direction*SPEED*error;
      if (Math.abs(Lspeed) > 1) {Lspeed = Lspeed/Math.abs(Lspeed);}
      if (Math.abs(Rspeed) > 1) {Rspeed = Rspeed/Math.abs(Rspeed);}
      
      if (Math.abs(error) < 12) {
<<<<<<< Updated upstream
        System.out.println("slowing down");
=======
        System.out.println("?????????  slowing down");
>>>>>>> Stashed changes
        Lspeed = Lspeed*0.1;
        Rspeed = Rspeed*0.1;
      }
      
      robotMap.RDrive2.set(ControlMode.PercentOutput, Rspeed);
      robotMap.LDrive1.set(ControlMode.PercentOutput, Lspeed);
    }

    @Override 
    public void end(boolean interrupted) {
<<<<<<< Updated upstream
=======
      System.out.println("___________GO ANGLE FINISHED_______________");
>>>>>>> Stashed changes
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