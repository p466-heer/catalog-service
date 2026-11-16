ALTER TABLE book
    ALTER COLUMN created_by SET NOT NULL,
    ALTER COLUMN last_modified_by SET NOT NULL;
