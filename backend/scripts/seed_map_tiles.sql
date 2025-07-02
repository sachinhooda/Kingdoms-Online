-- Seed 1,000,000 map tiles (x: 0-999, y: 0-999)
INSERT INTO map_tile (x, y, terrain)
SELECT x, y, 'land'
FROM generate_series(0, 999) AS x,
     generate_series(0, 999) AS y;
