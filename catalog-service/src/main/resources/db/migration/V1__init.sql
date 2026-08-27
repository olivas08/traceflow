-- plants, components, timeline projection, outbox and idempotency store
CREATE TABLE plants (
    id UUID PRIMARY KEY,
    code VARCHAR(16) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(64) NOT NULL
);

CREATE TABLE components (
    id UUID PRIMARY KEY,
    serial_number VARCHAR(64) NOT NULL UNIQUE,
    part_number VARCHAR(255) NOT NULL,
    plant_id UUID NOT NULL REFERENCES plants (id),
    parent_serial VARCHAR(64),
    status VARCHAR(32) NOT NULL
);

CREATE INDEX idx_components_plant_id ON components (plant_id);

CREATE TABLE component_timeline (
    id UUID PRIMARY KEY,
    serial_number VARCHAR(64) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    type VARCHAR(64) NOT NULL,
    summary VARCHAR(512) NOT NULL,
    correlation_id VARCHAR(128)
);

CREATE INDEX idx_timeline_serial ON component_timeline (serial_number, occurred_at);

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

CREATE TABLE processed_events (
    event_id VARCHAR(128) PRIMARY KEY,
    processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);
