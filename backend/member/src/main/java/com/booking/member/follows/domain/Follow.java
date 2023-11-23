package com.booking.member.follows.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

//@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "follows")
@Builder
@AllArgsConstructor
public class Follow {
    @Id
//    @GeneratedValue
    @Column("follows_id")
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name = "following_id")
//    private Member following;
    @Column("following_id")
    private Integer following;

//    @ManyToOne
//    @JoinColumn(name = "follower_id")
//    private Member follower;
    @Column("follower_id")
    private Integer follower;
}
