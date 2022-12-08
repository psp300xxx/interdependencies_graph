package main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import utility.StringConstants;


@RestController
public class MainController {

    @GetMapping("/")
    public String index(){
        return StringConstants.WELCOME_MESSAGE.getValue();
    }



    @GetMapping("/error")
    public String fallbackError(){
        return StringConstants.ERROR_GENERAL_MESSAGE.getValue();
    }


}
