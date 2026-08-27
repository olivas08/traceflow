CREATE TABLE inspections (
    id UUID PRIMARY KEY,
    serial_number VARCHAR(64) NOT NULL,
    result VARCHAR(8) NOT NULL,
    inspector VARCHAR(255) NOT NULL,
    notes TEXT,
    inspected_at TIMESTAMP WITH TIME ZONE NOT NULL,
    idempotency_key VARCHAR(128) UNIQUE
);

CREATE INDEX idx_inspections_serial ON inspections (serial_number);

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    topic VARCHAR(255) NOT NULL,
    message_key VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    event_id UUID NOT NULL UNIQUE,
    correlation_id VARCHAR(128),
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_outbox_pending ON outbox_events (status, created_at);
