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

package org.alembic.aurion.template;

import org.alembic.aurion.template.model.CdaTemplate;
import java.util.List;

/**
 *
 * @author kim
 */
public interface TemplateManager {

   public List<CdaTemplate> getSectionTemplatesForDocument(String loincCode, boolean active);

   public List<CdaTemplate> getModuleTemplatesForSection(int id, boolean active);

   public CdaTemplate getTemplateForDocument(String loincCode);

   public List<CdaTemplate> getTemplates();
}

