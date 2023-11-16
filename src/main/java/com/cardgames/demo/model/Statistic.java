package com.cardgames.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "statistic")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "winner_id")
    private Long winnerId;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "ID", insertable = false, updatable = false)
    private Game game;
}
