package game.view.parser;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import game.callBack.MessageCallback;
import game.tiles.units.player.Player;
import game.gameManager.TileFactory;
import game.gameManager.*;
import game.utils.Position;

public class CLI {
    public CLI() {
    }

    public static void main(String[] args) {
        TileFactory tiles = new TileFactory();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select Your desired player:");
        List<Player> players = tiles.listPlayers();
        for (int i = 0; i < players.size(); i++) {
            System.out.println((i + 1) + ": " + players.get(i).describe());
        }

        int chosenPlayer = -1;
        while (true) {
            try {
                System.out.print("Enter player number (1-6): ");
                chosenPlayer = scanner.nextInt();
                scanner.nextLine();
                if (chosenPlayer >= 1 && chosenPlayer <= players.size()) {
                    break;
                } else {
                    System.out.println("Please select a valid player number between 1 and 6.");
                }
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input. Please enter numeric values only.");
                scanner.nextLine();
            }
        }

        MessageCallback messageCallback = System.out::println;
        LevelFiles fileParser = new LevelFiles(messageCallback);
        Player player = tiles.producePlayer(chosenPlayer);
        System.out.println("Your player is: " + player.getName());

        if (args.length < 1) {
            System.out.println("You must provide a directory path as argument.");
            return;
        }


        try {
            Path path = Paths.get(args[0]);
            if (!Files.isDirectory(path)) {
                System.out.println("Provided path is not a directory: " + path.toAbsolutePath());

                return;
            }



            System.out.println("Loading levels from: " + path.toAbsolutePath());

            List<String> gameLevelFiles;
            try (Stream<Path> walk = Files.walk(path, 1)) {
                gameLevelFiles = walk
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(name -> name.endsWith(".txt"))
                        .sorted()
                        .collect(Collectors.toList());
            }

            if (gameLevelFiles.isEmpty()) {
                System.out.println("No level files (.txt) found in the directory.");
                return;
            }

            List<Level> levels = new ArrayList<>();
            for (String levelFile : gameLevelFiles) {
                System.out.println("Loading level from file: " + levelFile);
                levels.add(fileParser.ExtractLevel(levelFile, player, tiles));
            }

            for (int i = 0; i < levels.size(); i++) {
                Level level = levels.get(i);
                Position playerPosition = level.GetPlayerStartPosition();
                level.getPlayer().setPosition(playerPosition);
                GameManager gameController = new GameManager(level, level.getPlayer(), level.GetEnemies(), messageCallback);

                System.out.println("\nLevel " + (i + 1) + " Started. Good luck!");

                while (!gameController.DeadPlayer() && !gameController.DeadEnemies()) {
                    gameController.printBoard();

                    String userChoice;
                    do {
                        userChoice = scanner.next();
                        if (userChoice.length() != 1 || "wasde".indexOf(userChoice.charAt(0)) == -1) {
                            System.out.println("Invalid input. Use: w-up, s-down, d-right, a-left, e-ability");
                        }
                    } while (userChoice.length() != 1 || "wasde".indexOf(userChoice.charAt(0)) == -1);

                    gameController.tick(userChoice.charAt(0));
                }

                if (gameController.DeadPlayer()) {
                    gameController.printBoard();
                    System.out.println("Game Over X(");
                    break;
                } else if (i == levels.size() - 1) {
                    System.out.println("You Won XD");
                } else {
                    levels.get(i + 1).SetPlayer(level.getPlayer());
                }
            }
        } catch (IOException e) {
            System.err.println("IOException occurred:");
            e.printStackTrace();
        }
    }
}

