package frc.robot;

<<<<<<< Updated upstream
import edu.wpi.first.wpilibj.SerialPort;
=======
import frc.robot.commands.goStraight;
import frc.robot.commands.goToAngle;
import frc.robot.commands.SUBSYS.raiseClimbArm;
>>>>>>> Stashed changes
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.goStraight;
import frc.robot.commands.SUBSYS.manualShoot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import frc.robot.commands.vision;






public class Robot extends TimedRobot {


  SendableChooser<Command> choose = new SendableChooser<>();
    goStraight gofivefeet = new goStraight(60, 0);
<<<<<<< Updated upstream
    routine1 route1 = new routine1();
    routine2 route2 = new routine2();
  manualShoot shoot = new manualShoot(0.3);
  vision _jevois = new vision(robotMap.jevois);

=======
    goToAngle angle = new goToAngle(-20, 1);

  raiseClimbArm climb = new raiseClimbArm();
  
  private goToAngle lockOn; // global lock on command 
    public double f(double a) {return a+3;} // convert distance into shooter power *** not complete
>>>>>>> Stashed changes


  @Override
  public void robotInit() {
    robotMap.roboInit();
    
    
    choose.addOption("5 feet", gofivefeet);
    SmartDashboard.putData(choose);
    CameraServer.startAutomaticCapture();
    CameraServer.getVideo().getSource().setVideoMode(PixelFormat.kMJPEG, 320, 240, 24);
  }


  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    robotMap.autoInit(false); //initialize settings for autonomous

    if (choose.getSelected() != null) {
      choose.getSelected().schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    angle.getDebug();
  }

  @Override
  public void teleopInit() {
    robotMap.RDrive2.setInverted(true);
    robotMap.RDrive4.setInverted(true);
    robotMap.LDrive1.setInverted(false);
    robotMap.LDrive3.setInverted(false);

<<<<<<< Updated upstream
    robotMap.shooter.set(ControlMode.PercentOutput, 0.3);
    
  }

  @Override
  public void teleopPeriodic() {
    _jevois.updateVision();
    System.out.println(_jevois.getDistance()+" = DISTANCE  || "+_jevois.getAngle()+" = ANGLE  || "+_jevois.getTracking()+" = TRACKING");
    System.out.println("RAW DATA == "+robotMap.jevois.readString());
  }


=======
  @Override
  public void teleopPeriodic() {
    drive();
    handleShooting();
    checkButtons();
    System.out.println("POV = "+robotMap.joystick.getPOV());
  }


  //////// TELEOP DRIVE CODE //////// called in teleop periodic
  public void drive() {
    ///////////////// CALCULATE VARIABLES AND DEADBANDS ////////////////////
    double power = robotMap.joystick.getRawAxis(1); 
        if (Math.abs(power) < CONST.DEADZONE) { power = 0; }
    double turn = robotMap.joystick.getRawAxis(2);
        if (Math.abs(turn) < CONST.DEADZONE) { turn = 0.0; }
    double slider = robotMap.joystick.getRawAxis(3);
    double slider2 = robotMap.joystick2.getRawAxis(3);
    double motor_power = (slider+1)/2; // slider +1/2 is so the entire slider can be used with no negative zone
    double turn_power = (slider2+1)/2;

    double lspeed = (power-(turn*turn_power))*motor_power;
    double rspeed = (power+(turn*turn_power))*motor_power;
  
    if (robotMap.joystick.getPOV()==0) {
      lspeed = lspeed*2;
      rspeed= rspeed*2;
    }
    if (robotMap.joystick.getPOV()==180) {
      lspeed= lspeed/2;
      rspeed = rspeed/2;
    }

    ///// SET MOTOR POWER
    robotMap.LDrive1.set(ControlMode.PercentOutput, lspeed);
    robotMap.RDrive2.set(ControlMode.PercentOutput, rspeed);
    

  }
  

  //////// BUTTON MAPPINGS /////////
  public void checkButtons() {
      // intake
      if (robotMap.joystick.getRawButton(2)) {
        //robotMap.intake.set(Value.kForward);
        robotMap.intakeSpark.set(0.9);

        } else {
        robotMap.intakeSpark.set(0);
        robotMap.intake.set(Value.kForward);
      }
  
      // climb
      if (robotMap.joystick.getRawButton(12) && !climb.isScheduled()) {
        climb.schedule();
      }

      
  }


  //// decides how to shoot with tracking or without
  public void handleShooting() {
      // shooter code
      robotMap.jevois.updateVision();
      if (robotMap.joystick.getRawButton(1)) { /// SHOOT
        if (robotMap.jevois.getTracking() && lockOn != null) { // IF JEVOIS IS TRACKING GIVE CONTROL TO MACHINE
          
          if (!lockOn.isScheduled()) {
            lockOn = new goToAngle(robotMap.jevois.getAngle(), 0.001);
            lockOn.schedule();
          }
    
          if (lockOn.isFinished()) {SHOOT(f(robotMap.jevois.getDistance()));}
        }
        else { // IF NOT TRACKING USE MANUAL CONTROL
          if (robotMap.joystick.getRawButton(3)) {robotMap.raiseHood();}
          else {robotMap.lowerHood();}
          SHOOT(0.4);
        } // *** add manual driver power
      } else {
        robotMap.shooter.set(ControlMode.PercentOutput, CONST.teleopIdle);
        robotMap.shooterIntake.set(0);
      }

    }


  ///// SHOOTER FUNCTION ////// intended to be constantly called
  public void SHOOT(double speed) {
    if (robotMap.shooter.getMotorOutputPercent()/speed > 0.9) {
      robotMap.shooterIntake.set(-0.4);
    } else {
      robotMap.shooter.set(ControlMode.PercentOutput, speed);
      robotMap.shooterIntake.set(0);
    } 
  }

>>>>>>> Stashed changes
  @Override
  public void disabledInit() {
    robotMap.LDrive1.setSelectedSensorPosition(0);
    robotMap.RDrive2.setSelectedSensorPosition(0);
    robotMap.LDrive3.setSelectedSensorPosition(0);
    robotMap.RDrive4.setSelectedSensorPosition(0);
    robotMap.RDrive2.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive1.setNeutralMode(NeutralMode.Coast);
    robotMap.RDrive4.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive3.setNeutralMode(NeutralMode.Coast);
  }

  @Override
  public void disabledPeriodic() {}

<<<<<<< Updated upstream
  public static void shoot(double speed) {
    if (robotMap.shooter.getMotorOutputPercent()/speed > 0.9) {
      robotMap.feeder.set(0.4);
    } else {
      robotMap.shooter.set(ControlMode.PercentOutput, speed);
      robotMap.feeder.set(0);
    } 
  }

  public static double  f(double in) {return Math.pow(in, 2)+5;}

  public String[] parseJevois(String input) {
    input.strip();
    try {
    String[] STRdata = input.split(",");
    return STRdata;
    } catch (Exception e) {
      System.out.println("FALIED TO PARSE DATA");}
      return null;
    //Integer[] data = {1,1,1};
    //for (int i=0; i<3; i++) {data[i]= Integer.parseInt(STRdata[i]);}

=======
  @Override
  public void testInit() {
    
>>>>>>> Stashed changes
  }

}
