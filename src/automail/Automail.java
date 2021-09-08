package automail;

import simulation.IMailDelivery;

public class Automail {

    private Robot[] robots;
    private MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRegRobots, int numFastRobots, int numBulkRobots) {  	
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots */
    	int index = 0;
    	robots = new Robot[numRegRobots];
    	for (int i = index; i < numRegRobots; i++) robots[i] = new RegularRobot(delivery, mailPool, i);
    	index += numRegRobots;
        for (int i = index; i < index+numFastRobots; i++) robots[i] = new FastRobot(delivery, mailPool, i);
        index += numFastRobots;
        for (int i = index; i < index+numBulkRobots; i++) robots[i] = new BulkRobot(delivery, mailPool, i);
    }

    public Robot[] getRobots() {
        return robots;
    }

    public MailPool getMailPool() {
        return mailPool;
    }

    public double getAvgOptTime(Robot robot) {
        double num = 0;
        double totalSteps = robot.getSteps();
        Robot currentRobot;
        /** Get total steps of all robots with the same type of inputted robot **/
        for (int i=0; i<robots.length; i++) {
            currentRobot = robots[i];
            if (robot.id.substring(0,1) == currentRobot.id.substring(0,1)) {
                totalSteps += currentRobot.getSteps();
                num++;
            }
        }

        return totalSteps/num;
    }

}
