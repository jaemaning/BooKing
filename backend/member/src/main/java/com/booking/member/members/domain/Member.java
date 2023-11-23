package com.booking.member.members.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.ws.rs.DefaultValue;

//@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "members")
@AllArgsConstructor
@Builder
public class Member {
    @Id
//    @GeneratedValue
    @Column("members_id")
    private Integer id;

    private String loginId;

    @Setter
    private String email;

    @Setter
    private Integer age;

    @Setter
    private Gender gender;

    @Setter
    @Column
    private String nickname;

    @Setter
    @Column
    private String fullName;

    //@Column(columnDefinition = "TEXT")
//    @Setter
//    @Column(length = 255)
//    private String address;
    @Column("latitude")
    @Setter
    @DefaultValue("0")
    private Double lat;

    @Column("longitude")
    @Setter
    @DefaultValue("0")
    private Double lgt;

    @Setter
    private UserRole role;

    @Setter
    private String profileImage;

//    @OneToMany(mappedBy = "following",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Follow> followingMembers=new ArrayList<>(); // Members this user is following
//
//    @OneToMany(mappedBy = "follower",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Follow> followerMembers=new ArrayList<>();

    private String provider;


    @DefaultValue("0")
    @Setter
    private Integer point;

//    @OneToMany(mappedBy = "payer",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Payment> payments=new ArrayList<>();
//
//    @OneToMany(mappedBy = "receiver",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Payment> receivers=new ArrayList<>();
}
