package io.pixelsdb.pixels.rover.mapper;


import io.pixelsdb.pixels.rover.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Messages, Long>
{
    Messages findById(long id);
}
