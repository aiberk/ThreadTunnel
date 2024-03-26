package cs131.pa3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import cs131.pa3.CarsTunnels.BasicTunnel;
import cs131.pa3.Abstract.Direction;
import cs131.pa3.Abstract.Vehicle;
import cs131.pa3.CarsTunnels.Car;
import cs131.pa3.CarsTunnels.Sled;

public class StudentTests {

    @Test
    public void testSingleCarEntryAndExit() {
        BasicTunnel tunnel = new BasicTunnel("SingleCarTunnel");
        Vehicle car = new Car("Car1", Direction.NORTH);
        assertTrue(tunnel.tryToEnter(car), "Car should be able to enter the tunnel");
        tunnel.exitTunnel(car);
    }

    @Test
    public void testMultipleCarsEntry() {
        BasicTunnel tunnel = new BasicTunnel("MultiCarTunnel");
        Vehicle car1 = new Car("Car1", Direction.NORTH);
        Vehicle car2 = new Car("Car2", Direction.NORTH);
        Vehicle car3 = new Car("Car3", Direction.NORTH);
        assertTrue(tunnel.tryToEnter(car1), "First car should enter");
        assertTrue(tunnel.tryToEnter(car2), "Second car should enter");
        assertTrue(tunnel.tryToEnter(car3), "Third car should enter");
    }

    @Test
    public void testSledEntryWhenEmpty() {
        BasicTunnel tunnel = new BasicTunnel("SledWhenEmptyTunnel");
        Vehicle sled = new Sled("Sled1", Direction.NORTH);
        assertTrue(tunnel.tryToEnter(sled), "Sled should be able to enter when the tunnel is empty");
    }

    @Test
    public void testSledEntryWithCarsPresent() {
        BasicTunnel tunnel = new BasicTunnel("SledWithCarsTunnel");
        Vehicle car1 = new Car("Car1", Direction.NORTH);
        tunnel.tryToEnter(car1);
        Vehicle sled = new Sled("Sled1", Direction.NORTH);
        assertFalse(tunnel.tryToEnter(sled), "Sled should not be able to enter when cars are present");
    }

    @Test
    public void testConcurrentEntriesWithDirectionChange() throws InterruptedException {
        BasicTunnel tunnel = new BasicTunnel("ConcurrentEntriesTunnel");
        Vehicle carNorth = new Car("CarNorth", Direction.NORTH);
        Vehicle sledSouth = new Sled("SledSouth", Direction.SOUTH);
        Thread carThread = new Thread(() -> assertTrue(tunnel.tryToEnter(carNorth)));
        Thread sledThread = new Thread(() -> assertFalse(tunnel.tryToEnter(sledSouth)));
        carThread.start();
        sledThread.start();
        carThread.join();
        sledThread.join();
        tunnel.exitTunnel(carNorth);
    }

    @Test
    public void testSequentialEntryExitMixedVehicles() {
        BasicTunnel tunnel = new BasicTunnel("SequentialEntryExitTunnel");
        Vehicle car1 = new Car("Car1", Direction.NORTH);
        Vehicle sled1 = new Sled("Sled1", Direction.NORTH);
        assertTrue(tunnel.tryToEnter(car1), "Car should enter");
        tunnel.exitTunnel(car1);
        assertTrue(tunnel.tryToEnter(sled1), "Sled should enter");
        tunnel.exitTunnel(sled1);
    }

    @Test
    public void testMaxCapacityWithDirectionEnforcement() {
        BasicTunnel tunnel = new BasicTunnel("MaxCapacityTunnel");
        Vehicle car1 = new Car("Car1", Direction.NORTH);
        Vehicle car2 = new Car("Car2", Direction.NORTH);
        Vehicle car3 = new Car("Car3", Direction.NORTH);
        Vehicle car4 = new Car("Car4", Direction.SOUTH);
        assertTrue(tunnel.tryToEnter(car1));
        assertTrue(tunnel.tryToEnter(car2));
        assertTrue(tunnel.tryToEnter(car3));
        assertFalse(tunnel.tryToEnter(car4), "Fourth car with different direction should not enter");
        tunnel.exitTunnel(car1);
        tunnel.exitTunnel(car2);
        tunnel.exitTunnel(car3);
    }
    
    @Test
    public void testTunnelStateAfterMultipleExits() {
        BasicTunnel tunnel = new BasicTunnel("ExitTestTunnel");
        Vehicle car1 = new Car("Car1", Direction.NORTH);
        Vehicle car2 = new Car("Car2", Direction.NORTH);
        Vehicle car3 = new Car("Car3", Direction.NORTH);
        Vehicle sled1 = new Sled("Sled1", Direction.NORTH);
        assertTrue(tunnel.tryToEnter(car1), "Car1 should enter");
        assertTrue(tunnel.tryToEnter(car2), "Car2 should enter");
        assertTrue(tunnel.tryToEnter(car3), "Car3 should enter");
        tunnel.exitTunnel(car1);
        tunnel.exitTunnel(car2);
        tunnel.exitTunnel(car3);
        assertTrue(tunnel.tryToEnter(sled1), "Sled1 should be able to enter after cars have exited");
        tunnel.exitTunnel(sled1);
    }


}
