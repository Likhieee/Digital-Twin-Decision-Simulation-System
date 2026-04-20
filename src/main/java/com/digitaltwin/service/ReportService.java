package com.digitaltwin.service;
import com.digitaltwin.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ReportService {
    private final SimulationService simulationService;
    public ReportService(SimulationService s) { this.simulationService = s; }

    public Map<String, Object> buildReport(Long runId, User user) {
        SimulationRun run = simulationService.findByIdAndUser(runId, user);
        DecisionScenario scenario = run.getScenario();
        List<SimulationResult> results = run.getResults();
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("runId", run.getId());
        report.put("runAt", run.getRunAt());
        report.put("scenarioTitle", scenario.getTitle());
        report.put("category", scenario.getCategory().name());
        report.put("description", scenario.getDescription());
        report.put("notes", run.getNotes());
        SimulationResult top = run.getTopResult();
        report.put("recommendation", top != null ? top.getOption().getName() : "N/A");
        report.put("topScore", top != null ? top.getScore() : 0.0);
        List<Map<String, Object>> rankedRows = new ArrayList<>();
        for (SimulationResult r : results) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("rank", r.getRank());
            row.put("optionName", r.getOption().getName());
            row.put("score", r.getScore());
            row.put("scoreFormatted", r.getScoreFormatted());
            row.put("outcome", r.getOutcome());
            row.put("badgeClass", r.getOutcomeBadgeClass());
            row.put("explanation", r.getExplanation());
            rankedRows.add(row);
        }
        report.put("rankedResults", rankedRows);
        List<Map<String, Object>> priorityRows = new ArrayList<>();
        for (Priority p : scenario.getPriorities()) {
            Map<String, Object> pRow = new LinkedHashMap<>();
            pRow.put("label", p.getLabel());
            pRow.put("weight", p.getWeight());
            pRow.put("weightPercent", Math.round(p.getWeight() * 100));
            pRow.put("direction", p.isHigherIsBetter() ? "Higher is better" : "Lower is better");
            priorityRows.add(pRow);
        }
        report.put("priorities", priorityRows);
        List<String> attributeNames = new ArrayList<>();
        for (Priority p : scenario.getPriorities()) attributeNames.add(p.getAttributeName());
        List<Map<String, Object>> matrix = new ArrayList<>();
        for (DecisionOption option : scenario.getOptions()) {
            Map<String, Object> optRow = new LinkedHashMap<>();
            optRow.put("optionName", option.getName());
            for (String attr : attributeNames) optRow.put(attr, option.getAttributeValue(attr));
            matrix.add(optRow);
        }
        report.put("attributeMatrix", matrix);
        report.put("attributeNames", attributeNames);
        if (results.size() >= 2) report.put("marginOverSecond", String.format("%.1f", results.get(0).getScore() - results.get(1).getScore()));
        return report;
    }

    public List<Map<String, Object>> buildHistorySummary(User user) {
        List<SimulationRun> runs = simulationService.getHistoryByUser(user);
        List<Map<String, Object>> summaries = new ArrayList<>();
        for (SimulationRun run : runs) {
            Map<String, Object> s = new LinkedHashMap<>();
            s.put("runId", run.getId());
            s.put("scenarioTitle", run.getScenario().getTitle());
            s.put("category", run.getScenario().getCategory().name());
            s.put("runAt", run.getRunAt());
            SimulationResult top = run.getTopResult();
            s.put("recommendation", top != null ? top.getOption().getName() : "N/A");
            s.put("topScore", top != null ? top.getScore() : 0.0);
            s.put("optionCount", run.getResults().size());
            summaries.add(s);
        }
        return summaries;
    }
}
