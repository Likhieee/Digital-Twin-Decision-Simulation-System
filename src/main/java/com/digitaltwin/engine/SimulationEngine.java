package com.digitaltwin.engine;

import com.digitaltwin.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * SimulationEngine implements the Weighted Sum Model (WSM) to score
 * decision options based on user-defined priorities and option attributes.
 *
 * Algorithm:
 * 1. For each priority attribute, find min & max values across all options.
 * 2. For each option, normalize each attribute value to [0, 1].
 * 3. Invert normalized value if lowerIsBetter (higherIsBetter = false).
 * 4. Multiply normalized value by priority weight and sum up → raw score [0,1].
 * 5. Convert to final score out of 100.
 * 6. Rank options by descending score.
 */
@Component
public class SimulationEngine {

    /**
     * Runs the simulation for a given scenario.
     *
     * @param scenario The scenario with options and priorities fully loaded
     * @return Ordered list of SimulationResult (rank 1 = best)
     */
    public List<SimulationResult> simulate(DecisionScenario scenario) {
        List<DecisionOption> options = scenario.getOptions();
        List<Priority> priorities = scenario.getPriorities();

        if (options == null || options.size() < 2) {
            throw new IllegalArgumentException(
                "Simulation requires at least 2 options. Found: " +
                (options == null ? 0 : options.size()));
        }
        if (priorities == null || priorities.isEmpty()) {
            throw new IllegalArgumentException(
                "Simulation requires at least 1 priority/criterion.");
        }

        // Step 1: Find min and max for each priority attribute
        Map<String, Double> minValues = new HashMap<>();
        Map<String, Double> maxValues = new HashMap<>();

        for (Priority priority : priorities) {
            String attr = priority.getAttributeName();
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;

            for (DecisionOption option : options) {
                double val = option.getAttributeValue(attr);
                if (val < min) min = val;
                if (val > max) max = val;
            }
            minValues.put(attr, min);
            maxValues.put(attr, max);
        }

        // Step 2 & 3: Score each option
        List<SimulationResult> results = new ArrayList<>();

        for (DecisionOption option : options) {
            double totalScore = 0.0;
            StringBuilder explanation = new StringBuilder();

            for (Priority priority : priorities) {
                String attr = priority.getAttributeName();
                double val    = option.getAttributeValue(attr);
                double min    = minValues.get(attr);
                double max    = maxValues.get(attr);

                double normalized;
                if (max == min) {
                    // All options have same value → neutral
                    normalized = 0.5;
                } else {
                    normalized = (val - min) / (max - min);
                }

                // Invert if lower is better (e.g., cost, commute time)
                if (!priority.isHigherIsBetter()) {
                    normalized = 1.0 - normalized;
                }

                double weightedScore = normalized * priority.getWeight();
                totalScore += weightedScore;

                explanation.append(String.format("  • %s: %.2f → normalized %.2f × weight %.2f = %.3f%n",
                    priority.getLabel(), val, normalized, priority.getWeight(), weightedScore));
            }

            double finalScore = totalScore * 100.0;
            String outcome    = resolveOutcome(finalScore);

            SimulationResult result = new SimulationResult();
            result.setOption(option);
            result.setScore(finalScore);
            result.setOutcome(outcome);
            result.setExplanation(explanation.toString().trim());
            results.add(result);
        }

        // Step 4: Sort by descending score and assign ranks
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRank(i + 1);
        }

        return results;
    }

    /**
     * Validates that all priority weights sum to approximately 1.0.
     * Returns a warning message if not, or null if valid.
     */
    public String validateWeights(List<Priority> priorities) {
        if (priorities == null || priorities.isEmpty()) return "No priorities defined.";
        double sum = priorities.stream().mapToDouble(Priority::getWeight).sum();
        if (Math.abs(sum - 1.0) > 0.01) {
            return String.format("Priority weights sum to %.2f. They should sum to 1.0 for accurate results.", sum);
        }
        return null;
    }

    /**
     * Maps a numeric score [0–100] to a descriptive outcome label.
     */
    private String resolveOutcome(double score) {
        if (score >= 80) return "Highly Recommended";
        if (score >= 60) return "Recommended";
        if (score >= 40) return "Neutral";
        if (score >= 20) return "Not Recommended";
        return "Strongly Discouraged";
    }
}
