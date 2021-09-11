package exceptions;
import automail.Robot;

/**
 * An exception thrown when the robot tries to deliver more items than its tube capacity without refilling.
 */
public class ExcessiveDeliveryException extends Throwable {
	public ExcessiveDeliveryException(Robot robot, int capacity){
		super("Robot(" + robot.getId() + ") attempting to deliver more than " + capacity + " items in a single trip!!");
	}
}
