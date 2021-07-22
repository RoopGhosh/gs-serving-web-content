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

    private List<String> fixed = new ArrayList<>();
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/loop100")
    public ResponseEntity<String> loop100() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(2);
        StringBuffer stringBuffer = new StringBuffer();
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();

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
            long freeMem = runtime.freeMemory();
            if (freeMem / (1024 * 1024) < 300) {
                while (freeMem / (1024 * 1024) < 300) {
                    freeMem = runtime.freeMemory();
                    runtime.gc();
                    fixed.removeAll(fixed.subList(0, 1000));
                }
            }

        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Loop 100 over. array size is "+ fixed.size());
    }

    private static String test(final Double i) {
        return i.toString();
    }

}
