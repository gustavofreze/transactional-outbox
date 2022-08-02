SET @id := UUID();
SET @value := ROUND((RAND() + 1) * 100, 2);
SET @country := 'BR';
SET @currency := 'BRL';

INSERT INTO checkout_adm.transaction (id, value, currency, country_iso_2)
VALUES (UUID_TO_BIN(@id), @value, @currency, @country);

SET @payload := JSON_OBJECT('id', @id,
                            'amount', JSON_OBJECT('value', @value, 'currency', @currency),
                            'country', JSON_OBJECT('alpha2', @country));

INSERT INTO checkout_adm.outbox_event(id, aggregate_type, aggregate_id, event_type, revision, payload, occurred_on)
VALUES (UUID(), 'Checkout', @id, 'CheckoutDone', 1, @payload, NOW());
