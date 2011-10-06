package org.alembic.aurion.mpilib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that reads in the contents of the file and creates the entries
 * in the MPI and then creates the MPI file.  The input file needs to be
 * a text file with ";" separating each field.  It can have comment lines that
 * start with a "#" and it can have empty lines that will be ignored...  Even
 * if there are empty fields all ";" must be present...
 *
 * The columns are as follows:
 *
 * Last_Name;First_Name;DOB;Gender;Authority_OID;EMPI_NUM;Address1;Address2;City;State;ZIP;Country;Phone_1;Phone_2
 *
 * @author Westberg
 */
public class CreateMpiFromTextFile
{
    private static final String INPUT_FILE = "inputMpiRecs.txt";
    private static final String OUTPUT_FILE = "outputMpiRecs.xml";

    private static final int LAST_NAME_COL = 0;
    private static final int FIRST_NAME_COL = 1;
    private static final int DOB_COL = 2;
    private static final int GENDER_COL = 3;
    private static final int ASSIGNING_AUTHORITY_OID = 4;
    private static final int EMPI_COL = 5;
    private static final int ADDRESS1_COL = 6;
    private static final int ADDRESS2_COL = 7;
    private static final int CITY_COL = 8;
    private static final int STATE_COL = 9;
    private static final int ZIP_COL = 10;
    private static final int COUNTRY_COL = 11;
    private static final int PHONE1_COL = 12;
    private static final int PHONE2_COL = 13;
    private static final int NUM_COLS = 14;


    /**
     * Main method that reads in the contents of the file and creates the entries
     * in the MPI and then creates the MPI file.  The input file needs to be
     * a text file with ";" separating each field.  It can have comment lines that
     * start with a "#" and it can have empty lines that will be ignored...  Even
     * if there are empty fields all ";" must be present...
     *
     * The columns are as follows:
     *
     * Last_Name;First_Name;DOB;Gender;Authority_OID;EMPI_NUM;Address1;Address2;City;State;ZIP;Country;Phone_1;Phone_2
     *
     */
    @Ignore
    @Test
    public void createMpiFile()
    {

        MiniMpi oMpi = MiniMpi.GetMpiInstance(OUTPUT_FILE);

        try
        {
            String saLine[] = readTextFile(INPUT_FILE);

            for (String sLine: saLine)
            {
                Patient oPatient = createOnePatient(sLine);
                oMpi.AddUpdate(oPatient);
            }


            MpiDataSaver.SaveMpi(oMpi.getPatients(), OUTPUT_FILE);
        }
        catch (Exception e)
        {
            fail("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * This method creates a patient object from the fields in the delimited
     * text line. The fields are delimited by ';'.
     *
     * @param sLine  The delimited text line of fields.
     * @return The instance of a Patient object that was created from the parsing
     *         the text line.
     */
    private Patient createOnePatient(String sLine)
    {
        Patient oPatient = new Patient();

        String saField[] = sLine.split(";");
        assertEquals("Line formatted incorrectly - had incorrect number of fields: '" + sLine + "'.", NUM_COLS, saField.length);

        oPatient.setLastName(saField[LAST_NAME_COL]);
        oPatient.setFirstName(saField[FIRST_NAME_COL]);
        oPatient.setGender(saField[GENDER_COL]);
        oPatient.setDateOfBirth(saField[DOB_COL]);
        oPatient.setOptedIn(true);

        PersonName oPersonName = new PersonName();
        oPersonName.setFirstName(saField[FIRST_NAME_COL]);
        oPersonName.setLastName(saField[LAST_NAME_COL]);
        PersonNames oPersonNames = new PersonNames();
        oPersonNames.add(oPersonName);
        oPatient.setNames(oPersonNames);


        Address oAddress = new Address();
        oAddress.setStreet1(saField[ADDRESS1_COL]);
        oAddress.setStreet2(saField[ADDRESS2_COL]);
        oAddress.setCity(saField[CITY_COL]);
        oAddress.setState(saField[STATE_COL]);
        oAddress.setZip(saField[ZIP_COL]);
        Addresses oAddresses = new Addresses();
        oAddresses.add(oAddress);
        oPatient.setAddresses(oAddresses);

        Identifier oIdentifier = new Identifier();
        oIdentifier.setOrganizationId(saField[ASSIGNING_AUTHORITY_OID]);
        oIdentifier.setId(saField[EMPI_COL]);
        Identifiers oIdentifiers = new Identifiers();
        oIdentifiers.add(oIdentifier);
        oPatient.setIdentifiers(oIdentifiers);

        PhoneNumbers oPhoneNumbers = new PhoneNumbers();
        PhoneNumber oPhoneNumber = new PhoneNumber();
        oPhoneNumber.setPhoneNumber(saField[PHONE1_COL]);
        oPhoneNumbers.add(oPhoneNumber);
        oPhoneNumber = new PhoneNumber();
        oPhoneNumber.setPhoneNumber(saField[PHONE2_COL]);
        oPhoneNumbers.add(oPhoneNumber);
        oPatient.setPhoneNumbers(oPhoneNumbers);

        return oPatient;

    }

    /**
     * This method reads the contents of the given text file.  It places
     * each row of the text file in a separate line.  It skips any rows that
     * start with # an any rows containing only white space.
     *
     * @param sFileName  The name of the file that contains the MPI information.
     * @return The array of lines containing patient information from the file.
     */
    private String[] readTextFile(String sFileName)
        throws Exception
    {
        ArrayList<String> saLine = new ArrayList<String>();
        FileReader frInputFile = null;

        try
        {
            File fInputFile = new File(sFileName);
            assertTrue("File: " + sFileName + " does not exist.", fInputFile.exists());

            frInputFile = new FileReader(fInputFile);
            assertNotNull("Failed to create file reader.", frInputFile);

            BufferedReader brInputFile = new BufferedReader(frInputFile);
            assertNotNull("Failed to create buffered file reader.", brInputFile);

            String sLine = null;
            do
            {
                sLine = brInputFile.readLine();
                if (sLine != null)
                {
                    sLine = sLine.trim();
                    if ((sLine.length() > 0) && (! sLine.startsWith("#")))
                    {
                        saLine.add(sLine);
                    }
                }
            }
            while (sLine != null);
        }
        finally
        {
            if (frInputFile != null)
            {
                frInputFile.close();
                frInputFile = null;
            }
        }

        if (saLine.size() > 0)
        {
            return saLine.toArray(new String[0]);
        }
        else
        {
            return new String[0];
        }

    }

}
