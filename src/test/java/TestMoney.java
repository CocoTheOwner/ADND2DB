import nl.codevs.dndinventory.data.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestMoney {

    @Test
    public void simpleSingular1() {
        assertEquals(new Money(10).getAsGP(), 10d);
    }

    @Test
    public void simpleSingular2() {
        assertEquals(new Money(10, 5, 10).getAsGP(), 10d + 5d / 10d + 10d / 100d);
    }

    @Test
    public void simpleSingular3() {
        assertEquals(new Money(10.5).getAsGP(), 10.5d);
    }

    @Test
    public void simpleSingular4() {
        assertEquals(new Money(1.5).getAsCP(), 150d);
    }

    @Test
    public void simpleSingular5() {
        assertEquals(new Money(10).getAsPP(), 2d);
    }

    @Test
    public void advancedSingulars1() {
        assertEquals(new Money(Money.Coin.PP,  1).getAsGP(), 5d);
    }

    @Test
    public void advancedSingulars2() {
        assertEquals(new Money(Money.Coin.CP, 100).getAsGP(), 1d);
    }

    @Test
    public void advancedSingulars3() {
        assertEquals(new Money(Money.Coin.SP, 100).getGP(), 10d);
    }

    @Test
    public void advancedSingulars4() {
        assertEquals(new Money(Money.Coin.SP, 0.5, Money.Coin.PP, 0.01).getAsCP(), 5d + 5d);
    }

    @Test
    public void advancedSingulars5() {
        assertEquals(new Money(Money.Coin.CP, 1, Money.Coin.GP, 10, Money.Coin.PP, 1).getAsGP(), 15.01);
    }

    @Test
    public void valueToString() {
        assertEquals(Money.fromString("10gp").toString(), "10gp");
    }

    @Test
    public void testMaximizeSimple() {
        assertEquals(5, Money.fromString("50cp").getSP());
    }

    @Test
    public void testMaximizeComplex() {
        assertEquals(1, Money.fromString("5.11GP").getSP());
        assertEquals(1, Money.fromString("5.11GP").getCP());
        assertEquals(5, Money.fromString("5.11GP").getGP());
        assertEquals(0, Money.fromString("5.11GP").getEP());
    }

    @Test
    public void testMaximizeString() {
        assertEquals(151, Money.fromString("15151cp").getGP());
        assertEquals(5, Money.fromString("15151cp").getSP());
        assertEquals(1, Money.fromString("15151cp").getCP());
    }

    @Test
    public void testAsGP() {
        assertEquals(151.51, Money.fromString("15151cp").getAsGP());
    }

    @Test
    public void testFromValueAndFactor() {
        assertEquals(15, Money.fromValueAndFactor(new Money(Money.Coin.SP, 2), 0.75).getAsCP());
    }
}