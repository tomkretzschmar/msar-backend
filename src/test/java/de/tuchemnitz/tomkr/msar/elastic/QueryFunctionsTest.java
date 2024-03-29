package de.tuchemnitz.tomkr.msar.elastic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.elastic.DocumentFunctions;
import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;
import de.tuchemnitz.tomkr.msar.elastic.QueryFunctions;
import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;


@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryFunctionsTest {

	private static Logger LOG = LoggerFactory.getLogger(QueryFunctionsTest.class);

	@Autowired
	QueryFunctions elastic;
	
	@Autowired
	IndexFunctions indexService;
	
	@Autowired
	DocumentFunctions documentService;
	
	@Autowired
	Config config;

	@Before
	public void before() {
		indexService.deleteIndex(config.getIndex());
	}

	
	
	@Test
	public void testSaveAndRetrieve() throws InterruptedException {
		LOG.debug("------------------------ testSaveAndRetrieve ------------------------ ");
		String basePath = "D:\\ws\\eclipse_ws\\metaapp2\\src\\test\\resources\\jsonExamples\\";
		
		Map<String, String> metaData = new HashMap<>();
		metaData.put("exifExample1.json", "exif");
		metaData.put("exifExample2.json", "exif");
		metaData.put("exifExample3.json", "exif");
		metaData.put("objectsExample1.json", "objects");
		metaData.put("objectsExample2.json", "objects");
		metaData.put("locationExample1.json", "location");
		metaData.put("locationExample2.json", "location");
		metaData.put("locationExample3.json", "location");
		
		for(Entry<String, String> entry : metaData.entrySet()) {
			Map<String, Object> map = JsonHelpers.readJsonToMapFromFile(new File(basePath + entry.getKey()));
			documentService.indexDocument(map);
		}
		
		Thread.sleep(5000);
		LOG.debug("------------------------ indexing done ------------------------");
		
//		elastic.searchAll();
		
		elastic.searchByValue("Nikon");
		elastic.searchByValue("Canon");
		elastic.searchByValue("EF50mm + Nikon");
		elastic.searchByValue("EF50mm + -Nikon");
		
		elastic.searchByValue("chair");
		elastic.searchByValue("chair + table");
		elastic.searchByValue("type:objects + -table");
		
		// fails because no mapping exists
		elastic.getSuggestions("Dresde", "city.completion");
		elastic.getSuggestions("Dre", "city.completion");
		elastic.getSuggestions("D", "city.completion");
		
		elastic.getSuggestions("Der", "city.completion");
		elastic.getSuggestions("Dersden", "city.completion");
		elastic.getSuggestions("Dersdi", "city.completion");
		elastic.getSuggestions("B", "city.completion");
		
		
		elastic.getSuggestions("Cha", "objects.completion");
		elastic.getSuggestions("Ta", "objects.completion");
		
		LOG.debug("--------------------------------------------------------------------- ");
	}
}
