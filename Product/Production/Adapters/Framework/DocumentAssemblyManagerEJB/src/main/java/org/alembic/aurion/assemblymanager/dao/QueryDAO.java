/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alembic.aurion.assemblymanager.dao;

import org.alembic.aurion.assemblymanager.AssemblyConstants;
import org.alembic.aurion.assemblymanager.dao.persistence.PersistentServiceFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kim
 */
public class QueryDAO {

   protected static Log log = LogFactory.getLog(QueryDAO.class);

   //@PersistenceContext(unitName="docassemblyPU")
   //protected EntityManagerFactory emf;
   private static PersistentServiceFactory factory = null;

   public QueryDAO() {
      factory = PersistentServiceFactory.getInstance(AssemblyConstants.DAS_PU_VALUE);
   }

   public PersistentServiceFactory getFactory() {
      return factory;
   }
}
