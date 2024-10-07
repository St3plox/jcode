package com.tveu.jcode.code_service.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "problem_submissions")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ProblemSubmission extends Submission {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

}
