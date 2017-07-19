/*
 * Wildcats
 * 
 * Class representing a	train and its behavior.	This class implements the Runnable
 * interface in	order to run in	its	own	thread.	
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Train implements Runnable
{
	private int trainID; // a unique ID (0-4) for a specific train
	private int currentTrainStation; //the current train station the train is at
	private int numPassengers; // the current number of passengers in the train
	private int loadedPassengers; // passengers loaded
	private int totalLoadedPassengers; //total number of passengers this train loaded
	private int totalUnloadedPassengers; //total number of passengers this train unloaded

	private ArrayList<TrainEvent> moveQueue; //contains trainEvents that define the movement of a train and the anticipated time of arriving at a destination
	private int[] passengerDestinations; //an array where the ith element represents the number of current passengers who's destination is the ith train station
	private TrainSystemManager trainsystemmanager; // = new TrainSystemManger();
	boolean addedThisLoop;
	
	public Train(int newTrainID, TrainSystemManager manager){
		trainID                 = newTrainID;
		currentTrainStation     = 0;
		numPassengers           = 0;
		loadedPassengers        = 0;
		totalLoadedPassengers   = 0;
		totalUnloadedPassengers = 0; 
		moveQueue               = new ArrayList<TrainEvent>();
		passengerDestinations   = new int[5];
		trainsystemmanager      = manager;
		addedThisLoop           = false;
	}
	
	public synchronized void run(){
		while (!Thread.interrupted()){
			if (moveQueue.size() == 0){
				addEvents();
				continue;
			}
			else {
				if (SimClock.getTime() % moveQueue.get(0).getExpectedArrivalTime() == 0){ // simclock mod arrivaltime
					if (SimClock.getTime() != 0 && SimClock.getTime() == moveQueue.get(0).getExpectedArrivalTime()){
							// Get passengers in station, load into train.
							TrainStation[] stations = trainsystemmanager.getStations();
							stations[moveQueue.get(0).getDestination()].setApproachingTrain(trainID);
							trainsystemmanager.setStations(stations);
						
							if (numPassengers == 0){
									int passengers = trainsystemmanager.pickUpPassengers(moveQueue.get(0).getDestination());
									
									// String Print Loading Passengers
									System.out.println(String.format("	[%d Simulated Seconds] LOADING PASSENGERS  : Train %d traveled to Station %d and has loaded %d passengers.", 
										SimClock.getTime(), trainID, moveQueue.get(0).getDestination(), passengers));								
									
									currentTrainStation = moveQueue.get(0).getDestination();
									trainsystemmanager.getStations()[moveQueue.get(0).getDestination()].setApproachingTrain(-1);
									totalLoadedPassengers += passengers;
									loadedPassengers += passengers;
									numPassengers = passengers;
									for (int i = 0; i < 5; i ++){
										passengerDestinations = Arrays.copyOf(trainsystemmanager.getStations()[currentTrainStation].getPassengerRequests(), trainsystemmanager.getStations()[currentTrainStation].getPassengerRequests().length);
										if (passengerDestinations[i] != 0){
											TrainEvent newTrainEvent = new TrainEvent();
											newTrainEvent.setDestination(i);
											newTrainEvent.setExpectedArrivalTime(10 + SimClock.getTime() + 5 * Math.abs(currentTrainStation - i));
											
											moveQueue.add(newTrainEvent);
											addedThisLoop = true;
										}
									}
							} else {// unload passengers
								int passengers = passengerDestinations[moveQueue.get(0).getDestination()];
								if (passengers > 0){
									totalUnloadedPassengers += passengers;
									// String Print Unloading Passengers
									System.out.println(String.format("		[%d Simulated Seconds] UNLOADING PASSENGERS: Train %d traveled to Station %d and has unloaded %d passengers.",
										SimClock.getTime(), trainID, moveQueue.get(0).getDestination(), passengers));
									
									passengerDestinations[moveQueue.get(0).getDestination()] = 0;
									numPassengers -= passengers;
								}
						}
						// Now that the passengers are unloaded, remove them from the moveQueue.
						moveQueue.remove(0);
						if (addedThisLoop){
							moveQueue.sort(Comparator.comparing(TrainEvent::getExpectedArrivalTime)); // magic shit
						}
						addedThisLoop = false;
					}
				}
			}
		}
	}
	
	private synchronized void addEvents(){
		int check = trainsystemmanager.passengersWaitingCheck(trainID);
		if (check >= 0){
			if(trainsystemmanager.getStations()[check].getApproachingTrain() == trainID){
				TrainEvent newTrainEvent = new TrainEvent();
				newTrainEvent.setDestination(check);
				newTrainEvent.setExpectedArrivalTime(10 + SimClock.getTime() + 5 * (Math.abs(currentTrainStation - check)-1));
				moveQueue.add(newTrainEvent);				
			}
		}
	}

	public void trainEvent(int eventDestination, int estArrivalTime){
		TrainEvent trainEvent = new TrainEvent();
		trainEvent.setDestination(eventDestination);
	}
	
	// Getters & Setters
	public int getTotalLoadedPassengers(){return totalLoadedPassengers;}
	public void setTotalLoadedPassengers(int totalLoadedPassengers){this.totalLoadedPassengers = totalLoadedPassengers;}
	public int getTotalUnloadedPassengers(){return totalUnloadedPassengers;}
	public void setTotalUnloadedPassengers(int totalUnloadedPassengers){this.totalUnloadedPassengers = totalUnloadedPassengers;}
	public int getCurrentTrainStation(){return currentTrainStation;}
	public void setCurrentTrainStation(int currentTrainStation){this.currentTrainStation = currentTrainStation;}
	public int getNumPassengers(){return numPassengers;}
	public void setNumPassengers(int numPassengers){this.numPassengers = numPassengers;}
}
