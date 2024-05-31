package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Field {
    private static final Logger LOG = LoggerFactory.getLogger(Field.class);

    public List<Car> cars;

    public Field(List<Car> cars) {
        this.cars = new ArrayList<>();
        this.cars.addAll(cars);
    }

    public boolean isMoveValid(Move move) {
        Optional<Car> moveCar = this.cars.stream().filter(c -> c.id() == move.car().id()).findFirst();

        if (moveCar.isEmpty()) {
            LOG.debug("Move {} is invalid: car does not exist", move);
            return false;
        }

        Car car = moveCar.get();

        List<Car> otherCars = this.cars.stream().filter(c -> c.id() != car.id()).toList();

        if (car.dir() == Car.Direction.Vertical) {
            if (move.dir() == Move.Direction.Up) {
                for (int py = 0; py < move.d(); ++py) {
                    Point p = new Point(car.pos().x(), car.pos().y() + car.len() + py);
                    Optional<Car> other = otherCars.stream().filter(c -> c.checkCollision(p)).findFirst();

                    if (other.isPresent()) {
                        LOG.debug("Move {} is invalid - overlapping at {} with {}", move, p, other);
                        return false;
                    }
                }
            } else if (move.dir() == Move.Direction.Down) {
                for (int py = 0; py < move.d(); ++py) {
                    Point p = new Point(car.pos().x(), car.pos().y() - py);
                    Optional<Car> other = otherCars.stream().filter(c -> c.checkCollision(p)).findFirst();

                    if (other.isPresent()) {
                        LOG.debug("Move {} is invalid - overlapping at {} with {}", move, p, other);
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            if (move.dir() == Move.Direction.Right) {
                for (int px = 0; px < move.d(); ++px) {
                    Point p = new Point(car.pos().x() + car.len() + px, car.pos().y());
                    Optional<Car> other = otherCars.stream().filter(c -> c.checkCollision(p)).findFirst();

                    if (other.isPresent()) {
                        LOG.debug("Move {} is invalid - overlapping at {} with {}", move, p, other);
                        return false;
                    }
                }
            } else if (move.dir() == Move.Direction.Left) {
                for (int px = 0; px < move.d(); ++px) {
                    Point p = new Point(car.pos().x() - px, car.pos().y());
                    Optional<Car> other = otherCars.stream().filter(c -> c.checkCollision(p)).findFirst();

                    if (other.isPresent()) {
                        LOG.debug("Move {} is invalid - overlapping at {} with {}", move, p, other);
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public Field applyMove(Move move) {
        return new Field(
                this.cars
                        .stream()
                        .map(c -> (c.id() == move.car().id()) ? c.move(move) : c)
                        .collect(Collectors.toList()));
    }

    protected Car getXCar() {
        return this.cars.stream().filter(c -> c.id() == 'X').findFirst().orElse(null);
    }

    public boolean isSolved() {
        return getXCar().checkCollision(new Point(5, 3));
    }

    public boolean isAnyOverlap(Point p) {
        return this.cars.stream().anyMatch(c -> c.checkCollision(p));
    }

    @Override
    public String toString() {
        return this.cars.stream().map(Car::toString).collect(Collectors.joining(";"));
    }
}
