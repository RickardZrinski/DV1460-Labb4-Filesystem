import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Filesystem implements Serializable
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
       m_BlockDevice.freeMemBlock(i);
    }
    return new String("Diskformat successful");
  }

  public String ls(String p_asPath)
  {
    String output;

    Node node = currentDirectory.getNode(p_asPath);

    if(node != null)
    {
      output = "Listing directories and files\n";

      ArrayList<Node> children = node.getChildren();

      for(Node child : children)
      {
        output += child.getData().getName() + " ";
      }
    } else
    {
      output = "Invalid path.";
    }

    return output;
  }
  
  public String create(String p_asPath,byte[] p_abContents)
  {
    String output;

    output = "Creating file ";

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

    if(parentNode != null)
    {
        Node newNode = new Node(currentDirectory, new Entry(pathArray[pathArray.length-1], false));
        parentNode.addChild(newNode);

        System.out.println("Writes to block number: " + m_BlockDevice.getNextAvailableIndex());
        newNode.getData().insertArrayIndex(m_BlockDevice.getNextAvailableIndex());
        String fetch = new String(p_abContents);

        String newLine = '\n' + fetch;
        byte[] toBytes = newLine.getBytes();

        byte[] to512bytes = new byte[512];
        for (int i = 0; i < to512bytes.length; i++) {
          to512bytes[i] = toBytes[i];
        }
        int check = m_BlockDevice.writeBlock(m_BlockDevice.getNextAvailableIndex(), to512bytes);

        if(check != 1)
        {
          output = "Creation of file failed!";
        }

    } else
    {
      output = "Invalid path.";
    }

    return output;
  }

  public String cat(String p_asPath)
  {
    String output;
    output = "Dumping contents of file ";

    if(currentDirectory.getNode(p_asPath) == null)
    {
      output = "Path does not exist!";
      return output;
    }

    if(currentDirectory.getNode(p_asPath).getData().isDirectory() == true)
    {
      output = "Path is a directory!";
      return output;
    }

    ArrayList<Integer> fetch = currentDirectory.getNode(p_asPath).getData().getArrayIndexes();
    for(int i=0; i<fetch.size(); i++)
    {
      byte[] fetchByteArray = m_BlockDevice.readBlock(fetch.get(i));
      String makeString = new String(fetchByteArray);
      output += makeString + "\n";
    }


    return output;
  }

  public String save(String p_sPath)
  {
    String output;
    output = "Saving filesystem to file " + p_sPath;

    try
    {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(p_sPath));
      out.writeObject(this);
      out.flush();
      out.close();
    }
    catch(IOException e)
    {
      e.getStackTrace();
    }

    return output;
  }

  public Filesystem read(String p_sPath)
  {
    System.out.println("Reading file " + p_sPath + " to restore filesystem");
    Filesystem filesystem = null;
    ObjectInputStream in;
    try
    {
      in = new ObjectInputStream(new FileInputStream(p_sPath));
      filesystem = (Filesystem) in.readObject();
      in.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }

    return filesystem;
  }

  public String rm(String p_asPath)
  {
    String output;
    output = "Removing file ";

    // Get node to remove
    if(!currentDirectory.getNode(p_asPath).getData().isDirectory())
    {
      Node node = this.currentDirectory.getNode(p_asPath);

      // We need to delete the data in memory before removing the node
      ArrayList<Integer> memIndexes = node.getData().getArrayIndexes();
      for(Integer index: memIndexes)
      {
        this.m_BlockDevice.freeMemBlock(index);
      }

      // Remove the node
      this.currentDirectory.remove(p_asPath);
    }
    return output;
  }

  public String copy(String p_asSource,String p_asDestination)
  {
    String output;
    output = "Copying file from " + p_asSource + " to " + p_asDestination;

    String[] sourceSplit = p_asSource.split("/");
    String sourcePath = "";
    for(int i=0; i< sourceSplit.length-1;i++)
    {
      sourcePath += sourceSplit[i]+"/";
    }

    if(sourceSplit.length > 1)
    {
      if(currentDirectory.getNode(p_asSource) == null)
      {
        output += "\nSource path does not exist!";
        return output;
      }

      if(currentDirectory.getNode(sourcePath).getData().isDirectory())
      {
        output += "\nSource path is a directory!";
        return output;
      }
    }
    else if(sourceSplit.length == 1)
    {
      if(currentDirectory.getNode(p_asSource).getData().isDirectory())
      {
        output += "\nSource path is a directory!";
      }
    }

    String[] targetsplit = p_asDestination.split("/");
    String targetPath = "";
    for(int i=0; i<targetsplit.length-1; i++)
    {
      targetPath += targetsplit[i];
    }

    Node targetCheck = currentDirectory.getNode(p_asDestination);
    //om det finns en målpath specifierad
    if(targetsplit.length > 1)
    {
      //kollar om målfilen existerar och är en katalog
      Node targetCheck2 = currentDirectory.getNode(targetsplit[targetsplit.length-1]);
      if(targetCheck2 != null && targetCheck.getData().isDirectory())
      {
        output += "Destination file is a directory!";
        return output;
      }
      //kollar om målpath existerar
      Node targetCheck3 = currentDirectory.getNode(targetPath);
      if(targetCheck3 == null)
      {
        output += "\nDestination path does not exist!";
        return output;
      }
    }
    //om målfilen ska skapas i root
    else if(targetsplit.length == 1 && targetCheck != null)
    {
      if(currentDirectory.getNode(p_asDestination).getData().isDirectory())
      {
        output += "\nDestination file is a directory!";
        return output;
      }
    }

    Node sourceFile = null;
    if(p_asSource.contains("/"))
    {
      sourceFile = currentDirectory.getNode(sourcePath);
    }
    else
    {
      sourceFile = currentDirectory.getNode(p_asSource);
    }

    Node destinationFile = null;
    ArrayList<Integer> sourceBlockIndex = sourceFile.getData().getArrayIndexes();
    ArrayList<Integer> destinationBlockIndex = new ArrayList<Integer>();

    //skapar ny nod i root
    if(targetsplit.length == 1)
    {
      currentDirectory.addChild(new Node(currentDirectory, new Entry(p_asDestination, false)));
      destinationFile = currentDirectory.getNode(p_asDestination);
      destinationBlockIndex = destinationFile.getData().getArrayIndexes();
    }
    //om målfilen ska läggas till i en path
    else if(targetsplit.length > 1)
    {
      String check = Character.toString(p_asDestination.charAt(0));
      //för specialfallet där man specifierar målpath till root och då targetsplit.length == 2
      if(check.contentEquals("..") && targetsplit.length == 2)
      {
        destinationFile = root.getNode(root.getData().getName());
        Node newNode = new Node(destinationFile, new Entry(targetsplit[targetsplit.length-1], false));
        destinationFile.addChild(newNode);
        destinationFile = destinationFile.getNode(targetsplit[targetsplit.length-1]);
        destinationBlockIndex = destinationFile.getData().getArrayIndexes();
      }
      else if(targetsplit.length >= 2 && !check.contentEquals(".."))
      {
        //går till högsta katalogen i målpath
        destinationFile = currentDirectory.getNode(targetPath);
        Node newNode= new Node(destinationFile, new Entry(targetsplit[targetsplit.length-1],false));
        destinationFile.addChild(newNode);
        destinationFile = destinationFile.getNode(targetsplit[targetsplit.length-1]);
        destinationBlockIndex = destinationFile.getData().getArrayIndexes();
      }
    }

    // remove data if destination file contains allocated blocks
    for(int i = 0; i<destinationBlockIndex.size(); i++)
    {
      this.m_BlockDevice.freeMemBlock(destinationBlockIndex.get(i));
      destinationBlockIndex.remove(i);
    }

    for(int i = 0; i<sourceBlockIndex.size(); i++)
    {
      byte[] sourceByteArray = m_BlockDevice.readBlock(sourceBlockIndex.get(i));
      int availableIndex = m_BlockDevice.getNextAvailableIndex();
      int status = m_BlockDevice.writeBlock(availableIndex,sourceByteArray);
      destinationFile.getData().insertArrayIndex(availableIndex);
      if(status == -1)
      {
        output += "\nBlock out of range!";
        return output;
      }

      if(status == -2)
      {
        output += "\nSize is not 512!";
        return output;
      }
    }
    output += "\nCopy was successfully done!";
    return output;
  }

  public String append(String p_asSource,String p_asDestination)
  {
    String output;
    output = "Appending file " + p_asSource + " to " + p_asDestination;


    if(currentDirectory.getNode(p_asSource) == null)
    {
      output += "\nSource path does not exist!";
      return output;
    }

    if(currentDirectory.getNode(p_asDestination) == null)
    {
      output += "\nDestination path does not exist!";
      return output;
    }

    if(currentDirectory.getNode(p_asSource).getData().isDirectory())
    {
      output += "\nSource path is a directory!";
      return output;
    }

    if(currentDirectory.getNode(p_asDestination).getData().isDirectory())
    {
      output += "\nDestionation path is a directory !";
      return output;
    }

    ArrayList<Integer> sourceBlockIndex = currentDirectory.getNode(p_asSource).getData().getArrayIndexes();
    ArrayList<Integer> destinationBlockIndex = currentDirectory.getNode(p_asDestination).getData().getArrayIndexes();

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

    byte[] sourceToBytes  = sourceString.getBytes();
    byte[] destinationToByes = destinationString.getBytes();


    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);

    outputStream.write(sourceToBytes,0,256);
    outputStream.write(destinationToByes,0,256);

    byte result[] = outputStream.toByteArray();

    //////////////////////////////////////////

    int blockIndex = m_BlockDevice.getNextAvailableIndex();
    currentDirectory.getNode(p_asDestination).getData().insertArrayIndex(blockIndex);
    int appendedfiles = m_BlockDevice.writeBlock(blockIndex,result);

    if(appendedfiles == 1)
    {
      output += "\nAppend was successful";
      return output;
    }

    else
    {
      output += "Creation of file failed!";
      return output;
    }

  }

  public String rename(String p_asSource,String p_asDestination)
  {
    String output;
    output = "Renaming file " + p_asSource + " to " + p_asDestination + "\n";
    Node rename = currentDirectory.getNode(p_asSource);

    if(rename.getData().isDirectory())
    {
      output += "This is a directory, sorry";
    } else
    {
      output += "Name changed to: " + p_asDestination;
    }

    rename.getData().setName(p_asDestination);

    return output;
  }


  public String mkdir(String p_asPath)
  {
    String output;

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
      output = "Creating directory ";

      Node newNode = new Node(parentNode, new Entry(pathArray[pathArray.length - 1], true));
      parentNode.addChild(newNode);

      output += newNode.getData().getName();
    } else
    {
      output = "Invalid path.";
    }

    return output;
  }

  public String cd(String p_asPath)
  {
    String output;

    Node node = currentDirectory.getNode(p_asPath);

    if(node != null && node.getData().isDirectory())
    {
      output = "Changing directory to " + p_asPath;

      currentDirectory = node;
    }
    else
    {
      output = "Invalid path.";
    }

    return output;
  }

  public String pwd()
  {
    return currentDirectory.getPath();
  }
}
