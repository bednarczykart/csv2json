package pl.pierce.testtask.csv2json.locale;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class AppLocaleResolver extends AcceptHeaderLocaleResolver {

    private final static List<Locale> LOCALES_LIST = Arrays.asList(
            new Locale("cs", "CZ"),
            new Locale("nl", "NL"),
            new Locale("da", "DK"),
            new Locale("pl", "PL"),
            new Locale("it", "IT"),
            new Locale("da", "DK"),
            new Locale("nb", "NO"),
            new Locale("de", "AT"),
            new Locale("es", "ES"),
            new Locale("nl", "BE"),
            new Locale("fr", "FR"),
            new Locale("de", "DE"),
            new Locale("fi", "FI"),
            new Locale("sv", "SE"),
            new Locale("en", "US"),
            new Locale("en", "GB"),
            new Locale("de", "CH"),
            new Locale("en", "IE"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        if( headerLang == null || headerLang.isEmpty()) {
            return Locale.getDefault();
        }

        return Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES_LIST);
    }

    private boolean isLangSupported(String lang) {
        return LOCALES_LIST.stream().anyMatch(locale -> lang.equals(locale.getLanguage() + "_" + locale.getCountry()));
    }

    public String getLangAndCountry(HttpServletRequest request, String langParam) {
        String lang;
        if(langParam == null || langParam.isEmpty() || !isLangSupported(langParam)) {
            Locale locale = resolveLocale(request);
            lang = locale.getLanguage() + "_" + locale.getCountry();
        }
        else {
            lang = langParam;
        }
        return lang;
    }
}

