package com.nexware.aajapan.payload;

public class JSTreeState {
	private Boolean opened;
	private Boolean disabled;
	private Boolean selected;

	public JSTreeState(Boolean selected) {
		this.selected = selected;
	}

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}
