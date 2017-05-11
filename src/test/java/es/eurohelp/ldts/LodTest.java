package es.eurohelp.ldts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author grozadilla
 * @author Mikel Egaña Aranguren
 *
 */
public class LodTest  {

	LinkedDataRequestBean requestBean;
	static List<LinkedDataRequestBean> tests = new ArrayList<LinkedDataRequestBean>();
	
	//http://172.16.0.81:8008/id/sector-publico/contrato/1-gobierno-vasco-donostia-easo-10-3024.0-2016-05-09
	//Accept: application/rdf+xml
	
	@Test
	public final void testGETResourceRDFXMLContent (){ 
	}
	
	@Test
	public final void testGETResourceRDFXML200 (){ 
		try {
			String baseUri = PropertiesManager.getInstance().getProperty("lod.baseUri");
			String method = Methodtype.GET.methodtypevalue();
			String accept = MIMEtype.RDFXML.mimetypevalue();
			String pathUri = "id/sector-publico/contrato/1-gobierno-vasco-donostia-easo-10-3024.0-2016-05-09";
			Map<String, String> parameters = new HashMap<String, String>();
			requestBean = new LinkedDataRequestBean(method,accept, baseUri, pathUri, parameters);
			HttpManager.getInstance().doRequest(requestBean);
			assertEquals(requestBean.getStatus(), 200);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testSPARQLPOSTNamedGraphsMetadataCSV200 (){ 
		try {
			String baseUri = PropertiesManager.getInstance().getProperty("lod.baseUri");
			String method = Methodtype.POST.methodtypevalue();
			String accept = MIMEtype.CSV.mimetypevalue();
			String pathUri = "sparql";
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("query",
					"SELECT * WHERE { ?sub ?pred ?obj .} LIMIT 10");
			requestBean = new LinkedDataRequestBean(method,accept, baseUri, pathUri, parameters);
			HttpManager.getInstance().doRequest(requestBean);
			assertEquals(requestBean.getStatus(), 200);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testSPARQLPOSTNamedGraphsMetadataCSVContent (){ 
		try {
			String baseUri = PropertiesManager.getInstance().getProperty("lod.baseUri");
			String method = Methodtype.POST.methodtypevalue();
			String accept = MIMEtype.CSV.mimetypevalue();
			String pathUri = "sparql";
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("query",
					"SELECT DISTINCT ?g ?p ?o "
					+ "WHERE { "
					+ "	?g ?p ?o ."
					+ "	GRAPH ?g "
					+ "		{"
					+ "			?sub ?pred ?obj"
					+ "		} "
					+ "}");
			requestBean = new LinkedDataRequestBean(method,accept, baseUri, pathUri, parameters);
			HttpManager.getInstance().doRequest(requestBean);
			String resultsPathName = PropertiesManager.getInstance().getProperty("lod.report.path") + requestBean.getTestName();

			// TODO: meter content en el Bean
			File file = new File(resultsPathName);
			String response_string = FileUtils.readFileToString(file);
			System.out.println("Read in: " + response_string);
			assertTrue(response_string.contains(
					"http://es.euskadi.eus/dataset/id/registro-de-contratos-del-sector-publico-de-euskadi,"
					+ "http://www.w3.org/1999/02/22-rdf-syntax-ns#type,http://rdfs.org/ns/void#Dataset"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//	/**
//	 * Test method for {@link es.eurohelp.ldts.HttpManager#doRequest(es.eurohelp.ldts.LinkedDataRequestBean)}.
//	 */
//	@Test
//	public final void testDataExecute() {
//		try {	
//			String method = Methodtype.GET.methodtypevalue();
//			String accept = MIMEtype.RDFXML.mimetypevalue();
//			String baseUri = PropertiesManager.getInstance().getProperty("lod.baseUri");
//			String pathUri = "data/turismo/alojamiento/Hotel/HotelAbando";  
//			Map<String, String> parameters = new HashMap<String, String>();
//			requestBean = new LinkedDataRequestBean(method,accept, baseUri, pathUri, parameters);
//			HttpManager.getInstance().doRequest(requestBean);
//			assertEquals(requestBean.getStatus(), 200);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//	}
//	
//	/**
//	 * Test method for {@link es.eurohelp.ldts.HttpManager#doRequest(es.eurohelp.ldts.LinkedDataRequestBean)}.
//	 */
//	@Test
//	public final void testRedirectExecute() {
//		try {	
//			String method = Methodtype.GET.methodtypevalue();
//			String accept = MIMEtype.HTML.mimetypevalue();
//			String baseUri = PropertiesManager.getInstance().getProperty("lod.baseUri");
//			String pathUri = "id/turismo/alojamiento/Hotel/HotelAbando";  
//			Map<String, String> parameters = new HashMap<String, String>();
//			requestBean = new LinkedDataRequestBean(method,accept, baseUri, pathUri, parameters);
//			HttpManager.getInstance().doRequest(requestBean);
//			assertEquals(requestBean.getStatus(), 302);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//	}
	
	@Rule
	public TestRule watchman = new TestWatcher() {
 
		@Override
		public Statement apply(Statement base, Description description) {
			return super.apply(base, description);
		}
 
		@Override
		protected void succeeded(Description description) {
			try {
				requestBean.setStatus(0);
				tests.add(requestBean);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}
 
		@Override
		protected void failed(Throwable e, Description description) {
			try {
				requestBean.setStatus(1);
				tests.add(requestBean);
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
	};
	
	@AfterClass
	public static void createReport() {
		try {
			ReportManager.getInstance().createReport(tests);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}