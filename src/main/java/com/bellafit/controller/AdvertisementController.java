package com.bellafit.controller;

import com.bellafit.dto.AdvertisementDTO;
import com.bellafit.dto.StatsResponse;
import com.bellafit.entity.Advertisement;
import com.bellafit.entity.Client;
import com.bellafit.repository.AdvertisementRepository;
import com.bellafit.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdvertisementController {
    
    private final AdvertisementRepository advertisementRepository;
    private final ClientRepository clientRepository;
    
    @GetMapping
    public ResponseEntity<List<AdvertisementDTO>> getActiveAdvertisements() {
        List<Advertisement> advertisements = advertisementRepository.findActiveAdvertisements(LocalDate.now());
        List<AdvertisementDTO> advertisementDTOs = advertisements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(advertisementDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> getAdvertisementById(@PathVariable Long id) {
        return advertisementRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AdvertisementDTO>> getAdvertisementsByClient(@PathVariable Long clientId) {
        List<Advertisement> advertisements = advertisementRepository.findByClientId(clientId);
        List<AdvertisementDTO> advertisementDTOs = advertisements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(advertisementDTOs);
    }
    
    @PostMapping
    public ResponseEntity<AdvertisementDTO> createAdvertisement(@Valid @RequestBody AdvertisementDTO advertisementDTO) {
        Client client = clientRepository.findById(advertisementDTO.getClientId())
                .orElse(null);
        
        if (client == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Advertisement advertisement = convertToEntity(advertisementDTO);
        advertisement.setClient(client);
        
        // Calcular dias totais
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(
                advertisementDTO.getStartDate(), 
                advertisementDTO.getEndDate()
        );
        advertisement.setTotalDays((int) totalDays);
        
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
        return ResponseEntity.ok(convertToDTO(savedAdvertisement));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> updateAdvertisement(@PathVariable Long id, @Valid @RequestBody AdvertisementDTO advertisementDTO) {
        return advertisementRepository.findById(id)
                .map(existingAdvertisement -> {
                    existingAdvertisement.setTitle(advertisementDTO.getTitle());
                    existingAdvertisement.setDescription(advertisementDTO.getDescription());
                    existingAdvertisement.setStartDate(advertisementDTO.getStartDate());
                    existingAdvertisement.setEndDate(advertisementDTO.getEndDate());
                    existingAdvertisement.setPackageType(advertisementDTO.getPackageType());
                    existingAdvertisement.setPackageValue(advertisementDTO.getPackageValue());
                    existingAdvertisement.setAmountPaid(advertisementDTO.getAmountPaid());
                    existingAdvertisement.setDisplayPriority(advertisementDTO.getDisplayPriority());
                    existingAdvertisement.setActive(advertisementDTO.isActive());
                    
                    // Recalcular dias totais
                    long totalDays = java.time.temporal.ChronoUnit.DAYS.between(
                            advertisementDTO.getStartDate(), 
                            advertisementDTO.getEndDate()
                    );
                    existingAdvertisement.setTotalDays((int) totalDays);
                    
                    Advertisement savedAdvertisement = advertisementRepository.save(existingAdvertisement);
                    return ResponseEntity.ok(convertToDTO(savedAdvertisement));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        return advertisementRepository.findById(id)
                .map(advertisement -> {
                    advertisement.setActive(false);
                    advertisementRepository.save(advertisement);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/expiring")
    public ResponseEntity<List<AdvertisementDTO>> getExpiringAdvertisements() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(7);
        List<Advertisement> advertisements = advertisementRepository.findAdvertisementsExpiringSoon(today, endDate);
        List<AdvertisementDTO> advertisementDTOs = advertisements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(advertisementDTOs);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        Long activeCount = advertisementRepository.countActiveAdvertisements(LocalDate.now());
        Double totalValue = advertisementRepository.getTotalActiveValue(LocalDate.now());
        
        StatsResponse stats = new StatsResponse(
            activeCount, 
            totalValue != null ? totalValue : 0.0
        );
        
        return ResponseEntity.ok(stats);
    }
    
    private AdvertisementDTO convertToDTO(Advertisement advertisement) {
        AdvertisementDTO dto = new AdvertisementDTO();
        dto.setId(advertisement.getId());
        dto.setClientId(advertisement.getClient().getId());
        dto.setTitle(advertisement.getTitle());
        dto.setDescription(advertisement.getDescription());
        dto.setStartDate(advertisement.getStartDate());
        dto.setEndDate(advertisement.getEndDate());
        dto.setPackageType(advertisement.getPackageType());
        dto.setPackageValue(advertisement.getPackageValue());
        dto.setAmountPaid(advertisement.getAmountPaid());
        dto.setDisplayPriority(advertisement.getDisplayPriority());
        dto.setActive(advertisement.isActive());
        
        // Campos adicionais
        dto.setClientName(advertisement.getClient().getName());
        dto.setBusinessName(advertisement.getClient().getBusinessName());
        dto.setFilePath(advertisement.getFilePath());
        dto.setFileType(advertisement.getFileType());
        dto.setFileSize(advertisement.getFileSize());
        dto.setDurationSeconds(advertisement.getDurationSeconds());
        dto.setTotalDays(advertisement.getTotalDays());
        dto.setRemainingDays(advertisement.getRemainingDays());
        dto.setPaymentStatus(advertisement.getPaymentStatus());
        
        return dto;
    }
    
    private Advertisement convertToEntity(AdvertisementDTO dto) {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(dto.getTitle());
        advertisement.setDescription(dto.getDescription());
        advertisement.setStartDate(dto.getStartDate());
        advertisement.setEndDate(dto.getEndDate());
        advertisement.setPackageType(dto.getPackageType());
        advertisement.setPackageValue(dto.getPackageValue());
        advertisement.setAmountPaid(dto.getAmountPaid());
        advertisement.setDisplayPriority(dto.getDisplayPriority());
        advertisement.setActive(dto.isActive());
        
        // Campos padrão
        advertisement.setFilePath("/uploads/default.jpg");
        advertisement.setFileType(Advertisement.FileType.IMAGE);
        advertisement.setFileSize(0L);
        advertisement.setDurationSeconds(0);
        
        return advertisement;
    }
} 