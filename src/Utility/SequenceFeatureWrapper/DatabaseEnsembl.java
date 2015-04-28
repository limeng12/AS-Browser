package Utility.SequenceFeatureWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Utility.Structuure.Tris;

public class DatabaseEnsembl {

	static private boolean ifConnect = false;

	static private Connection conn = null;

	public static void connect() {

		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://191.101.1.231/ensembl?"
							+ "user=limeng&password=6231498");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public static String getTranscriptDatabaseId(String transcriptId) {
		if (!ifConnect) {
			connect();
		}

		String transcriptDatabaseId = "";
		String sqlStr = "select transcript_id from transcript where stable_id="
				+ transcriptId;
		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				transcriptDatabaseId = rset.getString("transcript_id");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return transcriptDatabaseId;

	}

	public static Tris<String, Integer, Integer> getTranscriptRegion(
			String transcirptEnsemblId) {

		if (!ifConnect) {
			connect();
		}

		Tris<String, Integer, Integer> region = new Tris<String, Integer, Integer>();

		String sqlStr = "select seq_region_id,seq_region_start,seq_region_end from transcript where stable_id="
				+ "'" + transcirptEnsemblId + "'";

		String seqRegionId = "";

		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				int start = rset.getInt("seq_region_start");
				int end = rset.getInt("seq_region_end");
				region.setValue2(start);
				region.setValue3(end);
				seqRegionId = rset.getString("seq_region_id");

			}
			rset.close();
			stmt.close();

			sqlStr = "select name from seq_region where seq_region_id="
					+ seqRegionId;
			stmt = conn.createStatement();

			rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				String chr = rset.getString("name");
				region.setValue1(chr);

			}
			rset.close();
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
			System.out.println(sqlStr);
		}

		return region;

	}

	public static Tris<String, Integer, Integer> getGeneRegion(
			String geneEnsemblId) {
		if (!ifConnect) {
			connect();
		}

		Tris<String, Integer, Integer> region = new Tris<String, Integer, Integer>();

		String sqlStr = "select seq_region_id,seq_region_start,seq_region_end from gene where stable_id="
				+ "'" + geneEnsemblId + "'";
		String seqRegionId = "";

		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				int start = rset.getInt("seq_region_start");
				int end = rset.getInt("seq_region_end");
				region.setValue2(start);
				region.setValue3(end);
				seqRegionId = rset.getString("seq_region_id");

			}
			rset.close();
			stmt.close();

			sqlStr = "select name from seq_region where seq_region_id="
					+ seqRegionId;
			stmt = conn.createStatement();

			rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				String chr = rset.getString("name");
				region.setValue1(chr);

			}
			rset.close();
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
			System.out.println(sqlStr);

		}

		return region;

	}

	public static void main(String[] args) {
		// DatabaseEnsembl en = new DatabaseEnsembl();
		// en.connect();

	}

}
