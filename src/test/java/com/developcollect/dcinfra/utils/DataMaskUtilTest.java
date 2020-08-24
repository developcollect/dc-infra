package com.developcollect.dcinfra.utils;

import org.junit.Test;

import static com.developcollect.dcinfra.utils.DataMaskUtil.deEmail;
import static org.junit.Assert.*;


public class DataMaskUtilTest {


    @Test
    public void test1() {
        String s = deEmail("aha哈@qq.com");
        assertEquals("ah**@qq.com", s);
        s = deEmail("aha哈@qq.com.cn");
        assertEquals("ah**@qq.com.cn", s);
        s = deEmail("690712556@qq.com");
        assertEquals("69**@qq.com", s);
        s = deEmail("1370708885@163.cn");
        assertEquals("13**@163.cn", s);
        s = deEmail("1370708885@outlook.cn");
        assertEquals("13**@outlook.cn", s);
        s = deEmail("1@outlook.cn");
        assertEquals("1**@outlook.cn", s);
        s = deEmail("阿蒙@outlook.cn");
        assertEquals("阿蒙**@outlook.cn", s);
        s = deEmail("阿蒙sssss@outlook.cn");
        assertEquals("阿蒙**@outlook.cn", s);
    }

}