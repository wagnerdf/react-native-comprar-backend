UPDATE shipping_options
SET origin_zip_code = '70000000'
WHERE origin_zip_code IS NULL;

ALTER TABLE shipping_options
ALTER COLUMN origin_zip_code SET NOT NULL;