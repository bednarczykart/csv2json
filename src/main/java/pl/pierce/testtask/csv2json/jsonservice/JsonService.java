package pl.pierce.testtask.csv2json.jsonservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.pierce.testtask.csv2json.csv.Option;
import pl.pierce.testtask.csv2json.locale.AppLocaleResolver;
import pl.pierce.testtask.csv2json.csv.Attribute;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JsonService {

    private static final String LABEL_PREFIX = "label-";
    final private AppLocaleResolver appLocaleResolver = new AppLocaleResolver();

    private Map<String, Attribute> attributesMap;

    public JsonService(final ApplicationArguments arguments) {
        String jsonPath = arguments.getSourceArgs()[0];
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File(jsonPath);
            List<Attribute> attributesList = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Attribute[].class)));
            attributesMap = attributesList.stream().collect(Collectors.toMap(Attribute::getId, Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Attribute> getAttributes(HttpServletRequest request, String langParam) {
        String lang = appLocaleResolver.getLangAndCountry(request, langParam);
        List<Attribute> allAttributes = new ArrayList<> (attributesMap.size());
        attributesMap.forEach((key, value) -> allAttributes.add(createAttributeForLang(value, lang)));
        return allAttributes;
    }

    private Attribute createAttributeForLang(Attribute attribute, String lang) {
        Attribute newAttribute = Attribute.builder().
                id(attribute.getId()).
                build();
        String key = LABEL_PREFIX + lang;
        Map<String, String> newTranslationLabels = new HashMap<>(1);
        newTranslationLabels.put(key, attribute.getTranslationLabels().get(key));
        newAttribute.setTranslationLabels(newTranslationLabels);
        return newAttribute;
    }

    public Attribute getSingleAttribute( String id, HttpServletRequest request, String langParam) {
        Attribute attribute = attributesMap.get(id);
        if (attribute!= null) {
            String lang = appLocaleResolver.getLangAndCountry(request, langParam);
            return createAttributeForLang(attribute, lang);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find attribute id=" + id);
    }

    public Attribute getAttributeWithOptions(String id, HttpServletRequest request, String langParam) {
        Attribute attribute = getSingleAttribute(id, request, langParam);
        String key = LABEL_PREFIX + appLocaleResolver.getLangAndCountry(request, langParam);
        List<Option> optionList = new ArrayList<>();

        for(Option option : attributesMap.get(id).getOptions()) {
            Map<String, String> newLabelTranslations = new HashMap<>();
            newLabelTranslations.put(key, option.getLabelTranslations().get(key));

            optionList.add(Option.builder().
                    id(option.getId()).
                    sortOrder(option.getSortOrder()).
                    labelTranslations(newLabelTranslations).
                    build());
        }
        attribute.setOptions(optionList);
        return attribute;
    }

    public Attribute postAttribute(Attribute attribute) {

        return attributesMap.put(attribute.getId(), attribute);
    }

    public Option postAtributesOption(String id, Option option) {
        if(attributesMap.containsKey(id)) {
            attributesMap.get(id).getOptions().add(option);
            return option;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find attribute id=" + id);
    }

    public void deleteAttribute(String id) {
        if(attributesMap.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find attribute id=" + id);
        }
    }

    public void deleteAttributesOption(String id, String optionId) {
        if(attributesMap.containsKey(id)) {
            attributesMap.get(id).getOptions().removeIf(option -> option.getId().equals(optionId));
        }
    }
}
