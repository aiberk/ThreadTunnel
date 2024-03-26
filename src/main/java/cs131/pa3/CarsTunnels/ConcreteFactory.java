package cs131.pa3.CarsTunnels;

import cs131.pa3.Abstract.Direction;
import cs131.pa3.Abstract.Factory;
import cs131.pa3.Abstract.Tunnel;
import cs131.pa3.Abstract.Vehicle;
import cs131.pa3.CarsTunnels.BasicTunnel;

/**
 * The class implementing the Factory interface for creating instances of
 * classes
 * 
 * @author cs131a
 *
 */
public class ConcreteFactory implements Factory {

    @Override
    public BasicTunnel createNewBasicTunnel(String name) {
        BasicTunnel tunnel = new BasicTunnel(name);
        return tunnel;
    }

    @Override
    public Vehicle createNewCar(String name, Direction direction) {
        Vehicle car = new Car(name, direction);
        return car;
    }

    @Override
    public Vehicle createNewSled(String name, Direction direction) {
        Vehicle sled = new Sled(name, direction);
        return sled;
    }
}
