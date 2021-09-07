package automail;

import simulation.IMailDelivery;

/** A fast delivery robot with faster speed and no tube **/
public class FastRobot extends Robot{
    {
        TUBE_CAPACITY = 0;
        setSpeed(3);
    }

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param number gives the id of current robot
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public FastRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool);
        this.id = "F" + number;
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    private void moveTowards(int destination) {
        int speed = getSpeed();
        int current_floor = getCurrentFloor();
        int diff = destination - getCurrentFloor();
        assert(diff != 0);

        if (diff >= speed){
            /** destination is much higher than current floor **/
            setCurrentFloor(current_floor + speed);
            setSteps(speed);
        } else if (diff > 0){
            /** destination is higher but not further than the max steps that can be taken **/
            setCurrentFloor(current_floor + diff);
            setSteps(diff);
        } else if (diff <= -3){
            /** destination is much lower than current floor **/
            setSpeed(current_floor - speed);
            setSteps(speed);
        } else {
            /** destination is lower but not further than the max steps that can be taken **/
            setSpeed(current_floor + diff);
            setSteps(-diff);
        }
    }

}
