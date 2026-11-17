
-- Add organization ownership to roles.
-- NULL organization_id represents a system-defined role.

ALTER TABLE roles
    ADD COLUMN organization_id BIGINT NULL AFTER id;

-- Remove the old global uniqueness rule.
ALTER TABLE roles
    DROP INDEX uk_roles_name;

-- Connect organization-specific roles to their organization.
ALTER TABLE roles
    ADD CONSTRAINT fk_roles_organization
    FOREIGN KEY (organization_id)
    REFERENCES organizations(id);

-- Generate a scope identifier:
-- SYSTEM for global roles.
-- ORG:<organization_id> for organization-specific roles.
ALTER TABLE roles
    ADD COLUMN scope_key VARCHAR(32)
    GENERATED ALWAYS AS (
        CASE
            WHEN organization_id IS NULL THEN 'SYSTEM'
            ELSE CONCAT('ORG:', organization_id)
        END
    ) STORED;

-- A role name must be unique within its scope.
ALTER TABLE roles
    ADD CONSTRAINT uk_roles_scope_name
    UNIQUE (scope_key, name);
