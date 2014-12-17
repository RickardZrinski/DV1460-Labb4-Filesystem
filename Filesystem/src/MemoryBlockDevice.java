public class MemoryBlockDevice extends BlockDevice
{
  byte[][] m_abContents=new byte[250][512];
  int nextAvailableIndex;



  public MemoryBlockDevice()
  {
    for (int i = 0; i<250; i++)
    {
      m_abContents[i][0] = -1;
    }

    nextAvailableIndex = findAvailableIndex();
  }
  public int writeBlock(int p_nBlockNr,byte[] p_abContents)
  {
    if(p_nBlockNr>249 || p_nBlockNr<0)
    {
      // Block out-of-range
      return -1;
    }

    if(p_abContents.length!=512)
    {
      // Block size out-of-range
      return -2;
    }

    for(int nIndex=0;nIndex<512;nIndex++)
    {
      m_abContents[p_nBlockNr][nIndex]=p_abContents[nIndex];
    }

    nextAvailableIndex = findAvailableIndex();

    return 1;

  }

  public byte[] readBlock(int p_nBlockNr)
  {
    if(p_nBlockNr>249 || p_nBlockNr<0)
    {
      // Block out-of-range
      return new byte[0];
    }

    byte[] abBlock=new byte[512];

    for(int nIndex=0;nIndex<512;nIndex++)
    {
      abBlock[nIndex]=m_abContents[p_nBlockNr][nIndex];
    }

    return abBlock;
  }

  public int getNextAvailableIndex() {
    return nextAvailableIndex;
  }

  private int findAvailableIndex()
  {

    for(int i = 0; i<250; i++)
    {
      if(m_abContents[i][0] == -1)
      {
        nextAvailableIndex = i;
      }
    }

    return nextAvailableIndex;
  }
}