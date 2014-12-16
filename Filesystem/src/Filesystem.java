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
      if (!check)
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
   //   dumpArray(p_asPath);
      System.out.print("");
      String fetch = "";
//      for(int i=0; i< p_asPath.length; i++)
//      {
//        fetch += p_asPath[i];
//      }
      Entry test = new Entry(p_asPath+"/",true);
   //   System.out.println(fileSystemStructure.g(test).getName());
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


      Entry newEntry = new Entry(p_asPath,true);
      p_asPath = currentDirectory+newEntry.getName()+"/";
      boolean check = fileSystemStructure.addEntry(p_asPath, newEntry);
      if(!check)
      {
        return "";
      }
      else
      {
        return "error! directory already exists!";
      }
      //return new String("");
    }

  public String cd(String p_asPath)
    {
      System.out.print("Changing directory to ");
     // dumpArray(p_asPath);
      System.out.print("");

      String fetch = "";
//      for(int i=0; i< p_asPath.length; i++)
//      {
//        fetch += p_asPath[i];
//      }
     // @TODO must make checkDirExists() public so this method can work properly
      //boolean check = fileSystemStructure.checkDirExists(p_asPath);


//      if(check)
//      {
//        currentDirectory = currentDirectory+p_asPath;
//        return "successful!";
//      }
//
//      else
//      {
//        return "failed";
//      }

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
