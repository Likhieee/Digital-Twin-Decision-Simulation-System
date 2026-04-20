package com.digitaltwin.model;
import jakarta.persistence.*;

@Entity
@Table(name = "simulation_results")
public class SimulationResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="run_id",nullable=false) private SimulationRun simulationRun;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="option_id",nullable=false) private DecisionOption option;
    @Column(nullable=false) private double score;
    @Column(name="result_rank",nullable=false) private int rank;
    @Column(nullable=false) private String outcome;
    @Column(columnDefinition="TEXT") private String explanation;
    public SimulationResult() {}
    public String getScoreFormatted(){return String.format("%.1f",score);}
    public String getOutcomeBadgeClass(){if(score>=80)return "success";if(score>=60)return "primary";if(score>=40)return "warning";return "danger";}
    public Long getId(){return id;}
    public SimulationRun getSimulationRun(){return simulationRun;}
    public DecisionOption getOption(){return option;}
    public double getScore(){return score;}
    public int getRank(){return rank;}
    public String getOutcome(){return outcome;}
    public String getExplanation(){return explanation;}
    public void setId(Long v){this.id=v;}
    public void setSimulationRun(SimulationRun v){this.simulationRun=v;}
    public void setOption(DecisionOption v){this.option=v;}
    public void setScore(double v){this.score=v;}
    public void setRank(int v){this.rank=v;}
    public void setOutcome(String v){this.outcome=v;}
    public void setExplanation(String v){this.explanation=v;}
}
