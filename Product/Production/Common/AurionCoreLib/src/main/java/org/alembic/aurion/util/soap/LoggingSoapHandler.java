package org.alembic.aurion.util.soap;

import java.util.Collections;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * SOAPHandler for SOAP message logging.
 * 
 * @author Neil Webb
 */
public class LoggingSoapHandler implements SOAPHandler<SOAPMessageContext> {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LoggingSoapHandler.class);

    @SuppressWarnings("unchecked")
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    public boolean handleMessage(SOAPMessageContext context) {
        log.debug("Begin LoggingSoapHandler.handleMessage(...)");
        new SoapLogger().logSoapMessage(context);
        log.debug("End LoggingSoapHandler.handleMessage(...)");
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
    }

}
