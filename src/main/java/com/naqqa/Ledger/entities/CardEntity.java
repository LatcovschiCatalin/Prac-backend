package com.naqqa.Ledger.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "card")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "investor_id")
//    InvestorEntity investor;
//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    ProjectEntity project;

    private String name;

    private LocalDate date = LocalDate.now();
}
