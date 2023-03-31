package scoreboard.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scoreboard.entities.Game;
import scoreboard.entities.Team;
import scoreboard.exceptions.TeamAlreadyPlayingException;
import scoreboard.repositories.ScoreBoardMemoryRepository;
import scoreboard.sampledata.TestTeams;

public class ScoreBoardManagementServiceTest {

  ScoreBoardService service;

  @BeforeEach
  void initService() {
    service = new ScoreBoardManagementService(new ScoreBoardMemoryRepository());
    this.startDefaultGame();
  }

  @Test
  void whenAGameIsCreated_ThenItIsCorrectlyStarted() {
    assertEquals(service.getSummary().size(), 1);
    Game game = service.getSummary().get(0);
    assertEquals(game.getHomeScore(), 0);
    assertEquals(game.getAwayScore(), 0);
  }

  @Test
  void whenAGameIsCreatedWithNullTeams_ThenThrowAnException() {
    assertThrows(IllegalArgumentException.class, () -> service.startGame(null, TestTeams.GERMANY));
    assertThrows(IllegalArgumentException.class, () -> service.startGame(TestTeams.GERMANY, null));
    assertThrows(IllegalArgumentException.class, () -> service.startGame(null, null));
    assertThrows(IllegalArgumentException.class, () -> service.startGame(new Team(null, null), new Team(null, null)));
  }

  @Test
  void whenTheTeamIsBothHomeAndAway_ThenThrowAnException() {
    assertThrows(IllegalArgumentException.class, () -> service.startGame(TestTeams.BRAZIL, TestTeams.BRAZIL));
  }

  @Test
  void whenATeamIsPlaying_ThenCanNotBePresentInANewGame() {
    assertThrows(TeamAlreadyPlayingException.class, () -> service.startGame(TestTeams.SPAIN, TestTeams.BRAZIL));
    assertThrows(TeamAlreadyPlayingException.class, () -> service.startGame(TestTeams.SPAIN, TestTeams.GERMANY));
    assertThrows(TeamAlreadyPlayingException.class, () -> service.startGame(TestTeams.GERMANY, TestTeams.BRAZIL));
  }

  @Test
  void whenAGameIsFinished_ThenItIsRemovedIfExists() {
    assertEquals(service.getSummary().size(), 1);
    service.finishGame(TestTeams.GERMANY, TestTeams.BRAZIL);
    assertEquals(service.getSummary().size(), 1);
    service.finishGame(null, TestTeams.SPAIN);
    assertEquals(service.getSummary().size(), 1);
    service.finishGame(TestTeams.BRAZIL, TestTeams.SPAIN);
    assertEquals(service.getSummary().size(), 1);
    service.finishGame(TestTeams.SPAIN, TestTeams.BRAZIL);
    assertEquals(service.getSummary().size(), 0);
  }

  @Test
  void whenAnInvalidScoreIsUpdated_ThenNothingHappens() {
    service.updateScore(TestTeams.SPAIN, TestTeams.BRAZIL, -1, 0);
    service.updateScore(TestTeams.SPAIN, TestTeams.BRAZIL, 0, -1);
    assertEquals(service.getSummary().get(0).getHomeScore(), 0);
    assertEquals(service.getSummary().get(0).getAwayScore(), 0);
  }

  @Test
  void whenAScoreIsUpdated_ThenTheResultIsUpdated() {

    service.updateScore(TestTeams.SPAIN, TestTeams.BRAZIL, 1, 0);
    assertEquals(service.getSummary().get(0).getHomeScore(), 1);
    assertEquals(service.getSummary().get(0).getAwayScore(), 0);
    service.updateScore(TestTeams.SPAIN, TestTeams.BRAZIL, 2, 3);
    assertEquals(service.getSummary().get(0).getHomeScore(), 2);
    assertEquals(service.getSummary().get(0).getAwayScore(), 3);
    // Should not be updated because the order is not right
    service.updateScore(TestTeams.BRAZIL, TestTeams.SPAIN, 5, 8);
    assertEquals(service.getSummary().get(0).getHomeScore(), 2);
    assertEquals(service.getSummary().get(0).getAwayScore(), 3);
    service.updateScore(TestTeams.BRAZIL, TestTeams.GERMANY, 5, 8);
    assertEquals(service.getSummary().get(0).getHomeScore(), 2);
    assertEquals(service.getSummary().get(0).getAwayScore(), 3);
    assertEquals(service.getSummary().size(), 1);

  }

  @Test
  void whenSomeGamesAreStartedAndUpdated_ThenTheSummaryIsRight()
      throws InterruptedException, TeamAlreadyPlayingException {
    // We add sleep for the teams to be created in a diferent time
    Thread.sleep(1);
    service.startGame(TestTeams.ARGENTINA, TestTeams.PORTUGAL);
    Thread.sleep(1);
    service.startGame(TestTeams.NETHERLANDS, TestTeams.GERMANY);

    assertEquals(service.getSummary().size(), 3);
    assertEquals(service.getSummary().get(0).getHomeTeam().getId(), TestTeams.NETHERLANDS.getId());
    assertEquals(service.getSummary().get(1).getHomeTeam().getId(), TestTeams.ARGENTINA.getId());
    assertEquals(service.getSummary().get(2).getHomeTeam().getId(), TestTeams.SPAIN.getId());

    service.updateScore(TestTeams.SPAIN, TestTeams.BRAZIL, 2, 1);
    assertEquals(service.getSummary().get(0).getHomeTeam().getId(), TestTeams.SPAIN.getId());
    assertEquals(service.getSummary().get(1).getHomeTeam().getId(), TestTeams.NETHERLANDS.getId());
    assertEquals(service.getSummary().get(2).getHomeTeam().getId(), TestTeams.ARGENTINA.getId());

    service.updateScore(TestTeams.ARGENTINA, TestTeams.PORTUGAL, 2, 1);
    assertEquals(service.getSummary().get(0).getHomeTeam().getId(), TestTeams.ARGENTINA.getId());
    assertEquals(service.getSummary().get(1).getHomeTeam().getId(), TestTeams.SPAIN.getId());
    assertEquals(service.getSummary().get(2).getHomeTeam().getId(), TestTeams.NETHERLANDS.getId());

    service.updateScore(TestTeams.NETHERLANDS, TestTeams.GERMANY, 4, 2);
    assertEquals(service.getSummary().get(0).getHomeTeam().getId(), TestTeams.NETHERLANDS.getId());
    assertEquals(service.getSummary().get(1).getHomeTeam().getId(), TestTeams.ARGENTINA.getId());
    assertEquals(service.getSummary().get(2).getHomeTeam().getId(), TestTeams.SPAIN.getId());

  }

  private void startDefaultGame() {
    try {
      service.startGame(TestTeams.SPAIN, TestTeams.BRAZIL);
    } catch (TeamAlreadyPlayingException e) {
    }
  }
}
