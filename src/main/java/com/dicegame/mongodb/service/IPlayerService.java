package com.dicegame.mongodb.service;

import java.util.List;

import com.dicegame.mongodb.dto.Player;

public interface IPlayerService {

	// CRUD METHODS

	// Create player
	public Player createPlayer(Player player);

	// Get all players
	public List<Player> listPlayers();

	// Get player by id
	public Player getPlayer(Long id);

	// Update player
	public Player updatePlayer(Player player);

	// Delete player by id
	public void deletePlayer(Long id);

}
