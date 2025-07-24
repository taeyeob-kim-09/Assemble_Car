package mission2;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AssembleValidatorTest {
    @Test
    void testValidCombination() {
        int[] config = {1, 1, 1, 1}; // Sedan, GM, Mando, Bosch
        assertTrue(AssembleValidator.isValid(config));
        assertEquals("OK", AssembleValidator.runCar(config));
        assertEquals("PASS", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    void testBrokenEngine() {
        int[] config = {1, 4, 1, 1}; // Sedan, Broken engine
        assertTrue(AssembleValidator.isValid(config));
        assertEquals("BROKEN", AssembleValidator.runCar(config));
    }

    @Test
    void testInvalidCombination_sedan_continental() {
        int[] config = {1, 1, 2, 1};
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Sedan에는 Continental제동장치 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    void testInvalidCombination_truck_mando() {
        int[] config = {3, 1, 1, 1}; // Truck, GM, Mando, Bosch
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Truck에는 Mando제동장치 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    void testBoschBrakeWithNonBoschSteering() {
        int[] config = {2, 1, 3, 2}; // SUV, GM, Bosch Brake, Mobis Steering
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("INVALID", AssembleValidator.runCar(config));
        assertEquals("Bosch제동장치에는 Bosch조향장치 이외 사용 불가", AssembleValidator.testCarConfiguration(config));
    }

    @Test
    void testAddCustomValidator() {
        AssembleValidator.addCustomValidator(new ComponentValidator() {
            public boolean isValid(CarConfig config) {
                return !(config.carType == 2 && config.brake == 1); // SUV, Mando not allowed
            }
            public Optional<String> validationMessage(CarConfig config) {
                return Optional.of("SUV에는 Mando제동장치 사용 불가");
            }
        });

        int[] config = {2, 1, 1, 1}; // SUV, GM, Mando, Bosch
        assertFalse(AssembleValidator.isValid(config));
        assertEquals("SUV에는 Mando제동장치 사용 불가", AssembleValidator.testCarConfiguration(config));
    }
}
