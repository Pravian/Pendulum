package net.pravian.pendulum;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PendulumTest {

    public static void main(String[] args) {
        Pendulum timer = Pendulum.instance();

        timer.init("simple");

        timer.start("simple");
        try {
            Thread.sleep(1550);
        } catch (InterruptedException ex) {
            Logger.getLogger(PendulumTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.stop();

        System.out.println(timer.getTimingsMap().get("simple").getDiffMillis());

        timer.clear();

        timer.start("loop test");
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(55);
            } catch (InterruptedException ex) {
                Logger.getLogger(PendulumTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        timer.stop("loop test");

        System.out.println(timer.report());
    }

}
