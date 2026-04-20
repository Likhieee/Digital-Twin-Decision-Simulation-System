package com.digitaltwin.controller;
import com.digitaltwin.model.*;
import com.digitaltwin.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/scenarios")
public class ScenarioController {
    private final ScenarioService scenarioService;
    private final UserService userService;
    public ScenarioController(ScenarioService s, UserService u) { this.scenarioService=s; this.userService=u; }
    private User cu(UserDetails ud) { return userService.findByUsername(ud.getUsername()); }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails ud, @RequestParam(required=false) String status,
                       @RequestParam(required=false) String q, Model model) {
        User user = cu(ud);
        var scenarios = (q!=null&&!q.isBlank()) ? scenarioService.searchByKeyword(user,q)
            : (status!=null&&!status.isBlank()) ? scenarioService.findByUserAndStatus(user,ScenarioStatus.valueOf(status.toUpperCase()))
            : scenarioService.findAllByUser(user);
        model.addAttribute("scenarios",scenarios); model.addAttribute("categories",ScenarioCategory.values());
        model.addAttribute("statusFilter",status); model.addAttribute("query",q);
        return "scenario/list";
    }
    @GetMapping("/create")
    public String createForm(Model model) { model.addAttribute("categories",ScenarioCategory.values()); return "scenario/create"; }

    @PostMapping("/create")
    public String create(@AuthenticationPrincipal UserDetails ud, @RequestParam String title,
                         @RequestParam(required=false) String description, @RequestParam String category, RedirectAttributes ra) {
        try {
            DecisionScenario s = scenarioService.createScenario(title,description,ScenarioCategory.valueOf(category),cu(ud));
            ra.addFlashAttribute("successMsg","Scenario created!"); return "redirect:/scenarios/"+s.getId();
        } catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); return "redirect:/scenarios/create"; }
    }
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud, Model model) {
        User user = cu(ud); DecisionScenario s = scenarioService.findByIdAndUser(id,user);
        Double ws = scenarioService.getTotalPriorityWeight(s);
        model.addAttribute("scenario",s); model.addAttribute("weightSum",ws!=null?String.format("%.2f",ws):"0.00");
        model.addAttribute("weightsValid",ws!=null&&Math.abs(ws-1.0)<=0.01);
        model.addAttribute("statuses",ScenarioStatus.values()); model.addAttribute("categories",ScenarioCategory.values());
        return "scenario/detail";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud, @RequestParam String title,
                         @RequestParam(required=false) String description, @RequestParam String category,
                         @RequestParam String status, RedirectAttributes ra) {
        try { scenarioService.updateScenario(id,title,description,ScenarioCategory.valueOf(category),ScenarioStatus.valueOf(status),cu(ud));
              ra.addFlashAttribute("successMsg","Updated."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios/"+id;
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud, RedirectAttributes ra) {
        try { scenarioService.deleteScenario(id,cu(ud)); ra.addFlashAttribute("successMsg","Deleted."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios";
    }
    @PostMapping("/{id}/clone")
    public String clone(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud, RedirectAttributes ra) {
        try { DecisionScenario c=scenarioService.cloneScenario(id,cu(ud)); ra.addFlashAttribute("successMsg","Cloned!"); return "redirect:/scenarios/"+c.getId(); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); return "redirect:/scenarios/"+id; }
    }
    @PostMapping("/{id}/options/add")
    public String addOption(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud,
                            @RequestParam String name, @RequestParam(required=false) String description, RedirectAttributes ra) {
        try { scenarioService.addOption(id,name,description,cu(ud)); ra.addFlashAttribute("successMsg","Option added."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios/"+id;
    }
    @PostMapping("/options/{optionId}/delete")
    public String deleteOption(@PathVariable Long optionId, @AuthenticationPrincipal UserDetails ud,
                               @RequestParam Long scenarioId, RedirectAttributes ra) {
        try { scenarioService.deleteOption(optionId,cu(ud)); ra.addFlashAttribute("successMsg","Option deleted."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios/"+scenarioId;
    }
    @PostMapping("/options/{optionId}/attributes/add")
    public String addAttr(@PathVariable Long optionId, @RequestParam String attrName,
                          @RequestParam double attrValue, @RequestParam Long scenarioId, RedirectAttributes ra) {
        try { scenarioService.addAttributeToOption(optionId,attrName,attrValue); ra.addFlashAttribute("successMsg","Attribute added."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios/"+scenarioId;
    }
    @PostMapping("/{id}/priorities/add")
    public String addPriority(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud,
                              @RequestParam String attributeName, @RequestParam String label,
                              @RequestParam double weight, @RequestParam(defaultValue="true") boolean higherIsBetter, RedirectAttributes ra) {
        try { scenarioService.addPriority(id,attributeName,label,weight,higherIsBetter,cu(ud)); ra.addFlashAttribute("successMsg","Priority added."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios/"+id;
    }
    @PostMapping("/priorities/{priorityId}/delete")
    public String deletePriority(@PathVariable Long priorityId, @AuthenticationPrincipal UserDetails ud,
                                 @RequestParam Long scenarioId, RedirectAttributes ra) {
        try { scenarioService.deletePriority(priorityId,cu(ud)); ra.addFlashAttribute("successMsg","Priority deleted."); }
        catch (Exception e) { ra.addFlashAttribute("errorMsg",e.getMessage()); }
        return "redirect:/scenarios/"+scenarioId;
    }
}
