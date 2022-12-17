/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	private int[][] gameTable;
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

//This method is responsible for save the number of players in the gameTable matrix
/*and it is allowing the player to roll the dices, select the categories after the dice and
 * printing the results.
 */
	private void playGame() {
		gameTable = new int[N_CATEGORIES][nPlayers];
		int count = 0;
		while(count < N_SCORING_CATEGORIES){
			count++;
			for(int j = 1; j <= nPlayers; j++){
				rollFirst(j);
				for(int i = 0; i < 2; i++){
					continueRollingTheDice(j);
				}
				categorySelector(j);
			}
		}
	}

/*
 * This method is responsible for asking the first players which was inputed to roll the dice
 * and wait after that player clicks the dice button and then it displays the results.
 */
	private void rollFirst(int player){
		display.printMessage(playerNames[player - 1] + " roll the dice");
		display.waitForPlayerToClickRoll(player);
		for( int dice = 0; dice < N_DICE; dice++){
			rollDicer[dice] = rgen.nextInt(1, 6);
		}

		display.displayDice(rollDicer);
	}
	
	
	private void continueRollingTheDice(int player){
		display.printMessage("Please select:");
		display.waitForPlayerToSelectDice();
		for( int i = 0; i <N_DICE; i++ ){
			if( display.isDieSelected(i)){
				rollDicer[i] = rgen.nextInt(1, 6);
			}
		}
		display.displayDice(rollDicer);
	}
	
/*
 * This method is allowing the player to select the category and it saves the results.
 */
	private void categorySelector(int player){
		display.printMessage("Select a cateogry");
		int category = display.waitForPlayerToSelectCategory();
		if(validCategory(player,category)){
			calculateOverall(player,category);
		}
	}
	
	

/*
 * This method is responsible to count the dices by the players and then calculate the overall
 * score based on the specific categories, which I used from the YahtzeeConstants interface	
 */
	private void calculateOverall(int player, int category) {
		int countDice = 0;
		int sum = 0;
		if(category < 7){
			while(countDice != N_DICE){
				if(rollDicer[countDice] == category){
					sum += category;
				}
				countDice++;
			}
		}else if(category == YAHTZEE){
			if(categoryDetector(category)){
				sum+= 50;
			}else{
				sum = 0;
			}
		}else if(category == LARGE_STRAIGHT){
			if(categoryDetector(category)){
				sum+= 40;
			}else{
				sum = 0;
			}
		}else if(category == SMALL_STRAIGHT){
			if(categoryDetector(category)){
				sum+= 30;
			}else{
				sum = 0;
			}
		}else if(category == FULL_HOUSE){
			if(categoryDetector(category)){
				sum+= 25;
			}else{
				sum = 0;
			}
		}
		
	}

/*
 * This method is responsible to check if dice is fitting with the category
 */
	private boolean categoryDetector(int category) {
			ArrayList <Integer> one = new ArrayList<Integer>();
			ArrayList <Integer> two = new ArrayList<Integer>();
			ArrayList <Integer> three = new ArrayList<Integer>();
			ArrayList <Integer> four = new ArrayList<Integer>();
			ArrayList <Integer> five = new ArrayList<Integer>();
			ArrayList <Integer> six = new ArrayList<Integer>();

			for( int dice = 0; dice <N_DICE; dice++){
				if(rollDicer[dice] == 1){
					one.add(1);
				}else if(rollDicer[dice] == 2){
					two.add(1);
				}else if(rollDicer[dice] == 3){
					three.add(1);
				}else if(rollDicer[dice] == 4){
					four.add(1);
				}else if(rollDicer[dice] == 5){
					five.add(1);
				}else if(rollDicer[dice] == 6){
					six.add(1);
				}
			}

			if( category == THREE_OF_A_KIND){
				if( one.size() >=3 || two.size() >= 3|| three.size() >= 3|| four.size() >= 3|| five.size() >= 3|| six.size() >= 3){
					return true;
				}
			}else if( category == FOUR_OF_A_KIND){
				if( one.size() >=4 || two.size() >= 4|| three.size() >= 4|| four.size() >= 4|| five.size() >= 4|| six.size() >= 4){
					return true;
				}
			}else if( category ==SMALL_STRAIGHT){
				if(one.size() >= 1){
					return true;
			}else if(two.size() >= 1){
				return true;
			}else if(three.size() >= 1){
				return true;
			}
			}else if(category == LARGE_STRAIGHT){
				if(one.size() == 1 || two.size() == 1 || three.size() == 1 || four.size() == 1 || five.size() ==1){
					return true;
			}else if(two.size() == 1 || three.size() == 1 || four.size() == 1 || five.size() ==1 || six.size() ==1 ){
				return true;
			}
			}else if(category == YAHTZEE){
				if( one.size() == 5 || two.size() == 5|| three.size() == 5|| four.size() ==5|| five.size() ==5|| six.size() ==5){
					return true;
			}
			}else if(category == CHANCE){
				return true;
			}

		return false;

	}
	
	
	
/*
 * This method is responsible to detect and find out whether the category is valid or not
 */
	private boolean validCategory(int player, int category) {
		if(gameTable[category - 1][player - 1] != 0){
			return false;
		}
		return true;
	}

	
	

/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[] rollDicer = new int[N_DICE];
	

}