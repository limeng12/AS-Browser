package Utility.Structuure;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColorTable {
	public ColorTable() {

		super();
	}

	private static HashMap<String, Color> mcolorTable = new HashMap<String, Color>();

	public static void buildColorTable() {
		if (mcolorTable.size() != 0)
			return;

		// mcolorTable.put("Snow", new Color(255, 250, 250));
		mcolorTable.put("Gainsboro", new Color(220, 220, 220));
		mcolorTable.put("Moccasin", new Color(255, 228, 181));
		mcolorTable.put("MediumSlateBlue", new Color(123, 104, 238));
		mcolorTable.put("SteelBlue", new Color(70, 130, 180));
		mcolorTable.put("DarkSlateGray2", new Color(141, 238, 238));
		mcolorTable.put("SeaGreen3", new Color(67, 205, 128));
		mcolorTable.put("Green2", new Color(0, 238, 0));
		mcolorTable.put("OliveDrab1", new Color(192, 255, 62));
		mcolorTable.put("OliveDrab4", new Color(105, 139, 34));
		mcolorTable.put("Yellow1", new Color(255, 255, 0));
		mcolorTable.put("Yellow4", new Color(139, 139, 0));
		mcolorTable.put("RosyBrown", new Color(188, 143, 143));
		// mcolorTable.put("Beige", new Color(245, 245, 220));
		mcolorTable.put("SandyBrown", new Color(244, 164, 96));
		mcolorTable.put("OrageRed", new Color(255, 69, 0));
		mcolorTable.put("Tan1", new Color(255, 165, 79));
		mcolorTable.put("Pink", new Color(255, 192, 203));
		mcolorTable.put("Violet", new Color(238, 130, 238));
		// mcolorTable.put("Bisque1", new Color(255, 228, 196));
		mcolorTable.put("RosyBrown", new Color(188, 143, 143));

	}

	public static Color getRandomColor() {
		if (mcolorTable.size() == 0)
			return new Color(255, 255, 0);

		int n = (int) (Math.random() * (mcolorTable.size() - 1));
		Iterator<Map.Entry<String, Color>> ite = mcolorTable.entrySet()
				.iterator();
		int i = 0;
		Color result = new Color(255, 255, 0);
		while (ite.hasNext()) {
			ite.next();
			if (i == n) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) ite.next();
				result = (Color) entry.getValue();
				break;
			}
			i++;
		}

		return result;

	}

	public static Color getAColor(String name) {
		return mcolorTable.get(name);

	}

	public static void main(String[] args) {
		buildColorTable();
		ColorTable.getRandomColor();

	}

}
