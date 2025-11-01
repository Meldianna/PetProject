package com.fotos.redsocial.entity.dto.responses;

import java.time.LocalDate;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//used for future reminders
public class SimpleEventResponse {
    private String name;
    private LocalDate date;
    private Location location;
    private User organizer;
    
}

