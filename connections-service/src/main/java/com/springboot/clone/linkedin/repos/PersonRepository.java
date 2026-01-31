package com.springboot.clone.linkedin.repos;

import com.springboot.clone.linkedin.entities.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, String> {

    @Query("MATCH (p1:Person) -[:CONNECTED_TO]-> (p2:Person) WHERE p1.name = $username RETURN p2")
    List<Person> findFirstDegreeConnections(String username);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.name = $sender AND p2.name = $receiver " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(String sender, String receiver);

    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person) " +
            "WHERE p1.name = $sender AND p2.name = $receiver " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(String sender, String receiver);

    @Query("MATCH (p1:Person), (p2:Person) " +
            "WHERE p1.name = $sender AND p2.name = $receiver " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(String sender, String receiver);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.name = $sender AND p2.name = $receiver " +
            "DELETE r " +
            "CREATE (p1)-[:CONNECTED_TO]->(p2)")
    void acceptConnectionRequest(String sender, String receiver);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.name = $sender AND p2.name = $receiver " +
            "DELETE r")
    void rejectConnectionRequest(String sender, String receiver);
}
