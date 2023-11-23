DROP TABLE IF EXiSTS members;
DROP TABLE IF EXiSTS follows;
DROP TABLE IF EXiSTS payments;

CREATE TABLE members (
                         members_id INT PRIMARY KEY AUTO_INCREMENT,
                         login_id VARCHAR(255) NOT NULL,
                         email VARCHAR(255),
                         age INT,
                         gender VARCHAR(255),
                         nickname VARCHAR(255),
                         full_name VARCHAR(255),
                         latitude DOUBLE DEFAULT 0,
                         longitude DOUBLE DEFAULT 0,
                         role VARCHAR(255),
                         profile_image VARCHAR(255),
                         provider VARCHAR(255),
                         point INT DEFAULT 0
);

-- Follows Table
CREATE TABLE follows (
                         follows_id INT PRIMARY KEY AUTO_INCREMENT,
                         following_id INT,
                         follower_id INT,
                         FOREIGN KEY (following_id) REFERENCES members(members_id),
                         FOREIGN KEY (follower_id) REFERENCES members(members_id)
);

-- Payments Table
CREATE TABLE payments (
                          payments_id INT PRIMARY KEY AUTO_INCREMENT,
                          tid VARCHAR(255),
                          approved_at DATETIME,
                          amount INT,
                          type VARCHAR(255),
                          payer INT,
                          receiver INT,
                          FOREIGN KEY (payer) REFERENCES members(members_id),
                          FOREIGN KEY (receiver) REFERENCES members(members_id)
);