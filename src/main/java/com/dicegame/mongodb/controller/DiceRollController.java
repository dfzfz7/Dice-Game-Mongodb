package com.dicegame.mongodb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dicegame.mongodb.dto.DiceRoll;
import com.dicegame.mongodb.dto.Player;
import com.dicegame.mongodb.service.DiceRollServiceImpl;
import com.dicegame.mongodb.service.PlayerServiceImpl;

@RestController
@RequestMapping("/api")
public class DiceRollController {

	// Use of methods from Service
	@Autowired
	DiceRollServiceImpl diceRollServiceImpl;
	@Autowired
	PlayerServiceImpl playerServiceImpl;

	// Create Dice Roll for a player - Roll the dices
	@PostMapping("/players/{id}/games")
	public String createDiceRoll(@PathVariable(name = "id") Integer id) {
		// Get player to use roll the dices method
		Player player = playerServiceImpl.getPlayer(id);
		DiceRoll diceRoll = player.rollTheDices();

		// Find last roll added and assign new id
		List<DiceRoll> list = diceRollServiceImpl.listDiceRolls();
		Integer rollId;
		if (list.size() != 0) { // List not empty - find last id and assign +1
			DiceRoll lastRoll = list.get(list.size() - 1);
			rollId = lastRoll.getId() + 1;
		} else { // Empty list - assign id 1
			rollId = 1;
		}
		// Assign id to dice roll
		diceRoll.setId(rollId);

		// Add dice roll to database
		diceRollServiceImpl.createDiceRoll(diceRoll);

		//Update player with dice roll list
		playerServiceImpl.updatePlayer(player);

		return diceRoll.toString();
	}

	// Delete all dice rolls from a player
	@DeleteMapping("/players/{id}/games")
	public String deleteDiceRolls(@PathVariable(name = "id") Integer id) {
		// Get list of dice rolls from a player
		Player player = playerServiceImpl.getPlayer(id);
		List<DiceRoll> diceRolls = player.getDiceRoll();
		// Delete each dice roll from player
		for (DiceRoll roll : diceRolls) {
			Integer idRoll = roll.getId();
			diceRollServiceImpl.deleteDiceRoll(idRoll);
		}
		
		// Update successRate to 0
		player.setSuccessRate(0.0);
		
		// Update player dice rolls to empty list
		diceRolls = new ArrayList<DiceRoll>();
		player.setDiceRoll(diceRolls);
		
		//Update player with dice roll deleted
		playerServiceImpl.updatePlayer(player);
		
		return "Dice rolls from player " + player.getPlayerName() + " have been deleted";
	}

	// Get all dice rolls
	@GetMapping("/games")
	public List<DiceRoll> listDiceRolls() {
		return diceRollServiceImpl.listDiceRolls();
	}

	/*
	 * NOT USED METHODS
	 * 
	 * // Get dice roll by id
	 * 
	 * @GetMapping("/games/{id}") public DiceRoll getDiceRoll(@PathVariable(name =
	 * "id") Integer id) { return diceRollServiceImpl.getDiceRoll(id); }
	 * 
	 * // Update dice roll
	 * 
	 * @PutMapping("/games/{id}") public String updateRollDice(@PathVariable(name =
	 * "id") Integer id, @RequestBody DiceRoll diceRoll) { return
	 * "Dice roll cannot be modified!"; }
	 * 
	 * // Delete dice roll by id
	 * 
	 * @DeleteMapping("/games/{id}") public String deleteDiceRoll(@PathVariable(name
	 * = "id") Integer id) { return "Dice roll cannot not be deleted.\n " +
	 * "However you can delete all dice rolls from a player"; }
	 */

}
