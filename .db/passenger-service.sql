CREATE TABLE passengers
(
    id                   BIGSERIAL PRIMARY KEY,
    account_id           BIGINT,
    identity_document_id BIGINT REFERENCES identity_documents (id),
    first_name           VARCHAR(255),
    last_name            VARCHAR(255),
    phone_number         VARCHAR(20),
    date_of_birth        DATE,
    is_delete            BOOLEAN                     DEFAULT FALSE,
    created_at           TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE identity_documents
(
    id              BIGSERIAL PRIMARY KEY,
    document_type   VARCHAR(50)  NOT NULL,
    document_number VARCHAR(50)  NOT NULL,
--     full_name           VARCHAR(255) NOT NULL,
--     date_of_birth       DATE,
--     gender              VARCHAR(10)  NOT NULL,
--     nationality         VARCHAR(50)  NOT NULL,
--     place_of_origin     VARCHAR(255) NOT NULL,
--     place_of_residence  VARCHAR(255) NOT NULL,
    expiry_date     DATE         NOT NULL,
    issue_date      DATE         NOT NULL,
    issue_place     VARCHAR(255) NOT NULL,
--     verification_status VARCHAR(50)              DEFAULT 'UNVERIFIED',
    is_delete       BOOLEAN                  DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (document_type, document_number)
);

-- CREATE TABLE customer_identity_documents
-- (
--     id                   BIGSERIAL PRIMARY KEY,
--     customer_id          BIGSERIAL REFERENCES customers (id),
--     identity_document_id BIGSERIAL REFERENCES identity_documents (id),
--     is_primary           BOOLEAN                     DEFAULT FALSE,
--     is_delete            BOOLEAN                     DEFAULT FALSE,
--     created_at           TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at           TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     UNIQUE (customer_id, identity_document_id)
-- )

