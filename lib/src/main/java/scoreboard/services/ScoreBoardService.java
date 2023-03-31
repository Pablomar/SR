package scoreboard.services;

import java.util.List;

import scoreboard.entities.Game;
import scoreboard.entities.Team;
import scoreboard.exceptions.TeamAlreadyPlayingException;

public interface ScoreBoardService {

  Game startGame(Team homeTeam, Team awayTeam) throws TeamAlreadyPlayingException;

  void finishGame(Team homeTeam, Team awayTeam);

  void updateScore(Team homeTeam, Team awayTeam, int homeScore, int awayScore);

  List<Game> getSummary();

}
