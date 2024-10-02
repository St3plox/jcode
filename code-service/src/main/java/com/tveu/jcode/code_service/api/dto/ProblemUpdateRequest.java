package com.tveu.jcode.code_service.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ProblemUpdateRequest (

        String title,
        String description
){
}
