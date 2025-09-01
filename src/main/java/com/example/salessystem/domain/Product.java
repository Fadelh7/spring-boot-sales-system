package com.example.salessystem.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 80)
    private String category;

    @Column(nullable = false, updatable = false)
    private Instant creationDate;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) creationDate = Instant.now();
    }
}
