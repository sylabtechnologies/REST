package contactpayments;

public class CsvEntry
{
    private final String[] fields;

    public CsvEntry(String entry )
    {
        fields = entry.split(",");
        
        if (fields.length != ContactPayments.NUM_COLUMNS)
            throw new IllegalArgumentException();
    }
    
    public String getField(int index)
    {
        if (index < 0 || index > ContactPayments.NUM_COLUMNS - 1)
            throw new IllegalArgumentException();

        return fields[index];
    }
    
}
