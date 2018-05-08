package de.htw.ai.kbe.beleg1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.htw.ai.kbe.ProbsFileUtil.ProbsFileUtil;

public class Utils {
	
	private String outputFilename;			//Dateiname der Ausgabe-.txt-Datei (vom User über Commandline festgelegt)
	private boolean outputFileExists = false;	//Überprüfung ob eine Output-File vom User angegeben wurde
	private Options options;				//Commandline-Options (-p & -o)
	
	//Konstruktor
	public Utils(){
		options = new Options();
		options.addOption("p", true, "Properties");
        options.addOption("o", true, "Output File");
	}
	
	//Einlesen und Verarbeiten der Commandline (Parameter: args[] aus der main-function)
	public void readCommandline(String[] args){
		if(!(args.length==0)){	//Falls args in der Commandline vorhanden wird die Funktion ausgeführt
			CommandLineParser parser = new DefaultParser();       
	        try {
				CommandLine cmd = parser.parse(getCommandlineOptions(), args);		//Commandline einlesen und Optionen hinzufügen
				if(cmd.hasOption("o")){		//Output File-Option (-o example.txt)
					//System.out.println("Output File: " + cmd.getOptionValue("o"));
					File outputFile = new File(cmd.getOptionValue("o"));
					String fileType = outputFile.getName().substring(outputFile.getName().indexOf('.'));	//Überprüfung Outputfile-Dateiendung
					//System.out.println("File Type: " + fileType);
					if(fileType.toLowerCase().equals(".txt")){
						//OutputFile kann erstellt werden
						outputFileExists=true;		
						setOutputFilename(outputFile.getName());
					}
				}
				else {
					outputFileExists=false;
					System.out.println("Keine Output-File angegeben, Ausgabe erfolgt in der Konsole.");
				}
				
				if(cmd.hasOption("p")){		//Properties File-Option (-p example.properties)
					//System.out.println("Properties File: " + cmd.getOptionValue("p"));	
					File propertiesFile = new File(cmd.getOptionValue("p"));
					String fileType = propertiesFile.getName().substring(propertiesFile.getName().indexOf('.'));	//Dateiende der PropertiesFile überprüfen
					//System.out.println("File Type: " + fileType);
					if(fileType.toLowerCase().equals(".properties")){		
						if (propertiesFile.exists()){		//Überprüfen ob die Datei vorhanden ist
							Properties properties = ProbsFileUtil.loadProperties(propertiesFile.getPath());
							System.out.println("Führe aus: '" + properties.getProperty("RunMe") + "'...");
							if(properties.containsKey("RunMe")){	//Überprüfen ob der Key "RunMe" in der PropertiesFile vorhanden ist
								//Aufruf von loadClass um die Klasse im Key "RunMe" auszulesen
								CheckResult results = loadClass(properties.getProperty("RunMe"));	//CheckResult ist eine selbsterstellte Klasse, in welche die Ergebnisse gespeichert werden (Methodenzählung, Namen, etc)	
								if(outputFileExists){		//Wenn eine Output-File vom User angegeben wurde wird die createReportFile-function genutzt
									if(createReportFile(getOutputFilename(), results)){
										System.out.println(outputFilename + " wurde erstellt.");
									}
								}
								else {					//Ohne Output-File angegeben wird alles in der Konsole ausgegeben (printReport-function)
									printReport(results);
								}
							}
							else {
								System.out.println("Fehler: Die Properties-Datei enthält keinen RunMe-Key!");
							}
						}
						else {
							System.out.println("Fehler: Die Properties-Datei " + propertiesFile.getName() + " wurde nicht gefunden!");
						}
					}
				}
				else {
					System.out.println("Bitte geben Sie ihre Properties-Datei an ('-p example.properties')");
				}
				
			} catch (ParseException e) {
				System.out.println("Commandline konnte nicht geparset werden.");
				e.printStackTrace();
			}
		}
	}
	
	//Funktion zum Laden einer Klasse zur Laufzeit, gibt die Ergebnisse der Methoden-Invokes und Zählungen in dem CheckResult-Objekt zurück
	public CheckResult loadClass(String classpath)
	{
		File file = new File("./classesToLoad/");

		try {
		    URL url = file.toURI().toURL();          // file:/c:/myclasses/
		    URL[] urls = new URL[]{url};

		    // Create a new class loader with the directory
		    ClassLoader cl = new URLClassLoader(urls);

			Class app;
			try {
				app = Class.forName(classpath).newInstance().getClass();
			//Class app = Class.forName(classpath);	//In der PropertiesFile angegebene auszuführende Klasse wird aufgerufen
			Method[] methods = app.getDeclaredMethods();	//Alle Methoden die nur innerhalb der Klasse erstellt sind (keine geerbten) werden im Array methods gespeichert
			Method.setAccessible(methods, true);
			int count = 0;		//Variable zum Zählen der @RunMe-Methoden
			ArrayList<String> notInvokeable = new ArrayList<String>();	//Alle Namen der Nicht-ausführbaren Methoden
			ArrayList<String> methodNames = new ArrayList<String>();	//Alle Namen von allen Methoden in der Klasse
			//Alle Methoden durchiterieren
			for(int i=0; i<methods.length; i++){
				if(methods[i].isAnnotationPresent(de.htw.ai.kbe.beleg1.RunMe.class)) {	//Wenn eine @RunMe-Notation bei der Methode steht
						if(methods[i].getParameterCount()>0){	//Überprüfung ob Parameter in der Methode existieren (wenn ja wird diese nicht ausgeführt)
							System.out.println("Konnte die Methode " + methods[i].getName() + " aufgrund der Parameter nicht ausführen.");
							notInvokeable.add(methods[i].getName());	//@RunMe-Methoden mit Parametern gelten auch als NotInvokeable
						}
						else {
							try {
								methods[i].invoke(Class.forName(classpath).newInstance()); 	//Wenn die Methode ausgeführt werden kann wird sie mit "invoke" aufgerufen
								//System.out.println(methods[i].invoke(null));
							}
							catch(InvocationTargetException e){ //Falls die Methode auf die invoke angewendet wird eine Exception wirft wird diese ausgegeben und die Methode als NotInvokeableMethod gewertet
								notInvokeable.add(methods[i].getName());	//@RunMe-Methoden, die nicht ausgeführt werden konnten gelten auch als NotInvokeable
								System.out.println(e.getCause());
							}
							//System.out.println(methods[i].invoke(null));	
						}
						methodNames.add(methods[i].getName());	//Hinzufügen des Methodennamens für alle @RunMe-Methoden
						count++;	//Zähler für @RunMe-Methoden erhöhen
				}
				else {
					notInvokeable.add(methods[i].getName());	//Alle nicht ausführbaren Methodennamen
				}
			}
			CheckResult res = new CheckResult(methods.length, count, methodNames, notInvokeable);	//Ergebnis-Objekt erzeugen
			return res;
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Klasse '" + classpath + "' konnte nicht gefunden werden.");
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setOutputFilename(String n){
		outputFilename = n;
	}
	
	public String getOutputFilename(){
		return outputFilename;
	}
	
	public Options getCommandlineOptions(){
		return options;
	}
	
	//Methode zum Erstellen der Output.txt-Datei
	public boolean createReportFile(String outputFilename, CheckResult results){	//OutputFilename vom User festgelegt, results sind die Ergebnisse der loadClass-Methode (Anzahl der Methoden, Anzahl der @RunMe-Methoden, Liste mit allen @RunMe-Methodennamen, Liste mit allen NotInvokeable-Methodennamen)
		PrintWriter writer;
		ArrayList<String> methodNames = results.getRunMeMethodNames();
		ArrayList<String> notInvokeableMethodNames = results.getNotInvokeableMethodNames();
		try {	//Ausgabe in die Datei
			writer = new PrintWriter(outputFilename, "UTF-8");
			writer.println("Anzahl der Methoden: " + results.getMethodCount());
			writer.println("Anzahl der @RunMe-Methoden: " + results.getRunMeMethodCount());
			writer.println("Namen aller @RunMe-Methoden der Klasse:");
			for(int i=0;i<results.getRunMeMethodNames().size();i++){
				writer.println("\t" + methodNames.get(i));
			}
			writer.println("Namen aller Methoden die nicht Invoked wurden:");
			for(int j=0;j<results.getNotInvokeableMethodNames().size();j++){
				writer.println("\t" + notInvokeableMethodNames.get(j));
			}
	    	writer.close();
	    	return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//Ausgabe der Ergebnisse in der Konsole
	public void printReport(CheckResult results){
		ArrayList<String> methodNames = results.getRunMeMethodNames();
		ArrayList<String> notInvokeableMethodNames = results.getNotInvokeableMethodNames();
		
		System.out.println("----------------------");
		System.out.println("Anzahl der Methoden: " + results.getMethodCount());
		System.out.println("Anzahl der @RunMe-Methoden: " + results.getRunMeMethodCount());
		System.out.println("Namen aller @RunMe-Methoden der Klasse:");
		for(int i=0;i<results.getRunMeMethodNames().size();i++){
			System.out.println("\t" + methodNames.get(i));
		}
		System.out.println("Namen aller Methoden die nicht Invoked wurden:");
		for(int j=0;j<results.getNotInvokeableMethodNames().size();j++){
			System.out.println("\t" + notInvokeableMethodNames.get(j));
		}
		System.out.println("----------------------");
	}
}
