package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FirstController {

    @GetMapping("/hello")
    public String sayHello(HttpServletRequest request) {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        System.out.println(name + " " + age);
        return "/first/hello";
    }

    @GetMapping("/goodbye")
    public String sayGoodbye(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "age", required = false) int age,
                             Model model
    ) {
        model.addAttribute("message", name + " " + age);
        return "/first/goodbye";
    }

    @GetMapping("/first/calculator")
    public String calculate(
            @RequestParam("a") int a,
            @RequestParam("b") int b,
            @RequestParam("action") String action,
            Model model) {
        int result;
        switch (action) {
            case "mult":
                result = a * b;
                break;
            case "add":
                result = a + b;
                break;
            case "sub":
                result = a - b;
                break;
            case "div":
                result = a / b;
                break;
            default:
                result = -1;
        }
        model.addAttribute("result", result);

        return "/first/calculate";
    }

}
