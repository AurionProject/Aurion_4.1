package org.alembic.aurion.docregistry.adapter.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetSystemType;
import org.alembic.aurion.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import org.alembic.aurion.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
@Ignore // TODO: Move to an integration test
public class AdapterDocumentRegistryWebServiceProxyTest {

    public AdapterDocumentRegistryWebServiceProxyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testQueryForDocument() throws Exception {
    }

    @Test
    public void InvokeMock() throws Exception {
        String url = "http://localhost:8088/mockDocumentRegistry";
        AdapterComponentDocRegistryProxyObjectFactory factory = new AdapterComponentDocRegistryProxyObjectFactory();
        AdapterComponentDocRegistryProxy proxy = factory.getAdapterComponentDocRegistryProxy();

        AdhocQueryRequest adhocQuery = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType target = new NhinTargetSystemType();
        target.setUrl(url);

        AdhocQueryResponse response = proxy.registryStoredQuery(adhocQuery, assertion);
        assertNotNull(response);
    }
}