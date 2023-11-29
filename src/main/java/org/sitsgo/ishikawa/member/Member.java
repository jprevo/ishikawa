package org.sitsgo.ishikawa.member;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private Integer ogsId;

    @Column(nullable = true)
    private String ogsUsername;

    public Member() {
    }

    @Override
    public String toString() {
        return String.format(
                "Member[id=%d, ogsId=%d]",
                id, ogsId);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getOgsId() {
        return ogsId;
    }

    public void setOgsId(int ogsId) {
        this.ogsId = ogsId;
    }

    public String getOgsUsername() {
        return ogsUsername;
    }

    public void setOgsUsername(String ogsUsername) {
        this.ogsUsername = ogsUsername;
    }
}
