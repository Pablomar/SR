package scoreboard.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scoreboard.entities.Game;
import scoreboard.entities.Team;

public class ScoreBoardMemoryRepository implements ScoreBoardRepository {

  private final Map<String, Game> data = new HashMap<>();

  @Override
  public void addGame(Game game) {
    this.data.put(generateGameKey(game), game);
  }

  @Override
  public Game getGame(Team homeTeam, Team awayTeam) {
    return this.data.get(this.generateTeamsKey(homeTeam, awayTeam));
  }

  @Override
  public Game findGameByTeam(Team team) {
    for (Game game : this.data.values()) {
      if (game.getHomeTeam().getId().equals(team.getId()) || game.getAwayTeam().getId().equals(team.getId())) {
        return game;
      }
    }
    return null;
  }

  @Override
  public void deleteGame(Team homeTeam, Team awayTeam) {
    this.data.remove(this.generateTeamsKey(homeTeam, awayTeam));
  }

  @Override
  public List<Game> getAllGames() {
    return new ArrayList<Game>(this.data.values());
  }

  private String generateGameKey(Game game) {
    return this.generateTeamsKey(game.getHomeTeam(), game.getAwayTeam());
  }

  private String generateTeamsKey(Team homeTeam, Team awayTeam) {
    return homeTeam.getId().toString() + "_" + awayTeam.getId().toString();
  }
}
