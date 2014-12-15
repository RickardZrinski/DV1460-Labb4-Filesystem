import java.util.AbstractMap;
import java.util.TreeMap;

/**
 * Created by POL-PC on 2014-12-15.
 */
public class Filemap
{
    Filemap()
    {
         k = new TreeMap<String,Entry>();
    }

    void addEntrys()
    {
        Entry k1 = new Entry("pol_dir",true);
        Entry k2 = new Entry("Rickard_dir",true);
        Entry k3 = new Entry("martin_dir",true);

        k.put("pol",k1);
        k.put("pol/rickard",k2);
        k.put("pol/rickard/martin",k3);

    }

    void printall()
    {
        String dirname = "pol/rickard/martin";
       if(checkDirExists(dirname) == true)
       {
           System.out.println("Den finns");
       }
       else
       {
           System.out.println("finns ej");
       }
        //Entry l =(Entry) k.get("pol/rickard/martin");
        //System.out.println(l.getName());

    }

    public boolean checkDirExists(String path)
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

    private TreeMap k;
}
