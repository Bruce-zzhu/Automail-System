package automail;

import simulation.IMailDelivery;

public class RegularRobot extends Robot{
    private final String id;
    public RegularRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, number);
        this.id = "R" + number;
    }
}
