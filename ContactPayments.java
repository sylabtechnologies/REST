/**
@author D
@version 1.0
@param CsvFileName
@ set up the db connector and table params in ContactPayments-pros.xml
@ clear CsvFileName table
@ assume first csv record are column headers
@ write all records into CsvFileName table
@ as rowId + NUM_COLUMNS columns
@ write all bad records into CsvFileName-bad.csv
*/

package contactpayments;
import java.util.Properties;
import java.io.*;
import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSetMetaData;
import javax.sql.rowset.CachedRowSet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ContactPayments
{
    private final static Properties props;
    static
    {
        props = new Properties();
        try {
            props.loadFromXML(new FileInputStream("ContactPayments-props.xml"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // @param db config
    private final static String DATABASE = props.getProperty("DATABASE");
    private final static String USER     = props.getProperty("USER"); 
    private final static String PASSWORD = props.getProperty("PASSWORD");
    private final static String DATABASE_URL = props.getProperty("DATABASE_URL");
    private final static String CONNECT_TO = DATABASE_URL + DATABASE;
    //    + "?autoReconnect=true&useSSL=false";

    // @param number of columns
    public final static int NUM_COLUMNS
        = Integer.parseInt(props.getProperty("COLUMNNUMBER"));
    // @param max field
    public final static int FIELD_MAX
        = Integer.parseInt(props.getProperty("MAXFIELD"));
    
    private static String TABLE_NAME;
    private static final String MY_PRIMARY = "ENTRYID";

    public final static int CACHE_MAX = 1000;
    private static int receiveCount = 0;
    private static int successCount = 0;
    private static int faultCount = 0;
    
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Usage: contactpayments tablename");
            System.exit(1);
        }

        TABLE_NAME = args[0].toUpperCase();
        
        try
        (
            CachedRowSet crs = new CachedRowSetImpl();
        )
        {
            crs.setUrl(CONNECT_TO);            
            crs.setUsername(USER);
            crs.setPassword(PASSWORD);
            
            crs.setCommand("SELECT TABLENAME FROM sys.systables WHERE TABLETYPE = 'T'");
            crs.execute();
            boolean found = false;
            while (crs.next())
            {
                String name = crs.getString(1);
                if (name.equals(TABLE_NAME))
                {
                    found = true;
                    System.out.println("Delete from " + TABLE_NAME + " table");
                    break;
                }
            }

            System.out.print("Create " + TABLE_NAME + " table with");
            System.out.println( (NUM_COLUMNS + 1) + " columns, cut off at "
                    + FIELD_MAX + " characters");
            
            if (found)
                crs.setCommand("DELETE FROM " + TABLE_NAME);
            else
                crs.setCommand(createTable(TABLE_NAME));
            crs.execute();
            
            crs.setCommand("SELECT * FROM " + TABLE_NAME);
            crs.execute();
            processInput(crs, TABLE_NAME);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public static String getColumnName(int index)
    {
        if (index < 0 || index > ContactPayments.NUM_COLUMNS - 1)
            throw new IllegalArgumentException();
        
        return Character.toString((char) (index + 65));
    }

    // create primary key and A-Z columns
    private static String createTable(final String table)
    {
        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(table);
        query.append(" (\n");
        query.append(MY_PRIMARY +  " int NOT NULL,\n");

        for (int i = 0; i < NUM_COLUMNS; i++)
        {
            query.append( getColumnName(i) );
            query.append(" varchar(" + FIELD_MAX + "),\n");
        }

        query.append("PRIMARY KEY ( " + MY_PRIMARY + " ))\n");
        return query.toString();
    }
    
    
    // read file and insert rows
    private static void processInput(CachedRowSet jrs, final String table)
    throws SQLException
    {
        try
        (
            BufferedReader reader = new BufferedReader(new FileReader(table + ".csv"));
            CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180);
            BufferedWriter writer = new BufferedWriter(new FileWriter(table + "-bad.csv"));
            BufferedWriter logger = new BufferedWriter(new FileWriter(table + ".log"));
        )
        {
            receiveCount = 0;
            successCount = 0;
            faultCount   = 0;
            
            // ResultSetMetaData metaData = jrs.getMetaData();
            // int numberOfColumns = metaData.getColumnCount();
            // certUtil -hashfile C:\Users\Dennis\Downloads\commons-csv-1.7-bin.zip SHA512
            
            for (CSVRecord record : parser)
            {
                receiveCount++;
                if (receiveCount == 1)
                {
                    System.out.println("Skip " + record);
                    continue;
                }

                jrs.moveToInsertRow();

                CsvEntry entry = null;
                try
                {
                    entry = new CsvEntry(record);
                    successCount++;
                }
                catch (IllegalArgumentException ex)
                {
                    faultCount++;
                    writer.write(record.toString() + "\n");
                }
                
                if (entry != null)
                {
                    jrs.updateInt(MY_PRIMARY, successCount);

                    for (int i = 0; i < NUM_COLUMNS; i++)
                    {
                        String col = getColumnName(i);
                        String val = entry.getField(i);
                        jrs.updateString(col, val);
                    }

                    jrs.insertRow();
                    jrs.moveToCurrentRow();

                    if (successCount % CACHE_MAX == 0)
                    {
                        System.out.println("Rows commited: " + successCount);
                        jrs.acceptChanges();
                    }
                }
            }

            jrs.moveToCurrentRow();
            if (successCount % CACHE_MAX != 0)
            {
                System.out.println("Rows commited: " + successCount);
                jrs.acceptChanges();
            }
            
            writer.flush();
            
            logger.write("Total rows received: " + (receiveCount - 1) + "\n");
            logger.write("Total rows processed: " + successCount + "\n");
            logger.write("Total rows failed: " + faultCount + "\n");
            logger.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

}

