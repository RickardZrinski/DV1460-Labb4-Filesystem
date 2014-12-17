import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Entry
{
    private boolean isDirectory;
    private String name;
    private int size;
    private Date creationDate;
    private byte[] data;
    private ArrayList<Integer> memBlockIndex;

    Entry(String name, boolean isDirectory)
    {
        if(isDirectory() == false)
        {
            memBlockIndex = new ArrayList<Integer>();
        }
        this.name = name;
        this.isDirectory = isDirectory;
        Calendar cal = Calendar.getInstance();
        creationDate = cal.getTime();
    }

    public void insertArrayIndex(int insert)
    {
        memBlockIndex.add(insert);
    }

    public boolean isDirectory()
    {
        return isDirectory;
    }

    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return size;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setDirectory(boolean isDirectory)
    {
        this.isDirectory = isDirectory;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }
}
