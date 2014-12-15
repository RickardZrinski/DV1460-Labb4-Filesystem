public class TestShell
{
  public static void main(String[] args)
    {
      //MemoryBlockDevice BlockTest=new MemoryBlockDevice();
      //Filesystem FS=new Filesystem(BlockTest);
      //Shell Bash=new Shell(FS,null);
      //Bash.start();

      FileTree k = new FileTree();

      k.addEntrys();
      k.printall();

    }
}
