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
        System.out.println(Math.pow(3, 2.2));
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
    @Test
    public void test3() {
//        System.out.println("hello world");
        String s = "finance-product-prod.properties";
        System.out.println(getSourceFileName(s));
    }

    public static String getSourceFileName(String withEnvFileName) {
        return withEnvFileName.replaceAll("-(dev|staging|prod)\\.properties", ".properties");
    }

    /**
     *
     * @param sourceFileName like xxx-trade.properties
     * @return xxx-trade-{env}.properties
     */
    public static String getFileNameWithEnv(String sourceFileName, String env) {
        return sourceFileName.replaceAll("\\.", String.format("-%s.", env));
    }



    @Test
    public void test2() {
        System.out.println("dkjfk");

    }

















}
