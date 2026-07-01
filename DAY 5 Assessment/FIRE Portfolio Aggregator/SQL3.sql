-- SQL Query for Portfolio Aggregation
SELECT h.asset_class, SUM(h.current_value) as total_value
FROM investors i
INNER JOIN holdings h ON i.investor_id = h.investor_id
WHERE i.investor_id = ?
GROUP BY h.asset_class;

-- Covering Index for Index-Only Scan
CREATE INDEX idx_holdings_investor_asset_value 
ON holdings(investor_id, asset_class) INCLUDE (current_value);