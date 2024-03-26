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
	private int numWaitingCars = 0;
	private int numWaitingSleds = 0;
	private Direction direction = null;

	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * 
	 * @param name the name of the basic tunnel
	 */
	public BasicTunnel(String name) {
		super(name);
	}

	private synchronized boolean isSafeToEnter(Vehicle vehicle) {
		if (vehicle instanceof Car) {
			return (numSleds == 0) && (numCars < 3) &&
					((direction == null) || (direction == vehicle.getDirection()));
		} else if (vehicle instanceof Sled) {
			return (numSleds == 0) && (numCars == 0) &&
					((direction == null) || (direction == vehicle.getDirection()));
		}
		return false;
	}

	@Override
	protected synchronized boolean tryToEnterInner(Vehicle vehicle) {
		try {
			while (!isSafeToEnter(vehicle)) {
				wait();
			}
			if (numCars == 0 && direction == null) {
				direction = vehicle.getDirection();
			}
			if (vehicle instanceof Car) {
				numCars++;
				numWaitingCars--;
			} else if (vehicle instanceof Sled) {
				numSleds++;
				numWaitingSleds--;
			}
			direction = vehicle.getDirection();
			return true;

		} catch (InterruptedException e) {

			Thread.currentThread().interrupt();
			return false;
		}
	}

	@Override
	protected synchronized void exitTunnelInner(Vehicle vehicle) {
		if (vehicle instanceof Car) {
			numCars--;
		} else if (vehicle instanceof Sled) {
			numSleds--;
		}

		if (numCars == 0 && numSleds == 0) {
			direction = null;
		}
		notifyAll();

	}

}
