CREATE TABLE transaction
(
    id            BINARY(16) PRIMARY KEY NOT NULL COMMENT 'UUID que identifica a transação no formato canônico (separado por hífen na forma 8-4-4-4-12).',
    value         NUMERIC(15, 2)         NOT NULL COMMENT 'Valor da transação.',
    currency      VARCHAR(3)             NOT NULL COMMENT 'Moeda da transação no formato ISO-4217.',
    country_iso_2 VARCHAR(2)             NOT NULL COMMENT 'País da transação no formato ISO 3166-1-alpha-2.',
    created_at    TIMESTAMP(6)           NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Data em que o registro foi inserido.',
    INDEX transaction_idx01 (created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Tabela usada para persistir as transações.';
