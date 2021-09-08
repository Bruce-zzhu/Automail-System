package exceptions;

public class BulkRobotExcessiveDeliveryException extends Throwable  {
    public BulkRobotExcessiveDeliveryException(){
        super("Attempting to deliver more than 5 items in a single trip!!");
    }
}
