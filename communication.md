
# What Happens when:

POST: /api/v1/matchmaking/basicGame -> game_id

SUBSCRIBE: /user/id/game/{id}
SUBSCRIBE: /messages/game/{id}
SUBSCRIBE: /messages/game/{id}/gameFinished

MESSAGE: /messages/game/{id} (you joined)
MESSAGE: /meesages/game/{id} (another player joined)
... whenever a new player joins

// BEGINING OF A HAND
MESSAGE: /messages/game/{id} (the hand has started)
MESSAGE: /user/{id}/game/{id} (the hand started - your cards)

SEND: /app/game/{id}: CALL (it's your turn, are you paying the small blind) 
MESSAGE: /messages/game/{id} (you played an action , this is the new state)
MESSAGE: /messages/game/{id} (the next player plays the big blind, this is the new state)
SEND: /app/game/{id}: CALL (it's your turn, are you paying the small blind) 
MESSAGE: /messages/game/{id} (the round is over, deliver 3 community cards, this is the new state)

SEND: /app/game/{id}: BET (it's your turn)
MESSAGE: /messages/game/{id} (you played BET, this is the new state)
MESSAGE: /messages/game/{id} (someone else CALLED, this is the new state)
MESSAGE: /messages/game/{id} (the round is over, add 1 community card, this is the new state)
n
SEND: /app/game/{id}: CHECK (it's your turn, are you paying CHECK)
MESSAGE: /messages/game/{id} (you played CHECK, this is the new state)
MESSAGE: /messages/game/{id} (someone else BET, this is the new state)
SEND: /app/game/{id}: CALL (it's your turn, are you paying CALL)
MESSAGE: /messages/game/{id} (you played CALL, this is the new state)
MESSAGE: /messages/game/{id} (the round is over, add 1 community card, this is the new state)

SEND: /app/game/{id}: CHECK (it's your turn, are you paying CHECK)
MESSAGE: /messages/game/{id} (you played CHECK, this is the new state)
MESSAGE: /messages/game/{id} (someone else BET, this is the new state)
SEND: /app/game/{id}: CALL (it's your turn, are you paying CALL)
MESSAGE: /messages/game/{id} (you played CALL, this is the new state)

MESSAGE: /messages/game/{id} (the round is over, THIS IS THE SHOWDOWN)
MESSAGE: /messages/game/{id}/handFinished
// END OF A HAND

MESSAGE: /messages/game/{id}/handStarted (another player joined)
MESSAGE: /messages/game/{id} (the hand has started)
MESSAGE: /user/{id}/game/{id} (you recieve your cards)

SEND: /app/game/{id} : CHECK (you pay the big blind)
MESSAGE: /messages/game/{id} (you played CHECK, this is the new state)
MESSAGE: /messages/game/{id} (another player FOLDED, this is the new state)
MESSAGE: /messages/game/{id} (another player FOLDED, this is the new state)
MESSAGE: /messages/game/{id} (another player left the game, this is the new state)

    

# vNext
RequestForGame:
- action 

GameState: 
```
{
"players": [{
    id: number,
    money: number,
    action : GameAction,
    currentBet: number,
    isPlayer: boolean,
    isDealer: boolean
}],
"communityCards": [],
"pot": number,
"bigBlind": number,
"event": {
    "action": "" (one of ["Hand
Started", "PlayerAction", "RoundFinished", "HandFinished", "NewPlayer", "PlayerLeaves"])
    "message": ""
}
```

HandEnd:
```
{
    winners: [{
        player: id,
        winnings: number
    }],
    players: [{
        playerId: number
        cards: [Card1, Card2]
    }]
}
```

Cards:
```
{
    "cards": [Card1, Card2]
}
```

GameError
