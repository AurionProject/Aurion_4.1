/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package org.alembic.aurion.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Les Westberg
 */
public class CMInternalConnInfoServices
{
    private List<CMInternalConnInfoService> serviceList = new ArrayList<CMInternalConnInfoService>();

    /**
     * Default constructor.
     */
    public CMInternalConnInfoServices()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        serviceList = new ArrayList<CMInternalConnInfoService>();;
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnInfoServices oCompare)
    {
        if (oCompare.serviceList.size() != this.serviceList.size())
        {
            return false;
        }
        
        int iCnt = this.serviceList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.serviceList.get(i).equals(oCompare.serviceList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Return the list of services associated with this home community.
     * 
     * @return The list of serviecs associated with this home community.
     */
    public List<CMInternalConnInfoService> getService()
    {
        return serviceList;
    }

}