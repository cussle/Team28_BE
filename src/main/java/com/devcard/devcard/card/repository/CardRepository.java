package com.devcard.devcard.card.repository;

import com.devcard.devcard.card.vo.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>{
}