package scoreboard.entities;

import java.util.Date;

public class Game {
  private Team homeTeam;
  private Team awayTeam;
  private int homeScore = 0;
  private int awayScore = 0;
  private Date initDate;

  public Game(Team homeTeam, Team awayTeam) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(Team homeTeam) {
    this.homeTeam = homeTeam;
  }

  public Team getAwayTeam() {
    return awayTeam;
  }

  public void setAwayTeam(Team awayTeam) {
    this.awayTeam = awayTeam;
  }

  public int getHomeScore() {
    return homeScore;
  }

  public void setHomeScore(int homeScore) {
    this.homeScore = homeScore;
  }

  public int getAwayScore() {
    return awayScore;
  }

  public void setAwayScore(int awayScore) {
    this.awayScore = awayScore;
  }

  public Date getInitDate() {
    return initDate;
  }

  public void setInitDate(Date initDate) {
    this.initDate = initDate;
  }

  public String toString() {
    return this.getHomeTeam().getName() + " " + this.getHomeScore() + " - " + this.getAwayScore() + " "
        + this.getAwayTeam().getName();
  }
}
