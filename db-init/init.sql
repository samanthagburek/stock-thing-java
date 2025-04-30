CREATE TABLE IF NOT EXISTS detection_rule (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    rule_logic TEXT,
    enabled BOOLEAN DEFAULT TRUE,
    threshold INT,
    time_window_seconds INT
    );

CREATE TABLE log_entry (
    id SERIAL PRIMARY KEY,
    source_ip VARCHAR(255),
    destination_ip VARCHAR(255),
    source_port INTEGER,
    destination_port INTEGER,
    protocol VARCHAR(50),
    timestamp TIMESTAMP,
    packet_size INTEGER NOT NULL,
    icmp_type INTEGER,
    icmp_code INTEGER
    );

ALTER TABLE log_entry ALTER COLUMN source_port DROP NOT NULL;
ALTER TABLE log_entry ALTER COLUMN destination_port DROP NOT NULL;
ALTER TABLE log_entry ALTER COLUMN icmp_type DROP NOT NULL;
ALTER TABLE log_entry ALTER COLUMN icmp_code DROP NOT NULL;

CREATE TABLE detection_alert (
    id SERIAL PRIMARY KEY,
    alert_type VARCHAR(255),
    message TEXT,
    timestamp TIMESTAMP,
    log_entry_id BIGINT,
    rule_id BIGINT,
    CONSTRAINT fk_log_entry FOREIGN KEY (log_entry_id) REFERENCES log_entry(id),
    CONSTRAINT fk_detection_rule FOREIGN KEY (rule_id) REFERENCES detection_rule(id)
);

INSERT INTO detection_rule (name, description, rule_logic, enabled, threshold, time_window_seconds)
VALUES
    ('Rapid Port Scan', 'X+ unique ports accessed within Y second', 'UNIQUE_DEST_PORTS', TRUE, 10, 1),
    ('Large Data Transfer', 'Detects any packet over X bytes', 'LARGE_PACKET', TRUE, 10000, 1),
    ('Restricted Port Access', 'Detects traffic to restricted ports', 'RESTRICTED_PORTS', TRUE, 1, 1),
    ('ICMP Flood', 'More than X ICMP packets from the same IP in Y seconds', 'ICMP_FLOOD', TRUE, 20, 2),
    ('IP Sweep', 'X+ destination IPs from same source in Y seconds', 'IP_SWEEP', TRUE, 10, 5),
    ('Frequent Connections to Single Port', 'Same IP connects to the same port more than X times in Y seconds', 'FREQ_PORT_CONN', TRUE, 15, 3);
