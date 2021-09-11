package util;

import automail.BulkRobot;
import automail.ChargeGenerator;
import automail.MailItem;
import automail.Robot;
import exceptions.MailAlreadyDeliveredException;
import simulation.Clock;
import simulation.IMailDelivery;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReportDelivery implements IMailDelivery {

    /**An array list to record mails that have been delivered*/
    private Set<MailItem> deliveredItems;
    private static double total_delay = 0;

    private ChargeGenerator charger = ChargeGenerator.getInstance();
    private Configuration configuration = Configuration.getInstance();

    public ReportDelivery() throws Exception {
        deliveredItems = new HashSet<>();
    }

    /** Confirm the delivery and calculate the total score */
    @Override
    public void deliver(Robot robot, MailItem deliveryItem, String additionalLog ) {
        String feeInfo = "";

        if(!deliveredItems.contains(deliveryItem))
        {
            deliveredItems.add(deliveryItem);
            if (Boolean.parseBoolean(configuration.getProperty(Configuration.FEE_CHARGING_KEY))) {
                /** need to charge fee, generate fee charging information **/
                double maintenanceFee = charger.getMaintenanceFee(robot);
                double serviceFee = charger.getServiceFee(robot);
                double avgOptTime = charger.getAvgOptTime(robot);
                feeInfo = String.format(" | Service Fee: %.2f | Maintenance: %.2f | Avg. Operating Time: %.2f | Total Charge: %.2f"
                        , serviceFee
                        , maintenanceFee
                        , avgOptTime
                        , serviceFee + maintenanceFee);
            }

            System.out.printf("T: %3d > %7s-> Delivered(%4d) [%s%s]%n", Clock.Time(), robot.getIdTube(), deliveredItems.size(), deliveryItem.toString(), feeInfo);

            // Calculate delivery score
            total_delay += calculateDeliveryDelay(deliveryItem);
        }
        else{
            try {
                throw new MailAlreadyDeliveredException(deliveryItem.getId());
            } catch (MailAlreadyDeliveredException e) {
                e.printStackTrace();
            }
        }
    }

    public double getTotal_delay()
    {
        return total_delay;
    }

    @Override
    public Set<MailItem> getDeliveredItems() {
        return Collections.unmodifiableSet(deliveredItems);
    }

    private static double calculateDeliveryDelay(MailItem deliveryItem) {
        // Penalty for longer delivery times
        final double penalty = 1.2;
        double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

}
