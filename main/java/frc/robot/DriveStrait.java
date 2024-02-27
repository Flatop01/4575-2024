package frc.robot;
import javax.swing.text.StyleContext.SmallAttributeSet;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 




public class DriveStrait {
    private CANSparkMax Ldrive1;
    private CANSparkMax Ldrive2;
    private CANSparkMax Rdrive1;
    private CANSparkMax Rdrive2;
    private AHRS gyro;
    private double yaw;
    private double roll;
    private double pitch;
    private DifferentialDrive m_myRobot;
   
    
    DriveStrait (CANSparkMax Ldrive1_in, CANSparkMax Ldrive2_in, CANSparkMax Rdrive1_in, CANSparkMax Rdrive2_in, AHRS gyro_in) {
        Ldrive1 = Ldrive1_in;
        Ldrive2 = Ldrive2_in;
        Rdrive1 = Rdrive1_in;
        Rdrive2 = Rdrive2_in;
        gyro = gyro_in;
        Rdrive2.follow(Rdrive1);
        Ldrive2.follow(Ldrive1);   
        m_myRobot = new DifferentialDrive(Ldrive1, Rdrive1);
    }
    
    public void runGyro(double heading , double driverX , double driverY) {
        yaw = gyro.getYaw();
        roll = gyro.getRoll();
        pitch = gyro.getPitch();   
        SmartDashboard.putNumber("raw driver", driverX);
        SmartDashboard.putNumber("gyro only", (Math.round(yaw) -  heading)/60.0);
        SmartDashboard.putNumber("gyro stuff", driverX  + (Math.round(yaw) -  heading)/60.0);
        SmartDashboard.putNumber("raw driver y", driverY);


        m_myRobot.arcadeDrive(driverX, driverY);
//        m_myRobot.arcadeDrive(driverX  + (Math.round(yaw) -  heading)/60.0, driverY );
            
         
    }
    
    
}
