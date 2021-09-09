package automail;

import simulation.IMailDelivery;
import java.util.ArrayList;

public class Automail {

    private ArrayList<Robot> robots;
//    private Robot[] robots;
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

//    public Robot[] getRobots() {
//        return robots;
//    }

    public MailPool getMailPool() {
        return mailPool;
    }
}
