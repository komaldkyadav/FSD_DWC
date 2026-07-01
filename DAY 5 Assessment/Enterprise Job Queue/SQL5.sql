-- SQL Query with FOR UPDATE SKIP LOCKED
SELECT j.job_id, j.status, j.created_at, d.dept_name
FROM background_jobs j
INNER JOIN departments d ON j.dept_id = d.dept_id
WHERE j.status = 'PENDING' AND d.dept_name = 'Engineering'
ORDER BY j.created_at ASC
LIMIT 1
FOR UPDATE SKIP LOCKED;

-- Partial Index for Optimization
CREATE INDEX idx_jobs_pending_status 
ON background_jobs(job_id, created_at) 
WHERE status = 'PENDING';