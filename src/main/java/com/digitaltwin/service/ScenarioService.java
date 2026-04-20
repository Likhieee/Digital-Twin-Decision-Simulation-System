package com.digitaltwin.service;
import com.digitaltwin.model.*;
import com.digitaltwin.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScenarioService {
    private final ScenarioRepository scenarioRepository;
    private final DecisionOptionRepository optionRepository;
    private final PriorityRepository priorityRepository;
    public ScenarioService(ScenarioRepository s, DecisionOptionRepository o, PriorityRepository p) {
        this.scenarioRepository=s; this.optionRepository=o; this.priorityRepository=p;
    }
    public List<DecisionScenario> findAllByUser(User user) { return scenarioRepository.findByUserOrderByCreatedAtDesc(user); }
    public List<DecisionScenario> findByUserAndStatus(User user, ScenarioStatus status) { return scenarioRepository.findByUserAndStatusOrderByCreatedAtDesc(user,status); }
    public List<DecisionScenario> searchByKeyword(User user, String keyword) { return scenarioRepository.searchByUserAndKeyword(user,keyword); }
    public DecisionScenario findById(Long id) {
        return scenarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Scenario not found: "+id));
    }
    public DecisionScenario findByIdAndUser(Long id, User user) {
        DecisionScenario s = findById(id);
        if (!s.getUser().getId().equals(user.getId())) throw new SecurityException("Access denied");
        return s;
    }
    @Transactional
    public DecisionScenario createScenario(String title, String description, ScenarioCategory category, User user) {
        DecisionScenario s = DecisionScenario.builder().title(title).description(description)
            .category(category).status(ScenarioStatus.DRAFT).user(user).build();
        return scenarioRepository.save(s);
    }
    @Transactional
    public DecisionScenario updateScenario(Long id, String title, String description, ScenarioCategory category, ScenarioStatus status, User user) {
        DecisionScenario s = findByIdAndUser(id, user);
        s.setTitle(title); s.setDescription(description); s.setCategory(category); s.setStatus(status);
        return scenarioRepository.save(s);
    }
    @Transactional
    public void deleteScenario(Long id, User user) { scenarioRepository.delete(findByIdAndUser(id, user)); }
    @Transactional
    public DecisionScenario cloneScenario(Long originalId, User user) {
        DecisionScenario orig = findByIdAndUser(originalId, user);
        DecisionScenario clone = DecisionScenario.builder().title("Copy of "+orig.getTitle())
            .description(orig.getDescription()).category(orig.getCategory())
            .status(ScenarioStatus.DRAFT).user(user).options(new ArrayList<>()).priorities(new ArrayList<>()).build();
        clone = scenarioRepository.save(clone);
        for (DecisionOption origOpt : orig.getOptions()) {
            DecisionOption clonedOpt = DecisionOption.builder().name(origOpt.getName())
                .description(origOpt.getDescription()).scenario(clone).attributes(new ArrayList<>()).build();
            clonedOpt = optionRepository.save(clonedOpt);
            for (OptionAttribute attr : origOpt.getAttributes()) {
                clonedOpt.getAttributes().add(OptionAttribute.builder().name(attr.getName()).value(attr.getValue()).option(clonedOpt).build());
            }
            optionRepository.save(clonedOpt);
            clone.getOptions().add(clonedOpt);
        }
        for (Priority p : orig.getPriorities()) {
            clone.getPriorities().add(priorityRepository.save(Priority.builder()
                .attributeName(p.getAttributeName()).label(p.getLabel()).weight(p.getWeight())
                .higherIsBetter(p.isHigherIsBetter()).scenario(clone).build()));
        }
        return scenarioRepository.save(clone);
    }
    @Transactional
    public DecisionOption addOption(Long scenarioId, String name, String description, User user) {
        DecisionScenario s = findByIdAndUser(scenarioId, user);
        return optionRepository.save(DecisionOption.builder().name(name).description(description).scenario(s).attributes(new ArrayList<>()).build());
    }
    @Transactional
    public void addAttributeToOption(Long optionId, String attrName, double value) {
        DecisionOption opt = optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        opt.getAttributes().add(OptionAttribute.builder().name(attrName).value(value).option(opt).build());
        optionRepository.save(opt);
    }
    @Transactional
    public void deleteOption(Long optionId, User user) {
        DecisionOption opt = optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        if (!opt.getScenario().getUser().getId().equals(user.getId())) throw new SecurityException("Access denied");
        optionRepository.delete(opt);
    }
    @Transactional
    public Priority addPriority(Long scenarioId, String attributeName, String label, double weight, boolean higherIsBetter, User user) {
        DecisionScenario s = findByIdAndUser(scenarioId, user);
        return priorityRepository.save(Priority.builder().attributeName(attributeName).label(label).weight(weight).higherIsBetter(higherIsBetter).scenario(s).build());
    }
    @Transactional
    public void deletePriority(Long priorityId, User user) {
        Priority p = priorityRepository.findById(priorityId).orElseThrow(() -> new IllegalArgumentException("Priority not found"));
        if (!p.getScenario().getUser().getId().equals(user.getId())) throw new SecurityException("Access denied");
        priorityRepository.delete(p);
    }
    public long countByUser(User user) { return scenarioRepository.countByUser(user); }
    public long countByUserAndStatus(User user, ScenarioStatus status) { return scenarioRepository.countByUserAndStatus(user,status); }
    public Double getTotalPriorityWeight(DecisionScenario scenario) { return priorityRepository.sumWeightsByScenario(scenario); }
}
