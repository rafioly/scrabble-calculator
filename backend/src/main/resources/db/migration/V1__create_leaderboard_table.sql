CREATE TABLE leaderboard (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    score INT NOT NULL
);

CREATE INDEX idx_score ON leaderboard (score DESC);
