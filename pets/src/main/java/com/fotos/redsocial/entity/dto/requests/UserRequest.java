package com.fotos.redsocial.entity.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequest {
    private String name;
    private String email;
    private String phone;
    private String location;
}
