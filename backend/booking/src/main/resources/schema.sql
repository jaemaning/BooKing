DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS participants;
DROP TABLE IF EXISTS waitlist;
DROP TABLE IF EXISTS participants_state;
DROP TABLE IF EXISTS meetinginfos;
DROP TABLE IF EXISTS hashtag_meeting;
DROP TABLE IF EXISTS meetings;
DROP TABLE IF EXISTS hashtags;

CREATE TABLE meetings (
                          meeting_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          leader_id INT,
                          lat DOUBLE,
                          lgt DOUBLE,
                          book_isbn VARCHAR(255),
                          meeting_title VARCHAR(255),
                          description VARCHAR(255),
                          max_participants INT,
                          meeting_state VARCHAR(255),
                          address VARCHAR(255),
                          created_at DATETIME,
                          updated_at DATETIME
);

CREATE TABLE hashtags (
                          hashtag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          content VARCHAR(255)
);

CREATE TABLE hashtag_meeting (
                                 meeting_hashtag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 meeting_id BIGINT,
                                 hashtag_id BIGINT,
                                 FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id),
                                 FOREIGN KEY (hashtag_id) REFERENCES hashtags(hashtag_id)
);

CREATE TABLE meetinginfos (
                              meetinginfo_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              meeting_id BIGINT,
                              date DATETIME,
                              location VARCHAR(255),
                              address VARCHAR(255),
                              lat DOUBLE,
                              lgt DOUBLE,
                              fee INT,
                              FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id)
);

CREATE TABLE participants (
                              participant_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              member_id INT,
                              meeting_id BIGINT,
                              FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id)
);

CREATE TABLE waitlist (
                          waitlist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          member_id INT,
                          meeting_id BIGINT,
                          FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id)
);

CREATE TABLE participants_state (
                          state_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          member_id INT,
                          meetinginfo_id BIGINT,
                          attendance_status TINYINT(1),
                          payment_status TINYINT(1),
                          FOREIGN KEY (meetinginfo_id) REFERENCES meetinginfos(meetinginfo_id)
);

CREATE TABLE posts (
                       post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       meeting_id BIGINT,
                       member_id INT,
                       title VARCHAR(255),
                       content TEXT,
                       created_at DATETIME,
                       updated_at DATETIME,
                       FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id)
);
