package com.fotos.redsocial.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleUserDTO {
    private String name;
    private String email;
    private String phone;
    private LocationDTO location;
}
