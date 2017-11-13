/*
 * Wildcats
 *
 * Class that represents the state of a specific train station in the train system.
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TrainSimulation{
	
	private int totalSimulationRunTime; 
	private int simulatedSeconds;
	private long startTime;
	private TrainSystemManager manager;
	private Train train0;
	private Train train1;
	private Train train2;
	private Train train3;
	private Train train4;
	private Thread thread0;
	private Thread thread1;
	private Thread thread2;
	private Thread thread3;
	private Thread thread4;
	private ArrayList<ArrayList<PassengerArrival>> majorList;  
	
	private static int totalPassengerRequests;
	private static int totalPassengersLoaded;
	private static int totalPassengersUnloaded;
	private static int currentPassengersOnStations;
	private static int currentPassengersOnTrains;
	
	public TrainSimulation(){
		totalSimulationRunTime = 0;
		simulatedSeconds = 0;
		startTime = System.currentTimeMillis();
		manager = new TrainSystemManager();
		train0 = new Train(0, manager);
		train1 = new Train(1, manager);
		train2 = new Train(2, manager);
		train3 = new Train(3, manager);
		train4 = new Train(4, manager);
		majorList = new ArrayList<ArrayList<PassengerArrival>>();
		
		totalPassengerRequests = 0;
		totalPassengersLoaded = 0;
		totalPassengersUnloaded = 0;
		currentPassengersOnStations = 0;
		currentPassengersOnTrains = 0;
	}
	
	public void start(){
		System.out.println("***** START OF THE TRAIN SIMULATION *****");
		passengerArrivals();
		thread0 = new Thread(train0);
		thread1 = new Thread(train1);
		thread2 = new Thread(train2);
		thread3 = new Thread(train3);
		thread4 = new Thread(train4);
		thread0.start();
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		totalPassengerRequests      = 0;
		totalPassengersLoaded       = 0;
		totalPassengersUnloaded     = 0;
		currentPassengersOnStations = 0;
		currentPassengersOnTrains   = 0;
		
		while(SimClock.getTime() <= totalSimulationRunTime){
			if(checkTime() == true){  
				for(int i = 0; i < majorList.size(); i++){
					for(int j = 0; j < majorList.get(i).size(); j++){
						PassengerArrival passengerArrival = majorList.get(i).get(j);
						if ((SimClock.getTime() % passengerArrival.getTimePeriod() == 0) && (SimClock.getTime() != 0)){ // Checks if time is not 0.
							int station = i;
							manager.setPassengers(station, passengerArrival.getDestinationTrainStation(), passengerArrival.getNumPassengers()); //spawning the passengers
							
							//String Print for passengers requests.
							System.out.println(String.format("[%d Simulated Seconds] PASSENGER REQUEST   : There are %d passengers at Station %d requesting to go to Station %d.", 
								SimClock.getTime(), passengerArrival.getNumPassengers(), station, passengerArrival.getDestinationTrainStation()));
							totalPassengerRequests += passengerArrival.getNumPassengers();
							}
						}
					}
				}
			}
		
		totalPassengersLoaded = train0.getTotalLoadedPassengers() + train1.getTotalLoadedPassengers() + train2.getTotalLoadedPassengers() +
				train3.getTotalLoadedPassengers() + train4.getTotalLoadedPassengers(); 
		totalPassengersUnloaded = train0.getTotalUnloadedPassengers() + train1.getTotalUnloadedPassengers() + train2.getTotalUnloadedPassengers() +
				train3.getTotalUnloadedPassengers() + train4.getTotalUnloadedPassengers(); 
			
		System.out.println("\n");
		printTrainState();
		thread0.interrupt();
		thread1.interrupt();
		thread2.interrupt();
		thread3.interrupt();
		thread4.interrupt();
	}
	
	public void passengerArrivals(){
		try{
			File inFile = new File("src/TrainConfig.txt");
			Scanner scanner = new Scanner(inFile);
			scanner.useDelimiter("\n");
			totalSimulationRunTime = Integer.parseInt(scanner.next().replaceAll("\r", ""));
			simulatedSeconds = Integer.parseInt(scanner.next().replaceAll("\r", ""));
			int currentTrainStation = 0;
			while(scanner.hasNext()){
				String tempString = scanner.next();
				ArrayList<PassengerArrival> passengerList = new ArrayList<>();
				String[] currentLine = tempString.split(";");
				for(int j = 0; j < currentLine.length; j++){
					String[] arrival = currentLine[j].split(" ");
					PassengerArrival passenger = new PassengerArrival();
					passenger.setNumPassengers(Integer.parseInt(arrival[0]));
					passenger.setDestinationTrainStation(Integer.parseInt(arrival[1]));
					passenger.setTimePeriod(Integer.parseInt(arrival[2].replaceAll("\r", "")));
					
					int estimatedTime = Integer.parseInt(arrival[2].replaceAll("\r", ""))+20+(5*(Math.abs(currentTrainStation - Integer.parseInt(arrival[1]))-1));
					passenger.setExpectedTimeOfArrival(estimatedTime);
					passengerList.add(passenger);
				}
				majorList.add(passengerList);
				currentTrainStation++;
			}
		scanner.close();
		}
		catch (FileNotFoundException e){
			System.out.println("File not found!");
		}
	}
	
	public boolean checkTime(){
		long mostCurrentTime = System.currentTimeMillis();
		if(mostCurrentTime - startTime >= simulatedSeconds){
			startTime = mostCurrentTime; 
			SimClock.tick();
			return true;
		}
		else
			return false;
		}
	
	public int checkNeg(Train train){
		/*
		 * Checks if passengers were loaded and unloaded correctly.
		 */
		int passengers = train.getNumPassengers();
		if (passengers<0){
			train.setNumPassengers(passengers+(2*passengers));
		}
		return train.getNumPassengers();
	}
	
	public void printTrainState(){
		/*
		 * Prints the state of the Trains and train stations described in
		 * Simulation State.
		 */
		String returnString = "***** END OF SIMULATION STATISTICS ***** \n";
		returnString += (String.format("Total Passengers Requests: %d \n", totalPassengerRequests));
		returnString += (String.format("Total Passengers Loaded  : %d \n", totalPassengersLoaded));
		returnString += (String.format("Total Passengers Unloaded: %d \n\n", totalPassengersUnloaded));
		
		returnString += "TRAIN 0 \n";
		returnString += "Current passengers on Train 0: " + checkNeg(train0) + "\n";
		returnString += "Total Loads on Train 0: "   + train0.getTotalLoadedPassengers()   + "\n";
		returnString += "Total Unloads on Train 0: " + train0.getTotalUnloadedPassengers() + "\n\n";
		
		returnString += "TRAIN 1 \n";
		returnString += "Current passengers on Train 1: " + checkNeg(train1) + "\n";
		returnString += "Total Loads on Train 1: "   + train1.getTotalLoadedPassengers()   + "\n";
		returnString += "Total Unloads on Train 1: " + train1.getTotalUnloadedPassengers() + "\n\n";
		
		returnString += "TRAIN 2 \n";
		returnString += "Current passengers on Train 2: " + checkNeg(train2) + "\n";
		returnString += "Total Loads on Train 2: "   + train2.getTotalLoadedPassengers()   + "\n";
		returnString += "Total Unloads on Train 2: " + train2.getTotalUnloadedPassengers() + "\n\n";
		
		returnString += "TRAIN 3 \n";
		returnString += "Current passengers on Train 3: " + checkNeg(train3) + "\n";
		returnString += "Total Loads on Train 3: "   + train3.getTotalLoadedPassengers()   + "\n";
		returnString += "Total Unloads on Train 3: " + train3.getTotalUnloadedPassengers() + "\n\n";
		
		returnString += "TRAIN 4 \n";
		returnString += "Current passengers on Train 4: " + checkNeg(train4) + "\n";
		returnString += "Total Loads on Train 4: "   + train4.getTotalLoadedPassengers()   + "\n";
		returnString += "Total Unloads on Train 4: " + train4.getTotalUnloadedPassengers() + "\n\n";
		
		returnString += "STATIONS \n";
		for (int i = 0; i < 5; i++){
			int total = 0;
			for (int j = 0; j < 5; j++){
				total += manager.getStations()[i].getPassengerRequests()[j];
			}
			returnString += (String.format("Passengers Waiting at Station %d: %d\n", i, total));
			
		}		
		System.out.println(returnString);
	}
	
	// Getters & Setters
	public static int getTotalPassengerRequests(){return totalPassengerRequests;}
	public static void setTotalPassengerRequests(int totalPassengerRequests){TrainSimulation.totalPassengerRequests = totalPassengerRequests;}
	public static int getTotalPassengersLoaded(){return totalPassengersLoaded;}
	public static void setTotalPassengersLoaded(int totalPassengersLoaded){TrainSimulation.totalPassengersLoaded = totalPassengersLoaded;}
	public static int getTotalPassengersUnloaded(){return totalPassengersUnloaded;}
	public static void setTotalPassengersUnloaded(int totalPassengersUnloaded){TrainSimulation.totalPassengersUnloaded = totalPassengersUnloaded;}
	public static int getCurrentPassengersOnStations(){return currentPassengersOnStations;}
	public static void setCurrentPassengersOnStations(int currentPassengersOnStations){TrainSimulation.currentPassengersOnStations = currentPassengersOnStations;}
	public static int getCurrentPassengersOnTrains(){return currentPassengersOnTrains;}
	public static void setCurrentPassengersOnTrains(int currentPassengersOnTrains){TrainSimulation.currentPassengersOnTrains = currentPassengersOnTrains;}
}
