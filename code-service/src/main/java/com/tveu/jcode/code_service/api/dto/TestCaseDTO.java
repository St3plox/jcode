package com.tveu.jcode.code_service.api.dto;

import lombok.Builder;

@Builder
public record TestCaseDTO(

        String id,
        
        String problemID,
        String input,
        String output
) {
}
