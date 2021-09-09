package automail;

import com.unimelb.swen30006.wifimodem.WifiModem;

import java.util.HashMap;


/** The class is a calculator for each delivery of a robot **/
public class ChargeGenerator {
    private HashMap<String, Double> typeBasedRate = new HashMap<>();
    private HashMap<Integer, Double> recentServiceFee = new HashMap<>();

    private Automail automail;
    private WifiModem wifiModem;
    private Building building;

    private static ChargeGenerator charger;

    private ChargeGenerator() throws Exception {
        this.automail = automail;
        this.building = Building.getInstance();
        this.wifiModem = WifiModem.getInstance(building.getMailroomLocationFloor());
        /** Set up type based rate for each robot type **/
        typeBasedRate.put("R", 0.025);
        typeBasedRate.put("F", 0.05);
        typeBasedRate.put("B", 0.01);

        for (int i = building.getLowestFloor(); i <= building.getnFloors(); i++){
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

    public void setAutomail(Automail automail) {
        this.automail = automail;
    }


    public double getTotalCharge(Robot robot) {
        double maintenance = getMaintenanceFee(robot);
        double service = getServiceFee(robot);

        return maintenance + service;
    }

    public double getMaintenanceFee(Robot robot) {
        String type = robot.id.substring(0,1);
        double avgOptTime = automail.getAvgOptTime(robot);
        double rate = typeBasedRate.get(type);
        return avgOptTime * rate;
    }

    public double getServiceFee(Robot robot) {
        int currentFloor = robot.getCurrentFloor();
        double serviceFee = wifiModem.forwardCallToAPI_LookupPrice(currentFloor);

        if (serviceFee < 0){
            /** Lookup failed, get the recent service fee on current floor **/
            serviceFee = recentServiceFee.get(currentFloor);
        }

        recentServiceFee.put(currentFloor, serviceFee);
        return serviceFee;
    }


    public double getAvgOptTime(Robot robot) {
        return automail.getAvgOptTime(robot);
    }

}
