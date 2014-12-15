public class Entry
{
    boolean isDirectory;
    String name;
    int size;
    String creationDate;
    byte[] data;

    Entry(String name, boolean isDirectory)
    {
        this.name = name;
        this.isDirectory = isDirectory;
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

    public String getCreationDate()
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

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }
}
