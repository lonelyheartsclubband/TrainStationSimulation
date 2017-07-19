/*
 * Wildcats
 * 
 * Class that allow trains to manipulate the state of the train stations. There will only	
 * be one TrainSystemManager object constructed in trainSimulation. This object is	
 * shared among all train threads.
 */

public class TrainSystemManager
{
	private static final int NUM_TRAIN_STATIONS = 5;
	private TrainStation[] trainStations;
	
	public TrainSystemManager(){
		// Initializes TrainStation array and puts a new TrainStation at each index
		trainStations = new TrainStation[NUM_TRAIN_STATIONS];
		for (int i = 0; i < NUM_TRAIN_STATIONS; i++){
			trainStations[i] = new TrainStation();
		}
	}
	
	// methods that access and manipulate the state of train stations and implement the locking & synchronization
	
	public synchronized int pickUpPassengers(int station){
		/*
		 * Iterates through the train stations, in order going up from 0 to 4 and checks if there are any passengers 
		 * waiting on those floors. If there are, it'll return the number of passengers it added up which are then "picked up" 
		 * by the train. If it didn't see any on the way up, then it'll check on the way down doing the same thing.
		 */
		int passengers = 0;
		for (int i = 0; i < NUM_TRAIN_STATIONS; i++){
			if (i > station){
				passengers += trainStations[station].getPassengerRequests()[i];//passengerRequests[i];
			}
		}if (passengers == 0){
			for (int i = NUM_TRAIN_STATIONS - 1; i >= 0; i--){
				int[] passengerRequests = trainStations[station].getPassengerRequests();
				passengers += trainStations[station].getPassengerRequests()[i]; //passengerRequests[i];
			}
		}return passengers;
	}
	
	public synchronized int passengersWaitingCheck(int trainID){
		/*
		 * Checks the first station that has no train approaching and has passengers waiting
		 * and set the approaching train of that station to the TrainID. The train with that TrainID 
		 * will go pick up the passengers.
		 */
		for (int i = 0; i < NUM_TRAIN_STATIONS; i++){
			int[] requests = trainStations[i].getPassengerRequests();
			for (int j = 0; j < requests.length; j++){
				if (requests[j] > 0 ){
						if (trainStations[i].getApproachingTrain() < 0){
							trainStations[i].setApproachingTrain(trainID);
							return i;
		}}}}
		// if there are no passengers waiting, return -1
		return -1;
	}

	// Getters & Setters
	public synchronized void setPassengers(int stationFrom, int destinationTrainStation, int numPassengers){trainStations[stationFrom].getPassengerRequests()[destinationTrainStation] = numPassengers;}
	public synchronized TrainStation[] getStations() {return trainStations;}
	public synchronized void setStations(TrainStation[] trainStations){this.trainStations = trainStations;}

}
