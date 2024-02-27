package frc.robot;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Safty {
    private CANSparkMax Ldrive1;
    private CANSparkMax Ldrive2;
    private CANSparkMax Rdrive1;
    private CANSparkMax Rdrive2;
    private CANSparkMax shooter1;
    private CANSparkMax shooter2;
    private CANSparkMax intake1;
    private CANSparkMax intake2;
    private CANSparkMax advancer;
    
    Safty (CANSparkMax Ldrive1_in, CANSparkMax Ldrive2_in, CANSparkMax Rdrive1_in, CANSparkMax Rdrive2_in, 
           CANSparkMax shooter1_in, CANSparkMax shooter2_in, CANSparkMax intake1_in, CANSparkMax intake2_in, CANSparkMax advancer_in) {
        Ldrive1 = Ldrive1_in;
        Ldrive2 = Ldrive2_in;
        Rdrive1 = Rdrive1_in;
        Rdrive2 = Rdrive2_in;
        shooter1 = shooter1_in;
        shooter2 = shooter2_in;
        intake1 = intake1_in;
        intake2 = intake2_in;
        advancer = advancer_in;
    }

    public void checkifsafe() {
        if (Ldrive1.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on Ldrive1", Ldrive1.getStickyFaults());
        }
        if (Ldrive2.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on Ldrive2", Ldrive2.getStickyFaults());
        }
        if (Rdrive2.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on Rdrive2", Rdrive2.getStickyFaults());
        }
        if (Rdrive1.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on Rdrive1", Rdrive1.getStickyFaults());
        }
        if (shooter1.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on shooter1", shooter1.getStickyFaults());
        }
        if (shooter2.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on shooter2", shooter2.getStickyFaults());
        }
        if (intake1.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on intake1", intake1.getStickyFaults());
        }
        if (intake2.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on intake2", intake2.getStickyFaults());
        }
        if (advancer.getStickyFaults() > 1 ) {
            SmartDashboard.getNumber("Sticky Fault on advancer", advancer.getStickyFaults());
        }
    }
}
