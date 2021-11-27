package io.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestBoardInitials.class,
        TestPieceMoves.class,
        TestPieces.class,
        TestHandClass.class
})
public class TestSuite {
}
