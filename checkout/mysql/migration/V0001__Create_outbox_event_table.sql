USE checkout_adm;

CREATE TABLE outbox_event
(
    id             VARCHAR(36) PRIMARY KEY NOT NULL COMMENT 'UUID que identifica o evento no formato canônico (separado por hífen na forma 8-4-4-4-12).',
    aggregate_type VARCHAR(255)            NOT NULL COMMENT 'Nome da raiz de agregação que produziu o evento no formato CamelCase.',
    aggregate_id   VARCHAR(36)             NOT NULL COMMENT 'Representação textual do identificador da raiz de agregação.',
    event_type     VARCHAR(255)            NOT NULL COMMENT 'Nome do evento no formato CamelCase.',
    revision       INT                     NOT NULL COMMENT 'Número positivo que indica a versão do payload produzido do evento.',
    payload        JSON                    NOT NULL COMMENT 'Payload do evento como um objeto json.',
    occurred_on    DATETIME(6)             NOT NULL COMMENT 'Momento em que o evento ocorreu.',
    created_at     DATETIME(6)             NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Data em que o registro foi inserido.',
    INDEX outbox_event_idx01 (aggregate_id),
    INDEX outbox_event_idx02 (occurred_on),
    INDEX outbox_event_idx03 (created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Tabela usada para persistir eventos de forma atômica, para eventual publicação no message broker.';
