package org.sitsgo.ishikawa.gowebsite.ffg;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sitsgo.ishikawa.go.PlayerUtil;
import org.sitsgo.ishikawa.gowebsite.WebsiteParsingException;

public class FFGExtractor {
    private final Document document;

    public FFGExtractor(Document document) {
        this.document = document;
    }

    public String getName() throws WebsiteParsingException {
        Element nameElement = document.selectFirst("#ffg_main_content h3");

        if (nameElement == null) {
            throw new WebsiteParsingException("This FFG Member has no name");
        }

        String name = nameElement.text().trim();

        if (name.isEmpty()) {
            throw new WebsiteParsingException("This FFG Member has no name");
        }

        return name;
    }

    public String getMainRank() throws WebsiteParsingException {
        Element mainRankElement = document.selectFirst("#ffg_main_content div:contains(Échelle principale)");

        if (mainRankElement == null) {
            throw new WebsiteParsingException("This FFG Member has no main rank");
        }

        return convertRankElement(mainRankElement);
    }

    public String getHybridRank() throws WebsiteParsingException {
        Element hybridRankElement = document.selectFirst("#ffg_main_content div:contains(Échelle hybride)");

        if (hybridRankElement == null) {
            throw new WebsiteParsingException("This FFG Member has no main rank");
        }

        return convertRankElement(hybridRankElement);
    }

    private String convertRankElement(Element element) throws WebsiteParsingException {
        String rank = parseRankFromElement(element);

        if (!PlayerUtil.isRankValid(rank)) {
            throw new WebsiteParsingException("This rank is not valid");
        }

        return rank;
    }

    private String parseRankFromElement(Element rankElement) {
        String rankText = rankElement.text();

        String[] parts = rankText.split(":");
        String rank = parts[parts.length - 1];
        rank = rank.trim();

        return rank;
    }
}
