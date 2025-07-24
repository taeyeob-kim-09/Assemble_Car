package mission2;


import java.util.Optional;
import java.util.*;

@FunctionalInterface
interface ComponentValidator {
    boolean isValid(CarConfig config);

    default Optional<String> validationMessage(CarConfig config) {
        return Optional.empty();
    }
}

class CarConfig {
    int carType;
    int engine;
    int brake;
    int steering;

    CarConfig(int carType, int engine, int brake, int steering) {
        this.carType = carType;
        this.engine = engine;
        this.brake = brake;
        this.steering = steering;
    }
}

class AssembleValidator {
    static final int BROKEN_ENGINE = 4;

    private static final List<ComponentValidator> validatorsWithMessage = new ArrayList<>();

    static {
        registerValidator(
                (c -> !(c.carType == 1 && c.brake == 2)),
                "Sedan에는 Continental제동장치 사용 불가"
        );
        registerValidator(
                (c -> !(c.carType == 2 && c.engine == 2)),
                "SUV에는 TOYOTA엔진 사용 불가"
        );
        registerValidator(
                (c -> !(c.carType == 3 && c.engine == 3)),
                "Truck에는 WIA엔진 사용 불가"
        );
        registerValidator(
                (c -> !(c.carType == 3 && c.brake == 1)),
                "Truck에는 Mando제동장치 사용 불가"
        );
        registerValidator(
                (c -> !(c.brake == 3 && c.steering != 1)),
                "Bosch제동장치에는 Bosch조향장치 이외 사용 불가"
        );
    }

    private static void registerValidator(ComponentValidator validator, String message) {
        validatorsWithMessage.add(new ComponentValidator() {
            @Override
            public boolean isValid(CarConfig config) {
                return validator.isValid(config);
            }
            @Override
            public Optional<String> validationMessage(CarConfig config) {
                return Optional.of(message);
            }
        });
    }

    public static boolean isValid(int[] configArr) {
        CarConfig config = toConfig(configArr);
        return validatorsWithMessage.stream().allMatch(v -> v.isValid(config));
    }

    public static String runCar(int[] configArr) {
        CarConfig config = toConfig(configArr);
        if (!isValid(configArr)) return "INVALID";
        if (config.engine == BROKEN_ENGINE) return "BROKEN";
        return "OK";
    }

    public static String testCarConfiguration(int[] configArr) {
        CarConfig config = toConfig(configArr);
        return validatorsWithMessage.stream()
                .filter(v -> !v.isValid(config))
                .map(v -> v.validationMessage(config).orElse("FAIL"))
                .findFirst()
                .orElse("PASS");
    }

    public static void addCustomValidator(ComponentValidator validator) {
        validatorsWithMessage.add(validator);
    }

    private static CarConfig toConfig(int[] configArr) {
        return new CarConfig(configArr[0], configArr[1], configArr[2], configArr[3]);
    }
}
