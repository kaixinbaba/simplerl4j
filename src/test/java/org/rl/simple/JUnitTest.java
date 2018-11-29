package org.rl.simple;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

public class JUnitTest {


    @Test
    public void test() {
        System.out.println("hello world");
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtils.nextDouble(0.0, 1.0));

        }
    }
}
