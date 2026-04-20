package com.digitaltwin.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique=true,nullable=false) private String username;
    @Column(unique=true,nullable=false) private String email;
    @Column(nullable=false) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role = Role.USER;
    @Column(nullable=false) private boolean enabled = true;
    @Column(name="created_at",nullable=false,updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,orphanRemoval=true)
    private List<DecisionScenario> scenarios = new ArrayList<>();
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); }
    public User() {}
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<DecisionScenario> getScenarios() { return scenarios; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String v) { this.username = v; }
    public void setEmail(String v) { this.email = v; }
    public void setPassword(String v) { this.password = v; }
    public void setRole(Role v) { this.role = v; }
    public void setEnabled(boolean v) { this.enabled = v; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final User u = new User();
        public Builder username(String v){u.username=v;return this;}
        public Builder email(String v){u.email=v;return this;}
        public Builder password(String v){u.password=v;return this;}
        public Builder role(Role v){u.role=v;return this;}
        public Builder enabled(boolean v){u.enabled=v;return this;}
        public User build(){return u;}
    }
}
