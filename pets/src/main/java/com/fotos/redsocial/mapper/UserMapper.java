package com.fotos.redsocial.mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.entity.dto.responses.LocationResponse;
import com.fotos.redsocial.entity.dto.responses.SimpleUserResponse;

@Component
public class UserMapper {
    @Autowired
    private LocationMapper locationMapper;

    public SimpleUserResponse toSimpleUserResponse(User user){
        LocationResponse location = locationMapper.tLocationResponse(user.getLivesIn());

        return new SimpleUserResponse(
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            location
        );
    }

    public List<SimpleUserResponse> toSimpleUserResponseList(List<User> users){
        return users.stream()
        .map(this::toSimpleUserResponse)
        .collect(Collectors.toList());
    }

    
}
