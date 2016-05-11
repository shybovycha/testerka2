package checker;

import checker.entities.SolutionResult;
import checker.entities.TestCase;
import checker.repositories.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by shybovycha on 10/05/16.
 */
class InvalidFieldFormatException extends Exception {
    public InvalidFieldFormatException(String fieldStr, String message) {
        super(String.format("Invalid field format for string '%s'. %s", fieldStr, message));
    }
}

class InvalidMoveFormatException extends Exception {
    public InvalidMoveFormatException(String moveStr, String message) {
        super(String.format("Invalid move format for string '%s'. %s", moveStr, message));
    }
}

class InvalidCarFormatException extends Exception {
    public InvalidCarFormatException(String carStr, String message) {
        super(String.format("Invalid car format for string '%s'. %s", carStr, message));
    }
}

class Car {
    public Point pos;
    public int len;
    public char dir, id;

    public Car(char id, int x, int y, char direction, int length) {
        this.id = id;
        this.pos = new Point(x, y);
        this.len = length;
        this.dir = direction;
    }

    public boolean checkCollision(Point p) {
        return (dir == 'V' && p.x == pos.x && p.y >= pos.y && p.y <= pos.y + len - 1)
                || (dir == 'H' && p.y == pos.y && p.x >= pos.x && p.x <= pos.x + len - 1);
    }

    public Car dup() {
        return new Car(this.id, this.pos.x, this.pos.y, this.dir, this.len);
    }

    public Car move(Move move) {
        Car newCar = this.dup();

        if (move.dir == 'D') newCar.pos.y -= move.d;

        if (move.dir == 'U') newCar.pos.y += move.d;

        if (move.dir == 'L') newCar.pos.x -= move.d;

        if (move.dir == 'R') newCar.pos.x += move.d;

        return newCar;
    }

    public static Car parseString(String input) throws InvalidCarFormatException {
        String[] pieces = input.split(" ");

        if (pieces.length != 5)
            throw new InvalidCarFormatException(input, String.format("Expected 5 car descriptors, but got %d", pieces.length));

        if (pieces[0].length() != 1)
            throw new InvalidCarFormatException(input, "Car ID is invalid");

        char carId = pieces[0].charAt(0);

        int x = Integer.parseInt(pieces[1]), y = Integer.parseInt(pieces[2]);

        if (x < 0 || x > 5 || y < 0 || y > 5)
            throw new InvalidCarFormatException(input, String.format("Car position (%d, %d) is invalid", x, y));

        char direction = pieces[3].charAt(0);

        if (direction != 'V' && direction != 'H')
            throw new InvalidCarFormatException(input, "Car direction is invalid");

        Integer length = Integer.parseInt(pieces[4]);

        if (length < 1 || length > 6)
            throw new InvalidCarFormatException(input, String.format("Car length (%d) is invalid", length));

        return new Car(carId, x, y, direction, length);
    }

    @Override
    public String toString() {
        return String.format("%c [%c] %s", this.id, this.dir, this.pos.toString());
    }
}

class Field {
    public List<Car> cars;

    public Field(List<Car> cars) {
        this.cars = new ArrayList<>();
        this.cars.addAll(cars);
    }

    public boolean isMoveValid(Move move) {
        Car car = move.car;

        List<Car> otherCars =
                this.cars.stream().filter(c -> c.id != car.id).collect(Collectors.toList());

        if (car.dir == 'V') {
            for (int py = car.pos.y; py > -1; --py) {
                Point p = new Point(car.pos.x, py);

                if (otherCars.stream().anyMatch(c -> c.checkCollision(p))) return false;
            }

            for (int py = car.pos.y + car.len; py < 6; ++py) {
                Point p = new Point(car.pos.x, py);

                if (otherCars.stream().anyMatch(c -> c.checkCollision(p))) return false;
            }
        } else {
            for (int px = car.pos.x - 1; px > -1; --px) {
                Point p = new Point(px, car.pos.y);

                if (otherCars.stream().anyMatch(c -> c.checkCollision(p))) return false;
            }

            for (int px = car.pos.x + car.len; px < 6; ++px) {
                Point p = new Point(px, car.pos.y);

                if (otherCars.stream().anyMatch(c -> c.checkCollision(p))) return false;
            }
        }

        return true;
    }

    public Field applyMove(Move move) {
        return new Field(
                this.cars
                        .stream()
                        .map(c -> (c.id == move.car.id) ? c.move(move) : c.dup())
                        .collect(Collectors.toList()));
    }

    protected Car getXCar() {
        return this.cars.stream().filter(c -> c.id == 'X').findFirst().get();
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

class Move {
    public Car car;
    public char dir;
    public int d;

    public Move(Car car, char direction, int distance) {
        this.car = car;
        this.dir = direction;
        this.d = distance;
    }

    @Override
    public String toString() {
        return String.format("%c %c %d", this.car.id, this.dir, this.d);
    }
}

class FieldParser {
    public FieldParser() {}

    public Field parseField(String input) {
        try {
            return parseSilently(input);
        } catch (Exception e) {
            return null;
        }
    }

    protected Field parseSilently(String input) throws InvalidFieldFormatException {
        List<Car> cars = new ArrayList<>();

        Scanner scanner = new Scanner(new StringReader(input));

        try {
            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                cars.add(Car.parseString(scanner.nextLine()));
            }
        } catch (Exception e) {
            // TODO: logger
        }

        return new Field(cars);
    }
}

class MoveParser {
    private Field field;

    public MoveParser(Field field) {
        this.field = field;
    }

    public Move parseOne(String input) throws InvalidMoveFormatException {
        String[] pieces = input.split(" ");

        if (pieces.length != 3)
            throw new InvalidMoveFormatException(input, String.format("Expected 3 move descriptors, but got %d", pieces.length));

        if (pieces[0].length() != 1)
            throw new InvalidMoveFormatException(input, "Car ID is invalid");

        char carId = pieces[0].charAt(0);
        char direction = pieces[1].charAt(0);

        if (direction != 'D' && direction != 'U' && direction != 'R' && direction != 'L')
            throw new InvalidMoveFormatException(input, "Move direction is invalid");

        Integer length = Integer.parseInt(pieces[2]);

        Optional<Car> car = this.field.cars
                .stream()
                .filter(c -> (c.id == carId))
                .findFirst();

        if (!car.isPresent())
            throw new InvalidMoveFormatException(input, String.format("Car with this ID ('%s') does not exist", carId));

        return new Move(car.get(), direction, length);
    }

    public List<Move> parseAll(String input) {
        List<Move> res = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new StringReader(input));

            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                String s = scanner.nextLine();
                res.add(this.parseOne(s));
            }
        } catch (Exception e) {
            // TODO: logger
        }

        return res;
    }
}

class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point subtract(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }

    public int length() {
        return (int) Math.round(Math.sqrt(this.x * this.x - this.y * this.y));
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}

@Component
public class SolutionChecker {
    public SolutionChecker() {}

    @Autowired
    private TestCaseRepository testCaseRepository;

    // gets list of test cases passed successfully
    public List<SolutionResult> check(RunnableSolution solution) {
        List<SolutionResult> results = new ArrayList<>();

        // as we do not have dependency injection here, we have no TestCaseRepository
        // thus make this class being instantiated once
        Iterable<TestCase> testCases = testCaseRepository.findAll();

        for (TestCase testCase : testCases) {
            results.add(new SolutionResult(solution, testCase, checkSingleTest(solution, testCase)));
        }

        return results;
    }

    public boolean checkSingleTest(RunnableSolution solution, checker.entities.TestCase testCase) {
        String output = solution.getOutputFor(testCase.getInput());

        Field field = new FieldParser().parseField(testCase.getInput());

        List<Move> moves = new MoveParser(field).parseAll(output);

        for (Move move : moves) {
            if (!field.isMoveValid(move))
                return false;

            field = field.applyMove(move);
        }

        return field.isSolved();
    }
}
