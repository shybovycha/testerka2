package pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour;

import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidCarIdMoveException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidFieldFormatException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidMoveDirectionException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidMoveFormatException;
import pl.edu.uj.mpi.testerka2.worker.solution_checkers.rush_hour.exceptions.InvalidNumberOfMoveDescriptorsException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MoveParser {
    private final Field field;

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
        char directionChar = pieces[1].charAt(0);

        Move.Direction direction = Move.Direction.fromChar(directionChar)
            .orElseThrow(() -> new InvalidMoveDirectionException(input, directionChar));

        int length = Integer.parseInt(pieces[2]);

        Optional<Car> car = this.field.cars
                .stream()
                .filter(c -> (c.id() == carId))
                .findFirst();

        if (car.isEmpty())
            throw new InvalidCarIdMoveException(input, carId);

        return new Move(car.get(), direction, length);
    }

    public List<Move> parseAll(String input) throws InvalidFieldFormatException {
        List<Move> res = new ArrayList<>();

        try (Scanner scanner = new Scanner(new StringReader(input))) {
            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                String s = scanner.nextLine();
                res.add(this.parseOne(s));
            }
        } catch (InvalidMoveFormatException e) {
            throw new InvalidFieldFormatException("Can not parse field", e.getMessage());
        }

        return res;
    }
}
