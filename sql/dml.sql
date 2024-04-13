-- Populating Members Table
INSERT INTO Members (FName, LName, Email, GoalWeightKGs, GoalDeadline, HeightCM, WeightKGs, Age, Sex)
VALUES
('John', 'Doe', 'JDoe@gmail.com', 60, '2024-06-01', 170, 70, 40, 'Male'),
('Jane', 'Des', 'JDes@gmail.com', 50, '2024-07-01', 180, 60, 30, 'Female'),
('Jeff', 'Dans', 'JDans1@gmail.com', 65, '2024-10-10', 174, 70, 20, 'Male'),
('Joe', 'Dans', 'JDans2@gmail.com', 66, '2024-11-11', 177, 70, 23, 'Male');

-- Populating Trainers Table
INSERT INTO Trainers (Email, FName, LName)
VALUES
('DDes1@gmail.com', 'Dan', 'Des'),
('T2@gmail.com', 'Tim', 'Tran'),
('T3@gmail.com', 'Tom', 'Tran');

-- Populating Admins Table
INSERT INTO Admins (Email)
VALUES
('Admin1@gmail.com'),
('Admin2@gmail.com'),
('Admin3@gmail.com');

-- Populating Equipment Table
INSERT INTO Equipment (EquipmentName, EquipmentDescription, Condition)
VALUES
('Dumbell', '35lbs', 'new'),
('Dumbell', '35lbs', 'new'),
('Dumbell', '15lbs', 'needs replacement'),
('Dumbell', '15lbs', 'poor'),
('Dumbell', '25lbs', 'lightly worn'),
('Dumbell', '25lbs', 'like new'),
('Bench Bar', '55lbs', 'new'),
('Cable Machine', 'two cable variation with pullup bars', 'new');

-- Populating Rooms Table
INSERT INTO Rooms (RoomName)
VALUES
('Room 101'),
('Room 102'),
('Room 103'),
('Room 104'),
('Room 105'),
('Room 106'),
('Room 107'),
('PS Room');

-- Populating Billings Table, where Membership cost is $250 and classes cost $100
-- Assuming members in the same order they are listed
INSERT INTO Billings (MemberID, AmountDue)
VALUES
(1, 350),
(2, 350),
(3, 450),
(4, 250);

-- Populating TrainerAvailablity Table
-- Assuming Trainers in the same order they are listed
INSERT INTO TrainerAvailability (TrainerID, StartTime, EndTime)
VALUES
(1,'08:30','18:00'),
(2,'09:30','19:00'),
(3,'10:30','20:00');

-- Populating Routines Table
-- Assuming Members in the same order they are listed
INSERT INTO Routines (MemberID, RoutineDescription, DurationMins)
VALUES
(1, '10x4 Bench Press 225lbs', 45),
(1, '12x3 Barbell Rows 150lbs', 30),
(1, '8x4 Forearm Curls 30lbs', 15),
(2, '12x4 Bench Press 105lbs', 45);

-- Populating Classes Table
-- Assuming Trainers and Members in the same order they are listed
INSERT INTO Classes (RoomID, TrainerID, ClassType, ClassDescription, StartTime, EndTime, Available)
VALUES
(1, 1, 'Group', 'Yoga Class with Dan', '08:30', '10:00', TRUE),
(3, 1, 'Personal', 'Cardio Class with Dan', '12:30', '14:00', TRUE),
(2, 2, 'Group', 'Yoga Session with Tim', '08:30', '10:00', TRUE);

-- Populating Takes Table
-- Assuming Classes and Members in the same order they are listed
INSERT INTO Takes (MemberID, ClassID)
VALUES
(1,1),
(3,1),
(3,3),
(2,1);



