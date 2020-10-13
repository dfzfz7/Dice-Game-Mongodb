package com.dicegame.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.dicegame.mongodb.dto.Player;

public interface IPlayerDAO extends MongoRepository<Player, Integer> {

}
