package org.alembic.aurion.lift;

import org.alembic.aurion.lift.proxy.properties.interfaces.ProducerProxyPropertiesFacade;
import org.alembic.aurion.lift.proxy.server.SSLServer;
import org.alembic.aurion.nhinclib.NhincConstants;
import org.alembic.aurion.properties.PropertyAccessor;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;

/**
 *
 * @author webbn
 */
@RunWith(JMock.class)
public class ServerAppTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    public ServerAppTest() {
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

	
    @Test
    public void testCreateServer() {
        System.out.println("Need to implement tests");
    }
}
