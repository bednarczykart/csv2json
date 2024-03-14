package pl.pierce.testtask.csv2json.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Csv2Json {

    private static final int CSV_HEADER_INDEX = 0;
    private static final char CSV_SEPARATOR = ';';
    private static final int CSV_ATTRIBUTE_NAME_POSITION = 0;

    private final String attributesPath;
    private final String optionsPath;

    private final List<Attribute> attributes = new ArrayList<>();

    public Csv2Json(String attributesPath, String optionsPath) {
        this.attributesPath = attributesPath;
        this.optionsPath = optionsPath;
    }

    public void readCsvFiles() {
        readAttributesFile();
        readOptionsFile();
    }

    private void readAttributesFile() {
        List<String[]> allAttributes = readCsvFileFromPath(attributesPath);

        if (!allAttributes.isEmpty()) {
            String[] header = allAttributes.remove(CSV_HEADER_INDEX);

            for (String[] singleAttribute : allAttributes) {
                Attribute newAttribute = Attribute.builder().
                        id(singleAttribute[CSV_ATTRIBUTE_NAME_POSITION]).build();

                Map<String, String> labelsMap = new HashMap<>(header.length-1);
                for (int i = 1; i < header.length; i++) {
                    labelsMap.put(header[i], singleAttribute[i]);
                }
                newAttribute.setTranslationLabels(labelsMap);
                attributes.add(newAttribute);
            }
        }
    }

    private void readOptionsFile() {
        List<String[]> allOptions = readCsvFileFromPath(optionsPath);

        if (!allOptions.isEmpty()) {
            String[] header = allOptions.remove(CSV_HEADER_INDEX);

            for (String[] singleOption : allOptions) {
                Option newOption = Option.builder().
                        id( singleOption[CSV_ATTRIBUTE_NAME_POSITION]).
                        sortOrder(Integer.parseInt(singleOption[header.length-1])).
                        build();

                Map<String, String> labelsMap = new HashMap<>(header.length-3);
                for (int i = 1; i < header.length-2; i++) {
                    labelsMap.put(header[i], singleOption[i]);
                }
                newOption.setLabelTranslations(labelsMap);

                final int attributeIndex =  header.length-2;
                Optional<Attribute> existingAttribute = attributes.stream().
                        filter(attribute -> attribute.getId().equals(singleOption[attributeIndex])).
                        findFirst();

                if(existingAttribute.isPresent()) {
                    if(existingAttribute.get().getOptions() == null) {
                        existingAttribute.get().setOptions(new ArrayList<>());
                    }
                    existingAttribute.get().getOptions().add(newOption);
                }
            }
        }
    }

    private List<String[]> readCsvFileFromPath(String path) {
        List<String[]> csvFileContent = new LinkedList<>();

        try {
            CSVParser parser = new CSVParserBuilder().withSeparator(CSV_SEPARATOR).build();
            try (CSVReader csvReader = new CSVReaderBuilder(
                    new FileReader(path))
                    .withCSVParser(parser)
                    .build()) {
                csvFileContent = csvReader.readAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return csvFileContent;
    }

    public void createJsonFile(String path) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(new File(path), attributes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
