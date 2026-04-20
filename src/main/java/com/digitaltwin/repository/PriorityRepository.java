package com.digitaltwin.repository;

import com.digitaltwin.model.DecisionScenario;
import com.digitaltwin.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findByScenario(DecisionScenario scenario);

    @Query("SELECT SUM(p.weight) FROM Priority p WHERE p.scenario = :scenario")
    Double sumWeightsByScenario(@Param("scenario") DecisionScenario scenario);

    long countByScenario(DecisionScenario scenario);
}
