package com.fotos.redsocial.service.interfaces;

import java.util.List;

import com.fotos.redsocial.entity.dto.requests.FriendshipRequest;
import com.fotos.redsocial.entity.dto.requests.UserRequest;
import com.fotos.redsocial.entity.dto.responses.SimpleUserResponse;

public interface UserService {
    public SimpleUserResponse createUser(UserRequest request);
    public List<SimpleUserResponse> createFriendship(FriendshipRequest request);
    public List<SimpleUserResponse> getAllFriendsByEmail(String email);
    
}
