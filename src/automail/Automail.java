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
    	
    	/** Initialize robots, currently only regular robots */
//    	robots = new Robot[numRegRobots];
//    	for (int i = 0; i < numRegRobots; i++) {
//            robots[i] = new Robot(delivery, mailPool, i);
//        }

        robots = new ArrayList<Robot>();
        for (int i = 0; i < numRegRobots; i++) robots.add(new RegularRobot(delivery, mailPool, i));
        for (int i = 0; i < numFastRobots; i++) robots.add(new FastRobot(delivery, mailPool, i));
        for (int i = 0; i < numBulkRobots; i++) robots.add(new BulkRobot(delivery, mailPool, i));

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
