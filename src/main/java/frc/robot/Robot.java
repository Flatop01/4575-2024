// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SPI; //I connected using SPI
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private XboxController m_driver;
  private XboxController m_opp;
 
  //auto selector
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static final String klime = "LimeLight Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private static final AHRS gyro = new AHRS(SPI.Port.kMXP); //add as a global variable near the top
 

  // private static final MotorController m_right1Motor = new CANSParkMax(1,MotorType.kBrushless);
  private static final int m_left1Motorid = 1;
  private CANSparkMax m_left1Motor;
  // private final MotorController m_right2Motor = new CANSparkMax(2,MotorType.kBrushless);
  private static final int m_left2Motorid = 2;
  private CANSparkMax m_left2Motor;
  // private final MotorController m_left1Motor = new CANSparkMax(3,MotorType.kBrushless);
  private static final int m_right1Motorid = 3;
  private CANSparkMax m_right1Motor;
  // private final MotorController m_left2Motor = new CANSparkMax(4,MotorType.kBrushless);
  private static final int m_right2Motorid = 4;
  private CANSparkMax m_right2Motor;
  //MotorControllerGroup m_leftMotor = new MotorControllerGroup(m_left1Motor, m_left2Motor);
  //MotorControllerGroup m_rightMotor = new MotorControllerGroup(m_right1Motor, m_right2Motor);


  // private final MotorController m_intake1Motor = new CANSparkMax(14,MotorType.kBrushless);
  private static final int m_intake1Motorid = 14;
  private CANSparkMax m_intake1Motor;
  // private final MotorController m_intake2Motor = new CANSparkMax(5,MotorType.kBrushless);
  private static final int m_intake2Motorid = 5;
  private CANSparkMax m_intake2Motor;
  // private final MotorController m_advancerMotor = new CANSparkMax(13,MotorType.kBrushless);
  private static final int m_advancerMotoroid = 13;
  private CANSparkMax m_advancerMotor;
  // private final MotorController m_shooter1Motor = new Spark(0);
  private static final int m_shooter1Motorid = 0;
  private Spark m_shooter1Motor;
  // private final MotorController m_shooter2Motor = new Spark(1);
  private static final int m_shooter2Motorid = 1;
  private Spark m_shooter2Motor;
  // private final MotorController m_shoulderintakeMotor = new CANSparkMax(9,MotorType.kBrushless);
  private static final int m_shoulderintakeMotorid = 9;
  private CANSparkMax m_shoulderintakeMotor;
  // private final MotorController m_shouldermoveMotor = new CANSparkMax(10,MotorType.kBrushless);
  private static final int m_shouldermoveMotorid = 10;
  private CANSparkMax m_shouldermoveMotor;
  // private final MotorController m_shoulderrotateMotor = new CANSparkMax(3,MotorType.kBrushless);
  private static final int m_shoulderrotateMotorid = 11;
  private CANSparkMax m_shoulderrotateMotor;
  // private final MotorController m_climbMotor = new CANSparkMax(12,MotorType.kBrushless);
  private static final int m_climbMotorid = 12;
  private CANSparkMax m_climbMotor;
 
  private double starttime;
  private double heading;
  //limelight stuff
 
  @Override
  public void robotInit() {
   
    //auto selector
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    m_chooser.addOption("LimeLight Auto", klime);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_right1Motor = new CANSparkMax(m_right1Motorid, MotorType.kBrushless);
    m_right2Motor = new CANSparkMax(m_right2Motorid, MotorType.kBrushless);
    m_left1Motor = new CANSparkMax(m_left1Motorid, MotorType.kBrushless);
    m_left2Motor = new CANSparkMax(m_left2Motorid, MotorType.kBrushless);
    m_intake1Motor = new CANSparkMax(14, MotorType.kBrushless);
    m_intake2Motor = new CANSparkMax(5, MotorType.kBrushless);
    m_shooter2Motor = new Spark(1);
    m_shooter1Motor = new Spark(0);
    m_advancerMotor = new CANSparkMax(m_advancerMotoroid, MotorType.kBrushless);
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_right1Motor.setInverted(true);
    m_right2Motor.follow(m_right1Motor);
    m_left2Motor.follow(m_left1Motor);

    m_shooter1Motor.setInverted(true);
   
    m_intake2Motor.follow(m_intake1Motor);

    m_myRobot = new DifferentialDrive(m_left1Motor, m_right1Motor);
    m_driver = new XboxController(0);
    m_opp = new XboxController(1);
   
    //limelight stuff
    // Make sure you only configure port forwarding once in your robot code.
        // Do not place these function calls in any periodic functions
        for (int port = 5800; port <= 5807; port++) {
            PortForwarder.add(port, "limelight.local", port);
        }
  }
 
  @Override
  public void autonomousInit() {
    starttime = Timer.getFPGATimestamp();
    heading = gyro.getYaw();
     m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }
  @Override
  public void disabledPeriodic() {
    double yaw =  gyro.getYaw();
        double pitch = gyro.getPitch();
        double roll =  gyro.getRoll();
        SmartDashboard.putNumber("gyro pitch", pitch);
        SmartDashboard.putNumber("gyro yaw", yaw);
        SmartDashboard.putNumber("gyro roll", roll);
        //limelight stuff
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
NetworkTableEntry tx = table.getEntry("tx");
NetworkTableEntry ty = table.getEntry("ty");
NetworkTableEntry ta = table.getEntry("ta");

//read values periodically
double x = tx.getDouble(0.0);
double y = ty.getDouble(0.0);
double area = ta.getDouble(0.0);

//post to smart dashboard periodically
SmartDashboard.putNumber("LimelightX", x);
SmartDashboard.putNumber("LimelightY", y);
SmartDashboard.putNumber("LimelightArea", area);
  }
 
 
 
  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
     switch (m_autoSelected) {
      case kCustomAuto:
        //limelight stuff
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
NetworkTableEntry tx = table.getEntry("tx");
NetworkTableEntry ty = table.getEntry("ty");
NetworkTableEntry ta = table.getEntry("ta");

//read values periodically
double x = tx.getDouble(0.0);
double y = ty.getDouble(0.0);
double area = ta.getDouble(0.0);

//post to smart dashboard periodically
SmartDashboard.putNumber("LimelightX", x);
SmartDashboard.putNumber("LimelightY", y);
SmartDashboard.putNumber("LimelightArea", area);

   double time = Timer.getFPGATimestamp();


    if (time - starttime < 1.3 ) {
      m_left1Motor.set(.2);
      m_right1Motor.set(.2);
      m_intake1Motor.set(-1);
    }
    if (time - starttime > 2){
      m_intake1Motor.set(0);
      m_left1Motor.set(-.1);
      m_right1Motor.set(-.2);
    }
    if (time - starttime > 3) {
      m_left1Motor.set(0);
    }
     
   
  if (time - starttime > 3.01){
    if ( x >= 5) {
      m_left1Motor.set(.1);
     
      if (x <= 5){
        m_left1Motor.set(0);
      }
     
    } else if (x <= -5) {
      m_right1Motor.set(.1);
      if (x>=-5){
        m_right1Motor.set(0);
      }
    } else {
      double time1 = Timer.getFPGATimestamp();
      m_shooter1Motor.set(1);
      m_shooter2Motor.set(1);
      if (System.currentTimeMillis() - time1 < 2){
        m_advancerMotor.set(1);
      }
    }
  }
        break;
      case kDefaultAuto:
      default:
       
        // double leftSlow = 0.24;
        // double rightSlow = 0.24;
        // double rotateSpeed = 0.35;
        // double rotateSpeedSlow = 0.25;
        double yaw =  gyro.getYaw();
        double pitch = gyro.getPitch();
        double roll =  gyro.getRoll();
        SmartDashboard.putNumber("gyro pitch", pitch);
        SmartDashboard.putNumber("gyro yaw", yaw);
        SmartDashboard.putNumber("gyro roll", roll);
        if (yaw > 3) {
          m_right1Motor.set(.25);
        }
        double timed = Timer.getFPGATimestamp();
        /*if (Math.abs(gyro.getYaw()) <= 3) {
   m_left1Motor.set(leftSlow - (gyro.getYaw()) / 15);
   m_right1Motor.set(rightSlow - (gyro.getYaw()) / 15);
  } else if (Math.abs(gyro.getYaw()) < 10) {
   if (gyro.getYaw() > 0) {
    m_left1Motor.set(leftSlow);
    m_right1Motor.set(1.1 * rightSlow);
   } else if (gyro.getYaw() < 0) {
    m_left1Motor.set(1.1 * leftSlow);
    m_right1Motor.set(rightSlow);
   }
  } else
   if (gyro.getYaw() > 0) {
    while (gyro.getYaw() > 10 && isAutonomous()) {
     m_left1Motor.set(-rotateSpeed);
     m_right1Motor.set(-rotateSpeed);
    }
   while (gyro.getYaw() > 0 && isAutonomous()) {
    m_left1Motor.set(-rotateSpeedSlow);
    m_right1Motor.set(-rotateSpeedSlow);
   }
   while (gyro.getYaw() > 0 && isAutonomous()) {
    m_left1Motor.set(rotateSpeedSlow);
    m_right1Motor.set(rotateSpeedSlow);
   }
  } else {
   while (gyro.getYaw() < -10 && isAutonomous()) {
    m_left1Motor.set(rotateSpeed);
    m_right1Motor.set(rotateSpeed);
   }
   while (gyro.getYaw() < 0 && isAutonomous()) {
    m_left1Motor.set(rotateSpeedSlow);
    m_right1Motor.set(rotateSpeedSlow);
   }
   while (gyro.getYaw() < 0 && isAutonomous()) {
    m_left1Motor.set(-rotateSpeedSlow);
    m_right1Motor.set(-rotateSpeedSlow);
   }
  }*/

         if(timed - starttime < 5) {
          m_shooter1Motor.set(.8);
          m_shooter2Motor.set(.8);
        } else {
          m_shooter1Motor.set(0);
          m_shooter2Motor.set(0);
        }
        if(timed - starttime > 4) {
          m_advancerMotor.set(-0.5);
        }
        if (timed - starttime > 6.3 ) {
     
      m_right1Motor.set(-.2);
      m_left1Motor.set(-.2);
      m_intake1Motor.set(-.5);
      
      
        }
        if (timed - starttime > 7.7){
     
      
      m_intake1Motor.set(0);
      m_right1Motor.set(.2);
      m_left1Motor.set(.2);
      m_advancerMotor.set(-0.2);
      
      
    }
    if (timed - starttime > 9) {
      m_left1Motor.set(0);
      m_right1Motor.set(0);
      m_shooter1Motor.set(.75);
      m_shooter2Motor.set(.75);
    }
    if (timed - starttime > 11) {
      m_advancerMotor.set(1);
      m_intake1Motor.set(-0.2);
     
    }

        break;

        case klime:
       
        LimeLightAuto:

        break;
     
      }
   
   
}
  @Override
  public void teleopInit() {
    heading = gyro.getYaw();
  }


  @Override
  public void teleopPeriodic() {
    //gyro stuff
       
        double yaw = gyro.getYaw();
        double pitch =gyro.getPitch();
        double roll = gyro.getRoll();
        SmartDashboard.putNumber("gyro pitch", pitch);
        SmartDashboard.putNumber("gyro yaw", yaw);
        SmartDashboard.putNumber("gyro roll", roll);
       
    //limelight stuff
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
NetworkTableEntry tx = table.getEntry("tx");
NetworkTableEntry ty = table.getEntry("ty");
NetworkTableEntry ta = table.getEntry("ta");

//read values periodically
double x = tx.getDouble(0.0);
double y = ty.getDouble(0.0);
double area = ta.getDouble(0.0);

//post to smart dashboard periodically
SmartDashboard.putNumber("LimelightX", x);
SmartDashboard.putNumber("LimelightY", y);
SmartDashboard.putNumber("LimelightArea", area);
SmartDashboard.putNumber("x", m_driver.getLeftX());
SmartDashboard.putNumber("y", m_driver.getLeftY());

  if ((Math.abs(m_driver.getLeftX()) > 0.1) || (Math.abs(m_driver.getLeftY()) > 0.115625 )) {
    heading = Math.round(gyro.getYaw());
    SmartDashboard.putNumber("Heading:", heading);
  }

 

// auto stuff that I found in wpilib that should stop auto once telop begins
    // if (m_autonomousCommand != null) {
    //   m_autonomousCommand.cancel();
    // }
 
    // if (m_opp.getRightBumper()) {
    //   m_shoulderintakeMotor.set(1);
    // } else {
    //   m_shoulderintakeMotor.set(0);
    // }
    // if (m_opp.getLeftBumper()) {
    //   m_shoulderintakeMotor.set(-1);
    // } else {
    //   m_shoulderintakeMotor.set(0);
    // }
    // if (m_opp.getAButton()) {
    //   m_shoulderrotateMotor.set(1);
    // } else {
    //   m_shoulderrotateMotor.set(0);
    // }
    // if (m_opp.getBButton()) {
    //   m_shoulderrotateMotor.set(-1);
    // } else {
    //   m_shoulderrotateMotor.set(0);
    // }

    // if (m_opp.getYButton()) {
    //   m_shouldermoveMotor.set(1);
    // } else {
    //   m_shouldermoveMotor.set(0);
    // }
    // if (m_opp.getXButton()) {
    //   m_shouldermoveMotor.set(-1);
    // } else {
    //   m_shouldermoveMotor.set(0);
    // }
    if (m_driver.getLeftBumper()){
      m_intake1Motor.set(1);
      m_advancerMotor.set(1);
    } else if (m_driver.getRightBumper()) {
      m_intake1Motor.set(-1);
      m_advancerMotor.set(-1);
    } else {
      m_intake1Motor.set(0);
      m_advancerMotor.set(0);
    }
   
    if (m_driver.getAButton()){
     
      m_shooter1Motor.set(.75);
      m_shooter2Motor.set(.75);
    } else if (m_driver.getYButton()) {
     
      m_shooter1Motor.set(-1);
      m_shooter2Motor.set(-1);
    } else {
      m_shooter1Motor.set(0);
      m_shooter2Motor.set(0);
    }
    // if (m_opp.getBButton()){
    //   m_advancerMotor.set(-0.4);
     
    // } else if (m_opp.getXButton()) {
    //   m_advancerMotor.set(0.4);
     
    // } else {
    //   m_advancerMotor.set(0);
    // }
if (m_driver.getAButton()) {
 m_shooter1Motor.set(0);
  m_shooter2Motor.set(0);
}
if (m_driver.getYButton()){
  heading = Math.round(yaw);
}
    if (Math.round(yaw) - heading > 5){
      if(Math.round(yaw)- heading > 5 ){
        m_right1Motor.set(.25);
      }
      if(Math.round(yaw)-heading > 5){
        m_left1Motor.set(.25);
      }
    }else{
     m_myRobot.arcadeDrive(m_driver.getLeftX()/2.0, m_driver.getLeftY()/2.0);
    }
     
   

  }
}