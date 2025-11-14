package com.fotos.redsocial.entity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocialConnectionResponse {
    private String friendEmail;
    /** El tipo de conexión (ej. "ADOPTED", "FOSTERS", "CONNECTION_FOUND"). */
    private String connectionType;
    
}
