import java.io.*;
import java.util.ArrayList;

public class Filesystem
{
  private BlockDevice m_BlockDevice;
  private Node currentDirectory;
  private Node root;

  public Filesystem(BlockDevice p_BlockDevice)
    {
      m_BlockDevice=p_BlockDevice;
      root = new Node(null, new Entry("/", true));
      currentDirectory = root;
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
      System.out.print("Listing directories...\n");

      for(int i = 0; i< currentDirectory.getChildren().size(); i++ )
      {
        System.out.println(currentDirectory.getChildren().get(i).getData().getName() + " ");
      }

      return new String("");
    }

  /*
    [palle/xxx/ute]$ ls
    Listing directory .Path element: palle
     Path element: xxx
    Path element: ute
    curNode = this
    Entered while loop
      Child at path array pos0: palle
      Child was found.
    curNode data: palle
    Path array pos: 1
    Entered while loop
    Child at path array pos1: xxx
    Child was found.
    curNode data: xxx
    Path array pos: 2
    Entered while loop
    Child at path array pos2: ute
    Child was found.
    curNode data: ute
    Path array pos: 3
  */

  //@TODO needs a system to be decided upon how the structure for size, filename,creationdate, isdirectory, etc should be stored in memblockdevice
  public String create(String p_asPath,byte[] p_abContents)
  {
    System.out.print("Creating file ");
    System.out.print("");

    if(!currentDirectory.getData().getName().contentEquals(p_asPath))
    {
        currentDirectory.addChild(new Node(currentDirectory, new Entry(p_asPath, false)));

        System.out.println(" Written to block number: " + m_BlockDevice.getNextAvailableIndex());
        currentDirectory.getNode(p_asPath).getData().insertArrayIndex(m_BlockDevice.getNextAvailableIndex());
        String fetch = new String(p_abContents);

        String newLine = '\n' + fetch;
        byte[] toBytes = newLine.getBytes();

        byte[] to512bytes = new byte[512];
        for (int i = 0; i < to512bytes.length; i++) {
          to512bytes[i] = toBytes[i];
        }
        int check = m_BlockDevice.writeBlock(m_BlockDevice.getNextAvailableIndex(), to512bytes);

        if(check == 1)
        {

        }
        else
        {
          System.out.println("Creation of file failed!");
        }

    }

    return new String("");
  }

  public String cat(String p_asPath)
    {
      System.out.print("Dumping contents of file ");
      System.out.print("");
      if(!currentDirectory.getNode(p_asPath).getData().isDirectory())
      {
          ArrayList<Integer> fetch = currentDirectory.getNode(p_asPath).getData().getArrayIndexes();
          for(int i=0; i<fetch.size(); i++)
          {
            byte[] fetchByteArray = m_BlockDevice.readBlock(fetch.get(i));
            String makeString = new String(fetchByteArray);
            System.out.println(makeString);
          }
      }
      else
      {
        return "This is not a file!";
      }

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

  // Check this out am not sure if this work.
  public String rename(String p_asSource,String p_asDestination)
  {
    System.out.print("Renaming file ");
    System.out.println(p_asSource);
    System.out.print(" to ");
    System.out.print(p_asDestination);
    Node rename = root.getNode(currentDirectory.getPath());
    if(rename.getData().isDirectory() == false)
    {
      rename.getData().setName(p_asDestination);
      System.out.println("filename is now changed");
    }

    return new String("");
  }


  public String mkdir(String p_asPath)
    {
      System.out.print("Creating directory ");
      System.out.println(p_asPath);

      if(currentDirectory.getData().getName() == "/")
      {
        Node newNode = new Node(root, new Entry(p_asPath, true));
        root.addChild(newNode);
        return newNode.data.getName();
      }

      Node newNode = new Node(currentDirectory, new Entry(p_asPath, true));
      currentDirectory.addChild(newNode);

      return newNode.data.getName();
    }

  public String cd(String p_asPath)
    {
      System.out.print("Changing directory to ");
      System.out.print("");
      System.out.println(p_asPath);

      currentDirectory = currentDirectory.getNode(p_asPath);

      return new String("");
    }

  public String pwd()
    {
      return currentDirectory.getPath();
    }

//  private void dumpArray(String p_asArray)
//    {
//      for(int nIndex=0;nIndex<p_asArray.length;nIndex++)
//        {
//          System.out.print(p_asArray[nIndex]+"=>");
//        }
//    }

}
