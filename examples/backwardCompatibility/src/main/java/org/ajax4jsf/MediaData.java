package org.ajax4jsf;

import java.awt.Color;
import java.io.Serializable;

public class MediaData implements Serializable{

	private static final long serialVersionUID = 1L;
	Integer Width=110;
	Integer Height=50;
	Color Background=new Color(0,0,0);
	Color DrawColor=new Color(255,255,255);
	public MediaData() {
	}
	public Color getBackground() {
		return Background;
	}
	public void setBackground(Color background) {
		Background = background;
	}
	public Color getDrawColor() {
		return DrawColor;
	}
	public void setDrawColor(Color drawColor) {
		DrawColor = drawColor;
	}
	public Integer getHeight() {
		return Height;
	}
	public void setHeight(Integer height) {
		Height = height;
	}
	public Integer getWidth() {
		return Width;
	}
	public void setWidth(Integer width) {
		Width = width;
	}
}