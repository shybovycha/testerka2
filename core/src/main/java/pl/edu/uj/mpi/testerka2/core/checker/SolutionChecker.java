package pl.edu.uj.mpi.testerka2.core.checker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.uj.mpi.testerka2.core.entities.Solution;
import pl.edu.uj.mpi.testerka2.core.entities.SolutionResult;
import pl.edu.uj.mpi.testerka2.core.entities.TestCase;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionRepository;
import pl.edu.uj.mpi.testerka2.core.repositories.SolutionResultRepository;
import pl.edu.uj.mpi.testerka2.core.repositories.TestCaseRepository;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

class InvalidCarFormatException extends Exception {
    public InvalidCarFormatException(String carStr, String message) {
        super(String.format("Invalid car format for string '%s'. %s", carStr, message));
    }
}

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

class InvalidNumberOfDescriptorsException extends InvalidCarFormatException {
    public InvalidNumberOfDescriptorsException(String carStr, int numberOfDescriptors) {
        super(carStr, String.format("Expected 5 car descriptors, but got %d", numberOfDescriptors));
    }
}

class InvalidCarIdException extends InvalidCarFormatException {
    public InvalidCarIdException(String carStr, String carId) {
        super(carStr, String.format("Invalid car ID (%s)", carId));
    }
}

class InvalidCarPositionException extends InvalidCarFormatException {
    public InvalidCarPositionException(String carStr, int x, int y) {
        super(carStr, String.format("Car position (%d, %d) is invalid", x, y));
    }
}

class InvalidCarDirectionException extends InvalidCarFormatException {
    public InvalidCarDirectionException(String carStr, char direction) {
        super(carStr, String.format("Car direction (%s) is invalid", direction));
    }
}

class InvalidCarLengthException extends InvalidCarFormatException {
    public InvalidCarLengthException(String carStr, int length) {
        super(carStr, String.format("Car length (%d) is invalid", length));
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
            throw new InvalidNumberOfDescriptorsException(input, pieces.length);

        if (pieces[0].length() != 1)
            throw new InvalidCarIdException(input, pieces[0]);

        char carId = pieces[0].charAt(0);

        int x = Integer.parseInt(pieces[1]), y = Integer.parseInt(pieces[2]);

        if (x < 0 || x > 5 || y < 0 || y > 5)
            throw new InvalidCarPositionException(input, x, y);

        char direction = pieces[3].charAt(0);

        if (direction != 'V' && direction != 'H')
            throw new InvalidCarDirectionException(input, direction);

        int length = Integer.parseInt(pieces[4]);

        if (length < 1 || length > 6)
            throw new InvalidCarLengthException(input, length);

        return new Car(carId, x, y, direction, length);
    }

    @Override
    public String toString() {
        return String.format("%c [%c, %d] %s", this.id, this.dir, this.len, this.pos.toString());
    }
}

class Field {
    private static final Logger LOG = LoggerFactory.getLogger(Field.class);

    public List<Car> cars;

    public Field(List<Car> cars) {
        this.cars = new ArrayList<>();
        this.cars.addAll(cars);
    }

    public boolean isMoveValid(Move move) {
        Optional<Car> moveCar = this.cars.stream().filter(c -> c.id == move.car.id).findFirst();

        if (!moveCar.isPresent()) {
            LOG.debug("Move {} is invalid: car does not exist", move);
            return false;
        }

        Car car = moveCar.get();

        List<Car> otherCars =
                this.cars.stream().filter(c -> c.id != car.id).collect(Collectors.toList());

        if (car.dir == 'V') {
            if (move.dir == 'U') {
                for (int py = 0; py < move.d; ++py) {
                    Point p = new Point(car.pos.x, car.pos.y + car.len + py);
                    Optional<Car> other = otherCars.stream().filter(c -> c.checkCollision(p)).findFirst();

                    if (other.isPresent()) {
                        LOG.debug("Move {} is invalid - overlapping at {} with {}", move, p, other);
                        return false;
                    }
                }
            } else if (move.dir == 'D') {
                for (int py = 0; py < move.d; ++py) {
                    Point p = new Point(car.pos.x, car.pos.y - py);
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
            if (move.dir == 'R') {
                for (int px = 0; px < move.d; ++px) {
                    Point p = new Point(car.pos.x + car.len + px, car.pos.y);
                    Optional<Car> other = otherCars.stream().filter(c -> c.checkCollision(p)).findFirst();

                    if (other.isPresent()) {
                        LOG.debug("Move {} is invalid - overlapping at {} with {}", move, p, other);
                        return false;
                    }
                }
            } else if (move.dir == 'L') {
                for (int px = 0; px < move.d; ++px) {
                    Point p = new Point(car.pos.x - px, car.pos.y);
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

    protected Field parseSilently(String input) throws InvalidCarFormatException {
        List<Car> cars = new ArrayList<>();

        try (Scanner scanner = new Scanner(new StringReader(input))) {
            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                cars.add(Car.parseString(scanner.nextLine()));
            }
        }

        return new Field(cars);
    }
}

class InvalidNumberOfMoveDescriptorsException extends InvalidMoveFormatException {
    public InvalidNumberOfMoveDescriptorsException(String moveStr, int numberOfDescriptors) {
        super(moveStr, String.format("Expected 3 move descriptors, but got %d", numberOfDescriptors));
    }
}

class InvalidCarIdMoveException extends InvalidMoveFormatException {
    public InvalidCarIdMoveException(String moveStr, char id) {
        super(moveStr, String.format("Car ID (%s) is invalid", id));
    }

    public InvalidCarIdMoveException(String moveStr, String id) {
        super(moveStr, String.format("Car ID (%s) is invalid", id));
    }
}

class InvalidMoveDirectionException extends InvalidMoveFormatException {
    public InvalidMoveDirectionException(String moveStr, char direction) {
        super(moveStr, String.format("Move direction (%s) is invalid", direction));
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
            throw new InvalidNumberOfMoveDescriptorsException(input, pieces.length);

        if (pieces[0].length() != 1)
            throw new InvalidCarIdMoveException(input, pieces[0]);

        char carId = pieces[0].charAt(0);
        char direction = pieces[1].charAt(0);

        if (direction != 'D' && direction != 'U' && direction != 'R' && direction != 'L')
            throw new InvalidMoveDirectionException(input, direction);

        int length = Integer.parseInt(pieces[2]);

        Optional<Car> car = this.field.cars
                .stream()
                .filter(c -> (c.id == carId))
                .findFirst();

        if (!car.isPresent())
            throw new InvalidCarIdMoveException(input, carId);

        return new Move(car.get(), direction, length);
    }

    public List<Move> parseAll(String input) throws InvalidMoveFormatException {
        List<Move> res = new ArrayList<>();

        try (Scanner scanner = new Scanner(new StringReader(input))) {
            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                String s = scanner.nextLine();
                res.add(this.parseOne(s));
            }
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
    private static final Logger LOG = LoggerFactory.getLogger(SolutionChecker.class);

    private TestCaseRepository testCaseRepository;
    private SolutionRepository solutionRepository;
    private SolutionResultRepository solutionResultRepository;
    private List<SolutionRunner> runnersAvailable;

    @Autowired
    public SolutionChecker(
        TestCaseRepository testCaseRepository,
        SolutionRepository solutionRepository,
        SolutionResultRepository solutionResultRepository,
        List<SolutionRunner> runnersAvailable) {

        this.testCaseRepository = testCaseRepository;
        this.solutionRepository = solutionRepository;
        this.solutionResultRepository = solutionResultRepository;
        this.runnersAvailable = runnersAvailable;
    }

    // gets list of test cases passed successfully
    public void check(Solution solution) {
        Optional<SolutionRunner> runner = runnersAvailable.stream().filter(r -> r.accepts(solution)).findFirst();

        solution.setStatus(Solution.SolutionStatus.CHECKING);
        solutionRepository.save(solution);

        LOG.debug("CHECKING SOLUTION {}", solution.getId());

        if (!runner.isPresent()) {
            solution.setStatus(Solution.SolutionStatus.REJECTED);
            solutionRepository.save(solution);
            LOG.debug("REJECTED - {} HAS NO MATCH", solution.getLanguage());
            return;
        }

        // as we do not have dependency injection here, we have no TestCaseRepository
        // thus make this class being instantiated once
        Iterable<TestCase> testCaseEntities = testCaseRepository.findAll();

        LOG.debug("RUNNING TEST CASES...");

        // List<SolutionResult> results = new ArrayList<>();

        try {
            for (TestCase testCase : testCaseEntities) {
                SolutionResult result = new SolutionResult(solution, testCase, false);

                // we can go on even if one test case was timed out
                try {
                    result = checkSingleTest(runner.get(), solution, testCase);

                    LOG.debug("TEST CASE #{}: {}", testCase.getId(), result.getPassed() ? "PASSED" : "FAILED");
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    result.setOutput("<TIMEOUT>");

                    LOG.error("TEST CASE #{} TIMED OUT", testCase.getId(), e);
                } finally {
                    solutionResultRepository.save(result);

                    solution.getResults().add(result);
                    solutionRepository.save(solution);
                }
            }
        } catch (Exception e) {
            // but for more critical errors we stop checker
            solution.setStatus(Solution.SolutionStatus.RUN_ERROR);
            solution.setErrorMessage(e.getMessage());
            solutionRepository.save(solution);

            LOG.error("ERRORS DURING RUN", e);

            return;
        }

        if (solution.getResults().stream().allMatch(SolutionResult::getPassed)) {
            solution.setStatus(Solution.SolutionStatus.PASSED_CORRECT);
        } else {
            solution.setStatus(Solution.SolutionStatus.PASSED_INCORRECT);
        }

        LOG.debug("DONE with status {}", solution.getStatusString());

        solutionRepository.save(solution);
    }

    public SolutionResult checkSingleTest(SolutionRunner runner, Solution solution, TestCase testCase) throws Exception {
        SolutionResult result = new SolutionResult(solution, testCase);

        String output = runner.getOutputFor(solution, testCase.getInput());

        result.setOutput(output);

        Field field = new FieldParser().parseField(testCase.getInput());

        List<Move> moves = new MoveParser(field).parseAll(output);

        result.setPoints(moves.size());

        for (Move move : moves) {
            if (!field.isMoveValid(move)) {
                LOG.debug("Move {} is not valid", move.toString());
                break;
            }

            field = field.applyMove(move);
        }

        result.setPassed(field.isSolved());

        return result;
    }
}
