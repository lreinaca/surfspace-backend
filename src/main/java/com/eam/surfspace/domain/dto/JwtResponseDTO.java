package com.eam.surfspace.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private String email;

    public JwtResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
        this.type = "Bearer";
    }
}

