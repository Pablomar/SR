package scoreboard.services;

import java.util.Date;
import java.util.List;

import scoreboard.entities.Game;
import scoreboard.entities.Team;
import scoreboard.exceptions.TeamAlreadyPlayingException;
import scoreboard.repositories.ScoreBoardRepository;
import scoreboard.utils.ScoreBoardUtils;

public class ScoreBoardManagementService implements ScoreBoardService {

  private ScoreBoardRepository scoreBoardRepository;

  public ScoreBoardManagementService(ScoreBoardRepository repository) {
    this.scoreBoardRepository = repository;
  }

  @Override
  public Game startGame(Team homeTeam, Team awayTeam) throws TeamAlreadyPlayingException {
    if (!ScoreBoardUtils.areValidTeams(homeTeam, awayTeam)) {
      throw new IllegalArgumentException("Teams can not be null and must have mandatory fields present");
    }
    if (homeTeam.getId().equals(awayTeam.getId())) {
      throw new IllegalArgumentException("The same team can not be in both home team and away team");
    }
    if (this.scoreBoardRepository.findGameByTeam(homeTeam) != null
        || this.scoreBoardRepository.findGameByTeam(awayTeam) != null) {
      throw new TeamAlreadyPlayingException();
    }

    Game game = new Game(homeTeam, awayTeam);
    game.setInitDate(new Date());
    this.scoreBoardRepository.addGame(game);
    return game;
  }

  @Override
  public void finishGame(Team homeTeam, Team awayTeam) {
    if (!ScoreBoardUtils.areValidTeams(homeTeam, awayTeam)) {
      return;
    }
    this.scoreBoardRepository.deleteGame(homeTeam, awayTeam);
  }

  /**
   * In isValidResult only positive values are checked, it is not checked that the
   * score is higher than the previous one because a VAR decission could make that
   * a goal could be discounted to a team
   */
  @Override
  public void updateScore(Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
    if (!ScoreBoardUtils.areValidTeams(homeTeam, awayTeam)) {
      return;
    }
    if (!ScoreBoardUtils.isValidResult(homeScore, awayScore)) {
      return;
    }
    Game game = this.scoreBoardRepository.getGame(homeTeam, awayTeam);
    if (game == null) {
      return;
    }
    game.setHomeScore(homeScore);
    game.setAwayScore(awayScore);
    this.scoreBoardRepository.addGame(game);
  }

  @Override
  public List<Game> getSummary() {
    List<Game> list = this.scoreBoardRepository.getAllGames();
    list.sort((game1, game2) -> {
      if (ScoreBoardUtils.getTotalScore(game1) > ScoreBoardUtils.getTotalScore(game2)) {
        return -1;
      } else if (ScoreBoardUtils.getTotalScore(game1) < ScoreBoardUtils.getTotalScore(game2)) {
        return 1;
      }
      return game1.getInitDate().before(game2.getInitDate()) ? 1 : -1;
    });
    return list;
  }

}
