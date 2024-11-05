package com.tveu.jcode.code_service.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "problem_submissions")
@SuperBuilder
@Data
public class ProblemSubmission extends Submission {


    public ProblemSubmission() {
        super();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
}
