package scoreboard.utils;

import scoreboard.entities.Game;
import scoreboard.entities.Team;

public class ScoreBoardUtils {

  public static boolean areValidTeams(Team homeTeam, Team awayTeam) {
    return ScoreBoardUtils.isValidTeam(homeTeam) && ScoreBoardUtils.isValidTeam(awayTeam);
  }

  public static boolean isValidTeam(Team team) {
    return team != null && team.getId() != null && team.getName() != null;
  }

  public static boolean isValidResult(int homeScore, int awayScore) {
    return homeScore >= 0 && awayScore >= 0;
  }

  public static int getTotalScore(Game game) {
    return game.getHomeScore() + game.getAwayScore();
  }
}
