package com.developcollect.dcinfra.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.developcollect.dcinfra.utils.DateUtil.*;
import static org.junit.Assert.*;

public class DateUtilTest {


    @Test
    public void test() {
        //        final String s1 = formatDuration(3721, TimeUnit.SECONDS, "HH:mm:ss");
//        final String s2 = formatDuration(3721, TimeUnit.SECONDS, "HHHH:mm:ss");
//        final String s3 = formatDuration(3721, TimeUnit.SECONDS, "H:mm:ss");
//        final String s34 = formatDuration(3721, TimeUnit.SECONDS, "H:m:s");
//        final String s5 = formatDuration(3721, TimeUnit.SECONDS, "H:mm:s");
//        final String s6 = formatDuration(3721, TimeUnit.SECONDS, "H:m:ss");
//        final String s7 = formatDuration(3721, TimeUnit.SECONDS, "m:ss");
//        final String s8 = formatDuration(3721, TimeUnit.SECONDS, "mm:ss");
//        final String s9 = formatDuration(3721, TimeUnit.SECONDS, "mmmm:s");
//
//        System.out.println(1);


        final LocalDateTime localDateTime211 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 1, 12, 12, 9, 21));
        final LocalDateTime localDateTime322 = beginOfMonthLocalDateTime(LocalDateTime.of(2018, 2, 11, 12, 9, 21));
        final LocalDateTime localDateTime373 = beginOfMonthLocalDateTime(LocalDateTime.of(2000, 2, 11, 12, 9, 21));
        final LocalDateTime localDateTime434 = beginOfMonthLocalDateTime(LocalDateTime.of(2020, 3, 19, 12, 9, 21));
        final LocalDateTime localDateTime535 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 4, 21, 12, 9, 21));
        final LocalDateTime localDateTime636 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 5, 30, 12, 9, 21));
        final LocalDateTime localDateTime647 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 6, 30, 12, 9, 21));
        final LocalDateTime localDateTime738 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 7, 31, 12, 9, 21));
        final LocalDateTime localDateTime719 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 8, 26, 12, 9, 21));
        final LocalDateTime localDateTime720 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 9, 23, 12, 9, 21));
        final LocalDateTime localDateTime741 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 10, 27, 12, 9, 21));
        final LocalDateTime localDateTime752 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 11, 21, 12, 9, 21));
        final LocalDateTime localDateTime763 = beginOfMonthLocalDateTime(LocalDateTime.of(2019, 12, 22, 12, 9, 21));

        final LocalDateTime localDateTime21 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 1, 12, 12, 9, 21));
        final LocalDateTime localDateTime32 = endOfMonthLocalDateTime(LocalDateTime.of(2018, 2, 11, 12, 9, 21));
        final LocalDateTime localDateTime37 = endOfMonthLocalDateTime(LocalDateTime.of(2000, 2, 11, 12, 9, 21));
        final LocalDateTime localDateTime43 = endOfMonthLocalDateTime(LocalDateTime.of(2020, 3, 19, 12, 9, 21));
        final LocalDateTime localDateTime53 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 4, 21, 12, 9, 21));
        final LocalDateTime localDateTime63 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 5, 30, 12, 9, 21));
        final LocalDateTime localDateTime64 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 6, 30, 12, 9, 21));
        final LocalDateTime localDateTime73 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 7, 31, 12, 9, 21));
        final LocalDateTime localDateTime71 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 8, 26, 12, 9, 21));
        final LocalDateTime localDateTime72 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 9, 23, 12, 9, 21));
        final LocalDateTime localDateTime74 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 10, 27, 12, 9, 21));
        final LocalDateTime localDateTime75 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 11, 21, 12, 9, 21));
        final LocalDateTime localDateTime76 = endOfMonthLocalDateTime(LocalDateTime.of(2019, 12, 22, 12, 9, 21));


        final LocalDate localDateTime8 = beginOfWeekLocalDate(LocalDate.of(2020, 4, 28));
        final LocalDate localDateTime9 = endOfWeekLocalDate(LocalDate.of(2020, 4, 28));
        final LocalDate localDateTime81 = beginOfWeekLocalDate(LocalDate.of(2020, 9, 28));
        final LocalDate localDateTime92 = endOfWeekLocalDate(LocalDate.of(2020, 9, 28));

        final LocalDateTime localDateTime10 = beginOfYearLocalDateTime(LocalDateTime.of(2020, 4, 28, 0, 0, 0));
        final LocalDateTime localDateTime11 = endOfYearLocalDateTime(LocalDateTime.of(2020, 4, 28, 0, 0, 0));

        System.out.println(1);
    }

}