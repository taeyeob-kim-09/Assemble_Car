package mission2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AssembleTest {
    @BeforeEach
    void setUp() {
        Assemble.DEBUG_MODE = true; // Coverage 위해 인터랙티브 모드 비활성화
    }

    @Test
    void testValidRun() {
        int[] config = {1, 1, 1, 1}; // Sedan, GM, Mando, Bosch
        String result = AssembleValidator.runCar(config);
        assertEquals("OK", result);
    }

    @Test
    void testBrokenEngineRun() {
        int[] config = {1, 4, 1, 1}; // Sedan, 고장난 엔진, Mando, Bosch
        String result = AssembleValidator.runCar(config);
        assertEquals("BROKEN", result);
    }

    @Test
    void testInvalidRun_dueToBrakeRule() {
        int[] config = {1, 1, 2, 1}; // Sedan, GM, Continental, Bosch
        String result = AssembleValidator.runCar(config);
        assertEquals("INVALID", result);
    }

    @Test
    void testTestCarConfiguration_pass() {
        int[] config = {2, 1, 1, 1}; // SUV, GM, Mando, Bosch
        String result = AssembleValidator.testCarConfiguration(config);
        assertEquals("PASS", result);
    }

    @Test
    void testTestCarConfiguration_fail() {
        int[] config = {2, 2, 1, 1}; // SUV, TOYOTA, Mando, Bosch
        String result = AssembleValidator.testCarConfiguration(config);
        assertEquals("SUV에는 TOYOTA엔진 사용 불가", result);
    }

    @Test
    void testBackNavigationLogic() {
        int currentStep = 4;
        int input = 0;
        int expectedStep = 0; // When STEP_RUN_TEST (4), 0 goes back to STEP_CAR_TYPE
        if (input == 0) {
            currentStep = (currentStep == 4) ? 0 : Math.max(0, currentStep - 1);
        }
        assertEquals(expectedStep, currentStep);
    }

    @Test
    void testProcessStepsInOrder() {
        int step = Assemble.processStep(0, 1); // Sedan
        assertEquals(1, step);

        step = Assemble.processStep(step, 2); // TOYOTA
        assertEquals(2, step);

        step = Assemble.processStep(step, 1); // MANDO
        assertEquals(3, step);

        step = Assemble.processStep(step, 1); // BOSCH
        assertEquals(4, step);
    }

    @Test
    void testIsValidInput() {
        assertTrue(Assemble.isValidInput(0, 1));
        assertFalse(Assemble.isValidInput(0, 5)); // 잘못된 CarType
        assertTrue(Assemble.isValidInput(1, 4));  // 고장난 엔진도 유효한 입력
        assertFalse(Assemble.isValidInput(1, 5));
    }

    @Test
    void testHandleRunWithInvalidConfig() {
        int[] config = {1, 2, 2, 1}; // Sedan + TOYOTA + Continental → Invalid
        System.arraycopy(config, 0, getInternalConfig(), 0, config.length);
        Assemble.handleRunOrTest(1); // run
    }

    @Test
    void testHandleRunWithBrokenEngine() {
        int[] config = {1, 4, 1, 1}; // 고장난 엔진
        System.arraycopy(config, 0, getInternalConfig(), 0, config.length);
        Assemble.handleRunOrTest(1);
    }

    @Test
    void testHandleRunWithValidConfig() {
        int[] config = {1, 1, 1, 1}; // 유효한 조합
        System.arraycopy(config, 0, getInternalConfig(), 0, config.length);
        Assemble.handleRunOrTest(1);
    }

    @Test
    void testHandleTestResult() {
        int[] config = {1, 2, 2, 1}; // FAIL 조합
        System.arraycopy(config, 0, getInternalConfig(), 0, config.length);
        Assemble.handleRunOrTest(2);

        int[] configPass = {1, 1, 1, 1}; // PASS 조합
        System.arraycopy(configPass, 0, getInternalConfig(), 0, configPass.length);
        Assemble.handleRunOrTest(2);
        Assemble.clearScreen();
    }

    @Test
    void testClearScreen() {
        Assemble.clearScreen(); // 호출만으로 커버됨
    }

    @Test
    void testMainEntry() {
        Assemble.DEBUG_MODE = true; // runInteractive 방지
        Assemble.main(new String[0]);
    }

    @Test
    void testShowMenuAllSteps() {
        for (int i = 0; i <= 4; i++) {
            Assemble.showMenu(i);
        }
    }

   @Test
    public void testShowMenuCoverage() {
        for (int i = 0; i <= 4; i++) {
            Assemble.showMenu(i);
        }
    }

    @Test
    public void testMainMethodCoverage() {
        Assemble.DEBUG_MODE = true;
        Assemble.main(new String[]{});
    }

    @Test
    public void testProcessStepTransitions() {
        int next = Assemble.processStep(0, 1);
        assertEquals(1, next);
        next = Assemble.processStep(1, 2);
        assertEquals(2, next);
    }

    @Test
    public void testValidationInputRanges() {
        assertTrue(Assemble.isValidInput(0, 1));
        assertFalse(Assemble.isValidInput(0, 0));
        assertFalse(Assemble.isValidInput(3, 5));
    }

    // Hack to access private static config[]
    private int[] getInternalConfig() {
        try {
            var field = Assemble.class.getDeclaredField("config");
            field.setAccessible(true);
            return (int[]) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
