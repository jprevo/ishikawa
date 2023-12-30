package org.sitsgo.ishikawa.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class I18n {

    private final MessageSource source;

    public I18n(MessageSource source) {
        this.source = source;
    }

    public String t(String message, Object... args) {
        return source.getMessage(message, args, Locale.FRANCE);
    }

    public String t(String message) {
        return source.getMessage(message, null, Locale.FRANCE);
    }
}
