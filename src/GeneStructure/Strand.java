package GeneStructure;

public enum Strand {
	NEGATIVE {
		public String toSymbol() {
			return "-";
		}
	},
	POSITIVE {
		public String toSymbol() {
			return "+";
		}
	},

	UNKNOWN {
		public String toSymbol() {
			return "*";
		}
	};

	public abstract String toSymbol();
}
