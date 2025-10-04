package com.technicalchallenge.repository;

import com.technicalchallenge.model.AdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Interface for managing AdditionalInfo entities in the database
public interface AdditionalInfoRepository extends JpaRepository<AdditionalInfo, Long> {

    // Custom query to find active AdditionalInfo by entity type and entity ID
    @Query("SELECT a FROM AdditionalInfo a WHERE a.entityType = :entityType AND a.entityId = :entityId AND a.active = true")
    List<AdditionalInfo> findActiveByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Long entityId);

    // Custom query to find active AdditionalInfo by entity type, entity ID, and field name
    @Query("SELECT a FROM AdditionalInfo a WHERE a.entityType = :entityType AND a.entityId = :entityId AND a.fieldName = :fieldName AND a.active = true")
    AdditionalInfo findActiveByEntityTypeAndEntityIdAndFieldName(@Param("entityType") String entityType, @Param("entityId") Long entityId, @Param("fieldName") String fieldName);

    List<AdditionalInfo> findByEntityTypeAndEntityIdAndActiveTrue(String entityType, Long entityId);
}
