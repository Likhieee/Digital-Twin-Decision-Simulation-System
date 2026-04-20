package com.digitaltwin.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "decision_options")
public class DecisionOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(columnDefinition="TEXT") private String description;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="scenario_id",nullable=false) private DecisionScenario scenario;
    @OneToMany(mappedBy="option",cascade=CascadeType.ALL,orphanRemoval=true) private List<OptionAttribute> attributes = new ArrayList<>();
    public DecisionOption() {}
    public double getAttributeValue(String attributeName){
        return attributes.stream().filter(a->a.getName().equalsIgnoreCase(attributeName))
            .mapToDouble(OptionAttribute::getValue).findFirst().orElse(0.0);
    }
    public Long getId(){return id;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public DecisionScenario getScenario(){return scenario;}
    public List<OptionAttribute> getAttributes(){return attributes;}
    public void setId(Long v){this.id=v;}
    public void setName(String v){this.name=v;}
    public void setDescription(String v){this.description=v;}
    public void setScenario(DecisionScenario v){this.scenario=v;}
    public void setAttributes(List<OptionAttribute> v){this.attributes=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private final DecisionOption o=new DecisionOption();
        public Builder name(String v){o.name=v;return this;}
        public Builder description(String v){o.description=v;return this;}
        public Builder scenario(DecisionScenario v){o.scenario=v;return this;}
        public Builder attributes(List<OptionAttribute> v){o.attributes=v;return this;}
        public DecisionOption build(){return o;}
    }
}
