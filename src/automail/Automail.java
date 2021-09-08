package automail;

import simulation.IMailDelivery;

public class Automail {

    private Robot[] robots;
    private MailPool mailPool;
    
    public Automail(MailPool mailPool, IMailDelivery delivery, int numRegRobots, int numFastRobots, int numBulkRobots) throws Exception {
    	/** Initialize the MailPool */
    	
    	this.mailPool = mailPool;
    	
    	/** Initialize robots */
    	int num_robot = 0;
    	robots = new Robot[numRegRobots+numFastRobots+numBulkRobots];
    	for (int i = num_robot; i < numRegRobots; i++) robots[i] = new RegularRobot(delivery, mailPool, i);
    	num_robot += numRegRobots;
        for (int i = num_robot; i < num_robot+numFastRobots; i++) robots[i] = new FastRobot(delivery, mailPool, i);
        num_robot += numFastRobots;
        for (int i = num_robot; i < num_robot+numBulkRobots; i++) robots[i] = new BulkRobot(delivery, mailPool, i);

    }

    public Robot[] getRobots() {
        return robots;
    }

    public MailPool getMailPool() {
        return mailPool;
    }

    public double getAvgOptTime(Robot robot) {
        double num = 0;
        double totalSteps = 0;
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
