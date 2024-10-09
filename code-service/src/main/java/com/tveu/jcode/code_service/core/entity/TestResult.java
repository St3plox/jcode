package com.tveu.jcode.code_service.core.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test_results")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {

    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    // Many-to-One relationship with TestCases
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Column(name = "isSuccessful", nullable = false)
    private Boolean isSuccessful;

    @Column(name = "output", columnDefinition = "TEXT", nullable = false)
    private String output;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
