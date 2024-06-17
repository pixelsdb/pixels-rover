package io.pixelsdb.pixels.rover.mapper;

import io.pixelsdb.pixels.rover.model.QueryResults;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryResultsRepository extends JpaRepository<QueryResults, Long>
{
    QueryResults findById(long id);
}
