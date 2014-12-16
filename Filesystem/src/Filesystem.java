import java.util.TreeMap;

public class Filesystem
{
  private BlockDevice m_BlockDevice;
  private EntryTree fileSystemStructure;
  private String currentDirectory;
  public Filesystem(BlockDevice p_BlockDevice)
    {
      m_BlockDevice=p_BlockDevice;
      currentDirectory = "";
      fileSystemStructure = new EntryTree();

      boolean directory = true;
      Entry test = new Entry("/",true);

      boolean check =  fileSystemStructure.addEntry("/",test);
      if (check)
      {
        fileSystemStructure.addEntry("/", new Entry("/", directory));
        currentDirectory = "/";
      }

    }

  public String format()
  {

      byte[] format =new byte[512];
      for (int f = 0; f<512; f++)
      {
        format[f] = -1;
      }

      for(int i = 0; i<250; i++)
      {
         int formatResponse = m_BlockDevice.writeBlock(i,format);
         if(formatResponse == -1)
         {
           return new String("Diskformat failed");
         }

      }
      return new String("Diskformat sucessfull");
  }

  public String ls(String p_asPath)
    {
      System.out.print("Listing directory ");
      System.out.print("");

      if(p_asPath.contentEquals(currentDirectory) | p_asPath.contentEquals("."))
      {
        fileSystemStructure.printChildren(currentDirectory);
      }
      else
      {
//        String[] splitUp = p_asPath.split("");
//        for(int i=0; i< splitUp.length; i++)
//        {
//          if(!splitUp[i].contentEquals(""))
//          {
//            fileSystemStructure.printChildren(splitUp[i]);
//          }
//
//        }

      }

      return new String("");
    }


  public String create(String p_asPath,byte[] p_abContents)
    {
      System.out.print("Creating file ");
  //    dumpArray(p_asPath);
      System.out.print("");
      return new String("");
    }

  public String cat(String p_asPath)
    {
      System.out.print("Dumping contents of file ");
   //   dumpArray(p_asPath);
      System.out.print("");
      return new String("");
    }

  public String save(String p_sPath)
    {
      System.out.print("Saving blockdevice to file "+p_sPath);

      return new String("");
    }

  public String read(String p_sPath)
    {
      System.out.print("Reading file "+p_sPath+" to blockdevice");
      return new String("");
    }

  public String rm(String p_asPath)
    {
      System.out.print("Removing file ");
    //  dumpArray(p_asPath);
      System.out.print("");
      return new String("");
    }

  public String copy(String p_asSource,String p_asDestination)
    {
      System.out.print("Copying file from ");
    //  dumpArray(p_asSource);
      System.out.print(" to ");
    //  dumpArray(p_asDestination);
      System.out.print("");
      return new String("");
    }

  public String append(String p_asSource,String p_asDestination)
    {
      System.out.print("Appending file ");
  //    dumpArray(p_asSource);
      System.out.print(" to ");
    //  dumpArray(p_asDestination);
      System.out.print("");
      return new String("");
    }

  public String rename(String p_asSource,String p_asDestination)
    {
      System.out.print("Renaming file ");
    //  dumpArray(p_asSource);
      System.out.print(" to ");
   //   dumpArray(p_asDestination);
      System.out.print("");
      return new String("");
    }

  public String mkdir(String p_asPath)
    {
      System.out.print("Creating directory ");
      System.out.println(p_asPath);

      p_asPath = currentDirectory+p_asPath+"/";
      System.out.println(p_asPath);
      Entry newEntry = new Entry(p_asPath,true);
      boolean check = fileSystemStructure.addEntry(p_asPath, newEntry);
      if(check)
      {
        return "";
      }
      else
      {
        return "error! directory already exists!";
      }
    }

  public String cd(String p_asPath)
    {
      System.out.print("Changing directory to ");
      System.out.print("");

      p_asPath += "/";
      boolean check = fileSystemStructure.checkDirExists(p_asPath);
      if(check)
      {
        currentDirectory = p_asPath+"/";
      }

      return new String("");
    }

  public String pwd()
    {
      return currentDirectory;
    }

//  private void dumpArray(String p_asArray)
//    {
//      for(int nIndex=0;nIndex<p_asArray.length;nIndex++)
//        {
//          System.out.print(p_asArray[nIndex]+"=>");
//        }
//    }

}
