package automail;

import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;

public class FastRobot extends Robot {
    public FastRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, "F" + number, 3, 0);
    }


    @Override
    public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
        // do nothing
    }

}
