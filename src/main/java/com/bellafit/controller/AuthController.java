package com.bellafit.controller;

import com.bellafit.dto.LoginRequest;
import com.bellafit.dto.LoginResponse;
import com.bellafit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                    .body(new Object() {
                        public final String error = e.getMessage();
                        public final String message = "Falha na autenticação";
                    });
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new Object() {
                        public final String error = "Erro interno do servidor";
                        public final String message = e.getMessage();
                    });
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Bella Fit Propagandas API está funcionando!");
    }
} 