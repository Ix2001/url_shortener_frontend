package com.alabuga.shortener.repository;

import com.alabuga.shortener.entity.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    @Query("select s from ShortLink s where s.user.id = :userId order by s.createdAt desc")
    List<ShortLink> findByUserId(java.util.UUID userId);

    Optional<ShortLink> findByCode(String code);

    boolean existsByCode(String code);

    @Modifying
    @Transactional
    @Query("delete from ShortLink s where s.createdAt < :threshold")
    void deleteOldLinks(LocalDateTime threshold);
}