import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Email
{
    private User sender, receiver;
    private String subject, text;
    private Media attachment;
    private ArrayList<String> Spamblacklist = new ArrayList<>();
    private ArrayList<String> Recepients = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void Hub()
    {
        Spamblacklist = Fileloader(0);
        Recepients = Fileloader(1);

        System.out.println("********Welcome to Pigeon********");
        System.out.println();
        System.out.println("1.Compose E-mail");
        System.out.println("2.Inbox");
        System.out.println("3.Access diffrent folders");

        int decision = scanner.nextInt();
        scanner.nextLine();
        System.out.println("*******************************");
        if (decision == 1)  emailComposition();
        else if (decision == 2) Inbox();
        else if (decision == 3) FolderAcess();
    }

    public void emailComposition()
    {
        try
        {
            StringBuilder log = new StringBuilder();

            if(Recepients.isEmpty()) System.out.print("To: ");
            else
            {
                System.out.print("To (Recomended - ");
                for(String str : Mostcommonrecepeints(Recepients))
                {
                    System.out.print(str + ";");
                }
                System.out.print("):");
            }

            log.append("To: ");
            SQLServer sqlServer = new SQLServer();
            String receiver1 = "";
            while(true)
            {
                String strreceiver = scanner.nextLine() + ";";
                ArrayList<String> everyreceiver = Indorgroupchecker(strreceiver);
                for (int i = 0; i < everyreceiver.size(); i++)
                {
                    if (!sqlServer.validateLogin(everyreceiver.get(i))) System.out.println("Invalid email: " + everyreceiver.get(i) + " ");
                }
                receiver1 = strreceiver;
                break;
            }
            log.append(receiver1).append("\n");

            System.out.print("Subject: ");
            log.append("Subject: ");
            String subject = scanner.nextLine();
            log.append(subject).append("\n");

            System.out.println("---------------------------");
            log.append("---------------------------\n");

            String text = scanner.nextLine();
            log.append(text).append("\n");
            if (text.length() > 65000) throw new InvalidEmailException("Limit of 65000 characters has been exceeded");

            System.out.println("Destination: ");
            System.out.println("1.Send");
            System.out.println("2.Draft");
            int decision = scanner.nextInt();
            if (decision == 1)
            {
                EmailSender(1, log, receiver1);
            }
            else if (decision == 2)
            {
                EmailSender(2, log, "0");
            }
            else throw new InvalidEmailException("Invalid response");
        }
        catch (InvalidEmailException e)
        {
            System.out.println("ERROR: " + e.getMessage());
            Hub();
        }
    }

    public void EmailSender(int decision, StringBuilder log, String Receiver)
    {
        if(decision == 1)
        {
            //aq mere serveris shit ra rom imena miuvdes u get me brochacho
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Sent.txt", true));
                bufferedWriter.append(log);
                bufferedWriter.write("\n---EMAIL-END---\n");
                bufferedWriter.close();
                Recepients.add(Receiver);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 2)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Draft.txt", true));
                bufferedWriter.append(log);
                bufferedWriter.write("\n---EMAIL-END---\n");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("E-mail Uploaded to Draft");
        }
        Hub();
    }

    public ArrayList<String> Mostcommonrecepeints(ArrayList<String> recepients)
    {
        HashMap<String, Integer> frequencymap = new HashMap<>();
        for(String  str : recepients)
        {
            frequencymap.put(str, frequencymap.getOrDefault(str, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(frequencymap.entrySet());

        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        ArrayList<String> topapperances = new ArrayList<>();
        for(int i = 0; i < Math.min(3, sortedList.size()); i++)
        {
            topapperances.add(sortedList.get(i).getKey());
        }
        return topapperances;
    }

    public void Inbox()
    {
        System.out.println("Inbox action: ");
        System.out.println("1.View inbox");
        System.out.println("2.Delete all in inbox ");

        int decision1 = scanner.nextInt();

        if(decision1 == 1) Folderviewer(4);
        else if(decision1 == 2) FolderDeleter(4);
    }
    public void FolderAcess()
    {
        System.out.println("Select folder: ");
        System.out.println();
        System.out.println("1.Spam");
        System.out.println("2.Draft");
        System.out.println("3.All E-mails sent");

        int decision = scanner.nextInt();
        System.out.println("*******************************");
        if(decision == 1)
        {
            System.out.println("Spam folder action: ");
            System.out.println();
            System.out.println("1.View all in spam");
            System.out.println("2.Delete all in spam");
            System.out.println("3.Add a new user in the spamlist");

            int decision1 = scanner.nextInt();
            System.out.println("*******************************");

            if (decision1 == 1) Folderviewer(1);
            if (decision1 == 2) FolderDeleter(1);
            if (decision1 == 3) Newadditiontospam();

        }
        else if(decision == 2)
        {
            System.out.println("Draft folder action: ");
            System.out.println();
            System.out.println("1.View all in Draft");
            System.out.println("2.Delete all in Draft");

            int decision1 = scanner.nextInt();
            System.out.println("*******************************");

            if (decision1 == 1) Folderviewer(2);
            if (decision == 2) FolderDeleter(2);
        }
        else if(decision == 3)
        {
            System.out.println("All-Emails sent folder action: ");
            System.out.println();
            System.out.println("1.View all in All-Emails sent");
            System.out.println("2.Delete all in All-Emails sent (this will only delete the emails on your own end)");

            int decision1 = scanner.nextInt();

            System.out.println("*******************************");

            if(decision1 == 1) Folderviewer(3);
            if(decision1 == 2) FolderDeleter(3);
        }
    }
    public void Newadditiontospam()
    {
        System.out.println("Spam-List user: ");

        String decision = scanner.nextLine();

        Spamblacklist.add(decision);
        System.out.println(decision + " added to the spam-List");
        System.out.println("*******************************");
        Savefiler(0);
        Hub();
    }
    public void FolderDeleter(int decision)
    {
        if(decision == 1)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Spam.txt"));
                bufferedWriter.write("");
                System.out.println("Spam folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 2)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Draft.txt"));
                bufferedWriter.write("");
                System.out.println("Draft folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 3)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Sent.txt"));
                bufferedWriter.write("");
                System.out.println("Sent folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 4)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\User\\Documents\\Pigeon\\Inbox.txt"));
                bufferedWriter.write("");
                System.out.println("Inbox folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        Hub();
    }

    public ArrayList<String> Indorgroupchecker (String string)
    {
        StringBuilder currentUser = new StringBuilder();
        ArrayList<String> Users = new ArrayList<>();

        for (Character c : string.toCharArray())
        {
            if (c.equals(';'))
            {
                Users.add(currentUser.toString().trim());
                currentUser.setLength(0);
            }
            else
            {
                currentUser.append(c).append("\n");
            }

            for (int i = 0; i < Users.size(); i++)
            {
                System.out.println("\nSpam Email: " + (i + 1));
                System.out.println(Users.get(i));
            }
        }
        return Users;
    }

    public void Folderviewer(int decision)
    {
        StringBuilder currentmail = new StringBuilder();
        if(decision == 1)
        {
            ArrayList<String> Spamemails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get("C:\\Users\\User\\Documents\\Pigeon\\Spam.txt"));
                for (String line : jumpbledemails)
                {
                    if (line.equals("---EMAIL-END---"))
                    {
                        Spamemails.add(currentmail.toString().trim());
                        currentmail.setLength(0);
                    }
                    else
                    {
                        currentmail.append(line).append("\n");
                    }
                    for (int i = 0; i < Spamemails.size(); i++)
                    {
                        System.out.println("\nSpam Email: " + (i + 1));
                        System.out.println(Spamemails.get(i));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if (decision == 2)
        {
            ArrayList<String> Draftemails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get("C:\\Users\\User\\Documents\\Pigeon\\Draft.txt"));
                for (String line : jumpbledemails)
                {
                    if (line.equals("---EMAIL-END---"))
                    {
                        Draftemails.add(currentmail.toString().trim());
                        currentmail.setLength(0);
                    }
                    else
                    {
                        currentmail.append(line).append("\n");
                    }
                    for (int i = 0; i < Draftemails.size(); i++)
                    {
                        System.out.println("\nDraft Email: " + (i + 1));
                        System.out.println(Draftemails.get(i));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 3)
        {
            ArrayList<String> Sentemails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get("C:\\Users\\User\\Documents\\Pigeon\\Sent.txt"));
                for (String line : jumpbledemails)
                {
                    if (line.equals("---EMAIL-END---"))
                    {
                        Sentemails.add(currentmail.toString().trim());
                        currentmail.setLength(0);
                    }
                    else
                    {
                        currentmail.append(line).append("\n");
                    }
                    for (int i = 0; i < Sentemails.size(); i++)
                    {
                        System.out.println("\nSent Email: " + (i + 1));
                        System.out.println(Sentemails.get(i));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 4)
        {

            ArrayList<String> emails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get("C:\\Users\\User\\Documents\\Pigeon\\Inbox.txt"));
                for (String line : jumpbledemails)
                {
                    if (line.equals("---EMAIL-END---"))
                    {
                        emails.add(currentmail.toString().trim());
                        currentmail.setLength(0);
                    }
                    else
                    {
                        currentmail.append(line).append("\n");
                    }
                    for (int i = 0; i < emails.size(); i++)
                    {
                        System.out.println("\nSent Email: " + (i + 1));
                        System.out.println(emails.get(i));
                    }
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        Hub();
    }
    public void Savefiler(int indicator)
    {
        if(indicator == 0)
        {
            try
            {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("")));
                objectOutputStream.writeObject(Spamblacklist);
                objectOutputStream.close();
            }
            catch (FileNotFoundException e) {

                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(indicator == 1)
        {
            try
            {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("")));
                objectOutputStream.writeObject(Recepients);
                objectOutputStream.close();
            }
            catch (FileNotFoundException e) {

                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public ArrayList Fileloader(int indicator)
    {
        if(indicator == 0)
        {
            ArrayList<String> spam = new ArrayList<>();
            try
            {
                ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream("")));
                spam = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();
            }
            catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return spam;
        }
        else
        {
            ArrayList<String> recepeints = new ArrayList<>();
            try
            {
                ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream("")));
                recepeints = (ArrayList<String>) objectInputStream.readObject();
                objectInputStream.close();
            }
            catch (FileNotFoundException e) {

                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            catch (ClassNotFoundException e){throw new RuntimeException(e);}
            return recepeints;

        }
    }
}
