package automail;

import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;

public class BulkRobot extends Robot {
    protected int TUBE_CAPACITY = 5;

    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool);
        this.id = "B" + number;
    }

    @Override
    public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
        addToTube(mailItem);
        deliveryItem = mailItem;
    }

}
