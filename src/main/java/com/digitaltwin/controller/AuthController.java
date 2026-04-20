package com.digitaltwin.controller;
import com.digitaltwin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required=false) String error, @RequestParam(required=false) String logout,
                            @RequestParam(required=false) String expired, Model model) {
        if (error != null) model.addAttribute("errorMsg","Invalid username or password.");
        if (logout != null) model.addAttribute("logoutMsg","Logged out successfully.");
        if (expired != null) model.addAttribute("expiredMsg","Session expired.");
        return "auth/login";
    }
    @GetMapping("/register")
    public String registerPage() { return "auth/register"; }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username, @RequestParam String email,
                                  @RequestParam String password, @RequestParam String confirmPassword,
                                  RedirectAttributes ra) {
        if (!password.equals(confirmPassword)) { ra.addFlashAttribute("errorMsg","Passwords do not match."); return "redirect:/auth/register"; }
        if (password.length() < 6) { ra.addFlashAttribute("errorMsg","Password must be at least 6 characters."); return "redirect:/auth/register"; }
        try {
            userService.registerUser(username, email, password);
            ra.addFlashAttribute("successMsg","Account created! Please log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) { ra.addFlashAttribute("errorMsg", e.getMessage()); return "redirect:/auth/register"; }
    }
}
