package frc.robot;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;



public class Buttonpusher {
    private CANSparkMax shoter1;
    private CANSparkMax shoter2;
    private CANSparkMax advance;
    private boolean beambreak;
    
    
    Buttonpusher (CANSparkMax shoter1_in, CANSparkMax shoter2_in, CANSparkMax advance_in, boolean beambreak_in ) {
        shoter1 = shoter1_in;
        shoter2 = shoter2_in;
        advance = advance_in;
        beambreak = beambreak_in;
    }


    public void doshooter(boolean button) {
        if (button) {
            shoter1.set(1);
            shoter2.set(1);
        } else if (shoter1.get() == 5676) {
            while (beambreak) {
                advance.set(0.2);
            }

        }
    }
}
