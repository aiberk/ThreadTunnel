/**
     * Car Tunells.
     * Known Bugs: Kill working but not passing tests
     *
     * @author Abraham Iberkleid
     * aiberkleid@brandeis.edu
     * March 26, 2024
     * COSI 131A PA3
     */
package cs131.pa3.CarsTunnels;

import java.util.concurrent.atomic.AtomicBoolean;

import cs131.pa3.Abstract.Direction;
import cs131.pa3.Abstract.Tunnel;
import cs131.pa3.Abstract.Vehicle;

/**
 * 
 * The class for the Basic Tunnel, extending Tunnel.
 * 
 * @author cs131a
 *
 */
public class BasicTunnel extends Tunnel {
	private int numCars = 0;
	private int numSleds = 0;
	private Direction direction = null;

	/**
	 * Constructs a new BasicTunnel with the given name.
	 *
	 * @param name The name of the tunnel.
	 */
	public BasicTunnel(String name) {
		super(name);
	}

	/**
	 * Checks if it is safe for a vehicle to enter the tunnel.
	 *
	 * @param vehicle The vehicle trying to enter.
	 * @return true if it is safe for the vehicle to enter, false otherwise.
	 */
	private synchronized boolean isSafeToEnter(Vehicle vehicle) {
		boolean safe = false;
		Direction vehicleDirection = vehicle.getDirection();
		if (vehicle instanceof Car) {
			safe = (numSleds == 0) && (numCars < 3) &&
					((direction == null) || (direction == vehicleDirection));
		}
		if (vehicle instanceof Sled) {
			safe = (numSleds == 0) && (numCars == 0) &&
					((direction == null) || (direction == vehicleDirection));
		}
		return safe;
	}

	/**
	 * Tries to enter the tunnel with the given vehicle.
	 * Updates the number of cars or sleds in the tunnel if the vehicle can enter.
	 *
	 * @param vehicle The vehicle attempting to enter.
	 * @return true if the vehicle successfully enters, false otherwise.
	 */
	@Override
	protected synchronized boolean tryToEnterInner(Vehicle vehicle) {
		String vehicleType = vehicle instanceof Car ? "Car" : vehicle instanceof Sled ? "Sled" : null;

		while (!isSafeToEnter(vehicle)) {
			return false;
		}

		switch (vehicleType) {
			case "Car":
				numCars = numCars + 1;
				break;
			case "Sled":
				numSleds = numSleds + 1;
				break;
			default:
				return false;

		}

		if (direction == null) {
			direction = vehicle.getDirection();
		}
		notifyAll();
		return true;
	}

	/**
	 * Exits the tunnel with the given vehicle.
	 * Updates the number of cars or sleds in the tunnel and the direction if
	 * necessary.
	 *
	 * @param vehicle The vehicle exiting the tunnel.
	 */
	@Override
	protected synchronized void exitTunnelInner(Vehicle vehicle) {
		if (vehicle instanceof Car) {
			numCars = numCars - 1;
		}
		if (vehicle instanceof Sled) {
			numSleds = numSleds - 1;
		}

		if (numCars == 0 && numSleds == 0) {
			direction = null;
		}
		notifyAll();
	}
}