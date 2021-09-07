package automail;

import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;

/** The bulk robot deliver mails with more capacity **/
public class BulkRobot extends Robot {
    {
        TUBE_CAPACITY = 5;
    }

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param number gives the id of current robot
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
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
