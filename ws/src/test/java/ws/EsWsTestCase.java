package ws;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import backend.ws.es.IEsEndpoint;
import domain.wrapper.VADomain;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/ws-config.xml")
public class EsWsTestCase {

	@Autowired
	private IEsEndpoint esEndpointImpl;

	@Test
	public void testEsWs() {

		// Search req = new Search();
		VADomain obj = new VADomain();

		obj.setDomanda("ciao");

		// chiamata

		this.esEndpointImpl.search(obj);

	}

}
