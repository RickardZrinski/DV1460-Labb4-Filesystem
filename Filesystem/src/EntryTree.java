import java.util.AbstractMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by POL-PC on 2014-12-15.
 */
public class EntryTree
{
    EntryTree()
    {
        k = new TreeMap<String,Entry>();
    }

    public boolean addEntry(String path,Entry entry)
    {
        boolean added;

        if(!checkDirExists(path))
        {
            k.put(path,entry);
            added = true;
        } else
        {
            added = false;
        }

        return added;
    }

    private boolean checkDirExists(String path)
    {
        boolean exists = false;

        String dirPath;
        String entryName;

        int index = path.lastIndexOf("/");

        dirPath = path.substring(0, index);
        entryName = path.substring(index+1, path.length());

        if(k.containsKey(dirPath))
        {
            exists = true;
        }

        System.out.println("dirPath: " + dirPath);
        System.out.println("entryName: " + entryName);

        return exists;
    }

    public void printChildren(String currentDirectory)
    {
        System.out.println(k.size());
        Set<String> keys = k.keySet();
        for(String key : keys)
        {
            System.out.println("values of"+key+" is: "+k.get(key));
        }
    }

    private TreeMap k;
}
