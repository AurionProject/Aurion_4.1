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
package org.alembic.aurion.assemblymanager.utils;

import org.alembic.aurion.util.hash.SHA1HashCode;

/**
 *
 * @author kim
 */
public class HashCodeUtil {

   /**
    * Calculate the hash code.
    * @param rawData
    * @return
    */
   public static String calculateHashCode(byte[] rawData) {
      String sHash = "";
      if (rawData != null) {
         try {
            sHash = SHA1HashCode.calculateSHA1(new String(rawData));
         } catch (Throwable t) {
            System.err.println("Failed to create SHA-1 Hash code.  Error: " + t.getMessage() +
                    "Data Text: " + new String(rawData));
         }
      } else {
         System.out.println("No SHA-1 Hash Code created because document was null.");
      }

      return sHash;
   }
}
