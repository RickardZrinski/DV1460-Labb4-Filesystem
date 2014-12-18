import java.io.*;
import java.lang.reflect.Array;
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
    root = new Node(null, new Entry("/", true));
    currentDirectory = root;

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
    return new String("Diskformat successful");
  }

  public String ls(String p_asPath)
    {
      Node node = currentDirectory.getNode(p_asPath);

      if(node != null)
      {
        System.out.print("Listing directories...\n");

        ArrayList<Node> children = node.getChildren();

        for(Node child : children)
        {
          System.out.print(child.getData().getName() + " ");
        }
      } else
      {
        System.out.println("Invalid path.");
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

        System.out.println(" Writes to block number: " + m_BlockDevice.getNextAvailableIndex());
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

  //@TODO save also needs to save the nodes for directories/files
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

  //@TODO read also needs to load the file for nodes that represents directories/files
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

    // Get node to remove
    Node node = this.currentDirectory.getNode(p_asPath);

    // We need to delete the data in memory before removing the node
    ArrayList<Integer> memIndexes = node.getData().getArrayIndexes();
    for(Integer index: memIndexes)
    {
      this.m_BlockDevice.freeMemBlock(index);
    }

    // Remove the node
    this.currentDirectory.remove(p_asPath);

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
    System.out.print(p_asSource);
    System.out.print(" to ");
    System.out.print(p_asDestination);


    if(currentDirectory.getNode(p_asSource) == null)
    {
      return new String("\nsource path does not exist!");
    }

    if(currentDirectory.getNode(p_asDestination) == null)
    {
      return new String("\nDestination path does not exist!");
    }

    if(currentDirectory.getNode(p_asSource).getData().isDirectory())
    {
      return new String("\nSource path is a directory !");
    }

    if(currentDirectory.getNode(p_asDestination).getData().isDirectory())
    {
      return new String("\nDestionation path is a directory !");
    }

    ArrayList<Integer> sourceBlockIndex = currentDirectory.getNode(p_asSource).getData().getArrayIndexes();
    ArrayList<Integer> destinationBlockIndex = currentDirectory.getNode(p_asSource).getData().getArrayIndexes();

    String sourceString = "";
    for(int i=0; i<sourceBlockIndex.size(); i++)
    {
      byte[] fetchByteArray = m_BlockDevice.readBlock(sourceBlockIndex.get(i));
      sourceString = new String(fetchByteArray);
    }

    String destinationString = "";
    for(int i=0; i<destinationBlockIndex.size(); i++)
    {
      byte[] fetchByteArray = m_BlockDevice.readBlock(destinationBlockIndex.get(i));
      destinationString = new String(fetchByteArray);
    }

    // remove from simulated disk and from the saved index inside node.
    for(int i = 0; i<destinationBlockIndex.size(); i++)
    {
      this.m_BlockDevice.freeMemBlock(destinationBlockIndex.get(i));
      destinationBlockIndex.remove(i);
    }

    destinationString = destinationString + sourceString;

    byte[] toBytes = destinationString.getBytes();

    byte[] to512bytes = new byte[512];
    for (int i = 0; i < to512bytes.length; i++)
    {
      to512bytes[i] = toBytes[i];
    }

    int blockIndex = m_BlockDevice.getNextAvailableIndex();
    currentDirectory.getNode(p_asDestination).getData().insertArrayIndex(blockIndex);
    int appendedfiles = m_BlockDevice.writeBlock(blockIndex, to512bytes);

    if(appendedfiles == 1)
    {
      return new String("\nAppend is successfully");
    }

    else
    {
      return new String("Creation of file failed!");
    }

  }

  
  public String rename(String p_asSource,String p_asDestination)
  {
    System.out.print("Renaming file ");
    System.out.println(p_asSource);
    System.out.print(" to ");
    System.out.print(p_asDestination+"\n");
    Node rename = root.getNode(currentDirectory.getPath());
    ArrayList<Node> files = rename.getChildren();

    for(int i = 0; i<files.size(); i++)
    {
      if(files.get(i).getData().isDirectory() == false)
      {
        if(files.get(i).getData().getName().compareTo(p_asSource) == 0)
        {
          files.get(i).getData().setName(p_asDestination);
          return new String("File name changed to: "+p_asDestination);
        }
      }
    }


    return new String("");
  }


  public String mkdir(String p_asPath)
    {
      String[] pathArray = p_asPath.split("/");
      String pathTo = "";
      for(int i = 0;i < pathArray.length-1;i++)
      {
        pathTo += pathArray[i] + "/";
      }

      Node parentNode;
      if(pathArray.length > 1)
      {
        parentNode = currentDirectory.getNode(pathTo);
      } else
      {
        parentNode = currentDirectory;
      }

      if (parentNode != null)
      {
        System.out.print("Creating directory ");

        Node newNode = new Node(parentNode, new Entry(pathArray[pathArray.length - 1], true));
        parentNode.addChild(newNode);

        return newNode.getData().getName();
      } else
      {
        return "Invalid path.";
      }
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
