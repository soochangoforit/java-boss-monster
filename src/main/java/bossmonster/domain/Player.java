package bossmonster.domain;

public class Player {

    private final PlayerName playerName;

    private final PlayerStatus playerStatus;

    private Player(PlayerName playerName, PlayerStatus playerStatus) {
        this.playerName = playerName;
        this.playerStatus = playerStatus;
    }

    public static Player from(PlayerName playerName, PlayerStatus playerStatus) {
        return new Player(playerName, playerStatus);
    }

    public int getPlayerHp() {
        return playerStatus.getPlayerHp();
    }

    public int getPlayerMp() {
        return playerStatus.getPlayerMp();
    }

    public String getPlayerName() {
        return playerName.getPlayerName();
    }

    public int getInitialPlayerHp() {
        return playerStatus.getInitialPlayerHp();
    }

    public int getInitialPlayerMp() {
        return playerStatus.getInitialPlayerMp();
    }

    public void effectedMpBy(AttackType attackType) {
        playerStatus.effectedMpBy(attackType);
    }
}
