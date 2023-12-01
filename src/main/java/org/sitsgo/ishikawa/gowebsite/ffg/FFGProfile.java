package org.sitsgo.ishikawa.gowebsite.ffg;

public class FFGProfile {
    String name;
    String mainRank;
    String hybridRank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainRank() {
        return mainRank;
    }

    public void setMainRank(String mainRank) {
        this.mainRank = mainRank;
    }

    public void clearMainRank() {
        this.mainRank = null;
    }

    public String getHybridRank() {
        return hybridRank;
    }

    public void clearHybridRank() {
        this.hybridRank = null;
    }

    public void setHybridRank(String hybridRank) {
        this.hybridRank = hybridRank;
    }

    public void clear() {
        name = null;
        mainRank = null;
        hybridRank = null;
    }
}
