package com.digitaltwin.model;
import jakarta.persistence.*;

@Entity
@Table(name = "priorities")
public class Priority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="attribute_name",nullable=false) private String attributeName;
    @Column(nullable=false) private String label;
    @Column(nullable=false) private double weight;
    @Column(name="higher_is_better",nullable=false) private boolean higherIsBetter = true;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="scenario_id",nullable=false) private DecisionScenario scenario;
    public Priority() {}
    public Long getId(){return id;}
    public String getAttributeName(){return attributeName;}
    public String getLabel(){return label;}
    public double getWeight(){return weight;}
    public boolean isHigherIsBetter(){return higherIsBetter;}
    public DecisionScenario getScenario(){return scenario;}
    public void setId(Long v){this.id=v;}
    public void setAttributeName(String v){this.attributeName=v;}
    public void setLabel(String v){this.label=v;}
    public void setWeight(double v){this.weight=v;}
    public void setHigherIsBetter(boolean v){this.higherIsBetter=v;}
    public void setScenario(DecisionScenario v){this.scenario=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private final Priority p=new Priority();
        public Builder attributeName(String v){p.attributeName=v;return this;}
        public Builder label(String v){p.label=v;return this;}
        public Builder weight(double v){p.weight=v;return this;}
        public Builder higherIsBetter(boolean v){p.higherIsBetter=v;return this;}
        public Builder scenario(DecisionScenario v){p.scenario=v;return this;}
        public Priority build(){return p;}
    }
}
