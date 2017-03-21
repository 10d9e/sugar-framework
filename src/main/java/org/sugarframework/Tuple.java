package org.sugarframework;

public class Tuple<T, L> {

	public T first;

	public L second;

	public Tuple(T first, L second) {
		super();
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public L getSecond() {
		return second;
	}

	public void setSecond(L second) {
		this.second = second;
	}
	
}
