package com.samaBank.controller;

import com.samaBank.model.Account;
import com.samaBank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/create")
    public String createAccountForm() {
        return "create-account";
    }

    @PostMapping("/create")
    public String createAccount(@RequestParam String owner, @RequestParam double balance) {
        accountService.createAccount(owner, balance);
        return "redirect:/home";
    }

    @GetMapping("/view/{id}")
    public String viewAccount(@PathVariable Long id, Model model) {
        Account account = accountService.findAccountById(id);
        model.addAttribute("account", account);
        return "view-account";
    }

    @GetMapping("/transfer")
    public String transferFundsForm() {
        return "transfer-funds";
    }

    @PostMapping("/transfer")
    public String transferFunds(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam double amount) {
        accountService.transferFunds(fromAccountId, toAccountId, amount);
        return "redirect:/home";
    }
    
    @PostMapping("/account/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountService.deleteAccountById(id);
        return "redirect:/accounts"; // Redirect to the accounts page
    }
}