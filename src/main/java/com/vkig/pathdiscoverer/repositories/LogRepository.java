package com.vkig.pathdiscoverer.repositories;

import com.vkig.pathdiscoverer.models.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to handle logging related database operations.
 */
@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
