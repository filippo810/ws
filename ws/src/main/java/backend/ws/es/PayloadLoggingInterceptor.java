package backend.ws.es;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

public class PayloadLoggingInterceptor
	implements ClientInterceptor {

	private static final Logger log = LogManager.getLogger(PayloadLoggingInterceptor.class);

	private static final boolean IS_DEBUG_ENABLED = log.isDebugEnabled();


	protected boolean isLogEnabled() {
		return log.isDebugEnabled();
	}


	protected void logMessage(String message) {
		log.debug(message);
	}


	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

		DOMSource source = (DOMSource) messageContext.getRequest().getPayloadSource();
		String xml = sourceToString(source);

		if (IS_DEBUG_ENABLED) {

			log.debug(xml);

		}

		return true;
	}


	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {

		if (IS_DEBUG_ENABLED) {

			// Log Response
			DOMSource source = (DOMSource) messageContext.getResponse().getPayloadSource();
			String xml = sourceToString(source);
			log.debug(xml);

		}

		return true;

	}


	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {

		return true;
	}


	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex)
		throws WebServiceClientException {
		// TODO Auto-generated method stub

	}


	private String sourceToString(DOMSource source) {

		try {

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			return result.getWriter().toString();

		} catch (TransformerException ex) {
			log.error("NapPayloadLoggingInterceptor error", ex);
			return null;
		}

	}

}
