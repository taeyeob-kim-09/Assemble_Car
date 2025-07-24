import java.util.Scanner;

public class Assemble {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";


    private static final int CAR_TYPE = 0;
    private static final int ENGINE = 1;
    private static final int BRAKE = 2;
    private static final int STEERING = 3;
    private static final int RUN_OR_TEST = 4;

    private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    private static final int GM = 1, TOYOTA = 2, WIA = 3, BROKEN_ENGINE = 4;
    private static final int MANDO = 1, CONTINENTAL = 2, BOSCH_BRAKE = 3;
    private static final int BOSCH_STEERING = 1, MOBIS = 2;

    private static final int[] config = new int[5];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int currentStep = CAR_TYPE;

        while (true) {
            clearScreen();
            showMenu(currentStep);

            System.out.print("INPUT > ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("바이바이");
                break;
            }

            int answer;
            try {
                answer = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ERROR :: 숫자만 입력 가능");
                delay(800);
                continue;
            }

            if (!isValidAnswer(currentStep, answer)) {
                delay(800);
                continue;
            }

            if (answer == 0) {
                currentStep = currentStep == RUN_OR_TEST ? CAR_TYPE : Math.max(CAR_TYPE, currentStep - 1);
                continue;
            }

            handleAnswer(currentStep, answer);
            currentStep = nextStep(currentStep);
        }
        scanner.close();
    }

    private static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private static void showMenu(int step) {
        switch (step) {
            case CAR_TYPE -> showCarTypeMenu();
            case ENGINE -> showEngineMenu();
            case BRAKE -> showBrakeMenu();
            case STEERING -> showSteeringMenu();
            case RUN_OR_TEST -> showRunOrTestMenu();
        }
    }

    private static boolean isValidAnswer(int step, int answer) {
        return switch (step) {
            case CAR_TYPE -> isInRange(answer, 1, 3, "차량 타입은 1 ~ 3 범위만 선택 가능");
            case ENGINE -> isInRange(answer, 0, 4, "엔진은 1 ~ 4 범위만 선택 가능");
            case BRAKE -> isInRange(answer, 0, 3, "제동장치는 1 ~ 3 범위만 선택 가능");
            case STEERING -> isInRange(answer, 0, 2, "조향장치는 1 ~ 2 범위만 선택 가능");
            case RUN_OR_TEST -> isInRange(answer, 0, 2, "Run 또는 Test 중 하나를 선택 필요");
            default -> false;
        };
    }

    private static boolean isInRange(int value, int min, int max, String errorMessage) {
        if (value < min || value > max) {
            System.out.println("ERROR :: " + errorMessage);
            return false;
        }
        return true;
    }

    private static void handleAnswer(int step, int answer) {
        switch (step) {
            case CAR_TYPE -> selectCarType(answer);
            case ENGINE -> selectEngine(answer);
            case BRAKE -> selectBrake(answer);
            case STEERING -> selectSteering(answer);
            case RUN_OR_TEST -> handleRunOrTest(answer);
        }
    }

    private static int nextStep(int current) {
        return current < RUN_OR_TEST ? current + 1 : current;
    }

    private static void selectCarType(int selection) {
        config[CAR_TYPE] = selection;
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", carTypeName(selection));
        delay(800);
    }

    private static void selectEngine(int selection) {
        config[ENGINE] = selection;
        System.out.printf("%s 엔진을 선택하셨습니다.\n", engineName(selection));
        delay(800);
    }

    private static void selectBrake(int selection) {
        config[BRAKE] = selection;
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", brakeName(selection));
        delay(800);
    }

    private static void selectSteering(int selection) {
        config[STEERING] = selection;
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", steeringName(selection));
        delay(800);
    }

    private static void handleRunOrTest(int choice) {
        if (choice == 1) {
            runCar();
            delay(2000);
        } else if (choice == 2) {
            System.out.println("Test...");
            delay(1500);
            testCarConfiguration();
            delay(2000);
        }
    }

    private static void runCar() {
        if (!isConfigurationValid()) {
            System.out.println("자동차가 동작되지 않습니다");
            return;
        }
        if (config[ENGINE] == BROKEN_ENGINE) {
            System.out.println("엔진이 고장나있습니다.\n자동차가 움직이지 않습니다.");
            return;
        }
        System.out.printf("Car Type : %s\n", carTypeName(config[CAR_TYPE]));
        System.out.printf("Engine   : %s\n", engineName(config[ENGINE]));
        System.out.printf("Brake    : %s\n", brakeName(config[BRAKE]));
        System.out.printf("Steering : %s\n", steeringName(config[STEERING]));
        System.out.println("자동차가 동작됩니다.");
    }

    private static void testCarConfiguration() {
        if (config[CAR_TYPE] == SEDAN && config[BRAKE] == CONTINENTAL) {
            printTestFail("Sedan에는 Continental제동장치 사용 불가");
        } else if (config[CAR_TYPE] == SUV && config[ENGINE] == TOYOTA) {
            printTestFail("SUV에는 TOYOTA엔진 사용 불가");
        } else if (config[CAR_TYPE] == TRUCK && config[ENGINE] == WIA) {
            printTestFail("Truck에는 WIA엔진 사용 불가");
        } else if (config[CAR_TYPE] == TRUCK && config[BRAKE] == MANDO) {
            printTestFail("Truck에는 Mando제동장치 사용 불가");
        } else if (config[BRAKE] == BOSCH_BRAKE && config[STEERING] != BOSCH_STEERING) {
            printTestFail("Bosch제동장치에는 Bosch조향장치 이외 사용 불가");
        } else {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        }
    }

    private static boolean isConfigurationValid() {
        return !(config[CAR_TYPE] == SEDAN && config[BRAKE] == CONTINENTAL) &&
                !(config[CAR_TYPE] == SUV && config[ENGINE] == TOYOTA) &&
                !(config[CAR_TYPE] == TRUCK && config[ENGINE] == WIA) &&
                !(config[CAR_TYPE] == TRUCK && config[BRAKE] == MANDO) &&
                !(config[BRAKE] == BOSCH_BRAKE && config[STEERING] != BOSCH_STEERING);
    }

    private static void printTestFail(String reason) {
        System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
        System.out.println(reason);
    }

    private static String carTypeName(int value) {
        return switch (value) {
            case 1 -> "Sedan";
            case 2 -> "SUV";
            case 3 -> "Truck";
            default -> "Unknown";
        };
    }

    private static String engineName(int value) {
        return switch (value) {
            case 1 -> "GM";
            case 2 -> "TOYOTA";
            case 3 -> "WIA";
            case 4 -> "고장난 엔진";
            default -> "Unknown";
        };
    }

    private static String brakeName(int value) {
        return switch (value) {
            case 1 -> "Mando";
            case 2 -> "Continental";
            case 3 -> "Bosch";
            default -> "Unknown";
        };
    }

    private static String steeringName(int value) {
        return switch (value) {
            case 1 -> "Bosch";
            case 2 -> "Mobis";
            default -> "Unknown";
        };
    }

    private static void showCarTypeMenu() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        System.out.println("  ____/_|_____________|____");
        System.out.println(" |                      O  |");
        System.out.println(" '-(@)----------------(@)--'");
        System.out.println("===============================");
        System.out.println("어떤 차량 타입을 선택할까요?\n1. Sedan\n2. SUV\n3. Truck");
        System.out.println("===============================");
    }

    private static void showEngineMenu() {
        System.out.println("어떤 엔진을 탑재할까요?\n0. 뒤로가기\n1. GM\n2. TOYOTA\n3. WIA\n4. 고장난 엔진");
        System.out.println("===============================");
    }

    private static void showBrakeMenu() {
        System.out.println("어떤 제동장치를 선택할까요?\n0. 뒤로가기\n1. MANDO\n2. CONTINENTAL\n3. BOSCH");
        System.out.println("===============================");
    }

    private static void showSteeringMenu() {
        System.out.println("어떤 조향장치를 선택할까요?\n0. 뒤로가기\n1. BOSCH\n2. MOBIS");
        System.out.println("===============================");
    }

    private static void showRunOrTestMenu() {
        System.out.println("멋진 차량이 완성되었습니다.\n어떤 동작을 할까요?\n0. 처음 화면으로 돌아가기\n1. RUN\n2. Test");
        System.out.println("===============================");
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}