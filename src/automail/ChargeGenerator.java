package automail;

import com.unimelb.swen30006.wifimodem.WifiModem;
import util.ReportDelivery;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** The class is a calculator for each delivery of a robot **/
public class ChargeGenerator {
    private HashMap<String, Double> typeBasedRate = new HashMap<>();
    private HashMap<Integer, Double> recentServiceFee = new HashMap<>();

    private Automail automail;
    private WifiModem wifiModem = WifiModem.getInstance(Building.getInstance().getMailroomLocationFloor());

    private static ChargeGenerator charger;

    private ChargeGenerator() throws Exception {
        /** Set up type based rate for each robot type **/
        typeBasedRate.put("R", 0.025);
        typeBasedRate.put("F", 0.05);
        typeBasedRate.put("B", 0.01);

        for (int i=0; i<=Building.getInstance().getnFloors(); i++){
            /** Initialize recent service fee for all floors to be empty **/
            recentServiceFee.put(i, 0.0);
        }
    }

    public static ChargeGenerator getInstance() throws Exception {
        if (charger == null) {
            charger = new ChargeGenerator();
        }
        return charger;
    }

    public double getTotalCharge(Robot robot) {
        double maintenance = getMaintenanceFee(robot);
        double service = getServiceFee(robot);

        return maintenance + service;
    }

    public double getMaintenanceFee(Robot robot) {
        double avgOpt = automail.getAvgOptTime(robot);
        String type = robot.id.substring(0,1);
        double rate = typeBasedRate.get(type);
        return avgOpt * rate;
    }

    public double getServiceFee(Robot robot) {
        int currentFloor = robot.getCurrentFloor();
        double service = wifiModem.forwardCallToAPI_LookupPrice(currentFloor);

        if (service < 0){
            /** Lookup failed **/
            service = recentServiceFee.get(currentFloor);
        }

        recentServiceFee.put(currentFloor, service);
        return service;
    }


    public void setAutomail(Automail automail) {
        this.automail = automail;
    }

    public double getAvgOptTime(Robot robot) {
        return automail.getAvgOptTime(robot);
    }

}
