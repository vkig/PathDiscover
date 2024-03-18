package com.vkig.pathdiscoverer.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The data entity to store log items in the database.
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(columnDefinition = "timestamp", name = "date_time")
    LocalDateTime when;
    String who;
    String what;
    @Lob
    String result;
}
