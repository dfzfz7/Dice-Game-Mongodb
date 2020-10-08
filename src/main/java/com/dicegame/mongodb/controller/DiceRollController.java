package com.dicegame.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	public String createDiceRoll(@PathVariable(name = "id") Long id) {
		// Get player to use roll the dices method
		Player player = playerServiceImpl.getPlayer(id);
		DiceRoll diceRoll = player.rollTheDices();
		// Add dice roll to database
		diceRollServiceImpl.createDiceRoll(diceRoll);
		return diceRoll.toString();
	}

	// Delete all dice rolls from a player
	@DeleteMapping("/players/{id}/games")
	public String deleteDiceRolls(@PathVariable(name = "id") Long id) {
		// Get list of dice rolls from a player
		Player player = playerServiceImpl.getPlayer(id);
		List<DiceRoll> diceRolls = player.getDiceRoll();
		// Delete each dice roll from player
		for (DiceRoll roll : diceRolls) {
			Long idRoll = roll.getId();
			diceRollServiceImpl.deleteDiceRoll(idRoll);
		}
		// Update successRate to 0
		player.setSuccessRate(0.0);
		return "Dice rolls from player " + player.getPlayerName() + " have been deleted";
	}

	/* NOT USED METHODS
	 * 
	 * // Get all dice rolls
	 * 
	 * @GetMapping("/games") public List<DiceRoll> listDiceRolls() { return
	 * diceRollServiceImpl.listDiceRolls(); }
	 * 
	 * // Get dice roll by id
	 * 
	 * @GetMapping("/games/{id}") public DiceRoll getDiceRoll(@PathVariable(name
	 * = "id") Long id) { return diceRollServiceImpl.getDiceRoll(id); }
	 * 
	 * // Update dice roll
	 * 
	 * @PutMapping("/games/{id}") public String
	 * updateRollDice(@PathVariable(name = "id") Long id, @RequestBody DiceRoll
	 * diceRoll) { return "Dice roll cannot be modified!"; }
	 * 
	 * // Delete dice roll by id
	 * 
	 * @DeleteMapping("/games/{id}") public String
	 * deleteDiceRoll(@PathVariable(name = "id") Long id) { return
	 * "Dice roll cannot not be deleted.\n " +
	 * "However you can delete all dice rolls from a player"; }
	 */

}
