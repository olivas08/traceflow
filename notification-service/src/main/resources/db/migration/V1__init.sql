CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    inspection_id UUID NOT NULL UNIQUE,
    serial_number VARCHAR(64) NOT NULL,
    message VARCHAR(512) NOT NULL,
    opened_at TIMESTAMP WITH TIME ZONE NOT NULL,
    correlation_id VARCHAR(128)
);

CREATE INDEX idx_notifications_opened_at ON notifications (opened_at DESC);

CREATE TABLE processed_events (
    event_id VARCHAR(128) PRIMARY KEY,
    processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);
