package scoreboard;

import scoreboard.entities.Game;
import scoreboard.entities.Team;
import scoreboard.exceptions.TeamAlreadyPlayingException;

public class ScoreBoardRunner {

  private static final Team spain = new Team(1, "Spain");
  private static final Team germany = new Team(2, "Germany");
  private static final Team brazil = new Team(3, "Brazil");
  private static final Team argentina = new Team(4, "Argentina");
  private static final Team senegal = new Team(5, "Senegal");
  private static final Team portugal = new Team(6, "Portugal");

  public static void main(String[] args) throws InterruptedException {
    ScoreBoard scoreBoard = new ScoreBoard();
    try {
      Game game1 = scoreBoard.startGame(spain, germany);
      System.out.println("Game added: " + game1.toString());
      Thread.sleep(1);
      Game game2 = scoreBoard.startGame(brazil, argentina);
      System.out.println("Game added: " + game2.toString());
      Thread.sleep(1);
      Game game3 = scoreBoard.startGame(senegal, portugal);
      System.out.println("Game added: " + game3.toString());
      scoreBoard.updateScore(game2, 1, 0);
      System.out.println("Game updated: " + game2.toString());
      scoreBoard.updateScore(game3, 2, 3);
      System.out.println("Game updated: " + game3.toString());
      scoreBoard.updateScore(game1, 3, 2);
      System.out.println("Game updated: " + game1.toString());
    } catch (TeamAlreadyPlayingException tape) {
    }
    printSummary(scoreBoard);
  }

  private static final void printSummary(ScoreBoard scoreBoard) {
    System.out.println("******** Summary *******\n" + scoreBoard.getSumaryAsString());
  }
}
