package com.digitaltwin.controller;
import com.digitaltwin.model.User;
import com.digitaltwin.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;
    public ReportController(ReportService r, UserService u) { this.reportService=r; this.userService=u; }
    private User cu(UserDetails ud) { return userService.findByUsername(ud.getUsername()); }

    @GetMapping("/{runId}")
    public String view(@PathVariable Long runId, @AuthenticationPrincipal UserDetails ud, Model model) {
        model.addAttribute("report",reportService.buildReport(runId,cu(ud))); model.addAttribute("runId",runId); return "report/view";
    }
    @GetMapping("/history")
    public String history(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user=cu(ud); model.addAttribute("summaries",reportService.buildHistorySummary(user)); return "report/history";
    }
}
