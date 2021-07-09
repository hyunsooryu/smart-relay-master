package com.boot.smartrelay;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TimeSettingTest {
    @Test
    void testUtcZeroTime(){
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");

        Collections.reverse(data);
        data.forEach(System.out::println);
    }



}
