package com.fotos.redsocial.entity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class FriendPairDTO{ 

    private String userEmail;
    private String friendEmail;

}
