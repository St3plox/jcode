package com.tveu.jcode.code_service.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", referencedColumnName = "id", nullable = false)
    private Submission submission;

    private String output;
    private String errors;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
