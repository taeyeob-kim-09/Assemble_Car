package mission2;


import java.util.*;
import java.util.Scanner;

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

interface ValidationRule {
    boolean isSatisfied(CarConfig config);
    String getMessage();
}

class Rule implements ValidationRule {
    private final ComponentValidator validator;
    private final String message;

    Rule(ComponentValidator validator, String message) {
        this.validator = validator;
        this.message = message;
    }

    @Override
    public boolean isSatisfied(CarConfig config) {
        return validator.isValid(config);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

class RuleSet {
    private final List<ValidationRule> rules = new ArrayList<>();

    void addRule(ValidationRule rule) {
        rules.add(rule);
    }

    boolean validate(CarConfig config) {
        return rules.stream().allMatch(rule -> rule.isSatisfied(config));
    }

    Optional<String> getFirstFailureMessage(CarConfig config) {
        return rules.stream()
                .filter(rule -> !rule.isSatisfied(config))
                .map(ValidationRule::getMessage)
                .findFirst();
    }
}

class AssembleValidator {
    static final int BROKEN_ENGINE = 4;
    private static final RuleSet ruleSet = new RuleSet();

    static {
        ruleSet.addRule(new Rule(c -> !(c.carType == 1 && c.brake == 2), "Sedan에는 Continental제동장치 사용 불가"));
        ruleSet.addRule(new Rule(c -> !(c.carType == 2 && c.engine == 2), "SUV에는 TOYOTA엔진 사용 불가"));
        ruleSet.addRule(new Rule(c -> !(c.carType == 3 && c.engine == 3), "Truck에는 WIA엔진 사용 불가"));
        ruleSet.addRule(new Rule(c -> !(c.carType == 3 && c.brake == 1), "Truck에는 Mando제동장치 사용 불가"));
        ruleSet.addRule(new Rule(c -> !(c.brake == 3 && c.steering != 1), "Bosch제동장치에는 Bosch조향장치 이외 사용 불가"));
    }

    public static boolean isValid(int[] configArr) {
        return ruleSet.validate(toConfig(configArr));
    }

    public static String runCar(int[] configArr) {
        CarConfig config = toConfig(configArr);
        if (!ruleSet.validate(config)) return "INVALID";
        if (config.engine == BROKEN_ENGINE) return "BROKEN";
        return "OK";
    }

    public static String testCarConfiguration(int[] configArr) {
        return ruleSet.getFirstFailureMessage(toConfig(configArr)).orElse("PASS");
    }

    public static void addCustomValidator(ComponentValidator validator, String message) {
        ruleSet.addRule(new Rule(validator, message));
    }

    private static CarConfig toConfig(int[] configArr) {
        return new CarConfig(configArr[0], configArr[1], configArr[2], configArr[3]);
    }
}
