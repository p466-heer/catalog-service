ALTER TABLE book
ADD COLUMN created_by VARCHAR(255),
ADD COLUMN last_modified_by VARCHAR(255);

UPDATE book
SET created_by = 'unknown',
    last_modified_by = 'unknown'
WHERE created_by IS NULL
   OR last_modified_by IS NULL;
