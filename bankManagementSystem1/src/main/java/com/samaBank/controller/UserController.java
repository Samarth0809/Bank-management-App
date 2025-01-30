package com.samaBank.controller;

import com.samaBank.model.Account;
import com.samaBank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<Account> accounts = accountService.findAllAccounts();
        model.addAttribute("accounts", accounts); // Ensure the attribute name matches the template
        return "users"; // Ensure this matches the Thymeleaf template name
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        accountService.deleteAccountById(id);
        return "redirect:/users";
    }
}