package com.dicegame.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.dicegame.mongodb.dto.DiceRoll;

public interface IDiceRollDAO extends MongoRepository<DiceRoll, Long> {

}
