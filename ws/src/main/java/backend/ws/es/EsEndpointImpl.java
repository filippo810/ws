package backend.ws.es;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;

import domain.wrapper.VADomain;
import model.ws.es.Configuration;
import model.ws.es.ConfigurationParams;
import model.ws.es.Document;
import model.ws.es.ProductSearchQuery;
import model.ws.es.Search;
import model.ws.es.SearchResponse;
import model.ws.es.SearchResult;

public class EsEndpointImpl implements IEsEndpoint {

	private WebServiceTemplate wsTemplate;

	public void setWsTemplate(WebServiceTemplate wsTemplate) {
		this.wsTemplate = wsTemplate;
	}

	private static Logger log = LogManager.getLogger(EsEndpointImpl.class);

	private SearchResponse search(Search req) {

		SearchResponse response = null;

		try {
			response = (SearchResponse) this.wsTemplate.marshalSendAndReceive(req);

		} catch (Exception e) {
			log.error(e);
		}
		return response;
	}

	private Search createSearch(VADomain obj) {

		Search req = new Search();

		ProductSearchQuery psq = new ProductSearchQuery();
		req.setArg0(psq);

		psq.setClientType("SOAP");

		psq.setFlagDidYouMean(true);
		psq.setGenerateStats(false);
		psq.setPageNumber(1);
		psq.setPageSize(5);

		psq.setProjectLanguage("ITA");
		psq.setQueryLanguage("ITA");

		psq.setText(obj.getDomanda());

		Configuration conf = new Configuration();
		psq.setConfiguration(conf);
		ConfigurationParams dcp = new ConfigurationParams();
		dcp.setConfigId("ITA");
		dcp.getCorpusNames().add("/es/it/unicreditfaqc");
		String[] sss = new String[] { "KEYWORD=20", "TITLE=15", "BODY=1", "METADATA=5" };
		dcp.getSectionWeights().addAll(Arrays.asList(sss));

		conf.setFaqcConfigurationParams(dcp);

		return req;

	}

	private void mapResponse(VADomain obj, SearchResponse res) {
		Document documents = null;
		List<model.ws.es.Field> fields = null;
		SearchResult value = new SearchResult();
		value = res.getReturn().getSimpleFaqSearchResult();
		obj.setRisposta(value.getMessage());
		System.out.println(" Risposta value: " + obj.getRisposta().toString());

		for (Iterator<Document> it = value.getDocuments().iterator(); it.hasNext();) {
			for (int i = 0; i < value.getDocuments().size(); i++) {
				documents = it.next();
				for (Iterator<model.ws.es.Field> itf = it.next().getFields().iterator(); itf.hasNext();) {
					System.out.println(" iterazione:" + it.toString() + "value getMessage : " + value.getMessage()
							+ " documents" + value.getDocuments().listIterator(i).toString());

					for (int j = 0; j < documents.getFields().size(); j++) {
						fields = documents.getFields();
						if (fields.get(j).getFieldId().toString().indexOf("SUGGESTION") != -1) {
							Object[] sugg = fields.get(j).getFieldValues().toArray();
							obj.setSuggestion(sugg);
							System.out.println("sugg :" + sugg.toString().trim());
							
							System.out.println("Suggestion:" + sugg);
						} else if (fields.get(j).getFieldId().toString().indexOf("LINK") != -1) {
							String sugg_link = fields.get(j).getFieldValues().toString();
							obj.setSuggestion_link(sugg_link);
							System.out.println("sugg Link:" + sugg_link);

						} else if (fields.get(j).getFieldId().toString().indexOf("ANSWER") != -1) {
							String answ = fields.get(j).getFieldValues().toString();
							obj.setSuggestion_link(answ);
							System.out.println("Answer:" + answ);

						} else if (fields.get(j).getFieldId().toString().indexOf("TITLE") != -1) {
							String titl = fields.get(j).getFieldValues().toString();
							obj.setTitle(titl);
							System.out.println("Title:" + titl);

						} else if (fields.get(j).getFieldId().toString().indexOf("QUESTION") != -1) {
							String quest = fields.get(j).getFieldValues().toString();
							obj.setQuestion(quest);
							System.out.println("Title:" + quest);

						}

						// obj.getRisposte().add(i,
						// value.getDocuments().get(i).toString());
						System.out.println("Current Item is: " + it.next());
					}
				}
			}
		}
	}

	public void search(VADomain domain) {
		Search request = this.createSearch(domain);

		SearchResponse response = this.search(request);

		this.mapResponse(domain, response);

	}

}
