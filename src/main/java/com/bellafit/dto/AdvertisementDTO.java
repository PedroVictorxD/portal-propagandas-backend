package com.bellafit.dto;

import com.bellafit.entity.Advertisement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdvertisementDTO {
    
    private Long id;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clientId;
    
    @NotBlank(message = "Título é obrigatório")
    private String title;
    
    private String description;
    
    private MultipartFile file;
    
    @NotNull(message = "Data de início é obrigatória")
    private LocalDate startDate;
    
    @NotNull(message = "Data de fim é obrigatória")
    private LocalDate endDate;
    
    @NotNull(message = "Tipo de pacote é obrigatório")
    private Advertisement.PackageType packageType;
    
    @NotNull(message = "Valor do pacote é obrigatório")
    private BigDecimal packageValue;
    
    private BigDecimal amountPaid;
    
    private Integer displayPriority = 1;
    
    private boolean active = true;
    
    // Campos para resposta
    private String clientName;
    private String businessName;
    private String filePath;
    private Advertisement.FileType fileType;
    private Long fileSize;
    private Integer durationSeconds;
    private Integer totalDays;
    private Integer remainingDays;
    private Advertisement.PaymentStatus paymentStatus;
} 