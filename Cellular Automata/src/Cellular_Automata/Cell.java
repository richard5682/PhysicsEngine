package Cellular_Automata;

import java.awt.Color;
import java.awt.Graphics2D;

import Interface.JGDrawable;

public class Cell{
	States state = States.AIR;
	
	public Cell() {
		
	}
	public Color GetColor() {
		switch(state) {
		case AIR:
			return null;
		case ANIMAL:
			return new Color(250,200,200);
		case FOOD:
			return Color.ORANGE;
		default:
			return null;
		}
	}
}
