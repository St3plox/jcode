package com.tveu.jcode.code_service.core.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_submission_id", nullable = false)
    private ProblemSubmission submission;

    private String output;
    private String errors;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
