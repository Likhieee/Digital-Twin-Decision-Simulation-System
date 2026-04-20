package com.digitaltwin.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "decision_scenarios")
public class DecisionScenario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    @Column(columnDefinition="TEXT") private String description;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private ScenarioCategory category = ScenarioCategory.OTHER;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private ScenarioStatus status = ScenarioStatus.DRAFT;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private User user;
    @OneToMany(mappedBy="scenario",cascade=CascadeType.ALL,orphanRemoval=true) private List<DecisionOption> options = new ArrayList<>();
    @OneToMany(mappedBy="scenario",cascade=CascadeType.ALL,orphanRemoval=true) private List<Priority> priorities = new ArrayList<>();
    @OneToMany(mappedBy="scenario",cascade=CascadeType.ALL,orphanRemoval=true) private List<SimulationRun> simulationRuns = new ArrayList<>();
    @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @PrePersist protected void onCreate(){createdAt=LocalDateTime.now();updatedAt=LocalDateTime.now();}
    @PreUpdate protected void onUpdate(){updatedAt=LocalDateTime.now();}
    public DecisionScenario() {}
    public boolean isReadyToSimulate(){return options.size()>=2&&!priorities.isEmpty();}
    public Long getId(){return id;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public ScenarioCategory getCategory(){return category;}
    public ScenarioStatus getStatus(){return status;}
    public User getUser(){return user;}
    public List<DecisionOption> getOptions(){return options;}
    public List<Priority> getPriorities(){return priorities;}
    public List<SimulationRun> getSimulationRuns(){return simulationRuns;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}
    public void setId(Long v){this.id=v;}
    public void setTitle(String v){this.title=v;}
    public void setDescription(String v){this.description=v;}
    public void setCategory(ScenarioCategory v){this.category=v;}
    public void setStatus(ScenarioStatus v){this.status=v;}
    public void setUser(User v){this.user=v;}
    public void setOptions(List<DecisionOption> v){this.options=v;}
    public void setPriorities(List<Priority> v){this.priorities=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private final DecisionScenario s=new DecisionScenario();
        public Builder title(String v){s.title=v;return this;}
        public Builder description(String v){s.description=v;return this;}
        public Builder category(ScenarioCategory v){s.category=v;return this;}
        public Builder status(ScenarioStatus v){s.status=v;return this;}
        public Builder user(User v){s.user=v;return this;}
        public Builder options(List<DecisionOption> v){s.options=v;return this;}
        public Builder priorities(List<Priority> v){s.priorities=v;return this;}
        public DecisionScenario build(){return s;}
    }
}
