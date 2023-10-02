package bossmonster.controller;

import static bossmonster.util.RetryUtil.read;

import bossmonster.domain.AttackType;
import bossmonster.domain.Boss;
import bossmonster.domain.BossGame;
import bossmonster.domain.DamageStrategy;
import bossmonster.domain.Player;
import bossmonster.domain.PlayerName;
import bossmonster.domain.PlayerStatus;
import bossmonster.dto.request.AttackTypeCodeDto;
import bossmonster.dto.request.BossHpDto;
import bossmonster.dto.request.PlayerNameDto;
import bossmonster.dto.request.PlayerStatusInfoDto;
import bossmonster.dto.response.AttackTypeDto;
import bossmonster.dto.response.BossAndPlayerStatusDto;
import bossmonster.dto.response.BossAttackDto;
import bossmonster.dto.response.PlayerBossInfoDto;
import bossmonster.view.InputView;
import bossmonster.view.OutputView;

public class BossGameController {

    private final InputView inputView;
    private final OutputView outputView;
    private final DamageStrategy damageStrategy;

    public BossGameController(InputView inputView, OutputView outputView, DamageStrategy damageStrategy) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.damageStrategy = damageStrategy;
    }

    public void run() {
        Boss boss = read(this::createBoss);
        Player player = read(this::createPlayer);
        printStartMessage();
        printBossAndPlayerStatus(boss, player);
        BossGame bossGame = BossGame.init(boss, player);
        playingGame(bossGame);
    }


    private void playingGame(BossGame bossGame) {
        while (true) {
            AttackType attackType = read(this::getAttackType, bossGame);
            bossGame.attackToBossFromPlayer(attackType);
            int bossDamage = bossGame.attackPlayerFromBoss();

            if (bossGame.isBossDead()) {
                printBossDeadMessage(bossGame, attackType);
                return;
            }
            if (bossGame.isPlayerDead()) {
                printPlayerDeadMessage(bossGame, attackType, bossDamage);
                return;
            }
            printGameCurrentStatus(bossGame, attackType, bossDamage);
        }
    }

    private void printPlayerDeadMessage(BossGame bossGame, AttackType attackType, int bossDamage) {
        PlayerBossInfoDto playerBossInfoResponseDto = new PlayerBossInfoDto(attackType, bossDamage, bossGame);
        outputView.printPlayerDeadMessage(playerBossInfoResponseDto);
    }

    private void printBossDeadMessage(BossGame bossGame, AttackType attackType) {
        BossAttackDto bossDeadResponseDto = new BossAttackDto(attackType, bossGame);
        outputView.printBossDeadMessage(bossDeadResponseDto);
    }

    private void printGameCurrentStatus(BossGame bossGame, AttackType attackType, int bossDamage) {
        PlayerBossInfoDto playerBossInfoResponseDto = new PlayerBossInfoDto(attackType, bossDamage, bossGame);
        outputView.printGameCurrentStatus(playerBossInfoResponseDto);
    }

    private AttackType getAttackType(BossGame bossGame) {
        AttackType attackType = readAttackType();
        bossGame.effectPlayerMpWith(attackType);
        return attackType;
    }


    private AttackType readAttackType() {
        return read(this::scanAttackType);
    }

    private AttackType scanAttackType() {
        AttackTypeDto attackTypeDto = new AttackTypeDto();
        AttackTypeCodeDto attackTypeCodeDto = read(inputView::scanAttackType, attackTypeDto);
        return AttackType.fromCode(attackTypeCodeDto.getAttackTypeCode());
    }

    private static void printStartMessage() {
        outputView.printStartMessage();
    }

    private void printBossAndPlayerStatus(Boss boss, Player player) {
        BossAndPlayerStatusDto bossAndPlayerStatusDto = new BossAndPlayerStatusDto(boss, player);
        outputView.printBossAndPlayerStatus(bossAndPlayerStatusDto);
    }

    private Player createPlayer() {
        PlayerName playerName = read(this::createPlayerName);
        PlayerStatus playerStatus = read(this::createPlayerStatus);
        return Player.from(playerName, playerStatus);
    }

    private PlayerStatus createPlayerStatus() {
        PlayerStatusInfoDto statusInfoDto = read(inputView::scanPlayerHpAndMp);
        return PlayerStatus.from(statusInfoDto.getPlayerHp(), statusInfoDto.getPlayerMp());
    }

    private PlayerName createPlayerName() {
        PlayerNameDto playerNameDto = read(inputView::scanPlayerNames);
        return PlayerName.from(playerNameDto.getPlayerName());
    }

    private Boss createBoss() {
        BossHpDto bossHpDto = read(inputView::scanBossHp);
        return Boss.from(bossHpDto.getHp(), damageStrategy);
    }
}
