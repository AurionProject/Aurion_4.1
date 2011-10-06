/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.lift.clientManager.client;

import org.alembic.aurion.lift.clientController.SocketClientManagerController;
import org.alembic.aurion.lift.common.util.LiftMessage;
import java.io.IOException;
import java.net.URI;

public interface ClientManager {
	public URI startClient(LiftMessage message, SocketClientManagerController controller) throws IOException;
}
