package automail;

import simulation.IMailDelivery;

/** A regular delivery robot **/
public class RegularRobot extends Robot{
    {
        TUBE_CAPACITY = 1;
    }

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param number gives the id of current robot
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public RegularRobot(IMailDelivery delivery, MailPool mailPool, int number) {
        super(delivery, mailPool);
        this.id = "R" + number;
    }
}
