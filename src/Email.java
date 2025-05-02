import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Email
{
    private User sender, receiver;
    private String subject, text;
    private Media attachment;

    public void emailComposition()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            StringBuilder log = new StringBuilder();

            System.out.print("To: ");
            log.append("To: ");
            String sender = scanner.nextLine();
            log.append(sender).append("\n");

            System.out.print("Subject: ");
            log.append("Subject: ");
            String subject = scanner.nextLine();
            log.append(subject).append("\n");

            System.out.println("---------------------------");
            log.append("---------------------------");

            String text = scanner.nextLine();
            log.append(text).append("\n");
            if (text.length() > 7500) throw new InvalidEmailException("Limit of 7500 characters has been exceeded");

            System.out.println("\nSend/Draft");
            String decision = scanner.nextLine().toLowerCase();
            if(decision.equals("send"))
            {
                Sendcurrentlyupload(log);
            }
            else if(decision.equals("draft"))
            {
                Draftcurrentlyupload(log);
            }
            else throw new InvalidEmailException("Invalid response");
        }
        catch (InvalidEmailException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void Sendcurrentlyupload(StringBuilder log)
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Sent.txt"));
            bufferedWriter.append(log);
            bufferedWriter.append("___________________________");
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Draftcurrentlyupload(StringBuilder log)
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Draft.txt"));
            bufferedWriter.append(log);
            bufferedWriter.append("___________________________");
            bufferedWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
