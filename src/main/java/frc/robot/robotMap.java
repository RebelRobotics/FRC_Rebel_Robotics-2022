package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;


public class robotMap {
  
  public static final Joystick joystick = new Joystick(1);
  public static final Joystick joystick2 = new Joystick(0);
  
 
  public static final ADIS16470_IMU imu = new ADIS16470_IMU();
 
  public static final TalonFX LDrive1 = new TalonFX(1);
  public static final TalonFX RDrive2 = new TalonFX(2);
  public static final TalonFX LDrive3 = new TalonFX(3);
  public static final TalonFX RDrive4 = new TalonFX(4);
    public static double Lvelocity = LDrive1.getSelectedSensorVelocity();
    public static double Rvelocity = RDrive2.getSelectedSensorVelocity();
    public static double Lposition = LDrive1.getSelectedSensorPosition();
    public static double Rposition = RDrive2.getSelectedSensorPosition();
    public static double Lrpm = Lvelocity*10*60/2048;
    public static double Rrpm = Rvelocity*10*60/2048;
    public static double LPos = 0;
    public static double Rpos = 0;
  public static final TalonFX shooter = new TalonFX(8);
  public static CANSparkMax feeder = new CANSparkMax(5, MotorType.kBrushless);

  public static DoubleSolenoid hood = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  public static SerialPort jevois = new SerialPort(9600, Port.kUSB);
  public static int jTurnTo = 45;
  public static double jDistance = 5.2;

  static void autoInit(boolean debug) {
    // ZERO THE ENCODERS
    LDrive1.setSelectedSensorPosition(0);
    RDrive2.setSelectedSensorPosition(0);
    LDrive3.setSelectedSensorPosition(0);
    RDrive4.setSelectedSensorPosition(0);

    
    RDrive2.setInverted(false);
    RDrive4.setInverted(false);
    LDrive1.setInverted(true);
    LDrive3.setInverted(true);
    RDrive2.setNeutralMode(NeutralMode.Brake);
    LDrive1.setNeutralMode(NeutralMode.Brake);
    RDrive4.setNeutralMode(NeutralMode.Brake);
    LDrive3.setNeutralMode(NeutralMode.Brake);
    imu.reset();
    if (debug) {
      System.out.println("ENCODERS = " + (LDrive1.getSelectedSensorPosition()/RDrive2.getSelectedSensorPosition()));
      System.out.println("IMU get angle = " + imu.getAngle());
    }
  }
  
  static void roboInit() {
    LDrive1.configFactoryDefault(30);
    LDrive3.configFactoryDefault(30);
    RDrive2.configFactoryDefault(30);
    RDrive4.configFactoryDefault(30);

    shooter.configFactoryDefault();
    shooter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    shooter.configPeakOutputForward(1);
    shooter.configPeakOutputReverse(-1);
    shooter.config_kF(0, 0);
    shooter.config_kP(0, 0.6);
    shooter.config_kI(0, 0.001);
    shooter.config_kD(0, 0.1);
    shooter.configAllowableClosedloopError(0, 0.01);

    feeder.setInverted(true);


    RDrive2.configPeakOutputForward(CONST.driveSpeed, 30);
    RDrive2.configPeakOutputReverse(-CONST.driveSpeed, 30);
    RDrive4.configPeakOutputForward(CONST.driveSpeed, 30);
    RDrive4.configPeakOutputReverse(-CONST.driveSpeed, 30);
    LDrive1.configPeakOutputForward(CONST.driveSpeed, 30);
    LDrive1.configPeakOutputReverse(-CONST.driveSpeed, 30);
    LDrive3.configPeakOutputForward(CONST.driveSpeed, 30);
    LDrive3.configPeakOutputReverse(-CONST.driveSpeed, 30);

    LDrive3.setSelectedSensorPosition(0);
    LDrive1.setSelectedSensorPosition(0);
    RDrive4.setSelectedSensorPosition(0);
    RDrive2.setSelectedSensorPosition(0);
  
    imu.calibrate();
    


    jevois.reset();

    RDrive2.setInverted(true);
    RDrive4.setInverted(true);
    LDrive3.follow(robotMap.LDrive1);
    RDrive4.follow(robotMap.RDrive2);
  }
}
//tmp/hs_err_pid
