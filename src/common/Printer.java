package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Printer {

	private static int toWriteInt = 0;

	public static void writeToFile(MyNode edge) {

		File log = new File(
				"/Volumes/default/My stuff/Developer/OSMJS/dataPoints.txt");
		// System.out.println(toWriteInt);

		try {
			if (!log.exists()) {
				System.out.println("We had to make a new file.");
				log.createNewFile();
			}

			FileWriter writer = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			writer.write("var " + "p" + toWriteInt
					+ " = new google.maps.LatLng(" + edge.getLat() + ","
					+ edge.getLon() + ");");
			writer.write("\r");
			writer.write("var " + "m" + toWriteInt
					+ " = new google.maps.Marker({" + "position:" + "p"
					+ toWriteInt + "," + " map: map,  title:'" + toWriteInt
					+ "'});");
			writer.write("\r");

			toWriteInt++;

			bufferedWriter.close();

		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}

	}

	public static void writeArrayForPoly(SearchNode edge) {
		File log = new File(
				"/Volumes/default/My stuff/Developer/OSMJS/dataPoints.txt");
		try {
			if (!log.exists()) {
				System.out.println("We had to make a new file.");
				log.createNewFile();
			}

			FileWriter writer = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			writer.write("new google.maps.LatLng(" + edge.getLat() + ","
					+ edge.getLon() + "),");
			writer.write("\r");

			writer.write("\r");

			toWriteInt++;

			bufferedWriter.close();

		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}

	}
	
	public static void writeArrayForPoly(MyNode edge) {
		File log = new File(
				"/Volumes/default/My stuff/Developer/OSMJS/dataPoints.txt");
		try {
			if (!log.exists()) {
				System.out.println("We had to make a new file.");
				log.createNewFile();
			}

			FileWriter writer = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			writer.write("new google.maps.LatLng(" + edge.getLat() + ","
					+ edge.getLon() + "),");
			writer.write("\r");

			writer.write("\r");

			toWriteInt++;

			bufferedWriter.close();

		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}

	}

	public static void writeCircleTest(double lat, double lon) {
		File log = new File(
				"/Volumes/default/My stuff/Developer/OSMJS/circlePoints.txt");
		try {
			if (!log.exists()) {
				System.out.println("We had to make a new file.");
				log.createNewFile();
			}

			FileWriter writer = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			writer.write("var a" + toWriteInt + " = new google.maps.LatLng("
					+ lat + "," + lon + ");");
			writer.write("\r");
			writer.write("var b" + toWriteInt
					+ " = new google.maps.Marker({position: a" + toWriteInt
					+ ", map: map, title: 'start '});");
			writer.write("\r");

			toWriteInt++;

			bufferedWriter.close();

		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}

	}
	public static void writeCircleTest(MyNode node) {
		File log = new File(
				"/Volumes/default/My stuff/Developer/OSMJS/circlePoints.txt");
		try {
			if (!log.exists()) {
				System.out.println("We had to make a new file.");
				log.createNewFile();
			}

			FileWriter writer = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			writer.write("var a" + toWriteInt + " = new google.maps.LatLng("
					+ node.getLat() + "," + node.getLon() + ");");
			writer.write("\r");
			writer.write("var b" + toWriteInt
					+ " = new google.maps.Marker({position: a" + toWriteInt
					+ ", map: map, title: 'start '});");
			writer.write("\r");

			toWriteInt++;

			bufferedWriter.close();

		} catch (IOException e) {
			System.out.println("COULD NOT LOG!!");
		}

	}

}
