package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Field {
    private static final Logger LOG = LoggerFactory.getLogger(Field.class);

    private record Tuple<A, B>(A first, B second) {}

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

        Optional<Tuple<Car, Point>> collision = IntStream.range(0, move.d())
            .mapToObj(i -> {
                if (car.dir() == Car.Direction.Vertical) {
                    if (move.dir() == Move.Direction.Up) {
                        return new Point(car.pos().x(), car.pos().y() + car.len() + i);
                    } else if (move.dir() == Move.Direction.Down) {
                        return new Point(car.pos().x(), car.pos().y() - i);
                    }
                } else if (car.dir() == Car.Direction.Horizontal) {
                    if (move.dir() == Move.Direction.Left) {
                        return new Point(car.pos().x() - i, car.pos().y());
                    } else if (move.dir() == Move.Direction.Right) {
                        return new Point(car.pos().x() + car.len() + i, car.pos().y());
                    }
                }

                return null;
            })
            .flatMap(p -> otherCars.stream().filter(c -> c.checkCollision(p)).map(c -> new Tuple<>(c, p)))
            .findFirst();

        if (collision.isPresent()) {
            collision.ifPresent(t -> LOG.debug("Move {} is invalid - overlapping at {} with {}", move, car, t.first()));
            return false;
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
