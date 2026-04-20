package com.digitaltwin.controller;
import com.digitaltwin.model.ScenarioStatus;
import com.digitaltwin.model.User;
import com.digitaltwin.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final UserService userService;
    private final ScenarioService scenarioService;
    private final SimulationService simulationService;
    public DashboardController(UserService u, ScenarioService s, SimulationService sim) {
        this.userService=u; this.scenarioService=s; this.simulationService=sim;
    }
    @GetMapping("/") public String root() { return "redirect:/dashboard"; }
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = userService.findByUsername(ud.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("totalScenarios", scenarioService.countByUser(user));
        model.addAttribute("activeScenarios", scenarioService.countByUserAndStatus(user, ScenarioStatus.ACTIVE));
        model.addAttribute("draftScenarios", scenarioService.countByUserAndStatus(user, ScenarioStatus.DRAFT));
        model.addAttribute("totalSimulations", simulationService.countByUser(user));
        model.addAttribute("recentScenarios", scenarioService.findAllByUser(user).stream().limit(5).toList());
        model.addAttribute("recentRuns", simulationService.getRecentRuns(user));
        return "dashboard";
    }
}
