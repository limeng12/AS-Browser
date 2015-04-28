package Utility.Structuure;

public class Pair<T1, T2> {
	public Pair(T1 v1, T2 v2) {
		this.value1 = v1;
		this.value2 = v2;
	}

	T1 value1;
	T2 value2;

	public String toString() {

		return value1 + "\t" + value2;
	}

	public T1 getValue1() {
		return value1;
	}

	public T2 getValue2() {
		return value2;
	}

	public void setValue1(T1 v1) {
		value1 = v1;
	}

	public void setValue2(T2 v2) {
		value2 = v2;
	}

}