package mission1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssembleTest {
    @Test
    void testInvalidCombination_sedanAndContinental() {
        int[] config = {1, 2, 2, 1}; // Sedan, TOYOTA, CONTINENTAL, BOSCH
        assertFalse(AssembleValidator.isValid(config));
    }

    @Test
    void testInvalidCombination_truckWithWIAEngine() {
        int[] config = {3, 3, 1, 1}; // Truck, WIA, MANDO, BOSCH
        assertFalse(AssembleValidator.isValid(config));
    }

    @Test
    void testInvalidCombination_truckWithMandoBrake() {
        int[] config = {3, 1, 1, 1}; // Truck, GM, MANDO, BOSCH
        assertFalse(AssembleValidator.isValid(config));
    }

    @Test
    void testInvalidCombination_boschBrakeAndMobisSteering() {
        int[] config = {1, 1, 3, 2}; // Sedan, GM, BOSCH, MOBIS
        assertFalse(AssembleValidator.isValid(config));
    }

    @Test
    void testValidCombination_sedanWithMandoAndBosch() {
        int[] config = {1, 1, 1, 1}; // Sedan, GM, MANDO, BOSCH
        assertTrue(AssembleValidator.isValid(config));
    }

    @Test
    void testValidCombination_truckWithToyotaAndBosch() {
        int[] config = {3, 2, 3, 1}; // Truck, TOYOTA, BOSCH, BOSCH
        assertTrue(AssembleValidator.isValid(config));
    }

    @Test
    void testBrokenEngineFailsToRun() {
        int[] config = {2, 4, 2, 1}; // SUV, 고장난 엔진, CONTINENTAL, BOSCH
        assertEquals("BROKEN", AssembleValidator.runCar(config));
    }

    @Test
    void testCarRunsSuccessfully() {
        int[] config = {2, 2, 1, 2}; // SUV, TOYOTA, MANDO, MOBIS
        assertTrue(AssembleValidator.isValid(config));
        assertEquals("OK", AssembleValidator.runCar(config));
    }

    @Test
    void testValidationMessageFromTestProducedCar() {
        int[] config = {3, 3, 1, 1};
        String result = AssembleValidator.testCarConfiguration(config);
        assertTrue(result.contains("Truck에는 Mando제동장치 사용 불가"));
    }
}