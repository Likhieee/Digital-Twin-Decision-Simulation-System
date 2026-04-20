package com.digitaltwin.controller;
import com.digitaltwin.model.User;
import com.digitaltwin.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/simulations")
public class SimulationController {
    private final SimulationService simulationService;
    private final ScenarioService scenarioService;
    private final UserService userService;
    public SimulationController(SimulationService sim, ScenarioService sc, UserService u) {
        this.simulationService=sim; this.scenarioService=sc; this.userService=u;
    }
    private User cu(UserDetails ud) { return userService.findByUsername(ud.getUsername()); }

    @GetMapping("/run/{scenarioId}")
    public String runForm(@PathVariable Long scenarioId, @AuthenticationPrincipal UserDetails ud, Model model) {
        User user=cu(ud); var scenario=scenarioService.findByIdAndUser(scenarioId,user);
        model.addAttribute("scenario",scenario);
        model.addAttribute("weightWarning",simulationService.getWeightWarning(scenarioId,user));
        model.addAttribute("canSimulate",scenario.isReadyToSimulate());
        return "simulation/run";
    }
    @PostMapping("/run/{scenarioId}")
    public String execute(@PathVariable Long scenarioId, @AuthenticationPrincipal UserDetails ud,
                          @RequestParam(required=false) String notes, RedirectAttributes ra) {
        try { var run=simulationService.runSimulation(scenarioId,notes,cu(ud));
              ra.addFlashAttribute("successMsg","Simulation complete!"); return "redirect:/simulations/results/"+run.getId(); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); return "redirect:/simulations/run/"+scenarioId; }
    }
    @GetMapping("/results/{runId}")
    public String results(@PathVariable Long runId, @AuthenticationPrincipal UserDetails ud, Model model) {
        var run=simulationService.findByIdAndUser(runId,cu(ud));
        model.addAttribute("run",run); model.addAttribute("scenario",run.getScenario());
        model.addAttribute("results",run.getResults()); model.addAttribute("topResult",run.getTopResult());
        return "simulation/results";
    }
    @GetMapping("/history")
    public String history(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user=cu(ud); model.addAttribute("history",simulationService.getHistoryByUser(user)); return "simulation/history";
    }
    @GetMapping("/history/scenario/{scenarioId}")
    public String scenarioHistory(@PathVariable Long scenarioId, @AuthenticationPrincipal UserDetails ud, Model model) {
        User user=cu(ud);
        model.addAttribute("history",simulationService.getHistoryByScenario(scenarioId,user));
        model.addAttribute("scenario",scenarioService.findByIdAndUser(scenarioId,user));
        return "simulation/history";
    }
    @PostMapping("/delete/{runId}")
    public String deleteRun(@PathVariable Long runId, @AuthenticationPrincipal UserDetails ud, RedirectAttributes ra) {
        try { simulationService.deleteRun(runId,cu(ud)); ra.addFlashAttribute("successMsg","Deleted."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/simulations/history";
    }
}
