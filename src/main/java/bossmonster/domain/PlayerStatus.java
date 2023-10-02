package bossmonster.domain;

import static bossmonster.domain.ExceptionMessage.INITIAL_HP_MP_SUM_EXCEPTION_MESSAGE;

public class PlayerStatus {

    private static final int TOTAL_SUM = 200;

    private final PlayerHp playerHp;
    private final PlayerMp playerMp;

    private PlayerStatus(int playerHp, int playerMp) {
        this(PlayerHp.from(playerHp), PlayerMp.from(playerMp));
    }

    private PlayerStatus(PlayerHp playerHp, PlayerMp playerMp) {
        validate(playerHp, playerMp);
        this.playerHp = playerHp;
        this.playerMp = playerMp;
    }

    private void validate(PlayerHp playerHp, PlayerMp playerMp) {
        validateTotalSum(playerHp, playerMp);
    }

    private void validateTotalSum(PlayerHp playerHp, PlayerMp playerMp) {
        if (!isRightTotalSum(playerHp, playerMp)) {
            throw new IllegalArgumentException(INITIAL_HP_MP_SUM_EXCEPTION_MESSAGE);
        }
    }

    private boolean isRightTotalSum(PlayerHp playerHp, PlayerMp playerMp) {
        return playerHp.plus(playerMp) == TOTAL_SUM;
    }

    public static PlayerStatus fromTest(PlayerHp playerHp, PlayerMp playerMp) {
        return new PlayerStatus(playerHp, playerMp);
    }

    public static PlayerStatus from(int playerHp, int playerMp) {
        return new PlayerStatus(playerHp, playerMp);
    }

    public void effectedMpByAttackType(AttackType attackType) {
        playerMp.effectedMpByAttackType(attackType);
    }

    public void effectedHpByBossDamage(int damageFromBoss) {
        playerHp.effectedByBossDamage(damageFromBoss);
    }

    public int getPlayerHp() {
        return playerHp.getCurrentPlayerHp();
    }

    public int getPlayerMp() {
        return playerMp.getPlayerMp();
    }

    public int getInitialPlayerHp() {
        return playerHp.getInitialPlayerHp();
    }

    public int getInitialPlayerMp() {
        return playerMp.getInitialPlayerMp();
    }

    public boolean isHpUnderMin() {
        return playerHp.isUnderMinHp();
    }

}
