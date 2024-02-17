package com.example.controllers;

import com.example.config.dao.PersonDAO;
import com.example.config.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PersonDAO personDAO;

    public AdminController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String adminPage(Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("people", personDAO.index());

        return "adminPage";
    }

    @PatchMapping("/add")
    public String addAdmin(@ModelAttribute("person") Person person) {
        System.out.println("Person id: " + person.getId());
        return "redirect:/people";
    }
}
