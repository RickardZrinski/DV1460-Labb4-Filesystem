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
        String dirname = "pol/rickard";
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

    public boolean checkDirExists(String dir)
    {
        boolean exists = false;
        String [] check = dir.split("/");
        if(check.length > 1)
        {
            System.out.println(check[0]);
            if(k.containsKey(check[check.length-2]))
            {
                exists = true;
            }
        }

        return exists;
    }

    private TreeMap k;
}
