package by.vm.readfileapp;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String pathToFile = "data.txt";
        String fromFile = getDataFromFile(pathToFile);
        List<Person> personList = getListsOfPersonsFromFile(new StringBuilder(fromFile));
        List<Person> correctPersonList = getListWithCompleteData(personList);
        List<Person<?>> correctPersonListForReverseSort = getListWithCompleteData(personList);

        String sortType = null;
        String outputType = "console";
        String outputPath = null;

        for (String arg : args) {
            if (arg.startsWith("--sort=") || arg.startsWith("-s=")) {
                sortType = arg.split("=")[1];
            } else if (arg.startsWith("--output=") || arg.startsWith("-o=")) {
                outputType = arg.split("=")[1];
            } else if (arg.startsWith("--path=")) {
                outputPath = arg.split("=")[1];
            }
        }
        if ("file".equals(outputType) && (outputPath == null || outputPath.isEmpty())) {
            System.out.println("Error: Output file path is missing. Use --path=<file>");
            return;
        }
        PrintStream originalOut = System.out;

        if ("file".equals(outputType)) {
            try {
                System.setOut(new PrintStream(outputPath));
            } catch (FileNotFoundException e) {
                System.out.println("Error: Cannot write to file " + outputPath);
                return;
            }
        }
        if (sortType == null) {
            sortPersonsByDepartmentSales(personList);
            System.out.println("-----------------------------------");
            sortPersonsByDepartmentHR(personList);
            System.out.println("-----------------------------------");
            sortPersonsByIncorrectData(personList);
        } else {
            switch (sortType) {
                case "name":
                    sortByNameNaturalOrder(correctPersonList);
                    System.out.println("-----------------------------------");
                    sortByNameByReverseOrder(correctPersonListForReverseSort);
                    break;
                case "salary":
                    sortNaturalOrderBySalary(correctPersonList);
                    System.out.println("-----------------------------------");
                    sortReverseOrderBySalary(correctPersonListForReverseSort);
                    break;
                case "asc":
                    sortByNameNaturalOrder(correctPersonList);
                    System.out.println("-----------------------------------");
                    sortNaturalOrderBySalary(correctPersonList);
                    break;
                case "desc":
                    sortByNameByReverseOrder(correctPersonListForReverseSort);
                    System.out.println("-----------------------------------");
                    sortReverseOrderBySalary(correctPersonListForReverseSort);
                    break;
                default:
                    System.out.println("Error: Unsupported sort type. Use --sort=name or --sort=salary.");
                    break;
            }
        }
        if ("file".equals(outputType)) {
            System.setOut(originalOut);
            System.out.println("Sorting results have been saved to: " + outputPath);
        }
    }

    private static String getDataFromFile(String pathToFile) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(pathToFile);
        if (inputStream == null) {
            System.out.println("The file " + pathToFile + " could not be found.");
        } else {
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }
        }
        return builder.toString();
    }

    private static void sortPersonsByDepartmentSales(List<Person> personList) {
        System.out.println("Sales:");
        List<Person> salesList = new ArrayList<>();
        for (Person p : personList) {
            if ((p.getDepartment().equals(String.valueOf(2)) || p.getDepartment().equals("Sales"))
                    && (isSalaryPositive(p.getSalary()) && isSalaryDigit(p.getSalary()))) {
                salesList.add(p);
                System.out.println(p.getJobTitle() + "," + p.getId() + "," + p.getName() + "," + p.getSalary());
            }
        }
        getPersonsAmountAndAverageSalary(salesList);
    }

    private static void sortPersonsByDepartmentHR(List<Person> personList) {
        System.out.println("HR:");
        List<Person> HRList = new ArrayList<>();
        for (Person p : personList) {
            if ((p.getDepartment().equals(String.valueOf(1).trim()) || p.getDepartment().equals("HR"))
                    && (isSalaryPositive(p.getSalary()) && isSalaryDigit(p.getSalary()))) {
                HRList.add(p);
                System.out.println(p.getJobTitle() + "," + p.getId() + "," + p.getName() + "," + p.getSalary());
            }
        }
        getPersonsAmountAndAverageSalary(HRList);
    }

    private static void sortPersonsByIncorrectData(List<Person> personList) {
        System.out.println("Incorrect data:");
        for (Person p : personList) {
            if (!isSalaryDigit(p.getSalary()) || !isSalaryPositive(p.getSalary())) {

                System.out.println(p.getJobTitle() + "," + p.getId() + "," + p.getName() + "," + p.getSalary());
            }
        }
    }

    private static boolean isSalaryPositive(Object salary) {
        if (salary instanceof Double) {
            return (Double) salary >= 0;
        }
        if (salary instanceof String) {
            try {
                return Double.parseDouble((String) salary) >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean isSalaryDigit(Object salary) {
        if (salary instanceof Double) {
            return true;
        }
        if (salary instanceof String) {
            try {
                Double.parseDouble((String) salary);
                return true;
            } catch (NumberFormatException e) {
            }
        }
        return false;
    }

    private static List getListWithCompleteData(List<Person> personList) {
        List<Person> correctList = new ArrayList<>();
        for (Person p : personList) {
            if (isSalaryPositive(p.getSalary()) && isSalaryDigit(p.getSalary())) {
                correctList.add(p);
            }
        }
        return correctList;
    }


    private static void sortNaturalOrderBySalary(List<Person> personList) {
        System.out.println("Sort by salary by natural oder");
        personList
                .stream()
                .filter(person -> !person.getJobTitle().contains("Manager"))
                .sorted(Comparator.comparing(person -> (Double) person.getSalary()))
                .forEach(p -> System.out.println(p.getJobTitle() + "," + p.getId() + "," + p.getName() + "," + p.getSalary()));

    }

    private static void sortReverseOrderBySalary(List<Person<?>> personList) {
        System.out.println("Sort by salary by reverse order:");
        personList.stream()
                .filter(person -> !person.getJobTitle().contains("Manager"))
                .sorted(Comparator.comparing((Person<?> person) -> {
                    Object salary = person.getSalary();
                    return (salary instanceof Number) ? ((Number) salary).doubleValue() : 0.0;
                }).reversed())
                .forEach(p -> System.out.println(p.getJobTitle() + ", " + p.getId() + ", " + p.getName() + ", " + p.getSalary()));
    }

    private static void sortByNameNaturalOrder(List<Person> personList) {
        System.out.println("Sort by name by natural order");
        personList
                .stream()
                .filter(person -> !person.getJobTitle().contains("Manager"))
                .sorted(Comparator.comparing(Person::getName))
                .forEach(p -> System.out.println(p.getJobTitle() + ", " + p.getId() + ", " + p.getName() + ", " + p.getSalary()));
    }

    public static void sortByNameByReverseOrder(List<Person<?>> personList) {
        System.out.println("Sort by name by reverse order:");
        personList
                .stream()
                .filter(person -> !person.getJobTitle().contains("Manager"))
                .sorted(Comparator.comparing(Person::getName, Comparator.reverseOrder()))
                .forEach(p -> System.out.println(p.getJobTitle() + ", " + p.getId() + ", " + p.getName() + ", " + p.getSalary()));
    }

    private static void getPersonsAmountAndAverageSalary(List<Person> personList) {
        int amount = 0;
        double averageSalary = 0;
        for (Person p : personList) {
            amount++;
            averageSalary += (Double) (p.getSalary()) / personList.size();
        }
        String formatedAverageSalary = String.format("%.2f", averageSalary);
        System.out.println("Amount of persons in department: " + amount);
        System.out.println("Average salary in department: " + formatedAverageSalary);
    }

    private static List<Person> getListsOfPersonsFromFile(StringBuilder builder) {
        List<Person> personList = new ArrayList<>();
        String[] lines = builder.toString().split("\n");

        for (String line : lines) {
            String[] parts = line.split(",");
            String jobTitle = parts[0].trim();
            int id = Integer.parseInt(parts[1].trim());
            String name = parts[2].trim();
            Object salary = parts[3].trim();
            String department = parts.length > 4 ? parts[4].trim() : "Unknown";

            personList.add(new Person<>(jobTitle, id, name, salary, department));
        }
        return personList;
    }
}
