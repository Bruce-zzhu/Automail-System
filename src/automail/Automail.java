package automail;

import simulation.IMailDelivery;
import java.util.ArrayList;

public class Automail {

    private ArrayList<Robot> robots;
    private MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRegRobots, int numFastRobots, int numBulkRobots) {  	
    	/** Initialize the MailPool */
    	this.mailPool = mailPool;

        // Initialize robots
        robots = new ArrayList<Robot>();
        // incremental robot id
        int numRobot = 0;
        for (int i = 0; i < numRegRobots; i++) robots.add(new RegularRobot(delivery, mailPool, i));
        numRobot += numRegRobots;
        for (int i = numRobot; i < numRobot+numFastRobots; i++) robots.add(new FastRobot(delivery, mailPool, i));
        numRobot += numFastRobots;
        for (int i = numRobot; i < numRobot+numBulkRobots; i++) robots.add(new BulkRobot(delivery, mailPool, i));

    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public MailPool getMailPool() {
        return mailPool;
    }

    /** Get the average operating time of a specific type type of robot */
    public double getAvgOptTime(Robot currentRobot) {
        String robotType = currentRobot.id.substring(0,1);
        double num = 0;
        double totalUnits = 0;

        /** Get total units of time that all robots with the same type of inputted robot have spent */
        for (Robot robot: robots) {
            if (robot.id.substring(0,1).equals(robotType)) {
                totalUnits += robot.getTotalUnits();
                num++;
            }
        }

        if (num != 0) {
            return totalUnits / num;
        } else {
            return 0;
        }

    }

}
