package com.bellafit.service;

import com.bellafit.dto.LoginRequest;
import com.bellafit.dto.LoginResponse;
import com.bellafit.entity.User;
import com.bellafit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
    
    public LoginResponse login(LoginRequest request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElse(null);
            
            if (user == null) {
                throw new RuntimeException("Usuário não encontrado");
            }
            
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Senha incorreta");
            }
            
            if (!user.isActive()) {
                throw new RuntimeException("Usuário inativo");
            }
            
            return new LoginResponse(
                    "Bearer " + generateSimpleToken(user),
                    user.getUsername(),
                    user.getName(),
                    user.getRole().name(),
                    user.getId()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro no login: " + e.getMessage());
        }
    }
    
    private String generateSimpleToken(User user) {
        // Token simples para desenvolvimento
        return user.getId() + "." + user.getUsername() + "." + System.currentTimeMillis();
    }
    
    @PostConstruct
    public void createDefaultAdmin() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Administrador");
            admin.setEmail("admin@bellafit.com");
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            
            userRepository.save(admin);
            System.out.println("✅ Usuário admin criado com sucesso!");
        }
    }
} 