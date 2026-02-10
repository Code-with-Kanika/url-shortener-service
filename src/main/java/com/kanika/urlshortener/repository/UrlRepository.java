package com.kanika.urlshortener.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanika.urlshortener.entity.UrlMapping;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {    

    Optional<UrlMapping> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    Optional<UrlMapping> findByLongUrl(String longUrl);
    
}
