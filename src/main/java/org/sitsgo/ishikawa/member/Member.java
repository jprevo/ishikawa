package org.sitsgo.ishikawa.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.sitsgo.ishikawa.gowebsite.ffg.FFGProfile;

import java.util.Date;

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
    private String osrUsername;
    private Integer ffgId;
    private String ffgName;
    private String ffgRankHybrid;
    private String ffgRankMain;
    private Boolean anonymous = false;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/YY à HH:mm")
    private Date ffgLastCheck;

    private Boolean isAdmin = false;
    private Boolean isInClub;

    @Override
    public String toString() {
        return String.format(
                "Member %s [id=%d]",
                getDisplayName(),
                id);
    }

    public String getDisplayName() {
        if (hasFfgData() && !getAnonymous()) {
            return getFfgName();
        }

        return getDiscordDisplayName();
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
        if (shouldRemoveUsername(kgsUsername)) {
            this.kgsUsername = null;
            return;
        }

        this.kgsUsername = kgsUsername;
    }

    public String getFoxUsername() {
        return foxUsername;
    }

    public boolean hasFoxUsername() {
        return foxUsername != null;
    }

    public void setFoxUsername(String foxUsername) {
        if (shouldRemoveUsername(foxUsername)) {
            this.foxUsername = null;
            return;
        }

        this.foxUsername = foxUsername;
    }

    public String getTygemUsername() {
        return tygemUsername;
    }

    public void setTygemUsername(String tygemUsername) {
        if (shouldRemoveUsername(tygemUsername)) {
            this.tygemUsername = null;
            return;
        }

        this.tygemUsername = tygemUsername;
    }

    public boolean hasTygemUsername() {
        return tygemUsername != null;
    }

    public String getIgsUsername() {
        return igsUsername;
    }

    public void setIgsUsername(String igsUsername) {
        if (shouldRemoveUsername(igsUsername)) {
            this.igsUsername = null;
            return;
        }

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
        this.setFfgLastCheck(new Date());
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

    public Date getFfgLastCheck() {
        return ffgLastCheck;
    }

    public void setFfgLastCheck(Date ffgLastCheck) {
        this.ffgLastCheck = ffgLastCheck;
    }

    public static boolean shouldRemoveUsername(String username) {
        return username.equals(getUsernameClearingValue());
    }

    public static String getUsernameClearingValue() {
        return "-";
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
