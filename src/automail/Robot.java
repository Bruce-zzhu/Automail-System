package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.Clock;
import simulation.IMailDelivery;

import java.util.ArrayList;

/**
 * The robot delivers mail!
 */
public class Robot {

    protected static final int INDIVIDUAL_MAX_WEIGHT = 2000;

    protected IMailDelivery delivery;
    protected final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING }
    protected RobotState current_state;
    protected int current_floor;
    protected int destination_floor;
    protected MailPool mailPool;
    protected boolean receivedDispatch;

    protected MailItem deliveryItem = null;
    protected ArrayList<MailItem> tube;
    protected final int TUBE_CAPACITY;

    protected int deliveryCounter;

    protected int totalUnits;
    protected final int speed;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IMailDelivery delivery, MailPool mailPool, String id, int speed, int tube_capacity){
       	this.id = id;
        // current_state = RobotState.WAITING;
    	current_state = RobotState.RETURNING;
        current_floor = Building.getInstance().getMailroomLocationFloor();
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.speed = speed;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
        this.totalUnits = current_floor;
        this.TUBE_CAPACITY = tube_capacity;
        tube = new ArrayList<>(TUBE_CAPACITY);
    }
    
    /**
     * This is called when a robot is assigned the mail items and ready to dispatch for the delivery 
     */
    public void dispatch() {
    	receivedDispatch = true;
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void operate() throws ExcessiveDeliveryException {
    	switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == Building.getInstance().getMailroomLocationFloor()){
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
                if(!isEmpty() && receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
                	setDestination();
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
    			if(current_floor == destination_floor){ // If already here drop off either way
                    /** Delivery complete, report this to the simulator! */
                    delivery.deliver(this, deliveryItem, "");
                    deliveryItem = null;
                    deliveryCounter++;
                    if(deliveryCounter > TUBE_CAPACITY + 1){  // Implies a simulation bug
                    	throw new ExcessiveDeliveryException(this, TUBE_CAPACITY + 1);
                    }
                    /** Check if want to return, i.e. if there is no item in the tube*/
                    if(tube.size() == 0){
                    	changeState(RobotState.RETURNING);
                    }
                    else{
                        /** If there is another item, set the robot's route to the location to deliver the item */
                        deliveryItem = tube.get(tube.size()-1);
                        tube.remove(tube.size()-1);   // hand took item from tube
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

    /**
     * Sets the route for the robot
     */
    public void setDestination() {
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    public void moveTowards(int destination) {
        int moveUnits = 0;
        if(current_floor < destination){
            moveUnits = Math.min(speed, destination - current_floor);
            current_floor += moveUnits;
        } else {
            moveUnits = Math.min(speed, current_floor - destination);
            current_floor -= moveUnits;
        }
        this.totalUnits += moveUnits;
    }
    
    public String getIdTube() {
    	return String.format("%s(%1d)", this.id, tube.size());
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    public void changeState(RobotState nextState){
    	assert(!(deliveryItem == null && tube != null));
    	if (current_state != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getIdTube(), current_state, nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getIdTube(), deliveryItem.toString());
    	}
    }

	public ArrayList<MailItem> getTube() {
		return tube;
	}

    public int getTubeSize() {
        return tube.size();
    }

	public boolean isEmpty() {
		return (deliveryItem == null && tube.size() == 0);
	}

	public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
		assert(deliveryItem == null);
		deliveryItem = mailItem;
		if (deliveryItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	public void addToTube(MailItem mailItem) throws ItemTooHeavyException {
		assert(tube == null);
		tube.add(mailItem);
		if (mailItem.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
	}

	public String getId() {
        return this.id;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public int getCurrentFloor() {
        return current_floor;
    }


}
