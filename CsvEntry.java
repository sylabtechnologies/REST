package contactpayments;
import java.util.Iterator;
import org.apache.commons.csv.CSVRecord;

public class CsvEntry
{
    private final String[] fields;

    /*
    @param csv string
    @throws IllegalArgumentException
    @ if cant match NUM_COLUMNS or max field size exceeded
    */
    CsvEntry(CSVRecord record)
    {
        if (record.size() != ContactPayments.NUM_COLUMNS)
            throw new IllegalArgumentException("table overflow");

        fields = new String[ContactPayments.NUM_COLUMNS];
        
        int count = 0;
        for (Iterator<String> iter = record.iterator(); iter.hasNext();)
        {
            fields[count] = iter.next();
            if (fields[count].length() > ContactPayments.FIELD_MAX)
                throw new IllegalArgumentException("field overflow");

            count++;
        }
    }
    
    public String getField(int index)
    {
        if (index < 0 || index > ContactPayments.NUM_COLUMNS - 1)
            throw new IllegalArgumentException();

        return fields[index];
    }
    
}
