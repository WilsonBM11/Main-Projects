CREATE TABLE Game (
    id VARCHAR(36) NOT NULL CONSTRAINT PK_Game PRIMARY KEY,
    game_name VARCHAR(20) NOT NULL CONSTRAINT UK_Game UNIQUE,
    game_owner VARCHAR(20) NOT NULL,
    game_status VARCHAR(6) NOT NULL CONSTRAINT CK_GameStatus CHECK (game_status IN ('lobby','rounds','ended')),
    createdAt VARCHAR(24) NOT NULL,
    updatedAt VARCHAR(24) NOT NULL,
    use_password BIT NOT NULL,
    game_password VARCHAR(20) NOT NULL,
    currentRound VARCHAR(36) NOT NULL
);

CREATE TABLE GameRound(
    id VARCHAR(36) NOT NULL CONSTRAINT PK_Round PRIMARY KEY,
    leader VARCHAR(20) NOT NULL,
    round_status VARCHAR(17) NOT NULL CONSTRAINT CK_RoundStatus CHECK (round_status IN ('waiting-on-leader','voting','waiting-on-group', 'ended')),
    result VARCHAR(8) NOT NULL CONSTRAINT CK_RoundResult CHECK (result IN ('none','citizens','enemies')),
    phase VARCHAR(5) NOT NULL CONSTRAINT CK_RoundPhase CHECK (phase IN ('vote1','vote2','vote3')),
    createdAt VARCHAR(24) NOT NULL,
    updatedAt VARCHAR(24) NOT NULL,
    game_id VARCHAR(36) NOT NULL CONSTRAINT FK_Round FOREIGN KEY REFERENCES Game(id)
);

CREATE TABLE Player(
    id VARCHAR(36) NOT NULL CONSTRAINT PK_Player PRIMARY KEY,
    player_name VARCHAR(20) NOT NULL,
    player_role VARCHAR(7) NOT NULL CONSTRAINT CK_PlayerRole CHECK (player_role IN ('citizen','enemy')),
    game_id VARCHAR(36) NOT NULL CONSTRAINT FK_Player FOREIGN KEY REFERENCES Game(id)
);

CREATE TABLE RoundGroup(
    id VARCHAR(36) NOT NULL CONSTRAINT PK_Group PRIMARY KEY,
    phase VARCHAR(5) NOT NULL CONSTRAINT CK_GroupPhase CHECK (phase IN ('vote1','vote2','vote3')),
    round_id VARCHAR(36) NOT NULL CONSTRAINT FK_Group FOREIGN KEY REFERENCES GameRound(id),
);

CREATE TABLE GroupPlayer(
    player_id VARCHAR(36) NOT NULL CONSTRAINT FK_GP_PlayerId FOREIGN KEY REFERENCES Player(id),
    group_id VARCHAR(36) NOT NULL CONSTRAINT FK_GP_GroupId FOREIGN KEY REFERENCES RoundGroup(id),
);

CREATE TABLE Vote(
    player_id VARCHAR(36) NOT NULL CONSTRAINT FK_V_PlayerId FOREIGN KEY REFERENCES Player(id),
    group_id VARCHAR(36) NOT NULL CONSTRAINT FK_V_GroupId FOREIGN KEY REFERENCES RoundGroup(id),
    vote BIT NOT NULL,
);

CREATE TABLE RoundAction(
    round_id VARCHAR(36) NOT NULL CONSTRAINT FK_A_RoundId FOREIGN KEY REFERENCES GameRound(id),
    player_id VARCHAR(36) NOT NULL CONSTRAINT FK_A_PlayerId FOREIGN KEY REFERENCES Player(id),
    round_action BIT NOT NULL,
);
