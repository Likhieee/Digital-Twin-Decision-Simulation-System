package com.digitaltwin.repository;

import com.digitaltwin.model.DecisionOption;
import com.digitaltwin.model.DecisionScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecisionOptionRepository extends JpaRepository<DecisionOption, Long> {

    List<DecisionOption> findByScenario(DecisionScenario scenario);

    long countByScenario(DecisionScenario scenario);
}
