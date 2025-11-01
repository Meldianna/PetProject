package com.fotos.redsocial.entity.dto.responses;

import java.time.LocalDate;
import java.util.List;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailedEventResponse {
    private String name;
    private LocalDate date;
    private Location location;
    private User organizer;
    private List<SimpleUserResponse> enrolledUsers;
    private List<ShelterResponse> participatingShelters;
}
