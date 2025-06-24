package com.bellafit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "advertisements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "total_days")
    private Integer totalDays;
    
    @Column(name = "remaining_days")
    private Integer remainingDays;
    
    @Column(name = "package_type")
    @Enumerated(EnumType.STRING)
    private PackageType packageType;
    
    @Column(name = "package_value", precision = 10, scale = 2)
    private BigDecimal packageValue;
    
    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;
    
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    @Column(name = "display_priority")
    private Integer displayPriority = 1;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateRemainingDays();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateRemainingDays();
    }
    
    private void calculateRemainingDays() {
        if (endDate != null) {
            LocalDate today = LocalDate.now();
            int remaining = endDate.compareTo(today);
            this.remainingDays = Math.max(0, remaining);
        }
    }
    
    public enum FileType {
        IMAGE, VIDEO
    }
    
    public enum PackageType {
        WEEKLY, MONTHLY, QUARTERLY, SEMIANNUAL
    }
    
    public enum PaymentStatus {
        PENDING, PAID, OVERDUE, CANCELLED
    }
} 