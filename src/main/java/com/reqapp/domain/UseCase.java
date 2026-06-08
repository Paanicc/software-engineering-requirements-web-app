package com.reqapp.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "use_cases")
public class UseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String preconditions;

    @Column(columnDefinition = "TEXT")
    private String mainFlow;

    @Column(columnDefinition = "TEXT")
    private String postconditions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToMany
    @JoinTable(
        name = "use_case_actors",
        joinColumns = @JoinColumn(name = "use_case_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "use_case_crc_cards",
        joinColumns = @JoinColumn(name = "use_case_id"),
        inverseJoinColumns = @JoinColumn(name = "crc_card_id")
    )
    private List<CrcCard> crcCards = new ArrayList<>();

    @OneToMany(mappedBy = "useCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UseCaseComment> comments = new ArrayList<>();

    public UseCase() {
    }

    public UseCase(String title, String preconditions, String mainFlow, String postconditions, Project project) {
        this.title = title;
        this.preconditions = preconditions;
        this.mainFlow = mainFlow;
        this.postconditions = postconditions;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreconditions() {
        return preconditions;
    }

    public void setPreconditions(String preconditions) {
        this.preconditions = preconditions;
    }

    public String getMainFlow() {
        return mainFlow;
    }

    public void setMainFlow(String mainFlow) {
        this.mainFlow = mainFlow;
    }

    public String getPostconditions() {
        return postconditions;
    }

    public void setPostconditions(String postconditions) {
        this.postconditions = postconditions;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<CrcCard> getCrcCards() {
        return crcCards;
    }

    public void setCrcCards(List<CrcCard> crcCards) {
        this.crcCards = crcCards;
    }

    public List<UseCaseComment> getComments() {
        return comments;
    }

    public void setComments(List<UseCaseComment> comments) {
        this.comments = comments;
    }
}
