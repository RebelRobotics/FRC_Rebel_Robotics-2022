package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.robotMap;
import frc.robot.commands.SUBSYS.calcDer;

public class goToAngle extends CommandBase {

    private double Lspeed;
    private double Rspeed;
    private double currentAngle;
    public double error=0;
    private double ANGLE;
    private double p;
    private double d;
    private double direction;
    private double Lrpm;
    private double Rrpm;
    private double avgRPM;
    private Thread runCalc;
    private calcDer calc_Derrivative;

    

    public goToAngle(double angle, double P, double D) {
        direction = (angle/Math.abs(angle));
        Lspeed = -direction*p;
        Rspeed = direction*p;
        ANGLE = angle;
        p = P; // P should be about 12/180
        d = D; //
        
    }
    
    @Override
    public void initialize() {
      calc_Derrivative = new calcDer(this);
      runCalc = new Thread(calc_Derrivative);
      runCalc.start();
    }

    @Override
    public void execute() {
      
      error = ANGLE + currentAngle;

      currentAngle = robotMap.imu.getAngle();
    
      Lrpm = robotMap.LDrive1.getSelectedSensorVelocity()*10*60/2048;
      Rrpm = robotMap.RDrive2.getSelectedSensorVelocity()*10*60/2048;
      avgRPM = Math.abs(Lrpm) + Math.abs(Rrpm);

      Lspeed = -(direction+(error*p)+(calc_Derrivative.der*d));
      Rspeed = direction+(error*p)+(calc_Derrivative.der*d);
      
      System.out.println();

      robotMap.RDrive2.set(ControlMode.PercentOutput, Rspeed);
      robotMap.LDrive1.set(ControlMode.PercentOutput, Lspeed);
    }

    @Override 
    public void end(boolean interrupted) {
      calc_Derrivative.stop();
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
