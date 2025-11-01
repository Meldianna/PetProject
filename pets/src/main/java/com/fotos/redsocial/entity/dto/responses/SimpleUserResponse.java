package com.fotos.redsocial.entity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleUserResponse {
    private String name;
    private String email;
    private String phone;
    private LocationResponse location;
}
