package io.test;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestBoard.class,
        TestField.class,
        TestHand.class,
        TestPiece.class
})
public class ShogiTests {
    @AfterClass
    public static void message() {
        System.out.println("These tests really did came in like a lion.");
    }
}
