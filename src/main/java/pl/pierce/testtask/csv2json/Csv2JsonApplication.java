package pl.pierce.testtask.csv2json;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.pierce.testtask.csv2json.csv.Csv2Json;

@SpringBootApplication
@OpenAPIDefinition
public class Csv2JsonApplication {

	private static final String ATTRIBUTES_PATH = "src/main/resources/attributes.csv";
	private static final String OPTIONS_PATH = "src/main/resources/options.csv";

	private static final String JSON_PATH = "src/main/resources/attributes.json";

	public static void main(String[] args) {

		Csv2Json csv2Json = new Csv2Json(ATTRIBUTES_PATH, OPTIONS_PATH);
		csv2Json.readCsvFiles();
		csv2Json.createJsonFile(JSON_PATH);

		String[] myArgs = {JSON_PATH};
		SpringApplication.run(Csv2JsonApplication.class, myArgs);
	}

}
