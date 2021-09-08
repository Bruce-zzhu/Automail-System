package automail;

import exceptions.BulkRobotExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.Clock;
import simulation.IMailDelivery;
import java.util.ArrayList;

public class BulkRobot extends Robot {
    private ArrayList<MailItem> tube;
    private final int MAX_CARRY_ITEM = 5;

    public BulkRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool, "B" + number, 1);
        tube = new ArrayList<>();
    }

    @Override
    public String getIdTube() {
        return String.format("%s(%1d)", id, tube.size());
    }

    @Override
    public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
        this.tube.add(mailItem);
        deliveryItem = mailItem;
        if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
    }


    @Override
    public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
        // do nothing
    }

    @Override
    public boolean isEmpty() {
        return (deliveryItem == null && tube.size() == 0);
    }



    public int getTubeSize() {
        return tube.size();
    }

    @Override
    public void operate() throws BulkRobotExcessiveDeliveryException {
        switch (current_state) {
            /** This state is triggered when the robot is returning to the mailroom after a delivery */
            case RETURNING:
                /** If its current position is at the mailroom, then the robot should change state */
                if (current_floor == Building.getInstance().getMailroomLocationFloor()) {
                    /** Tell the sorter the robot is ready */
                    mailPool.registerWaiting(this);
                    changeState(RobotState.WAITING);
                } else {
                    /** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.getInstance().getMailroomLocationFloor());
                    break;
                }
            case WAITING:
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if (!isEmpty() && receivedDispatch) {
                    receivedDispatch = false;
                    deliveryCounter = 0; // reset delivery counter
                    setDestination();
                    changeState(RobotState.DELIVERING);
                }
                break;
            case DELIVERING:
                if (current_floor == destination_floor) { // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(this, deliveryItem, "");
                    deliveryCounter++;
                    if (deliveryCounter > MAX_CARRY_ITEM) {  // Implies a simulation bug
                        throw new BulkRobotExcessiveDeliveryException();
                    }
                    /** Check if want to return, i.e. if there is no item in the tube*/
                    if (tube.size() == 0) {
                        deliveryItem = null;
                        changeState(RobotState.RETURNING);
                    } else {
                        /** If there is another item, set the robot's route to the location to deliver the item */
                        deliveryItem = tube.get(tube.size()-1);
                        tube.remove(tube.size()-1);
                        setDestination();
                        changeState(RobotState.DELIVERING);
                    }
                } else {
                    /** The robot is not at the destination yet, move towards it! */
                    moveTowards(destination_floor);
                }
                break;
        }
    }

}
