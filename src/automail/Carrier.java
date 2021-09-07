package automail;

import exceptions.ItemTooHeavyException;
import simulation.IMailDelivery;

import java.util.ArrayList;

/** Represents any carrier for delivering items **/
public abstract class Carrier {
    private static final int INDIVIDUAL_MAX_WEIGHT = 2000;
    protected int TUBE_CAPACITY;

    protected boolean receivedDispatch;
    protected IMailDelivery delivery;
    protected MailItem deliveryItem = null;
    protected ArrayList<MailItem>  tube = null;

    public Carrier(IMailDelivery delivery) {
        this.delivery = delivery;
        this.receivedDispatch = false;
    }

    /**
     * This is called when a robot is assigned the mail items and ready to dispatch for the delivery
     */
    public void dispatch() {
        receivedDispatch = true;
    }

    public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
        assert(deliveryItem == null);
        deliveryItem = mailItem;
        if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
    }

    public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
        assert(tube.size() < TUBE_CAPACITY);
        tube.add(mailItem);
        if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
    }

    public boolean isEmpty() {
        return (deliveryItem == null && tube.size() == 0);
    }

    public ArrayList<MailItem> getTube() {
        return tube;
    }
}
