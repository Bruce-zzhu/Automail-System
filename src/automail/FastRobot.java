package automail;

import simulation.IMailDelivery;

public class FastRobot extends Robot {
    private final String id;

    public FastRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, number);
        this.id = "F" + number;
    }
}
