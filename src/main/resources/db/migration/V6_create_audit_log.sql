CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    details TEXT,
    ip_address VARCHAR(45), -- Suporta IPv4 e IPv6
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_username ON audit_logs(username);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);