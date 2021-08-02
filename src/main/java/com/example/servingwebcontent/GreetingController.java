package com.example.servingwebcontent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
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
    public ResponseEntity<String> loop100(@RequestParam(name = "loopCount", required = false, defaultValue = "500") String loopCount,
                                          @RequestParam(name = "threads", required = false, defaultValue = "2") String threads,
                                          @RequestParam(name = "sleepTime", required = false, defaultValue = "20") String sleepTime,
                                          @RequestParam(name = "arraySize", required = false, defaultValue = "1000") String arraySize)
            throws InterruptedException{
        Executor executor = Executors.newFixedThreadPool(Integer.parseInt(threads));
        StringBuffer stringBuffer = new StringBuffer();
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();

        for (int i = 0; i < Integer.parseInt(loopCount); i++) {
            Thread.sleep(Integer.parseInt(sleepTime));
            executor.execute(() -> {
                String tss = new Random().doubles(100)
                        .mapToObj(GreetingController::test).collect(Collectors.joining(","));
                fixed.add(tss);
                if (fixed.size() > Integer.parseInt(arraySize)) {
                    fixed.remove(ThreadLocalRandom.current().nextInt(0, fixed.size()));
                }
                stringBuffer.append(tss);
            });
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Loop 100 over. array size is "+ fixed.size());
    }

    private static String test(final Double i) {
        return i.toString();
    }

}
