// UserRepository.java
package com.fotos.redsocial.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.entity.dto.responses.FriendPairDTO;
import com.fotos.redsocial.entity.dto.responses.SocialConnectionResponse;

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

    @Query("""
            MATCH (u:User)-[p:PREFERS]->(s:Specie)
            WHERE id(u) = $userId
            RETURN s
            """)
    List<String> findPreferredSpecies(String userId);

    @Query("""
            MATCH (u:User)-[p:PREFERS]->(t:Trait)
            WHERE id(u) = $userId
            RETURN t
            """)
    List<String> findPreferredTraits(String userId);


   /**
    * USADA EN BFS
     * Trae TODA la red de amigos como una lista de pares.
     */
    @Query("MATCH (u:User)-[:FRIENDS_WITH]-(f:User) RETURN u.email AS userEmail, f.email AS friendEmail")
    List<FriendPairDTO> getAllFriendshipPairs();

    
    @Query("""
        MATCH (u:User)-[r:ADOPTS|FOSTERS|TAKES_CARE_OF]->(a:Animal {id: $animalId})
        RETURN u.email AS friendEmail, type(r) AS connectionType

        UNION

        MATCH (a:Animal {id: $animalId})<-[:HOUSES]-(s:Shelter)
        MATCH (u:User)-[r:WORKS_IN]->(s)
        RETURN u.email AS friendEmail, type(r) AS connectionType
        """)
    Set<SocialConnectionResponse> findAllUserConnectedToAnimal(@Param("animalId") String animalId);
}