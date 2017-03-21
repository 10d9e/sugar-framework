package org.sugarframework.component;

public enum TextStyle {

	DEFAULT(""), MUTED("text-muted"), PRIMARY("text-primary"), SUCCESS("text-success"), INFO("text-info"), WARNING(
			"text-warning"), DANGER("text-danger");

	private TextStyle(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
