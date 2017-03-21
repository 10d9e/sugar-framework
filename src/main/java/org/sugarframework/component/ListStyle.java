package org.sugarframework.component;

public enum ListStyle {

	DEFAULT("list-group"), UNSTYLED("list-unstyled"), INLINE("list-inline");

	private ListStyle(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
