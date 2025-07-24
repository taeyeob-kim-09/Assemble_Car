package mission2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AssembleValidatorTest {
    @Test
    @DisplayName("PASS: 정상 조합")
    void testValidCombination_pass() {
        int[] config = {1, 1, 1, 1}; // Sedan + GM + Mando + Bosch
        assertTrue(AssembleValidator.isValid(config));
        assertEquals("OK", AssembleValidator.runCar(config));
        assertEquals("PASS", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("FAIL: Sedan에 Continental 제동장치")
    void testInvalidCombination_sedanContinental() {
        int[] config = {1, 1, 2, 1};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Sedan에는 Continental제동장치 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("FAIL: SUV에 TOYOTA 엔진")
    void testInvalidCombination_suvToyota() {
        int[] config = {2, 2, 1, 1};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("SUV에는 TOYOTA엔진 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("FAIL: Truck에 WIA 엔진")
    void testInvalidCombination_truckWia() {
        int[] config = {3, 3, 1, 1};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Truck에는 WIA엔진 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("FAIL: Truck에 Mando 제동장치")
    void testInvalidCombination_truckMando() {
        int[] config = {3, 1, 1, 1};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Truck에는 Mando제동장치 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("FAIL: Bosch 제동장치에 Mobis 조향장치")
    void testInvalidCombination_boschBrakeMobisSteering() {
        int[] config = {1, 1, 3, 2};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Bosch제동장치에는 Bosch조향장치 이외 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("BROKEN: 고장난 엔진")
    void testBrokenEngine() {
        int[] config = {1, 4, 1, 1};
        assertTrue(AssembleValidator.isValid(config)); // 조합은 유효함
        assertEquals("BROKEN", AssembleValidator.runCar(config));
        assertEquals("PASS", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    @DisplayName("Custom Rule 추가")
    void testCustomRule() {
        AssembleValidator.addCustomValidator(
                c -> !(c.engine == 1 && c.carType == 3),
                "Truck에는 GM 엔진 사용 불가"
        );

        int[] config = {3, 1, 2, 1};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Truck에는 GM 엔진 사용 불가", AssembleValidator.testCarConfiguration(config));
    }
}
