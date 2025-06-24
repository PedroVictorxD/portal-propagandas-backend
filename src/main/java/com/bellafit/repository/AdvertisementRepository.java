package com.bellafit.repository;

import com.bellafit.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    
    @Query("SELECT a FROM Advertisement a WHERE a.active = true AND a.startDate <= :today AND a.endDate >= :today ORDER BY a.displayPriority DESC, a.createdAt ASC")
    List<Advertisement> findActiveAdvertisements(@Param("today") LocalDate today);
    
    List<Advertisement> findByClientIdAndActiveTrue(Long clientId);
    
    List<Advertisement> findByClientId(Long clientId);
    
    @Query("SELECT a FROM Advertisement a WHERE a.endDate < :today AND a.active = true")
    List<Advertisement> findExpiredAdvertisements(@Param("today") LocalDate today);
    
    @Query("SELECT a FROM Advertisement a WHERE a.endDate BETWEEN :today AND :endDate AND a.active = true")
    List<Advertisement> findAdvertisementsExpiringSoon(@Param("today") LocalDate today, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Advertisement a WHERE a.active = true AND a.startDate <= :today AND a.endDate >= :today")
    Long countActiveAdvertisements(@Param("today") LocalDate today);
    
    @Query("SELECT COALESCE(SUM(a.packageValue), 0) FROM Advertisement a WHERE a.active = true AND a.startDate <= :today AND a.endDate >= :today")
    Double getTotalActiveValue(@Param("today") LocalDate today);
} 