package org.sugarframework.component;

public enum Style {
	STYLE_DEFAULT("default"), STYLE_SUCCESS("success"), STYLE_INFO("info"), STYLE_WARNING("warning"), STYLE_DANGER(
			"danger");

	private Style(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
