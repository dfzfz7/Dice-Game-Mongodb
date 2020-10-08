package com.dicegame.mongodb.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dicegame.mongodb.dto.DiceRoll;
import com.dicegame.mongodb.dto.Player;
import com.dicegame.mongodb.service.PlayerServiceImpl;

@RestController
@RequestMapping("/api")
public class PlayerController {

	// Use of methods from Service
	@Autowired
	PlayerServiceImpl playerServiceImpl;

	// Create player
	@PostMapping("/players")
	public String createPlayer(@RequestBody Player player) {
		// If name is null set name as Anonymous
		if(player.getPlayerName() == null) {
			player.setPlayerName("Anonymous");
		}
		// If date is null set actual date
		if(player.getRegDate() == null) {
			player.setRegDate(new Date(System.currentTimeMillis()));
		}
		// If success rate is null set to 0
		if(player.getSuccessRate() == null) {
			player.setSuccessRate(0.0);
		}
		// Create new player
		playerServiceImpl.createPlayer(player);
		return player.getPlayerName() + " has been created";
	}

	// Get all players
	@GetMapping("/players")
	public List<Player> listPlayers() {
		return playerServiceImpl.listPlayers();
	}

	// Get player by id
	@GetMapping("/players/{id}")
	public Player getPlayer(@PathVariable(name = "id") Long id) {
		return playerServiceImpl.getPlayer(id);
	}

	// Update player by id
	@PutMapping("/players/{id}")
	public String updatePlayer(@PathVariable(name = "id") Long id, @RequestBody Player player) {
		Player playerToUpdate = playerServiceImpl.getPlayer(id);
		// If name is modified set name
		if(player.getPlayerName() !=null) {
			playerToUpdate.setPlayerName(player.getPlayerName());
		}
		// Update player
		playerServiceImpl.updatePlayer(playerToUpdate);
		return "Player " + playerToUpdate.getPlayerName() +" has been updated!";
	}

	// Get dice rolls from a player as list
	@GetMapping("/players/{id}/games")
	public List<DiceRoll> getListDiceRolls(@PathVariable(name = "id") Long id) {
		Player player = playerServiceImpl.getPlayer(id);
		return player.getDiceRoll();
	}

	// Get player average ranking list (ordered by success rate)
	@GetMapping("/players/ranking")
	public List<Player> getListRanking() {
		List<Player> ranking = listPlayers();
		// Update success rate for all players
		for (Player player : ranking) {
			player.updateSuccessRate();
		}
		// Sort list by higher success rate
		ranking.sort(Comparator.comparing(Player::getSuccessRate).reversed());
		return ranking;
	}

	// Get loser - player with lower success rate
	@GetMapping("/players/ranking/loser")
	public Player getLoser() {
		List<Player> ranking = getListRanking();
		Player loser = ranking.get(ranking.size() - 1); // last player from ranking list
		return loser;
	}

	// Get winner - player with higher success rate
	@GetMapping("/players/ranking/winner")
	public Player getWinner() {
		List<Player> ranking = getListRanking();
		Player winner = ranking.get(0); // first player from ranking list
		return winner;
	}

	// Delete player by id
	@DeleteMapping("/players/{id}")
	public String deletePlayer(@PathVariable(name = "id") Long id) {
		// In order to delete Player:
		// Player does not have to have dice rolls 
		// if not error 500 will appear
		Player player = playerServiceImpl.getPlayer(id);
		String msg;
		if (player.getDiceRoll().size() != 0) {
			msg = "Player cannot be deleted due to still has dice rolls.\n "
					+ "First you should delete all dice rolls from player.\n"
					+ " Try DELETE at: http://localhost:8080/api/players/" + player.getId();
		} else {
			playerServiceImpl.deletePlayer(id);
			msg = "Player " + player.getPlayerName() + " has been deleted";
		}
		return msg;
	}

}
