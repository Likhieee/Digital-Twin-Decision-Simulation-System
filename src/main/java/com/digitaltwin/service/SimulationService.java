package com.digitaltwin.service;
import com.digitaltwin.engine.SimulationEngine;
import com.digitaltwin.model.*;
import com.digitaltwin.repository.SimulationRunRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimulationService {
    private final SimulationRunRepository simulationRunRepository;
    private final SimulationEngine simulationEngine;
    private final ScenarioService scenarioService;
    public SimulationService(SimulationRunRepository r, SimulationEngine e, ScenarioService s) {
        this.simulationRunRepository=r; this.simulationEngine=e; this.scenarioService=s;
    }
    @Transactional
    public SimulationRun runSimulation(Long scenarioId, String notes, User user) {
        DecisionScenario scenario = scenarioService.findByIdAndUser(scenarioId, user);
        if (!scenario.isReadyToSimulate()) throw new IllegalStateException("Need at least 2 options and 1 priority.");
        List<SimulationResult> results = simulationEngine.simulate(scenario);
        SimulationRun run = SimulationRun.builder().scenario(scenario).user(user).notes(notes).build();
        for (SimulationResult result : results) { result.setSimulationRun(run); run.getResults().add(result); }
        return simulationRunRepository.save(run);
    }
    public List<SimulationRun> getHistoryByUser(User user) { return simulationRunRepository.findByUserOrderByRunAtDesc(user); }
    public List<SimulationRun> getHistoryByScenario(Long scenarioId, User user) {
        return simulationRunRepository.findByScenarioOrderByRunAtDesc(scenarioService.findByIdAndUser(scenarioId, user));
    }
    public List<SimulationRun> getRecentRuns(User user) { return simulationRunRepository.findTop5ByUserOrderByRunAtDesc(user); }
    public SimulationRun findById(Long id) {
        return simulationRunRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Run not found: "+id));
    }
    public SimulationRun findByIdAndUser(Long runId, User user) {
        SimulationRun run = findById(runId);
        if (!run.getUser().getId().equals(user.getId())) throw new SecurityException("Access denied");
        return run;
    }
    @Transactional
    public void deleteRun(Long runId, User user) { simulationRunRepository.delete(findByIdAndUser(runId, user)); }
    public long countByUser(User user) { return simulationRunRepository.countByUser(user); }
    public String getWeightWarning(Long scenarioId, User user) {
        return simulationEngine.validateWeights(scenarioService.findByIdAndUser(scenarioId, user).getPriorities());
    }
}
