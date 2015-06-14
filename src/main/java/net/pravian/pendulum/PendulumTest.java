package net.pravian.pendulum;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PendulumTest {

    public static void main(String[] args) {
        Pendulum timer = Pendulum.instance();

        /**
        timer.init("simple");

        timer.start("simple");
        try {
            Thread.sleep(1550);
        } catch (InterruptedException ex) {
            Logger.getLogger(PendulumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.stopLast();

        System.out.println(timer.getTimersMap().get("simple").getData().getLastMs());

        timer.clearTimings();
        */

        timer.start("outer", "loop");
        for (int i = 1; i < 21; i++) {
            timer.start("loop");
            try {
                Thread.sleep(25 * i);
            } catch (InterruptedException ex) {
                Logger.getLogger(PendulumTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            timer.stopLast();
        }
        timer.stopLast();

        System.out.println(timer.report().getStringReport());
    }

}
