-- SQL Query for Delayed Shipments
SELECT s.shipment_id, c.company_name, s.dispatch_date
FROM shipments s
INNER JOIN couriers c ON s.courier_id = c.courier_id
WHERE s.status = 'DELAYED'
ORDER BY s.dispatch_date DESC;

-- Composite Index for Optimization
CREATE INDEX idx_shipments_status_dispatch 
ON shipments(status, dispatch_date DESC);