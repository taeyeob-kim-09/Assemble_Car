package mission2;

import java.util.*;
import java.util.Scanner;

public class Assemble {
    public static boolean DEBUG_MODE = true; // release 시 false로 설정

    private static final int STEP_CAR_TYPE = 0;
    private static final int STEP_ENGINE = 1;
    private static final int STEP_BRAKE = 2;
    private static final int STEP_STEERING = 3;
    private static final int STEP_RUN_TEST = 4;

    private static final int BROKEN_ENGINE = 4;

    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final int[] config = new int[5];

    public static void main(String[] args) {
        if (!DEBUG_MODE) {
            runInteractive();
        }
    }

    public static void runInteractive() {
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

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ERROR :: 숫자만 입력 가능");
                delay(800);
                continue;
            }

            if (choice == 0) {
                step = (step == STEP_RUN_TEST) ? STEP_CAR_TYPE : Math.max(STEP_CAR_TYPE, step - 1);
                continue;
            }

            if (!isValidInput(step, choice)) {
                delay(800);
                continue;
            }

            step = processStep(step, choice);
        }
        scanner.close();
    }

    public static int processStep(int step, int input) {
        config[step] = input;
        switch (step) {
            case STEP_CAR_TYPE -> System.out.printf("차량 타입으로 %d을 선택하셨습니다.\n", input);
            case STEP_ENGINE -> System.out.printf("%d 엔진을 선택하셨습니다.\n", input);
            case STEP_BRAKE -> System.out.printf("%d 제동장치를 선택하셨습니다.\n", input);
            case STEP_STEERING -> System.out.printf("%d 조향장치를 선택하셨습니다.\n", input);
            case STEP_RUN_TEST -> {
                handleRunOrTest(input);
                return STEP_RUN_TEST;
            }
        }
        delay(800);
        return step + 1;
    }

    public static void handleRunOrTest(int option) {
        switch (option) {
            case 1 -> {
                String result = AssembleValidator.runCar(config);
                switch (result) {
                    case "INVALID" -> System.out.println("자동차가 동작되지 않습니다");
                    case "BROKEN" -> System.out.println("엔진이 고장나있습니다.\n자동차가 움직이지 않습니다.");
                    case "OK" -> System.out.println("자동차가 동작됩니다.");
                }
                delay(2000);
            }
            case 2 -> {
                System.out.println("Test...");
                delay(1500);
                String result = AssembleValidator.testCarConfiguration(config);
                if ("PASS".equals(result)) {
                    System.out.println("자동차 부품 조합 테스트 결과 : PASS");
                } else {
                    System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
                    System.out.println(result);
                }
                delay(2000);
            }
        }
    }

    public static boolean isValidInput(int step, int input) {
        return switch (step) {
            case STEP_CAR_TYPE -> input >= 1 && input <= 3;
            case STEP_ENGINE -> input >= 1 && input <= 4;
            case STEP_BRAKE -> input >= 1 && input <= 3;
            case STEP_STEERING -> input >= 1 && input <= 2;
            case STEP_RUN_TEST -> input >= 0 && input <= 2;
            default -> false;
        };
    }

    public static void showMenu(int step) {
        switch (step) {
            case STEP_CAR_TYPE -> System.out.println("1. Sedan\n2. SUV\n3. Truck");
            case STEP_ENGINE -> System.out.println("1. GM\n2. TOYOTA\n3. WIA\n4. 고장난 엔진");
            case STEP_BRAKE -> System.out.println("1. MANDO\n2. CONTINENTAL\n3. BOSCH");
            case STEP_STEERING -> System.out.println("1. BOSCH\n2. MOBIS");
            case STEP_RUN_TEST -> System.out.println("1. RUN\n2. Test");
        }
    }

    public static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    public static void delay(int ms) {
        if (DEBUG_MODE) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}

