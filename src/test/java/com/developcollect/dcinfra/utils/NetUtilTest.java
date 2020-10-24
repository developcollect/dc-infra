package com.developcollect.dcinfra.utils;

import org.junit.Test;

public class NetUtilTest {

    @Test
    public void getInternetIp() {

        String internetIp = NetUtil.getInternetIp();

        System.out.println(internetIp);
    }
}