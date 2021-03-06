package com.example.mysnapchat;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    MainActivity mainActivity = new MainActivity();

    @Test
    public void checkTest_isWorking(){
        assertEquals(1, mainActivity.checkTests('a'));
        assertNotEquals(1, mainActivity.checkTests('b'));
        assertEquals(0, mainActivity.checkTests('x'));
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isEven_isCorrect(){
        assertTrue(isEven(4));
        assertFalse(isEven(55));
    }

    public boolean isEven(int input){
        if (input % 2 != 0){
            return false;
        }
        return true;
    }
}
