package com.example.servingwebcontent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> loop100() throws InterruptedException {
        List<String> fixed = new ArrayList<>();
        Executor executor = Executors.newFixedThreadPool(2);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 500; i++) {
            Thread.sleep(10);
            executor.execute(() -> {
                Random test = new Random();
                String tss = test.doubles(100).mapToObj(GreetingController::test).collect(Collectors.joining(","));
                fixed.add(tss);
                if (fixed.size() > 10000) {
                    fixed.remove(ThreadLocalRandom.current().nextInt(0, fixed.size()));
                }
                stringBuffer.append(tss);
            });
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Loop 100 over"+ stringBuffer);
    }

    private static String test(final Double i) {
        return i.toString();
    }

}
