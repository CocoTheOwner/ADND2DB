import nl.codevs.dndinventory.data.Coin;
import nl.codevs.dndinventory.data.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestValue {

    @Test
    public void simpleSingular1() {
        assertEquals(new Value(10).getAsGP(), 10d);
    }

    @Test
    public void simpleSingular2() {
        assertEquals(new Value(10, 5, 10).getAsGP(), 10d + 5d / 10d + 10d / 100d);
    }

    @Test
    public void simpleSingular3() {
        assertEquals(new Value(10.5).getAsGP(), 10.5d);
    }

    @Test
    public void simpleSingular4() {
        assertEquals(new Value(1.5).getAsCP(), 150d);
    }

    @Test
    public void simpleSingular5() {
        assertEquals(new Value(10).getAsPP(), 2d);
    }

    @Test
    public void advancedSingulars1() {
        assertEquals(new Value(Coin.PP,  1).getAsGP(), 5d);
    }

    @Test
    public void advancedSingulars2() {
        assertEquals(new Value(Coin.CP, 100).getAsGP(), 1d);
    }

    @Test
    public void advancedSingulars3() {
        assertEquals(new Value(Coin.SP, 100).getGP(), 10d);
    }

    @Test
    public void advancedSingulars4() {
        assertEquals(new Value(Coin.SP, 0.5, Coin.PP, 0.01).getAsCP(), 5d + 5d);
    }

    @Test
    public void advancedSingulars5() {
        assertEquals(new Value(Coin.CP, 1, Coin.GP, 10, Coin.PP, 1).getAsGP(), 15.01);
    }

    @Test
    public void valueToString() {
        assertEquals(new Value("10gp").toString(), "c0 s0 e0 g10 p0 totals 10.0gp");
    }

    @Test
    public void testMaximizeSimple() {
        System.out.println(new Value("50cp"));
        assertEquals(5, new Value("50cp").getSP());
    }

    @Test
    public void testMaximizeComplex() {
        assertEquals(1, new Value("5.11GP").getSP());
        assertEquals(1, new Value("5.11GP").getCP());
        assertEquals(5, new Value("5.11GP").getGP());
        assertEquals(0, new Value("5.11GP").getEP());
    }

    @Test
    public void testMaximizeString() {
        assertEquals(151, new Value("15151cp").getGP());
        assertEquals(5, new Value("15151cp").getSP());
        assertEquals(1, new Value("15151cp").getCP());
    }

    @Test
    public void testAsGP() {
        assertEquals(151.51, new Value("15151cp").getAsGP());
    }
}