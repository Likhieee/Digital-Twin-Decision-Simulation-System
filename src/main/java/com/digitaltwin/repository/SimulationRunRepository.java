package com.digitaltwin.repository;

import com.digitaltwin.model.DecisionScenario;
import com.digitaltwin.model.SimulationRun;
import com.digitaltwin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRunRepository extends JpaRepository<SimulationRun, Long> {

    List<SimulationRun> findByUserOrderByRunAtDesc(User user);

    List<SimulationRun> findByScenarioOrderByRunAtDesc(DecisionScenario scenario);

    List<SimulationRun> findTop5ByUserOrderByRunAtDesc(User user);

    long countByUser(User user);

    long countByScenario(DecisionScenario scenario);
}
