// UserServiceImpl.java
package com.fotos.redsocial.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.entity.dto.requests.FriendshipRequest;
import com.fotos.redsocial.entity.dto.requests.SimpleAnimalRequest;
import com.fotos.redsocial.entity.dto.requests.UserRequest;
import com.fotos.redsocial.entity.dto.responses.FosterAnimalResponse;
import com.fotos.redsocial.entity.dto.responses.SimpleUserResponse;
import com.fotos.redsocial.entity.relationship.FostersRelationship;
import com.fotos.redsocial.mapper.AnimalMapper;
import com.fotos.redsocial.mapper.UserMapper;
import com.fotos.redsocial.repository.AnimalRepository;
import com.fotos.redsocial.repository.LocationRepository;
import com.fotos.redsocial.repository.UserRepository;
import com.fotos.redsocial.service.interfaces.UserService;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalMapper animalMapper;


    @Override
    public SimpleUserResponse createUser(UserRequest request) {
       Location existingLocation = locationRepository.findByName(request.getLocation());
       if (existingLocation == null) throw new RuntimeException("La ubicación donde se quiere crear la cuenta es inválida");
       
       User existingUser = userRepository.findByEmail(request.getEmail());
       
       if (existingUser != null) throw new RuntimeException("El usuario ya existe");

        User user = new User(
            request.getName(),
            request.getEmail(),
            request.getPhone(),
            existingLocation
        );

        userRepository.save(user);

        return userMapper.toSimpleUserResponse(user);
    }
    
    @Override
    public List<SimpleUserResponse> createFriendship(FriendshipRequest request){
        userRepository.createFriendship(
            request.getFirstEmail(),
            request.getSecondEmail());
        List<SimpleUserResponse> friends = this.getAllFriendsByEmail(request.getFirstEmail());
        return friends;
    }

    @Override
    public List<SimpleUserResponse> getAllFriendsByEmail(String email){
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) throw new RuntimeException("El usuario no se encuentra");

        List<User> friends = userRepository.findAllFriendsByEmail(existingUser.getEmail());

        return userMapper.toSimpleUserResponseList(friends);
    }

    /*
    public SimpleAnimalResponse adoptAnimal(UserAnimalRequest request) {
        User existingUser = userRepository.findByEmail(request.getUserEmail());
        if (existingUser == null) throw new RuntimeException("El usuario no se encuentra");

        Optional<Animal> existingAnimal = animalRepository.findById(request.getAnimalId().toString());
        if (!existingAnimal.isPresent()) throw new RuntimeException("El animal no se encuentra");

        AdoptsRelationship adoption = new AdoptsRelationship(existingUser.getId(), LocalDate.now(), existingAnimal.get());
        
        existingUser.getAdoptedAnimals().add(adoption);

        userRepository.save(existingUser);

        return animalMapper.toSimpleAnimalResponse(existingAnimal.get());
    }*/

    public FosterAnimalResponse fosterAnimal(SimpleAnimalRequest request) {
        // Buscar el usuario
        User existingUser = userRepository.findByEmail(request.getUserEmail());
        if (existingUser == null) throw new RuntimeException("El usuario no se encuentra");

        // Buscar el animal por ID
        Animal existingAnimal = animalRepository.findByName(request.getAnimalName());
        if (existingAnimal == null) throw new RuntimeException("El animal no se encuentra");

        // Crear la relación FOSTERS
        FostersRelationship fostering = new FostersRelationship(
            LocalDate.now(),
            existingAnimal
        );
        
        // Agregar la relación al usuario
        existingUser.getFostering().add(fostering);
        
        // Guardar el usuario (esto persiste la relación)
        userRepository.save(existingUser);

        return new FosterAnimalResponse(
            existingAnimal.getName(),
            existingAnimal.getSpecie().getName()
        );
    }
}