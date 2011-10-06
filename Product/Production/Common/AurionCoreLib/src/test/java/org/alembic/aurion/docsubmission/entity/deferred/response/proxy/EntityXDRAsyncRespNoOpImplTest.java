/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alembic.aurion.docsubmission.entity.deferred.response.proxy;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class EntityXDRAsyncRespNoOpImplTest {

    public EntityXDRAsyncRespNoOpImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of provideAndRegisterDocumentSetBAsyncResponse method, of class EntityXDRAsyncRespNoOpImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBAsyncResponse() {
        System.out.println("testProvideAndRegisterDocumentSetBAsyncResponse");
        EntityDocSubmissionDeferredResponseProxyNoOpImpl instance = new EntityDocSubmissionDeferredResponseProxyNoOpImpl();
        
        XDRAcknowledgementType result = instance.provideAndRegisterDocumentSetBAsyncResponse(new RegistryResponseType(), new AssertionType(), new NhinTargetCommunitiesType());
        assertNotNull(result);
    }

}