package com.example.servingwebcontent;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServingWebContentApplication {

    private static List<String> fixed = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServingWebContentApplication.class, args);
        while (true) {
            //Thread.sleep(5);
            Random test = new Random();
            String tss = test.doubles(100).mapToObj(ServingWebContentApplication::test).collect(Collectors.joining(","));
            fixed.add(tss);
            if (fixed.size() > 100) {
                fixed.remove(ThreadLocalRandom.current().nextInt(0, fixed.size()));
            }
        }
    }

    private static String test(final Double i) {
        return i.toString();
    }

}
