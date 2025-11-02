package com.fotos.redsocial.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.Trait;

@Repository
public interface TraitRepository extends Neo4jRepository<Trait, Long>{
    Trait findByDescription(String description);
    List<Trait> findAllByNameInList(List<String> names);
}
