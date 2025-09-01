-- Insert 50 Students with string IDs
INSERT INTO student (id, first_name, last_name, code, programid, photo)
SELECT
    's' || i,
    'FirstName' || i,
    'LastName' || i,
    'STU' || LPAD(i::text, 3, '0'),
    'P' || (i % 5 + 1),
    'photo' || i || '.jpg'
FROM generate_series(1, 50) s(i);

-- Insert Payments linked to each Student
INSERT INTO payment (date, amount, type, status, file, student_id)
SELECT
            CURRENT_DATE,
            1000 + (i * 10),
            (i % 2 + 1),
            (i % 2),
            'file' || i || '.pdf',
            's' || i
FROM generate_series(1, 50) s(i);