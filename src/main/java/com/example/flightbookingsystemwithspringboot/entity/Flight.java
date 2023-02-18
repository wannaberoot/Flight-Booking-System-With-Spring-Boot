package com.example.flightbookingsystemwithspringboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "flight")
@Getter
@Setter
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(unique = true)
    private String identifier;
    @Column(name = "flight_name")
    private String name;
    @Column(name = "flight_description")
    private String description;
    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;
    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Seat> seats;

    public Flight(final String name, final String description,final BigDecimal ticketPrice) {
        this.identifier = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.ticketPrice = ticketPrice;
    }
}
