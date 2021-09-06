package automail;

import simulation.IMailDelivery;

public class BulkRobot extends Robot {
    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, "B" + number, 1);
    }
}
