package pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour;

import pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour.exceptions.InvalidFieldFormatException;
import pl.edu.uj.mpi.testerka2.solution_checkers.rush_hour.exceptions.InvalidCarFormatException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FieldParser {
    public FieldParser() {}

    public Field parseField(String input) throws InvalidFieldFormatException {
        try (Scanner scanner = new Scanner(new StringReader(input.trim()))) {
            List<Car> cars = new ArrayList<>();

            int n = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < n; i++) {
                cars.add(Car.parseString(scanner.nextLine()));
            }

            return new Field(cars);
        } catch (InvalidCarFormatException e) {
            throw new InvalidFieldFormatException("Can not parse field", e.getMessage());
        }
    }
}
