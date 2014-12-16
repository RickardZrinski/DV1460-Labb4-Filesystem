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

    public boolean checkDirExists(String path)
    {
        // Always return that root exists
        /*if(path.charAt(0) == '/')
        {
            return true;
        }*/

        boolean exists = false;
        String dirPath;

        int index = path.lastIndexOf("/");

        dirPath = path.substring(0, index);

        if(k.containsKey(dirPath))
        {
            exists = true;
        }

        return exists;
    }

    //@TODO must find a way to print all children for a given parent
    public void printChildren(String currentDirectory)
    {
        Set<String> keys = k.keySet();

        int origAmount = calcNrOfSlashes(currentDirectory);

        for(String theKey : keys)
        {
            if (calcNrOfSlashes(theKey) == origAmount)
            {
                System.out.println("folders and files within this directory is: " + theKey);
            }

        }
    }

    private int calcNrOfSlashes(String theKey)
    {
        int counter = 0;
        for(int i=0; i< theKey.length(); i++)
        {
            if(theKey.contentEquals("/"))
            {
                counter++;
            }
        }
        return counter;
    }

    private TreeMap k;
}
