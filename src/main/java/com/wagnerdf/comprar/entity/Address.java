package com.wagnerdf.comprar.entity;

import java.time.LocalDateTime;

import com.wagnerdf.comprar.enums.State;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false, length = 150)
    private String recipientName;

    @Column(nullable = false, length = 9)
    private String zipCode;

    @Column(nullable = false, length = 200)
    private String street;

    @Column(nullable = false, length = 20)
    private String number;

    @Column(length = 200)
    private String complement;

    @Column(nullable = false, length = 150)
    private String neighborhood;

    @Column(nullable = false, length = 150)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private State state;

    @Column(length = 255)
    private String reference;

    @Column(nullable = false)
    @Builder.Default
    private Boolean defaultAddress = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
