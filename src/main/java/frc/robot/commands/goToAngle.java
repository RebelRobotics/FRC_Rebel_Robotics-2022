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
    private int direction;
    private Thread runCalc;
    private calcDer calc_Derrivative;
    private double i;



    public goToAngle(double angle, double P, double I, double D) {
        direction = (int) (angle/Math.abs(angle));
        Lspeed = -direction*p;
        Rspeed = direction*p;
        ANGLE = angle;
        p = P;
        d = D;
        i = I;        
    }
    
    @Override
    public void initialize() {
      calc_Derrivative = new calcDer(this);
      runCalc = new Thread(calc_Derrivative);
      runCalc.start();
    }

    @Override
    public void execute() {
      currentAngle = robotMap.imu.getAngle();
      error = ANGLE + currentAngle;

      Lspeed = ((error*p)+(calc_Derrivative.der*d)+(calc_Derrivative.I*i));
      Rspeed = -1*((error*p)+(calc_Derrivative.der*d)+(calc_Derrivative.I*i));
      
      //System.out.println("I ---- "+calc_Derrivative.I*i);

      //System.out.println("Lspeed = "+direction+" * (("+error+" * "+p+") + ");

      //System.out.println("ERROR == "+error+" = "+ANGLE+" - "+currentAngle);

      robotMap.RDrive2.set(ControlMode.PercentOutput, Rspeed);
      robotMap.LDrive1.set(ControlMode.PercentOutput, Lspeed);
    }

    @Override 
    public void end(boolean interrupted) {
      System.out.println("#############################################################");
      calc_Derrivative.stop();
      robotMap.RDrive2.set(ControlMode.PercentOutput, 0);
      robotMap.LDrive1.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public boolean isFinished() {
      return (Math.abs(error) <= 1);
    }
    
    public void getDebug() {
      System.out.println("CURRENT ANGLE = " + currentAngle + "\n"+
                         "Erorr = " + error + "\n"+
                         "Lspeed = "+Lspeed+"\n"+
                         "Rspeed = "+Rspeed+"\n"+
                         "DIRECTION = "+direction+"\n"+
                         "FINISHED = "+this.isFinished()+"\n"
                         );
    }
}
