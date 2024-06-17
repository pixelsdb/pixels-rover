package io.pixelsdb.pixels.rover.mapper;

import io.pixelsdb.pixels.rover.model.SQLStatements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SQLStatementsRepository extends JpaRepository<SQLStatements, Long>
{
    SQLStatements findById(long id);
    SQLStatements findByUuid(String uuid);
}
