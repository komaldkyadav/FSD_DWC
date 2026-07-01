-- SQL Query with Window Function for Latest GPS Ping
SELECT r.rider_id, r.rider_name, r.bike_model, 
       p.latitude, p.longitude, p.recorded_at
FROM riders r
INNER JOIN (
    SELECT rider_id, latitude, longitude, recorded_at,
           ROW_NUMBER() OVER(PARTITION BY rider_id ORDER BY recorded_at DESC) as rn
    FROM gps_pings
) p ON r.rider_id = p.rider_id
WHERE p.rn = 1;

-- Composite Index for Optimization
CREATE INDEX idx_gps_pings_rider_timestamp 
ON gps_pings(rider_id, recorded_at DESC);