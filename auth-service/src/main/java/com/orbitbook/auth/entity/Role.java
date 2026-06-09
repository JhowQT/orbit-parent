package com.orbitbook.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ROLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
    @SequenceGenerator(name = "seq_role", sequenceName = "SEQ_ROLE", allocationSize = 1)
    @Column(name = "ID_ROLE")
    private Long idRole;

    @Column(name = "NAME_ROLE", nullable = false, length = 100)
    private String nameRole;

    @Column(name = "DESCRIPTION", nullable = false, length = 200)
    private String description;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    
}