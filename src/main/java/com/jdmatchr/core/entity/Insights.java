// src/main/java/com/jdmatchr/core/entity/Insights.java
package com.jdmatchr.core.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode; // For JSONB
import org.hibernate.type.SqlTypes; // For JSONB

import java.time.OffsetDateTime;
import java.util.Map; // Assuming analysis_result might be a flexible JSON object
import java.util.UUID;

// If using Lombok:
// import lombok.Getter;
// import lombok.Setter;
// import lombok.NoArgsConstructor;

@Entity
@Table(name = "insights") // Table name will be 'insights'
// @Getter
// @Setter
// @NoArgsConstructor
public class Insights {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Foreign key to the User who performed the analysis

    @Column(name = "job_title", length = 255, nullable = false)
    private String jobTitle;

    // This field might be a brief summary, while the detailed one is in analysisResult.jdSummary
    @Column(name = "job_description_summary", columnDefinition = "TEXT")
    private String jobDescriptionSummary;

    @Column(name = "resume_filename", length = 255)
    private String resumeFilename;

    @Column(name = "match_score") // Using NUMERIC(5, 2) in SQL schema, or just let Hibernate decide
    private Double matchScore; // Use Double for scores like 85.50, DTO will use Integer

    @Column(name = "ats_score") // New field
    private Integer atsScore;

    /**
     * Stores the detailed analysis results as a JSONB object in PostgreSQL.
     * Using Map<String, Object> for flexibility with Jackson JSON serialization.
     * Hibernate needs a hint for JSONB types.
     */
    @JdbcTypeCode(SqlTypes.JSON) // Standard JPA 3.0+ / Hibernate 6+ way for JSON/JSONB
    @Column(name = "analysis_result", columnDefinition = "jsonb") // Removed nullable = false to allow for gradual updates if needed
    private Map<String, Object> analysisResult;


    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;

    public Insights() {
    }

    // Getters and Setters (or Lombok)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobDescriptionSummary() { return jobDescriptionSummary; }
    public void setJobDescriptionSummary(String jobDescriptionSummary) { this.jobDescriptionSummary = jobDescriptionSummary; }
    public String getResumeFilename() { return resumeFilename; }
    public void setResumeFilename(String resumeFilename) { this.resumeFilename = resumeFilename; }
    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }
    public Integer getAtsScore() { return atsScore; } // Getter for atsScore
    public void setAtsScore(Integer atsScore) { this.atsScore = atsScore; } // Setter for atsScore
    public Map<String, Object> getAnalysisResult() { return analysisResult; }
    public void setAnalysisResult(Map<String, Object> analysisResult) { this.analysisResult = analysisResult; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }


    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}
