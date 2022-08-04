package com.ing.kata.Entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, unique = true)
    private String refAccount;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Column
    private BigDecimal balance;

}
