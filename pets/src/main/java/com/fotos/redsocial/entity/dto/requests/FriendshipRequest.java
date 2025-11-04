package com.fotos.redsocial.entity.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendshipRequest {
    private String firstEmail;
    private String secondEmail;
}
