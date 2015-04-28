package Utility.Structuure;

public class Quar<T1, T2, T3, T4> {
	public Quar(T1 t1, T2 t2, T3 t3, T4 t4) {
		value1 = t1;
		value2 = t2;
		value3 = t3;
		value4 = t4;

	}

	public Quar() {

	}

	T1 value1;
	T2 value2;
	T3 value3;
	T4 value4;

	public T1 getValue1() {
		return value1;
	}

	public T2 getValue2() {
		return value2;
	}

	public T3 getValue3() {
		return value3;
	}

	public T4 getValue4() {
		return value4;
	}

	public void setValue1(T1 tvalue1) {
		value1 = tvalue1;
	}

	public void setValue2(T2 tvalue2) {
		value2 = tvalue2;
	}

	public void setValue3(T3 tvalue3) {
		value3 = tvalue3;
	}

	public void setValue4(T4 tvalue4) {
		value4 = tvalue4;
	}

}
