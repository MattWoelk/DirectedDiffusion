import java.io.*;

public class OutputMaker
{
  private PrintWriter writer;

  public OutputMaker(String FileName) throws IOException
  {
    writer = new PrintWriter(new BufferedWriter(new FileWriter(FileName)));
  }

  public void AddOutput(String Output)
  {
    writer.print(Output);
    writer.flush();
  }

  public static void main(String[] args)
  {
    try
    {
      OutputMaker maker= new OutputMaker("test.txt");
      for(int i=0; i<100; i++)
      {
        maker.AddOutput(i + "\n");
      }
    }catch(IOException e){ System.out.println("System Error, deal with it"); }
  }
}
