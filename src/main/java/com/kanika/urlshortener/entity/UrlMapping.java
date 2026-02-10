package com.kanika.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "url_mapping",
    indexes = {
        @Index(name = "idx_shortcode", columnList = "shortCode"),
        @Index(name = "idx_longurl", columnList = "longUrl")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String shortCode;

    @Column(nullable = false, length = 2000, unique = true)
    private String longUrl;
}
