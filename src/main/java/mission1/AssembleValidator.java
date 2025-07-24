package mission1;

public class AssembleValidator {
    static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    static final int GM = 1, TOYOTA = 2, WIA = 3;
    static final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
    static final int BOSCH_S = 1, MOBIS = 2;

    public static boolean isValid(int[] config) {
        if (config[0] == SEDAN && config[2] == CONTINENTAL) return false;
        if (config[0] == SUV && config[1] == TOYOTA) return false;
        if (config[0] == TRUCK && config[1] == WIA) return false;
        if (config[0] == TRUCK && config[2] == MANDO) return false;
        if (config[2] == BOSCH_B && config[3] != BOSCH_S) return false;
        return true;
    }

    public static String runCar(int[] config) {
        if (!isValid(config)) return "INVALID";
        if (config[1] == 4) return "BROKEN";
        return "OK";
    }

    public static String testCarConfiguration(int[] config) {
        if (config[0] == SEDAN && config[2] == CONTINENTAL) return "Sedan에는 Continental제동장치 사용 불가";
        if (config[0] == SUV && config[1] == TOYOTA) return "SUV에는 TOYOTA엔진 사용 불가";
        if (config[0] == TRUCK && config[1] == WIA) return "Truck에는 WIA엔진 사용 불가";
        if (config[0] == TRUCK && config[2] == MANDO) return "Truck에는 Mando제동장치 사용 불가";
        if (config[2] == BOSCH_B && config[3] != BOSCH_S) return "Bosch제동장치에는 Bosch조향장치 이외 사용 불가";
        return "PASS";
    }
}
