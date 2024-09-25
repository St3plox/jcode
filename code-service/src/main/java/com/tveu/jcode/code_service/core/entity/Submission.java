package com.tveu.jcode.code_service.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "user_id")
    private Long userID;

    private String code;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
