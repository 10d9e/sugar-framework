package org.sugarframework.component;

public enum BackgroundStyle {

	DEFAULT(""), PRIMARY("bg-primary"), SUCCESS("bg-success"), INFO("bg-info"), WARNING("bg-warning"), DANGER(
			"bg-danger");

	private BackgroundStyle(final String text) {
		this.text = text;
	}

	private final String text;

	@Override
	public String toString() {
		return text;
	}
}
