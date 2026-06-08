package com.orbitbook.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS_ORBIT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrbit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users_orbit")
    @SequenceGenerator(name = "seq_users_orbit", sequenceName = "SEQ_USERS_ORBIT", allocationSize = 1)
    @Column(name = "ID_USERS_ORBIT")
    private Long idUsersOrbit;

    @Column(name = "NAME", nullable = false, length = 260)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROLE", nullable = false)
    private Role role;
}