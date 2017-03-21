package org.sugarframework.component;

public enum ColumnWidth {

	ONE("col-md-1"), TWO("col-md-2"), THREE("col-md-3"), FOUR("col-md-4"), FIVE("col-md-5"), SIX("col-md-6"), SEVEN(
			"col-md-7"), EIGHT("col-md-8"), NINE("col-md-9"), TEN("col-md-10"), ELEVEN("col-md-11"), TWELVE("col-md-12");

	private ColumnWidth(final String text) {
		this.text = text;
	}

	private final String text;
	
	public static ColumnWidth find(String s){
		for(ColumnWidth w : ColumnWidth.values()){
			if(s.contains(w.toString())){
				return w;
			}
		}
		return null;
	}
	
	public static ColumnWidth fromString(String s){
		for(ColumnWidth w : ColumnWidth.values()){
			if(w.toString().equals(s)){
				return w;
			}
		}
		return null;
	}

	@Override
	public String toString(){
		return text + " sugar-column";
	}
	
	public String getLiteralString() {
		return super.toString();
	}
	
	
}
