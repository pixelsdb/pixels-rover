package io.pixelsdb.pixels.rover.mapper;

import io.pixelsdb.pixels.rover.model.QueryResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Sort;

public interface QueryResultsRepository extends JpaRepository<QueryResults, Long>
{
    QueryResults findById(long id);
    QueryResults findBySqlStatementsUuid(String uuid);
    List<QueryResults> findByCreateTimeBetween(Timestamp startTime, Timestamp endTime, Sort sort);
}
