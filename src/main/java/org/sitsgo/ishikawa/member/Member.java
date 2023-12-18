package org.sitsgo.ishikawa.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGProfile;

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
    private String igsUsername;
    private Integer ffgId;
    private String ffgName;
    private String ffgRankHybrid;
    private String ffgRankMain;
    private Boolean anonymous = false;
    private String osrUsername;
    private Boolean isInClub;

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

    public boolean hasOgsUsername() {
        return ogsUsername != null;
    }

    public String getKgsUsername() {
        return kgsUsername;
    }

    public boolean hasKgsUsername() {
        return kgsUsername != null;
    }

    public void setKgsUsername(String kgsUsername) {
        this.kgsUsername = kgsUsername;
    }

    public String getFoxUsername() {
        return foxUsername;
    }

    public boolean hasFoxUsername() {
        return foxUsername != null;
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

    public boolean hasTygemUsername() {
        return tygemUsername != null;
    }

    public String getIgsUsername() {
        return igsUsername;
    }

    public void setIgsUsername(String igsUsername) {
        this.igsUsername = igsUsername;
    }

    public boolean hasIgsUsername() {
        return this.igsUsername != null;
    }

    public boolean hasFfgData() {
        return ffgId != null && ffgName != null;
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

    public boolean hasFfgRankHybrid() {
        return ffgRankHybrid != null;
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

    public void updateFromFFGProfile(FFGProfile profile) {
        this.setFfgName(profile.getName());
        this.setFfgRankMain(profile.getMainRank());
        this.setFfgRankHybrid(profile.getHybridRank());
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getOsrUsername() {
        return osrUsername;
    }

    public void setOsrUsername(String osrUsername) {
        this.osrUsername = osrUsername;
    }

    public boolean hasOsrUsername() {
        return osrUsername != null;
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

    public Boolean getInClub() {
        return isInClub;
    }

    public void setInClub(Boolean inClub) {
        isInClub = inClub;
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
