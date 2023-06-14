DROP TABLE codes;
CREATE TABLE IF NOT EXISTS codes (
    num_id BIGINT NOT NULL,
    id UUID NOT NULL,
    code TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    time BIGINT NOT NULL,
    initial_time BIGINT NOT NULL,
    views BIGINT NOT NULL,
    restricted BOOLEAN DEFAULT false NOT NULL,
    to_be_time_restricted BOOLEAN NOT NULL,
    to_be_view_restricted BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);