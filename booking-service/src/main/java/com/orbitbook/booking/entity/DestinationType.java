package com.orbitbook.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DESTINATION_TYPES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_destination_types")
    @SequenceGenerator(name = "seq_destination_types", sequenceName = "SEQ_DESTINATION_TYPES", allocationSize = 1)
    @Column(name = "ID_DESTINATION_TYPES")
    private Long idDestinationTypes;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 260)
    private String description;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "destinationType")
    private List<Destination> destinations;
}