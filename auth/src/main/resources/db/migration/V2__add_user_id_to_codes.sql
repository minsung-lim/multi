-- Add user_id column as nullable first
ALTER TABLE codes ADD COLUMN user_id VARCHAR(255);

-- Update existing records with a default value (if needed)
UPDATE codes SET user_id = 'system' WHERE user_id IS NULL;

-- Make the column not null after data is updated
ALTER TABLE codes ALTER COLUMN user_id SET NOT NULL; 