#!/bin/bash

CREDENTIALS="/scripts/config/credentials.cnf"

mysql --defaults-extra-file="${CREDENTIALS}" <./scripts/use-case/insert.sql

mysql --defaults-extra-file="${CREDENTIALS}" <<EOF
SELECT BIN_TO_UUID(id) as id, value FROM checkout_adm.transaction ORDER BY 1 DESC LIMIT 1;
EOF

printf mysql >null

printf "\n"

mysql --defaults-extra-file="${CREDENTIALS}" <<EOF
SELECT aggregate_id, event_type, occurred_on FROM checkout_adm.outbox_event ORDER BY 1 DESC LIMIT 1;
EOF

printf mysql >null
