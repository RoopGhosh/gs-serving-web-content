package com.example.servingwebcontent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/loop100")
    public void loop100() {
        List<String> fixed = new ArrayList<>();
        Executor executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 200; i++) {
            //Thread.sleep(5);
            executor.execute(() -> {
                Random test = new Random();
                String tss = test.doubles(100).mapToObj(GreetingController::test).collect(Collectors.joining(","));
                fixed.add(tss);
                if (fixed.size() > 10000) {
                    fixed.remove(ThreadLocalRandom.current().nextInt(0, fixed.size()));
                }
            });
        }
    }

    private static String test(final Double i) {
        return i.toString();
    }

}
