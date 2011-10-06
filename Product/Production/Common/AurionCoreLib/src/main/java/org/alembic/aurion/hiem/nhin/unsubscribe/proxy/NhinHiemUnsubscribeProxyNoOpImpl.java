/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.hiem.nhin.unsubscribe.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.hiem.consumerreference.ReferenceParametersElements;
import org.alembic.aurion.hiem.nhin.subscribe.proxy.NhinHiemSubscribeProxyNoOpImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author JHOPPESC
 */
public class NhinHiemUnsubscribeProxyNoOpImpl implements NhinHiemUnsubscribeProxy {

    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        return buildResponseElement();
    }

    private Element buildResponseElement()
    {
        Element responseElement = null;
        try
        {
            Document document = null;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            document = docBuilder.newDocument();
            responseElement = document.createElementNS("http://docs.oasis-open.org/wsn/b-2", "UnsubscribeResponse");
        }
        catch (ParserConfigurationException ex) {
            Logger.getLogger(NhinHiemSubscribeProxyNoOpImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseElement;
    }

}
