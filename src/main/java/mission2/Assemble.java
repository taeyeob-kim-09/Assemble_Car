package mission2;

import java.util.*;
import java.util.Scanner;

public class Assemble {
    private static final int STEP_CAR_TYPE = 0;
    private static final int STEP_ENGINE = 1;
    private static final int STEP_BRAKE = 2;
    private static final int STEP_STEERING = 3;
    private static final int STEP_RUN_TEST = 4;

    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private static int[] config = new int[4];

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

            int answer;
            try {
                answer = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력 가능합니다.");
                delay(1000);
                continue;
            }

            if (!isValidInput(step, answer)) {
                delay(1000);
                continue;
            }

            if (answer == 0) {
                step = Math.max(STEP_CAR_TYPE, step - 1);
                continue;
            }

            step = processStep(step, answer);
        }

        scanner.close();
    }

    private static int processStep(int step, int input) {
        switch (step) {
            case STEP_CAR_TYPE -> config[STEP_CAR_TYPE] = input;
            case STEP_ENGINE -> config[STEP_ENGINE] = input;
            case STEP_BRAKE -> config[STEP_BRAKE] = input;
            case STEP_STEERING -> config[STEP_STEERING] = input;
            case STEP_RUN_TEST -> handleRunOrTest(input);
        }
        return (step == STEP_RUN_TEST) ? STEP_CAR_TYPE : step + 1;
    }

    private static void handleRunOrTest(int input) {
        if (input == 1) {
            String result = AssembleValidator.runCar(config);
            switch (result) {
                case "OK" -> {
                    System.out.println("자동차가 동작됩니다.");
                    printConfig();
                }
                case "BROKEN" -> System.out.println("엔진이 고장나있습니다.\n자동차가 움직이지 않습니다.");
                case "INVALID" -> System.out.println("자동차가 동작되지 않습니다 (부품 조합 오류)");
            }
        } else if (input == 2) {
            String msg = AssembleValidator.testCarConfiguration(config);
            if (msg.equals("PASS")) {
                System.out.println("자동차 부품 조합 테스트 결과 : PASS");
            } else {
                System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
                System.out.println(msg);
            }
        }
        delay(2000);
    }

    private static boolean isValidInput(int step, int input) {
        return switch (step) {
            case STEP_CAR_TYPE -> inRange(input, 1, 3);
            case STEP_ENGINE -> inRange(input, 1, 4);
            case STEP_BRAKE -> inRange(input, 1, 3);
            case STEP_STEERING -> inRange(input, 1, 2);
            case STEP_RUN_TEST -> inRange(input, 1, 2);
            default -> false;
        };
    }

    private static boolean inRange(int val, int min, int max) {
        if (val < min || val > max) {
            System.out.printf("유효한 값은 %d ~ %d 입니다.%n", min, max);
            return false;
        }
        return true;
    }

    private static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    private static void printConfig() {
        String[] carTypes = {"", "Sedan", "SUV", "Truck"};
        String[] engines = {"", "GM", "TOYOTA", "WIA", "고장난 엔진"};
        String[] brakes = {"", "MANDO", "CONTINENTAL", "BOSCH"};
        String[] steerings = {"", "BOSCH", "MOBIS"};

        System.out.printf("Car Type : %s%n", carTypes[config[0]]);
        System.out.printf("Engine   : %s%n", engines[config[1]]);
        System.out.printf("Brake    : %s%n", brakes[config[2]]);
        System.out.printf("Steering : %s%n", steerings[config[3]]);
    }

    private static void showMenu(int step) {
        switch (step) {
            case STEP_CAR_TYPE -> {
                System.out.println("차량 타입 선택:\n1. Sedan\n2. SUV\n3. Truck");
            }
            case STEP_ENGINE -> {
                System.out.println("엔진 선택:\n1. GM\n2. TOYOTA\n3. WIA\n4. 고장난 엔진");
            }
            case STEP_BRAKE -> {
                System.out.println("제동장치 선택:\n1. MANDO\n2. CONTINENTAL\n3. BOSCH");
            }
            case STEP_STEERING -> {
                System.out.println("조향장치 선택:\n1. BOSCH\n2. MOBIS");
            }
            case STEP_RUN_TEST -> {
                System.out.println("작업 선택:\n1. RUN\n2. TEST");
            }
        }
    }
}
