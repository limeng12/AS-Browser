package Utility.SequenceFeatureWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Utility.Structuure.Tris;

public class DatabaseUCSC {
	static private boolean ifConnect = false;

	static private Connection conn = null;

	public static void connect() {
		if (ifConnect)
			return;

		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://191.101.1.231/ucsc?"
							+ "user=limeng&password=6231498");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		ifConnect = false;

	}

	public static Tris<String, Integer, Integer> getTranscriptRegion(
			String transcirptEnsemblId) {

		connect();

		Tris<String, Integer, Integer> region = new Tris<String, Integer, Integer>();

		String sqlStr = "select chrom,txStart,txEnd from ensgene where name="
				+ "'" + transcirptEnsemblId + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				String chr = rset.getString("chrom");
				int start = rset.getInt("txStart");
				int end = rset.getInt("txEnd");

				region.setValue1(chr);
				region.setValue2(start);
				region.setValue3(end);

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

	public static String ucscTranscriptNameToEnsemblName(String ucscName) {
		connect();
		String ensemblTranscriptName = "";

		String sqlStr = "select value from knownToEnsembl where name=" + "'"
				+ ucscName + "'";
		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				ensemblTranscriptName = rset.getString("value");

			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ensemblTranscriptName;

	}

	public static String refseqTNameToEnsemblName(String refseqName) {
		connect();
		String ucscTranscriptName = "";

		String sqlStr = "select name from knownToRefSeq where value=" + "'"
				+ refseqName + "'";
		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				ucscTranscriptName = rset.getString("name");

			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ucscTranscriptNameToEnsemblName(ucscTranscriptName);

	}

	public static ArrayList<Tris<String,Integer,Integer>> getASRegion(String chr,int begPos,int endPos){
		connect();
		
		ArrayList<Tris<String,Integer,Integer>> resultRegions=new ArrayList<Tris<String,Integer,Integer>>();

		String sqlStr = "select chrom,chromStart,chromEnd from knownAlt where chrom="+"'"+chr+"'"+" AND chromStart<"+endPos+" AND chromEnd>"+begPos;

		Statement stmt;
		try {
			stmt = conn.createStatement();

			ResultSet rset = stmt.executeQuery(sqlStr);
			while (rset.next()) {
				Tris<String, Integer, Integer> region = new Tris<String, Integer, Integer>();

				//String chr = rset.getString("chrom");
				int start = rset.getInt("chromStart");
				int end = rset.getInt("chromEnd");
								
				region.setValue1(chr);
				region.setValue2(start);
				region.setValue3(end);
				
				resultRegions.add(region);

			}
			rset.close();
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
			System.out.println(sqlStr);
		}

		return resultRegions;
		
		
	}
	
	public static void main(String[] args){
		
		ArrayList<Tris<String,Integer,Integer>> a=getASRegion("chr1",110906515,110931859);
		for(Tris<String,Integer,Integer> ite:a){
			System.out.println(ite.getValue2());
			
			
		}
		
		
	}
	
	
}
