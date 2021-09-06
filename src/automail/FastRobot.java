package automail;

import simulation.IMailDelivery;

public class FastRobot extends Robot {
    public FastRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, "F" + number, 3);
    }
}
