package com.bellafit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Número de contato é obrigatório")
    private String phoneNumber;
    
    private String businessName;
    
    private String businessDescription;
    
    private String address;
    
    private String socialMedia;
    
    private boolean active = true;
} 