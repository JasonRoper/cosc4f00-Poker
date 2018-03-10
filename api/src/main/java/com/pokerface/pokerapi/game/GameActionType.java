package com.pokerface.pokerapi.game;

/**
 * GameActionType is an ENUM, the types of actions are mapped to poker actions, Call Check Raise Fold Bet, All In's
 * are bets of total values or checks of not enough money, logically determined by GameService
 */
public enum GameActionType {
    CALL, CHECK, RAISE, FOLD, BET
}
