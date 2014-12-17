import java.io.*;
import java.util.ArrayList;

public class Filesystem
{
  private BlockDevice m_BlockDevice;
  private String currentDirectory;
  private Node root;

  public Filesystem(BlockDevice p_BlockDevice)
    {
      m_BlockDevice=p_BlockDevice;
      currentDirectory = "/";
      root = new Node(null, new Entry("/", true));
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
      System.out.print(p_asPath);

      Node foundNode = root.getNode(p_asPath);

      for(int i = 0; i< foundNode.getChildren().size(); i++ )
      {
        System.out.println(foundNode.getChildren().get(i).getData().getName());
      }


      return new String("");
    }

  /*
    // Verkar funka !
      [pol]$ ls pol
      Listing directory polPath element: pol
      curNode = this
      Entered while loop
      Child at path array pos0: pol
      Child was found.
      curNode data: pol
      Path array pos: 1
      fan --> undermapp till pol
      kuk --> undermapp till pol

  */

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

    byte[][] savedDisk =new byte[250][512];

    for(int i = 0; i<250; i++)
    {
      savedDisk[i] = m_BlockDevice.readBlock(i);
    }

    try
    {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(p_sPath));
      out.writeObject(savedDisk);
      out.flush();
      out.close();
    }
    catch(IOException e)
    {
      e.getStackTrace();
    }

    return new String("");
  }

  public String read(String p_sPath)
  {
    System.out.print("Reading file "+p_sPath+" to blockdevice");
    byte[][] disk = null;
    ObjectInputStream in = null;
    try
    {
      in = new ObjectInputStream(new FileInputStream(p_sPath));
      disk = (byte[][]) in.readObject();
      in.close();
    }

    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }

    for(int i = 0; i<250; i++)
    {
      m_BlockDevice.writeBlock(i,disk[i]);
    }

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

      if(currentDirectory == "/")
      {
        Node newNode = new Node(root, new Entry(p_asPath, true));
        root.addChild(newNode);
        return newNode.data.getName();
      }

      Node currentDir = root.getNode(currentDirectory);
      Node newNode = new Node(currentDir, new Entry(p_asPath, true));
      currentDir.addChild(newNode);

      return newNode.data.getName();
    }

  public String cd(String p_asPath)
    {
      System.out.print("Changing directory to ");
      System.out.print("");
      System.out.println(p_asPath);
                                           // ----> check if path exists must be checked......
      currentDirectory = p_asPath;

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
