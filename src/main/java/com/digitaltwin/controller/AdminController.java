package com.digitaltwin.controller;
import com.digitaltwin.model.User;
import com.digitaltwin.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final ScenarioService scenarioService;
    private final SimulationService simulationService;
    public AdminController(UserService u, ScenarioService s, SimulationService sim) {
        this.userService=u; this.scenarioService=s; this.simulationService=sim;
    }
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalUsers",userService.countAllUsers()); model.addAttribute("allUsers",userService.findAll()); return "admin/dashboard";
    }
    @GetMapping("/users")
    public String users(Model model) { model.addAttribute("users",userService.findAll()); return "admin/users"; }

    @PostMapping("/users/{userId}/toggle")
    public String toggle(@PathVariable Long userId, @AuthenticationPrincipal UserDetails ud, RedirectAttributes ra) {
        User admin=userService.findByUsername(ud.getUsername());
        if (admin.getId().equals(userId)) { ra.addFlashAttribute("errorMsg","Cannot disable yourself."); return "redirect:/admin/users"; }
        userService.toggleUserEnabled(userId); ra.addFlashAttribute("successMsg","Updated."); return "redirect:/admin/users";
    }
    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetails ud, RedirectAttributes ra) {
        User admin=userService.findByUsername(ud.getUsername());
        if (admin.getId().equals(userId)) { ra.addFlashAttribute("errorMsg","Cannot delete yourself."); return "redirect:/admin/users"; }
        try { userService.deleteUser(userId); ra.addFlashAttribute("successMsg","Deleted."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/admin/users";
    }
    @PostMapping("/users/create")
    public String createUser(@RequestParam String username, @RequestParam String email,
                             @RequestParam String password, @RequestParam(defaultValue="USER") String role, RedirectAttributes ra) {
        try {
            if ("ADMIN".equalsIgnoreCase(role)) userService.registerAdmin(username,email,password);
            else userService.registerUser(username,email,password);
            ra.addFlashAttribute("successMsg","User created.");
        } catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/admin/users";
    }
}
