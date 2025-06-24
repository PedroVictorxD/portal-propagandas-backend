package com.bellafit.repository;

import com.bellafit.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    List<Client> findByActiveTrue();
    
    List<Client> findByActiveFalse();
    
    @Query("SELECT c FROM Client c WHERE c.name LIKE %:searchTerm% OR c.businessName LIKE %:searchTerm% OR c.email LIKE %:searchTerm%")
    List<Client> searchClients(String searchTerm);
    
    boolean existsByEmail(String email);
} 