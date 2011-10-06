package org.alembic.aurion.docquery.entity.deferred.request;

import org.alembic.aurion.common.nhinccommon.AssertionType;
import org.alembic.aurion.common.nhinccommon.NhinTargetCommunitiesType;
import org.alembic.aurion.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author goldmanm
 */
public class EntityDocQueryDeferredReqTest {

  Mockery mockery;

  @Before
  public void setup() {
    mockery = new Mockery() {

      {
        setImposteriser(ClassImposteriser.INSTANCE);
      }
    };
  }

  /**
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReq.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery() {
    final DocQueryAcknowledgementType expected = new DocQueryAcknowledgementType();

    EntityDocQueryDeferredReq testSubject = new EntityDocQueryDeferredReq() {

      @Override
      protected DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(
          final RespondingGatewayCrossGatewayQueryRequestType body, final WebServiceContext context) {
        return expected;
      }
    };

    DocQueryAcknowledgementType result = testSubject.respondingGatewayCrossGatewayQuery(new RespondingGatewayCrossGatewayQueryRequestType());
    assertSame(result, expected);
  }
}
