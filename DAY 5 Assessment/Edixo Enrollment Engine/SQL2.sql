-- SQL Query for At-Risk Students (Anti-Join Pattern)
SELECT s.student_id, s.full_name
FROM students s
LEFT JOIN course_registrations cr ON s.student_id = cr.student_id
WHERE cr.student_id IS NULL;

-- Index for Foreign Key Optimization
CREATE INDEX idx_course_registrations_student_id 
ON course_registrations(student_id);