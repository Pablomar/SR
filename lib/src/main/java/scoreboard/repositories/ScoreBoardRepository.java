package scoreboard.repositories;

import java.util.List;

import scoreboard.entities.Game;
import scoreboard.entities.Team;

public interface ScoreBoardRepository {

  void addGame(Game game);

  Game getGame(Team homeTeam, Team awayTeam);

  List<Game> getAllGames();

  void deleteGame(Team homeTeam, Team awayTeam);

  Game findGameByTeam(Team team);

}
