package com.technicalchallenge.additionalinfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalInfoRepository extends JpaRepository<AdditionalInfo, Long> {

    @Query("SELECT a FROM AdditionalInfo a WHERE a.entityType = :entityType AND a.entityId = :entityId AND a.active = true")
    List<AdditionalInfo> findActiveByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Long entityId);

    @Query("SELECT a FROM AdditionalInfo a WHERE a.entityType = :entityType AND a.entityId = :entityId AND a.fieldName = :fieldName AND a.active = true")
    AdditionalInfo findActiveByEntityTypeAndEntityIdAndFieldName(@Param("entityType") String entityType, @Param("entityId") Long entityId, @Param("fieldName") String fieldName);

    List<AdditionalInfo> findByEntityTypeAndEntityIdAndActiveTrue(String entityType, Long entityId);
}
