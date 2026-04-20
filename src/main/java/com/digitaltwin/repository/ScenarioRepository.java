package com.digitaltwin.repository;

import com.digitaltwin.model.DecisionScenario;
import com.digitaltwin.model.ScenarioCategory;
import com.digitaltwin.model.ScenarioStatus;
import com.digitaltwin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<DecisionScenario, Long> {

    List<DecisionScenario> findByUserOrderByCreatedAtDesc(User user);

    List<DecisionScenario> findByUserAndStatusOrderByCreatedAtDesc(User user, ScenarioStatus status);

    List<DecisionScenario> findByUserAndCategoryOrderByCreatedAtDesc(User user, ScenarioCategory category);

    @Query("SELECT s FROM DecisionScenario s WHERE s.user = :user AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<DecisionScenario> searchByUserAndKeyword(@Param("user") User user,
                                                   @Param("keyword") String keyword);

    long countByUser(User user);

    long countByUserAndStatus(User user, ScenarioStatus status);
}
