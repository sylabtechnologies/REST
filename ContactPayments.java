/**
 * @author D.S.
 * @version 1.0
 * @param the name of the csv file (the file records will be validated to have NUM_COLUMNS columns)
 * @see more at ContactPayments.java :
 * 
 * run as : ContactPayments CsvFileName (w/o extension)
 * set up the database in ContactPayments-pros properties file
 * a table w/ CsvFileName will be created
 * w/ a rowId key plus NUM_COLUMNS columns
 * 
 */

package contactpayments;
import java.util.Properties;
import java.io.*;
import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSetMetaData;
import javax.sql.rowset.CachedRowSet;

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
    
    private final static String DATABASE = props.getProperty("DATABASE");
    private final static String USER     = props.getProperty("USER"); 
    private final static String PASSWORD = props.getProperty("PASSWORD");
    private final static String DATABASE_URL = props.getProperty("DATABASE_URL");
    private final static String CONNECT_TO = DATABASE_URL + DATABASE;
    //    + "?autoReconnect=true&useSSL=false";
    private static String TABLE_NAME;
    
    public  final static int NUM_COLUMNS = 11;
    private static final String MY_PRIMARY = "ENTRYID";
    private static final int ROWSET_MAX = 500;

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
                    System.out.println("delete from " + TABLE_NAME);
                    break;
                }
            }

            if (found)
                crs.setCommand("DELETE FROM " + TABLE_NAME);
            else
                crs.setCommand(createTable(TABLE_NAME));
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

    // create table with A-Z columns and entryId key
    private static String createTable(final String table)
    {
        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(table);
        query.append(" (\n");
        query.append(MY_PRIMARY +  " int NOT NULL,\n");

        for (int i = 0; i < NUM_COLUMNS; i++)
        {
            query.append( getColumnName(i) );
            query.append(" varchar(400),\n");
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(table + "-bad.csv"));
        )
        {
            receiveCount = 0;
            successCount = 0;
            faultCount   = 0;
            
            // ResultSetMetaData metaData = jrs.getMetaData();
            // int numberOfColumns = metaData.getColumnCount();

            jrs.setCommand("SELECT * FROM " + table);
            jrs.execute();
            
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                receiveCount++;

                jrs.moveToInsertRow();
                
                CsvEntry entry = null;
                try
                {
                    entry = new CsvEntry(line);
                    successCount++;
                }
                catch (IllegalArgumentException ex)
                {
                    faultCount++;
                    writer.write(line + "\n");
                }
                
                if (entry != null)
                {
                    jrs.updateInt(MY_PRIMARY, successCount);

                    for (int i = 0; i < NUM_COLUMNS; i++)
                    {
                        String col = getColumnName(i);
                        String val = entry.getField(i);
                        // force truncation
                        if (val.length() > 400)
                            val = val.substring(0, 400);
                        
                        jrs.updateString(col, val);
                    }

                    jrs.insertRow();
                    jrs.moveToCurrentRow();
                    
                    if (successCount % ROWSET_MAX == 0)
                    {
                        System.out.println("Rows commited: " + successCount);
                        jrs.acceptChanges();
                    }
                }
            }

            jrs.moveToCurrentRow();
            if (successCount % ROWSET_MAX != 0)
                jrs.acceptChanges();
            
            writer.flush();
            System.out.println("Total rows received: " + receiveCount);
            System.out.println("Total rows processed: " + successCount);
            System.out.println("Total rows failed: " + faultCount);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

