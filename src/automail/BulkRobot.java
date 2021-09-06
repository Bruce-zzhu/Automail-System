package automail;

import simulation.IMailDelivery;

public class BulkRobot extends Robot {
    private final String id;

    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, number);
        this.id = "B" + number;
    }
}
