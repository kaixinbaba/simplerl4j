package org.rl.simple;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.rl.simple.utils.JsonSerilizable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JUnitTest {


    @Test
    public void test() {
        System.out.println("hello world");
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtils.nextInt(0, 2));

        }
    }
    @Test
    public void testToFile() throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        map.put("l", list);
        map.put("2", list);
        map.put("3", "dfdf");
        map.put("4", "dfdkdf");
        String result = JsonSerilizable.serilizableForMap(map, "test.json");
        System.out.println(result);
    }
    @Test
    public void testFromFile() throws IOException {
        Map<String, Object> stringObjectHashMap = JsonSerilizable.deserilizableForMapFromFile("test.json");
        System.out.println(stringObjectHashMap);
    }
}
