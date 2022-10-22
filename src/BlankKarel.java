/*
 * File: BlankKarel.java
 * ---------------------
 * This class is a blank one that you can change at will.
 */

import stanford.karel.*;

public class BlankKarel extends SuperKarel {

	int steps = 0;
	int beepersUsed = 0;
	boolean sweepMap = true;

	// method to move Karel one step forward
	public void oneStep(boolean collectBeepers, boolean putBeepers){
		move();
		steps++;
		if (beepersPresent() && collectBeepers && putBeepers){
			return;
		}
		if (collectBeepers){
			collectBeeper();
		}
		if (noBeepersPresent() && putBeepers ) {
			putBeeper();
		}
	}
	// method to move Karel until it hits a wall
	public void moveToHit(boolean putBeepers, boolean collectBeepers){
		while (frontIsClear()){
			oneStep(collectBeepers,putBeepers);
		}
	}
	// method to collect beeper from ground if present
	public void collectBeeper(){
		if (beepersPresent()){
			pickBeeper();
			beepersUsed--;
		}
	}
	// method to move Karel in diagonal path
	public void diagonalMove(){
		while (frontIsClear()){
			oneStep(false,false);
			turnLeft();
			oneStep(false,true);
			turnRight();
		}
	}
	// method to move Karel a specific number of moves
	public void moveSpecific(int numberOfMoves, boolean collectBeepers, boolean putBeepers){
		for(int i=0; i<numberOfMoves;i++){
			oneStep(collectBeepers, putBeepers);

		}
	}
	// make turn when sweeping map in (Even x Even) or (Odd x Odd) map
	public void makeTurn(int sideDimension){
		for (int i=0;i<sideDimension/2;i++) {
			turnLeft();
			oneStep(true,false);
			turnLeft();
			moveToHit(false, true);
			turnRight();
			oneStep(true,false);
			turnRight();
			moveToHit(false, true);
		}

	}
	// make turn when sweeping map in (Even x Odd) map
	public void makeTurnDiff(int firstSide, int secondSide){
		for (int i=0;i<secondSide/2;i++) {
			moveToHit(false, true);
			turnLeft();
			oneStep(true, false);
			turnLeft();
			moveSpecific(firstSide - 1, true, false);
			turnRight();
			oneStep(true, false);
			turnRight();
		}


	}
	// function to help with dividing (Odd x Odd) map
	public void divideEvenMap(int n)  {
		moveSpecific(n-1,true ,true);
		turnRight();
		moveToHit(true,false);
		turnLeft();
		oneStep(false,true);
		turnLeft();
	}
	// method used to return to original point when no cleaning is needed
	public void returnToOrigin(){
		moveToHit(false,true);
		turnLeft();
		moveToHit(false,true);
		turnLeft();
	}
	// used to divide map when one dimension equals to one
	public void oneLine(int side){
		if (side%2 == 0){
			int n = side/2;
			turnLeft();
			moveSpecific(n,false,false);
			putBeeper();
			moveToHit(false,true);
		}else {
			int n = side/2 ;
			turnLeft();
			moveSpecific(n,false,false);
			putBeeper();
			oneStep(true,true);
			moveToHit(false,true);
		}
	}
	@Override
	public void putBeeper() {
		if (noBeepersPresent()) {
			super.putBeeper();
			beepersUsed++;
		}
	}
	@Override
	public void run() {
		super.run();
		int firstSide = 0;
		int secondSide = 0;
		while (frontIsClear()){
			oneStep(true,false);
			firstSide++;
		}
		turnLeft();
		while (frontIsClear()){
			oneStep(true,false);
			secondSide++;
		}
		if (firstSide <= 3 && secondSide <= 3) {
			return;
		} else if (firstSide == 0){
			turnLeft();
			oneLine(secondSide);
		} else if (secondSide == 0){
			oneLine(firstSide);
		} else if (firstSide%2 == secondSide%2) {
			int n = 0;
			if (firstSide % 2 == 0) { // when map is odd on both sides
				turnLeft();
				if (sweepMap) {
					moveToHit(false, true);
					makeTurn(firstSide);
					turnRight();
					turnRight();
				}else returnToOrigin();
				n = firstSide / 2;
				moveSpecific(n,true,false);
				turnLeft();
				putBeeper();
				moveToHit(true,true);
				turnLeft();
				moveToHit(false,true);
				putBeeper();
				turnLeft();
				diagonalMove();
				turnRight();
				moveToHit(false,false);
				turnRight();
				moveSpecific(n,true,false);
				turnRight();
				putBeeper();
				moveToHit(true,false);
				turnLeft();
				moveToHit(false,false);
				putBeeper();
				turnLeft();
				diagonalMove();
			} else {// when map is even on both sides
				if (sweepMap) {
					makeTurn(firstSide);
					turnLeft();
					oneStep(true, false);
					turnLeft();
					moveToHit(false, true);
					turnLeft();
				}else {
					turnLeft();
					returnToOrigin();
				}
				n = firstSide / 2 + 1;
				moveSpecific(n,true,false);
				putBeeper();
				turnLeft();
				for (int i = 0 ; i<3;i++){
					divideEvenMap(n);}
				moveSpecific(n-1,false,true);
				turnRight();
				moveToHit(true,false);
				turnRight();
				moveToHit(false,false);
			}} else  { // when map has different dimension
			int n = 0;
			if (firstSide % 2 == 0) {
				turnLeft();
				if (sweepMap) {
					makeTurnDiff(firstSide, secondSide);
					moveToHit(false, true);
					turnLeft();
					turnLeft();
					moveSpecific(firstSide - 1, true, false);
					turnRight();
					oneStep(true, false);
					turnRight();
					moveToHit(false, true);
					turnRight();
					turnRight();
				}else returnToOrigin();
				n = firstSide / 2;
				moveSpecific(n,true,false);
				putBeeper();
				turnLeft();
				moveToHit(true,true);
				turnLeft();
				moveToHit(false,true);
				turnLeft();
				n = secondSide / 2 ;
				moveSpecific(n,true,false);
				putBeeper();
				turnLeft();
				moveToHit(true,true);
				turnRight();
				oneStep(false,true);
				turnRight();
				moveToHit(true,true);
				turnLeft();
				moveToHit(false,true);
			} else {
				turnLeft();
				if (sweepMap) {
					makeTurnDiff(firstSide, secondSide);
					moveToHit(false, true);
					turnRight();
					turnRight();
				}else {
					returnToOrigin();
				}
				n = firstSide / 2 ;
				moveSpecific(n,true,false);
				putBeeper();
				turnLeft();
				moveToHit(true,true);
				turnRight();
				oneStep(true,true);
				putBeeper();
				turnRight();
				moveToHit(true,true);
				turnLeft();
				moveToHit(false,true);
				turnLeft();
				n = secondSide / 2;
				moveSpecific(n,true,false);
				putBeeper();
				turnLeft();
				moveToHit(true,true);
				turnLeft();
				moveToHit(false,true);
			}
		}
		System.out.println("steps = " + steps);
		System.out.println("beepersUsed = " + beepersUsed);
	}
}
