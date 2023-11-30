package org.sitsgo.ishikawa.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long discordId;
    private String discordUsername;
    private String discordDiscriminator;
    private String discordDisplayName;
    private String discordAvatarUrl;
    private Integer ogsId;
    private String ogsUsername;
    private String kgsUsername;
    private String foxUsername;
    private String tygemUsername;
    private Integer ffgId;
    private String ffgName;
    private String ffgRankHybrid;
    private String ffgRankMain;
    private Boolean anonymous = false;
    private String egfKey;
    private String egfName;
    private String efgRank;
    private String osrUsername;
    private Boolean isMember;

    public Member() {
    }

    @Override
    public String toString() {
        return String.format(
                "Member[id=%d]",
                id);
    }

    public Long getId() {
        return id;
    }

    public Integer getOgsId() {
        return ogsId;
    }

    public void setOgsId(Integer ogsId) {
        this.ogsId = ogsId;
    }

    public String getOgsUsername() {
        return ogsUsername;
    }

    public void setOgsUsername(String ogsUsername) {
        this.ogsUsername = ogsUsername;
    }

    public String getKgsUsername() {
        return kgsUsername;
    }

    public void setKgsUsername(String kgsUsername) {
        this.kgsUsername = kgsUsername;
    }

    public String getFoxUsername() {
        return foxUsername;
    }

    public void setFoxUsername(String foxUsername) {
        this.foxUsername = foxUsername;
    }

    public String getTygemUsername() {
        return tygemUsername;
    }

    public void setTygemUsername(String tygemUsername) {
        this.tygemUsername = tygemUsername;
    }

    public Integer getFfgId() {
        return ffgId;
    }

    public void setFfgId(Integer ffgId) {
        this.ffgId = ffgId;
    }

    public String getFfgName() {
        return ffgName;
    }

    public void setFfgName(String ffgName) {
        this.ffgName = ffgName;
    }

    public String getFfgRankHybrid() {
        return ffgRankHybrid;
    }

    public void setFfgRankHybrid(String ffgRankHybrid) {
        this.ffgRankHybrid = ffgRankHybrid;
    }

    public String getFfgRankMain() {
        return ffgRankMain;
    }

    public void setFfgRankMain(String ffgRankMain) {
        this.ffgRankMain = ffgRankMain;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getEgfKey() {
        return egfKey;
    }

    public void setEgfKey(String egfKey) {
        this.egfKey = egfKey;
    }

    public String getEgfName() {
        return egfName;
    }

    public void setEgfName(String egfName) {
        this.egfName = egfName;
    }

    public String getEfgRank() {
        return efgRank;
    }

    public void setEfgRank(String efgRank) {
        this.efgRank = efgRank;
    }

    public String getOsrUsername() {
        return osrUsername;
    }

    public void setOsrUsername(String osrUsername) {
        this.osrUsername = osrUsername;
    }

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public void setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }

    public String getDiscordDiscriminator() {
        return discordDiscriminator;
    }

    public void setDiscordDiscriminator(String discordDiscriminator) {
        this.discordDiscriminator = discordDiscriminator;
    }

    public Boolean getMember() {
        return isMember;
    }

    public void setMember(Boolean member) {
        isMember = member;
    }

    public String getDiscordDisplayName() {
        return discordDisplayName;
    }

    public void setDiscordDisplayName(String discordDisplayName) {
        this.discordDisplayName = discordDisplayName;
    }

    public String getDiscordAvatarUrl() {
        return discordAvatarUrl;
    }

    public void setDiscordAvatarUrl(String discordAvatarUrl) {
        this.discordAvatarUrl = discordAvatarUrl;
    }
}
