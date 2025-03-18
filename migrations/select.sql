SELECT * FROM insertion;
SELECT * FROM scaffs;
SELECT * FROM substitution;
SELECT * FROM users;
SELECT * FROM var_types;
SELECT * FROM vars;

DROP TABLE insertion;
DROP TABLE substitution;
DROP TABLE vars;
DROP TABLE var_types;
DROP TABLE scaff_tag;
DROP TABLE tags;
DROP TABLE scaffs;
DROP TABLE users;

-- Get all tables in database
SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';


WITH RECURSIVE ptr AS (SELECT * FROM scaffs WHERE scaff_id LIKE '111%')
SELECT * FROM scaffs;

-- Get all options for scaff
SELECT * FROM scaffs WHERE parent_id LIKE '111%';


-- Get all substitutions & insertions for scaff
SELECT * FROM scaffs s
WHERE s.id LIKE '111%'
AND (
    s.id IN (SELECT scaff_id FROM insertion) OR
    s.id IN (SELECT scaff_id FROM substitution)
);


-- Recursively get all ancestors' insertions & substitutions in order of application
WITH RECURSIVE ancs(n, id, parent_id) AS (                      -- (Get all ancestors)
    SELECT 0, id, parent_id FROM scaffs WHERE id = :startId     -- initial state
    UNION ALL
    SELECT n+1, s.id, s.parent_id FROM scaffs s                 -- recursive call
    INNER JOIN ancs a ON s.id = a.parent_id
    WHERE a.id NOT LIKE '000%'                                  -- stop at root
),
ins AS (                                                        -- (Get ancestors' insertions)
    SELECT a.n, 0 AS step, a.id, a.parent_id, ins.filepath, NULL AS variable, ins.value
    FROM ancs a
    INNER JOIN insertion ins ON a.id = ins.scaff_id
),
sub AS (                                                        -- (Get ancestors' substitutions)
    SELECT a.n, 1 AS step, a.id, a.parent_id, NULL AS filepath, sub.variable, sub.value
    FROM ancs a
    INNER JOIN substitution sub ON a.id = sub.scaff_id
)
SELECT * FROM ins
UNION ALL
SELECT * FROM sub
ORDER BY n DESC, step ASC;


-- Recursively get all ancestors' insertions in order of correct application
WITH RECURSIVE ancs(n, id, parent_id) AS (
    SELECT 0, id, parent_id FROM scaffs WHERE id LIKE '888%' -- Initial state
    UNION ALL
    SELECT n+2, s.id, s.parent_id FROM scaffs s -- Recursive call
    INNER JOIN ancs a ON s.id = a.parent_id
    WHERE a.id NOT LIKE '000%' -- Stop at root
)
SELECT a.n, a.id, a.parent_id, ins.filepath, ins.value
FROM ancs a
LEFT JOIN insertion ins ON a.id = ins.scaff_id
ORDER BY a.n DESC;


-- Get all insertions for a particular scaff
SELECT s.id, s.parent_id, ins.filepath, ins.value
FROM scaffs s
LEFT JOIN insertion ins    ON s.id = ins.scaff_id
WHERE s.id LIKE '888%';

-- Get all substitutions for a particular scaff
SELECT s.id, s.parent_id, sub.variable, sub.value
FROM scaffs s
LEFT JOIN substitution sub ON s.id = sub.scaff_id
WHERE s.id LIKE '888%';


SELECT * FROM substitution;

-- Render scaff
-- (Recursive backtrack to root)
SELECT * FROM scaffs WHERE parent_id LIKE '111%';



-- Produces cartesian product of entries
WITH
    rw AS (SELECT 10 AS w),
    rh AS (SELECT h FROM (VALUES (1), (2), (3)) as h),
    rl AS (SELECT l FROM (VALUES (6), (7), (8)) as l)
SELECT w, h, l FROM rw, rh, rl;


WITH
    -- rw AS (SELECT 10 AS w),
    rw AS (SELECT w FROM (VALUES (1)) as w),
    rh AS (SELECT h FROM (VALUES (1), (2), (3)) as h),
    rl AS (SELECT l FROM (VALUES (6), (7), (8)) as l)
SELECT * FROM rw
FULL OUTER JOIN rh ON rh.h = rw.w;


WITH RECURSIVE t(x, n) AS (
    SELECT 1, 0
    UNION ALL
    SELECT x*-1, n+1 FROM t WHERE n < 100
)
SELECT x, n FROM t;