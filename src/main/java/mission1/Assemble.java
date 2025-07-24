package mission1;

import java.util.Scanner;

public class Assemble {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    // Step identifiers
    private static final int STEP_CAR_TYPE = 0;
    private static final int STEP_ENGINE = 1;
    private static final int STEP_BRAKE = 2;
    private static final int STEP_STEERING = 3;
    private static final int STEP_RUN_TEST = 4;

    // Options
    private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    private static final int GM = 1, TOYOTA = 2, WIA = 3, BROKEN_ENGINE = 4;
    private static final int MANDO = 1, CONTINENTAL = 2, BOSCH_BRAKE = 3;
    private static final int BOSCH_STEERING = 1, MOBIS = 2;

    private static int[] config = new int[5];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int step = STEP_CAR_TYPE;

        while (true) {
            clearScreen();
            showMenu(step);

            System.out.print("INPUT > ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("바이바이");
                break;
            }
            //테스트 commit용
            int answer;
            try {
                answer = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ERROR :: 숫자만 입력 가능");
                delay(800);
                continue;
            }

            if (!isValidInput(step, answer)) {
                delay(800);
                continue;
            }

            if (answer == 0) {
                step = (step == STEP_RUN_TEST) ? STEP_CAR_TYPE : Math.max(STEP_CAR_TYPE, step - 1);
                continue;
            }

            step = processStep(step, answer);
        }

        scanner.close();
    }

    private static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private static void showMenu(int step) {
        switch (step) {
            case STEP_CAR_TYPE -> showCarTypeMenu();
            case STEP_ENGINE -> showEngineMenu();
            case STEP_BRAKE -> showBrakeMenu();
            case STEP_STEERING -> showSteeringMenu();
            case STEP_RUN_TEST -> showRunTestMenu();
        }
    }

    private static boolean isValidInput(int step, int input) {
        return switch (step) {
            case STEP_CAR_TYPE -> checkRange(input, 1, 3, "차량 타입은 1 ~ 3 범위만 선택 가능");
            case STEP_ENGINE -> checkRange(input, 0, 4, "엔진은 1 ~ 4 범위만 선택 가능");
            case STEP_BRAKE -> checkRange(input, 0, 3, "제동장치는 1 ~ 3 범위만 선택 가능");
            case STEP_STEERING -> checkRange(input, 0, 2, "조향장치는 1 ~ 2 범위만 선택 가능");
            case STEP_RUN_TEST -> checkRange(input, 0, 2, "Run 또는 Test 중 하나를 선택 필요");
            default -> false;
        };
    }

    private static boolean checkRange(int val, int min, int max, String errorMsg) {
        if (val < min || val > max) {
            System.out.println("ERROR :: " + errorMsg);
            return false;
        }
        return true;
    }

    private static int processStep(int step, int input) {
        switch (step) {
            case STEP_CAR_TYPE -> {
                selectCarType(input);
                return STEP_ENGINE;
            }
            case STEP_ENGINE -> {
                selectEngine(input);
                return STEP_BRAKE;
            }
            case STEP_BRAKE -> {
                selectBrake(input);
                return STEP_STEERING;
            }
            case STEP_STEERING -> {
                selectSteering(input);
                return STEP_RUN_TEST;
            }
            case STEP_RUN_TEST -> {
                handleRunOrTest(input);
                return STEP_RUN_TEST;
            }
            default -> {
                return STEP_CAR_TYPE;
            }
        }
    }

    private static void selectCarType(int option) {
        config[STEP_CAR_TYPE] = option;
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", carTypeName(option));
        delay(800);
    }

    private static void selectEngine(int option) {
        config[STEP_ENGINE] = option;
        System.out.printf("%s 엔진을 선택하셨습니다.\n", engineName(option));
        delay(800);
    }

    private static void selectBrake(int option) {
        config[STEP_BRAKE] = option;
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", brakeName(option));
        delay(800);
    }

    private static void selectSteering(int option) {
        config[STEP_STEERING] = option;
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", steeringName(option));
        delay(800);
    }

    private static void handleRunOrTest(int option) {
        if (option == 1) {
            runCar();
        } else if (option == 2) {
            System.out.println("Test...");
            delay(1500);
            testCarConfiguration();
        }
    }

    private static void runCar() {
        if (!isValidCarConfiguration()) {
            System.out.println("자동차가 동작되지 않습니다");
            return;
        }
        if (config[STEP_ENGINE] == BROKEN_ENGINE) {
            System.out.println("엔진이 고장나있습니다.\n자동차가 움직이지 않습니다.");
            return;
        }

        System.out.printf("Car Type : %s\n", carTypeName(config[STEP_CAR_TYPE]));
        System.out.printf("Engine   : %s\n", engineName(config[STEP_ENGINE]));
        System.out.printf("Brake    : %s\n", brakeName(config[STEP_BRAKE]));
        System.out.printf("Steering : %s\n", steeringName(config[STEP_STEERING]));
        System.out.println("자동차가 동작됩니다.");
        delay(2000);
    }

    private static void testCarConfiguration() {
        if (config[STEP_CAR_TYPE] == SEDAN && config[STEP_BRAKE] == CONTINENTAL) {
            printFail("Sedan에는 Continental제동장치 사용 불가");
        } else if (config[STEP_CAR_TYPE] == SUV && config[STEP_ENGINE] == TOYOTA) {
            printFail("SUV에는 TOYOTA엔진 사용 불가");
        } else if (config[STEP_CAR_TYPE] == TRUCK && config[STEP_ENGINE] == WIA) {
            printFail("Truck에는 WIA엔진 사용 불가");
        } else if (config[STEP_CAR_TYPE] == TRUCK && config[STEP_BRAKE] == MANDO) {
            printFail("Truck에는 Mando제동장치 사용 불가");
        } else if (config[STEP_BRAKE] == BOSCH_BRAKE && config[STEP_STEERING] != BOSCH_STEERING) {
            printFail("Bosch제동장치에는 Bosch조향장치 이외 사용 불가");
        } else {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        }
        delay(2000);
    }

    private static boolean isValidCarConfiguration() {
        return !(config[STEP_CAR_TYPE] == SEDAN && config[STEP_BRAKE] == CONTINENTAL) &&
                !(config[STEP_CAR_TYPE] == SUV && config[STEP_ENGINE] == TOYOTA) &&
                !(config[STEP_CAR_TYPE] == TRUCK && config[STEP_ENGINE] == WIA) &&
                !(config[STEP_CAR_TYPE] == TRUCK && config[STEP_BRAKE] == MANDO) &&
                !(config[STEP_BRAKE] == BOSCH_BRAKE && config[STEP_STEERING] != BOSCH_STEERING);
    }

    private static void printFail(String message) {
        System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
        System.out.println(message);
    }

    private static String carTypeName(int val) {
        return switch (val) {
            case 1 -> "Sedan";
            case 2 -> "SUV";
            case 3 -> "Truck";
            default -> "Unknown";
        };
    }

    private static String engineName(int val) {
        return switch (val) {
            case 1 -> "GM";
            case 2 -> "TOYOTA";
            case 3 -> "WIA";
            case 4 -> "고장난 엔진";
            default -> "Unknown";
        };
    }

    private static String brakeName(int val) {
        return switch (val) {
            case 1 -> "Mando";
            case 2 -> "Continental";
            case 3 -> "Bosch";
            default -> "Unknown";
        };
    }

    private static String steeringName(int val) {
        return switch (val) {
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

    private static void showRunTestMenu() {
        System.out.println("멋진 차량이 완성되었습니다.\n어떤 동작을 할까요?\n0. 처음 화면으로 돌아가기\n1. RUN\n2. Test");
        System.out.println("===============================");
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
