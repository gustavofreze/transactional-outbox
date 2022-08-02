#!/bin/bash

DATABASE_NAME="checkout_adm"

CREDENTIALS="/scripts/config/credentials.cnf"
CREATE_DATABASE="CREATE DATABASE ${DATABASE_NAME};"

# shellcheck disable=SC2059
printf "\n\nRunning DDL commands for the database ${DATABASE_NAME} ... \n\n"

mysql --defaults-extra-file="${CREDENTIALS}" -e "${CREATE_DATABASE}"
mysql --defaults-extra-file="${CREDENTIALS}" <./scripts/migration/V0000__Create_transaction_table.sql
mysql --defaults-extra-file="${CREDENTIALS}" <./scripts/migration/V0001__Create_outbox_event_table.sql

printf "\n\nEnd of execution. \n"
