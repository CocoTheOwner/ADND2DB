import nl.codevs.dndinventory.data.Coin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestCoin {
    @Test
    public void testDecrementGPEP() {
        assertEquals(Coin.GP.decrement(), Coin.EP);
    }

    @Test
    public void testDecrementFactorPPGP() {
        assertEquals(Coin.PP.decrementFactor(), 5);
    }

    @Test
    public void testExceptionCPDecrement() {
        assertThrowsExactly(RuntimeException.class, Coin.CP::decrement, "Cannot decrement CP");
    }

    @Test
    public void testExceptionCPDecrementFactor() {
        assertThrowsExactly(RuntimeException.class, Coin.CP::decrementFactor, "Cannot decrementFactor CP");
    }
}
