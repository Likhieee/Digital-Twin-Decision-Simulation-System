package com.digitaltwin.model;
import jakarta.persistence.*;

@Entity
@Table(name = "option_attributes")
public class OptionAttribute {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private double value;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="option_id",nullable=false) private DecisionOption option;
    public OptionAttribute() {}
    public Long getId(){return id;}
    public String getName(){return name;}
    public double getValue(){return value;}
    public DecisionOption getOption(){return option;}
    public void setId(Long v){this.id=v;}
    public void setName(String v){this.name=v;}
    public void setValue(double v){this.value=v;}
    public void setOption(DecisionOption v){this.option=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder{
        private final OptionAttribute a=new OptionAttribute();
        public Builder name(String v){a.name=v;return this;}
        public Builder value(double v){a.value=v;return this;}
        public Builder option(DecisionOption v){a.option=v;return this;}
        public OptionAttribute build(){return a;}
    }
}
