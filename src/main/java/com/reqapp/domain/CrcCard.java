package com.reqapp.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crc_cards")
public class CrcCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String className;

    @Column(length = 2000)
    private String responsibilities;

    @Column(length = 2000)
    private String collaborations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "crcCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrcCardComment> comments = new ArrayList<>();

    public CrcCard() {
    }

    public CrcCard(String className, String responsibilities, String collaborations, Project project) {
        this.className = className;
        this.responsibilities = responsibilities;
        this.collaborations = collaborations;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getCollaborations() {
        return collaborations;
    }

    public void setCollaborations(String collaborations) {
        this.collaborations = collaborations;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<CrcCardComment> getComments() {
        return comments;
    }

    public void setComments(List<CrcCardComment> comments) {
        this.comments = comments;
    }
}
