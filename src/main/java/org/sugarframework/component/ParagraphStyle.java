package org.sugarframework.component;

public enum ParagraphStyle {

	DEFAULT(""), PARAGRAPH_LEAD("lead");

	private ParagraphStyle(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
