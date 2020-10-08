package com.dicegame.mongodb.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "diceRoll")
public class DiceRoll {

	// ATTRIBUTES

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //AUTOGENERATE???

	private int dice1;

	private int dice2;

	private boolean won;

	// Entities relationship - Many to one 
	private Long player_id;

	// CONSTRUCTORS

	public DiceRoll() {
	}

	public DiceRoll(Long id, int dice1, int dice2, Player player) {
		this.id = id; 
		this.dice1 = dice1;
		this.dice2 = dice2;
		this.won = checkIfWon();
		this.player_id = player.getId();
	}

	// GETTERS & SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDice1() {
		return dice1;
	}

	public void setDice1(int dice1) {
		this.dice1 = dice1;
	}

	public int getDice2() {
		return dice2;
	}

	public void setDice2(int dice2) {
		this.dice2 = dice2;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public Long getPlayer() {
		return player_id;
	}

	public void setPlayer(Long player) {
		this.player_id = player;
	}

	// METHODS

	// Dice roll information
	@Override
	public String toString() {
		return "Dice Roll [id = " + id + ", dice 1 = " + dice1 + ", dice 2 = " + dice2 + ", roll won =" + won + ", player = " + player_id
				+ " ]";
	}

	// Check if dice roll was won (sum of dices must be 7 in order to win)
	public boolean checkIfWon() {
		int sum = dice1 + dice2;
		if (sum == 7) {
			won = true;
		} else {
			won = false;
		}
		return won;
	}
}
