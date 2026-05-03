-- V2__seed_south_india_locations.sql

-- Country
INSERT INTO country(code, name)
VALUES ('IN', 'India')
ON CONFLICT (code) DO NOTHING;

-- States (South India)
INSERT INTO state(country_id, code, name)
VALUES
 ((SELECT id FROM country WHERE code='IN'), 'KA', 'Karnataka'),
 ((SELECT id FROM country WHERE code='IN'), 'TN', 'Tamil Nadu'),
 ((SELECT id FROM country WHERE code='IN'), 'KL', 'Kerala'),
 ((SELECT id FROM country WHERE code='IN'), 'TS', 'Telangana'),
 ((SELECT id FROM country WHERE code='IN'), 'AP', 'Andhra Pradesh'),
 ((SELECT id FROM country WHERE code='IN'), 'PY', 'Puducherry')
ON CONFLICT (code) DO NOTHING;

-- Cities (sample set; we can expand anytime)
-- Karnataka
INSERT INTO city(state_id, code, name) VALUES
 ((SELECT id FROM state WHERE code='KA'), 'BLR', 'Bengaluru'),
 ((SELECT id FROM state WHERE code='KA'), 'MYS', 'Mysuru'),
 ((SELECT id FROM state WHERE code='KA'), 'MAQ', 'Mangaluru')
ON CONFLICT (code) DO NOTHING;

-- Tamil Nadu
INSERT INTO city(state_id, code, name) VALUES
 ((SELECT id FROM state WHERE code='TN'), 'CHE', 'Chennai'),
 ((SELECT id FROM state WHERE code='TN'), 'CBE', 'Coimbatore'),
 ((SELECT id FROM state WHERE code='TN'), 'MDU', 'Madurai')
ON CONFLICT (code) DO NOTHING;

-- Kerala
INSERT INTO city(state_id, code, name) VALUES
 ((SELECT id FROM state WHERE code='KL'), 'KOC', 'Kochi'),
 ((SELECT id FROM state WHERE code='KL'), 'TVM', 'Thiruvananthapuram'),
 ((SELECT id FROM state WHERE code='KL'), 'CCJ', 'Kozhikode')
ON CONFLICT (code) DO NOTHING;

-- Telangana
INSERT INTO city(state_id, code, name) VALUES
 ((SELECT id FROM state WHERE code='TS'), 'HYD', 'Hyderabad'),
 ((SELECT id FROM state WHERE code='TS'), 'WGL', 'Warangal')
ON CONFLICT (code) DO NOTHING;

-- Andhra Pradesh
INSERT INTO city(state_id, code, name) VALUES
 ((SELECT id FROM state WHERE code='AP'), 'VSK', 'Visakhapatnam'),
 ((SELECT id FROM state WHERE code='AP'), 'BZA', 'Vijayawada'),
 ((SELECT id FROM state WHERE code='AP'), 'GNT', 'Guntur')
ON CONFLICT (code) DO NOTHING;

-- Puducherry
INSERT INTO city(state_id, code, name) VALUES
 ((SELECT id FROM state WHERE code='PY'), 'PDY', 'Puducherry')
ON CONFLICT (code) DO NOTHING;