CREATE DATABASE BlazeDB;
USE BlazeDB;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(15) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    name VARCHAR(100),
    birth_date DATE,
    gender ENUM('Male', 'Female', 'Other'),
    gender_interest ENUM('Male', 'Female', 'All'),
    biography TEXT,
    profile_picture VARCHAR(255),
    relationship_type ENUM('Friends', 'Casual', 'Formal', 'Other'),
    privacy_setting ENUM('Public', 'Private') DEFAULT 'Public',
    dark_mode BOOLEAN DEFAULT FALSE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN DEFAULT FALSE
);


CREATE TABLE interests (
    interest_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE
);


CREATE TABLE users_interests (
    user_id INT,
    interest_id INT,
    PRIMARY KEY (user_id, interest_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (interest_id) REFERENCES interests(interest_id) ON DELETE CASCADE
);

CREATE TABLE locations (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE, -- Links each user to one location
    latitude DECIMAL(9,6) NOT NULL, -- High precision for coordinates
    longitude DECIMAL(9,6) NOT NULL, -- High precision for coordinates
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE swipes (
    swipe_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    swiped_user_id INT,
    direction ENUM('Right', 'Left') NOT NULL,
    swipe_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (swiped_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE matches (
    match_id INT AUTO_INCREMENT PRIMARY KEY,
    user1_id INT,
    user2_id INT,
    match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user1_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    match_id INT,
    sender_id INT,
    content TEXT,
    message_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('MATCH', 'MESSAGE', 'REMINDER', 'GENERAL') NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);