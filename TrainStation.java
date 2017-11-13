/*
 * Wildcats
 * 
 * Class that sets up and controls the simulation.	
 */

public class TrainStation{
	
	private int[] totalDestinationRequests; //an array where the ith element represents the number of passengers who has requested to go to the ith train station
	private int[] arrivedPassengers; //an array where the ith element represents the number of passengers who has reqeusted to go to the ith train station
	private int[] passengerRequests; //an array where the ith element represents the number of people who currently want to travel to the ith train station
	private int approachingTrain; //the trainID that is currently heading to the train station for passenger pickup
	
	public TrainStation() {
		totalDestinationRequests = new int[5];
		arrivedPassengers = new int[5];
		passengerRequests = new int[5];
		approachingTrain = -1; // no train currently heading to this floor
	}
	
	// Getters & Setters
	public int[] getTotalDestinationRequests(){return totalDestinationRequests;}
	public void setTotalDestinationRequests(int[] totalDestinationRequests){this.totalDestinationRequests = totalDestinationRequests;}
	public int[] getArrivedPassengers(){return arrivedPassengers;}
	public void setArrivedPassengers(int from, int numPassengers){arrivedPassengers[from] = numPassengers;}
	public int[] getPassengerRequests(){return passengerRequests;}
	public void setPassengerRequests(int[] passengerRequests){this.passengerRequests = passengerRequests;}
	public int getApproachingTrain(){return approachingTrain;}
	public void setApproachingTrain(int approachingTrain){this.approachingTrain = approachingTrain;}
}
