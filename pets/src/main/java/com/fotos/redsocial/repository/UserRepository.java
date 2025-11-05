// UserRepository.java
package com.fotos.redsocial.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.User;

@Repository
public interface UserRepository extends Neo4jRepository<User, String>{
    @Query("MATCH (u:User {email: $email}) RETURN u")
    User findByEmail(String email);

    @Query("MATCH (a:User), (b:User) " +
        "WHERE a.email = $emailA AND b.email = $emailB " +
        "MERGE (a)-[:FRIENDS_WITH]-(b)")
    void createFriendship(@Param("emailA") String emailA, @Param("emailB") String emailB);

    @Query("MATCH (u:User {email: $email})-[:FRIENDS_WITH]-(friend:User) " +
            "OPTIONAL MATCH (friend)-[r:LIVES_IN]->(l:Location) " +
            "RETURN friend, COLLECT(r), COLLECT(l)")
    List<User> findAllFriendsByEmail(String email);

    // ⭐ YA NO NECESITAS LA QUERY CUSTOM fosterAnimal
}