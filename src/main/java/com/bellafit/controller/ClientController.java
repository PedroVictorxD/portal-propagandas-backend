package com.bellafit.controller;

import com.bellafit.dto.ClientDTO;
import com.bellafit.entity.Client;
import com.bellafit.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {
    
    private final ClientRepository clientRepository;
    
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<Client> clients = clientRepository.findByActiveTrue();
        List<ClientDTO> clientDTOs = clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        // Verifica se o email já existe apenas se foi fornecido
        if (clientDTO.getEmail() != null && !clientDTO.getEmail().trim().isEmpty() && 
            clientRepository.existsByEmail(clientDTO.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientRepository.save(client);
        return ResponseEntity.ok(convertToDTO(savedClient));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    existingClient.setName(clientDTO.getName());
                    existingClient.setEmail(clientDTO.getEmail());
                    existingClient.setPhoneNumber(clientDTO.getPhoneNumber());
                    existingClient.setBusinessName(clientDTO.getBusinessName());
                    existingClient.setBusinessDescription(clientDTO.getBusinessDescription());
                    existingClient.setAddress(clientDTO.getAddress());
                    existingClient.setSocialMedia(clientDTO.getSocialMedia());
                    existingClient.setActive(clientDTO.isActive());
                    
                    Client savedClient = clientRepository.save(existingClient);
                    return ResponseEntity.ok(convertToDTO(savedClient));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setActive(false);
                    clientRepository.save(client);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ClientDTO>> searchClients(@RequestParam String term) {
        List<Client> clients = clientRepository.searchClients(term);
        List<ClientDTO> clientDTOs = clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientDTOs);
    }
    
    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setBusinessName(client.getBusinessName());
        dto.setBusinessDescription(client.getBusinessDescription());
        dto.setAddress(client.getAddress());
        dto.setSocialMedia(client.getSocialMedia());
        dto.setActive(client.isActive());
        return dto;
    }
    
    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setBusinessName(dto.getBusinessName());
        client.setBusinessDescription(dto.getBusinessDescription());
        client.setAddress(dto.getAddress());
        client.setSocialMedia(dto.getSocialMedia());
        client.setActive(dto.isActive());
        return client;
    }
} 