import java.util.Scanner;

public class Main {
    public static void guessTheNumber() {
        Scanner scanner = new Scanner(System.in);
        int number = 1 + (int)(10 * Math.random());
        int chances = 1;
        int guess;
        while (chances != 5) {
            System.out.println("Your " + chances + " guess:");
            guess = scanner.nextInt();
            if (guess == number) {
                System.out.println("Congratulations you won!");
                break;
            } else if (guess > number) {
                System.out.println("Your guess is higher than the number.");
                chances++;
            } else if (guess < number) {
                System.out.println("Your guess is lower than the number.");
                chances++;
            }
        }

    }
    public static void main(String[] args) {
        System.out.println("Welcome to number integer guessing game in range 1 - 10.");
        System.out.println("You have 5 chances to win game.");
        guessTheNumber();
    }
}
