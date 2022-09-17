package main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainRestController {

    private static final String HELLO_MESSAGE = "Hi, this is the interdependencies graph" +
            "application, in order to help you understand how the climate clock works!";

    @GetMapping("/")
    public String hello(){
        return HELLO_MESSAGE;
    }

    @PostMapping("/createNewGraph")
    public Map<String, String> createNewGraph(){
        return new HashMap<>();
    }

}
