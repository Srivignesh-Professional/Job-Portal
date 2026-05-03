-- V5__seed_job_categories.sql (Labour-specific categories)

INSERT INTO job_category(code, name)
VALUES
-- Construction
('PLUMBER', 'Plumber'),
('ELECTRICIAN', 'Electrician'),
('PAINTER', 'Painter'),
('MASON', 'Mason'),
('CARPENTER', 'Carpenter'),
('WELDER', 'Welder'),
('TILE_MASON', 'Tile Mason'),

-- Helpers & Labour
('HELPER', 'Helper/Labour'),
('LOADER', 'Loader/Unloader'),
('CLEANER', 'Cleaner'),
('COOK_HELPER', 'Cook Helper'),

-- Drivers & Transport
('DRIVER', 'Driver'),
('DELIVERY_BOY', 'Delivery Boy'),

-- Maintenance
('AC_TECHNICIAN', 'AC Technician'),
('PLUMBER_MAINTENANCE', 'Plumber (Maintenance)'),
('GARDENER', 'Gardener'),

-- Others
('SECURITY_GUARD', 'Security Guard'),
('DOMESTIC_HELP', 'Domestic Help')
ON CONFLICT (code) DO NOTHING;