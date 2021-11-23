import nl.codevs.dndinventory.data.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestCoin {
    @Test
    public void testDecrementGPEP() {
        assertEquals(Money.Coin.GP.decrement(), Money.Coin.EP);
    }

    @Test
    public void testDecrementFactorPPGP() {
        assertEquals(Money.Coin.PP.decrementFactor(), 5);
    }

    @Test
    public void testExceptionCPDecrement() {
        assertThrowsExactly(RuntimeException.class, Money.Coin.CP::decrement, "Cannot decrement CP");
    }

    @Test
    public void testExceptionCPDecrementFactor() {
        assertThrowsExactly(RuntimeException.class, Money.Coin.CP::decrementFactor, "Cannot decrementFactor CP");
    }
}
