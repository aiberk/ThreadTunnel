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

	public BasicTunnel(String name) {
		super(name);
		System.out.println("Created " + name + " Tunnel");
	}

	private synchronized boolean isSafeToEnter(Vehicle vehicle) {
		boolean safe = false;
		if (vehicle instanceof Car) {
			safe = (numSleds == 0) && (numCars < 3) &&
					((direction == null) || (direction == vehicle.getDirection()));
		} else if (vehicle instanceof Sled) {
			safe = (numSleds == 0) && (numCars == 0) &&
					((direction == null) || (direction == vehicle.getDirection()));
		}
		System.out.println("isSafeToEnter check for " + vehicle + ": " + safe + " | Cars: " + numCars + ", Sleds: "
				+ numSleds + ", Direction: " + direction);
		return safe;
	}

	// @Override
	// protected synchronized boolean tryToEnterInner(Vehicle vehicle) {
	// try {
	// while (!isSafeToEnter(vehicle)) {
	// System.out.println("Vehicle waiting: " + vehicle);
	// wait();
	// }
	// if (vehicle instanceof Car) {
	// numCars++;
	// } else if (vehicle instanceof Sled) {
	// numSleds++;
	// }
	// if (direction == null) {
	// direction = vehicle.getDirection();
	// }
	// System.out.println("Vehicle entered: " + vehicle + " | Cars: " + numCars + ",
	// Sleds: " + numSleds
	// + ", Direction: " + direction);
	// return true;
	// } catch (InterruptedException e) {
	// System.out.println("Thread interrupted for vehicle: " + vehicle);
	// Thread.currentThread().interrupt();
	// return false;
	// } finally {
	// notifyAll();
	// }
	// }

	@Override
	protected synchronized boolean tryToEnterInner(Vehicle vehicle) {
		while (!isSafeToEnter(vehicle)) {
			System.out.println("Vehicle waiting: " + vehicle + " | Cars: " + numCars + ", Sleds: " + numSleds
					+ ", Direction: " + direction);
			// Simulate the wait without actually calling wait()
			// NOTE: This is just for debugging and should not be used in production
			return false; // or break; to exit the loop
		}

		if (vehicle instanceof Car) {
			numCars++;
		} else if (vehicle instanceof Sled) {
			numSleds++;
		}

		if (direction == null) {
			direction = vehicle.getDirection();
		}

		System.out.println("Vehicle entered: " + vehicle + " | Cars: " + numCars + ", Sleds: " + numSleds
				+ ", Direction: " + direction);
		// notifyAll() should still be called to ensure correct behavior for other
		// threads
		notifyAll();
		return true;
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
		System.out.println("Vehicle exited: " + vehicle + " | Cars: " + numCars + ", Sleds: " + numSleds
				+ ", Direction: " + direction);
		notifyAll();
	}
}
