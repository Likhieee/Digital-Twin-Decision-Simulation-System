package com.digitaltwin.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulation_runs")
public class SimulationRun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="scenario_id",nullable=false) private DecisionScenario scenario;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private User user;
    @Column(name="run_at",nullable=false) private LocalDateTime runAt;
    @Column(columnDefinition="TEXT") private String notes;
    @OneToMany(mappedBy="simulationRun",cascade=CascadeType.ALL,orphanRemoval=true)
    @OrderBy("rank ASC") private List<SimulationResult> results = new ArrayList<>();
    @PrePersist protected void onCreate(){this.runAt=LocalDateTime.now();}
    public SimulationRun() {}
    public SimulationResult getTopResult(){return results.stream().filter(r->r.getRank()==1).findFirst().orElse(null);}
    public Long getId(){return id;}
    public DecisionScenario getScenario(){return scenario;}
    public User getUser(){return user;}
    public LocalDateTime getRunAt(){return runAt;}
    public String getNotes(){return notes;}
    public List<SimulationResult> getResults(){return results;}
    public void setId(Long v){this.id=v;}
    public void setScenario(DecisionScenario v){this.scenario=v;}
    public void setUser(User v){this.user=v;}
    public void setRunAt(LocalDateTime v){this.runAt=v;}
    public void setNotes(String v){this.notes=v;}
    public void setResults(List<SimulationResult> v){this.results=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private final SimulationRun r=new SimulationRun();
        public Builder scenario(DecisionScenario v){r.scenario=v;return this;}
        public Builder user(User v){r.user=v;return this;}
        public Builder notes(String v){r.notes=v;return this;}
        public SimulationRun build(){return r;}
    }
}
