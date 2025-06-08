package com.ldal.pigeonapp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class Client implements Serializable {
    private static boolean rememberMe;
    private boolean savedRememberMe;


    @Serial
    private static final long serialVersionUID = 2309L;

    private static final String filePath = new File("").getAbsolutePath();

    private static SQLServer sqlServer = null;

    private static User user;
    private User savedUser;


    private static final ArrayList<String> spamblacklist = new ArrayList<>();
    public static ArrayList<String> Recepients = new ArrayList<>();

    public static ArrayList<String> getSpamblacklist()
    {
        return spamblacklist;
    }

    public Client()
    {
        if (!loadSettings())
        {
            //aq default settingebi
            rememberMe = false;


            saveSettings();
        }

        try {
            File sent = new File(filePath + "\\Sent.txt");
            File draft = new File(filePath + "\\Draft.txt");
            File spam = new File(filePath + "\\Spam.txt");
            File settings = new File(filePath + "\\settings.txt");

            File[] files = {sent, draft, spam ,settings};

            for (File file : files) {
                if (!file.exists()) {
                    FileWriter fileWriter = new FileWriter(file);
                }
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }
        sqlServer = new SQLServer();

    }

    public boolean loadSettings(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\settings.txt")));
            Client loadedClient = (Client)objectInputStream.readObject();
            System.out.println("SRM : " + savedRememberMe);
            rememberMe = loadedClient.savedRememberMe;

            if(rememberMe) {
                user = loadedClient.savedUser;
                System.out.println("User loaded, username : " + user.getLogin());

                Recepients = loadedClient.Recepients;

            }


            objectInputStream.close();

        }catch (IOException | ClassNotFoundException | NullPointerException e){
            System.out.println("Error while importing settings! Resetting to defaults...");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void saveSettings(){
        try{
            savedRememberMe = rememberMe;
            if(rememberMe)savedUser = user;

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\settings.txt")));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            System.out.println("Settings saved!");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void login(String username){
        user = sqlServer.logIn(username);
    }

    public void pages(int pID){
        Scanner scanner = new Scanner(System.in);


        //user = sqlServer.SignUp(login, email, hashedPass, recoveryPass);
        //Email Hub
        if (pID == 3){

        }

        //Email composer
        else if (pID == 4){
            try
            {
                StringBuilder log = new StringBuilder();

                if(Recepients.isEmpty()) System.out.print("To: ");
                else
                {
                    System.out.print("To (Recommended - ");
                    for(String str : Mostcommonrecepeints(Recepients))
                    {
                        System.out.print(str + ";");
                    }
                    System.out.print("):");
                }

                log.append("To: ");
                String receiver1 = "";
                ArrayList<String> everyreceiver;
                receiverLoop:
                while(true)
                {
                    String strreceiver = scanner.nextLine() + ";";
                    everyreceiver = Indorgroupchecker(strreceiver);
                    for (String s : everyreceiver) {
                        if (!sqlServer.validateUser(s)) {
                            System.out.println("Invalid email: " + s + " ");
                            continue receiverLoop;
                        }
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
                if (text.length() > 6000) throw new InvalidEmailException("Limit of 6000 characters has been exceeded");

                System.out.println("Destination: ");
                System.out.println("1.Send");
                System.out.println("2.Draft");
                int decision = scanner.nextInt();
                if(decision != 1 && decision != 2) throw new InvalidEmailException("Invalid response");

                else if(decision == 1){
                    for (String recEmail : everyreceiver) {
                        System.out.println("Sending email to: " + recEmail);
                        EmailSender(new Email(this.user, sqlServer.userFromEmail(recEmail), text, subject));
                    }
                }

                else {
                    for (String recEmail : everyreceiver) {
                        emailDrafter(new Email(this.user, sqlServer.userFromEmail(recEmail), text, subject));
                    }
                }
            }
            catch (InvalidEmailException e) {
                System.out.println("ERROR: " + e.getMessage());
                pages(4);
            }
        }


        //Admin dashboard
        else if(pID == 9){
            System.out.println("*****Admin Dashboard*****");
            System.out.println("1.Get user info");
            System.out.println("2.Ban user");
            System.out.println("3.Unban user");

            int dec1 = scanner.nextInt();

            if(dec1 == 1){
                System.out.println("Choose method :" +
                        "\n1.Get by ID" +
                        "\n2.Get by Email" +
                        "\n3.Get by Login");

                int dec2 = scanner.nextInt();

                if(dec2 == 1){
                    System.out.print("Enter user ID : ");
                    int userID = scanner.nextInt();

                    System.out.println(sqlServer.userFromID(userID).toString());
                    pages(9);
                }
                else if(dec2 == 2){
                    System.out.print("Enter user Email : ");
                    String userEmail = scanner.next();

                    System.out.println(sqlServer.userFromEmail(userEmail));
                    pages(9);
                }
                else if(dec2 == 3) {
                    System.out.println("Enter user login : ");
                    String userLogin = scanner.next();

                    System.out.println(sqlServer.userFromLogin(userLogin));
                    pages(9);
                }
                else{
                    pages(9);
                    return;
                }
            }

            if(dec1 == 2){
                System.out.println("Enter user ID : ");
                int userID = scanner.nextInt();


            }
        }
    }

    public static void emailDrafter(Email email){
        try
        {
            ArrayList<Email> drafts = getDrafts();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\Draft.txt")));
            for(Email e : drafts) {
                objectOutputStream.writeObject(e);
            }
            objectOutputStream.writeObject(email);
            objectOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("E-mail Uploaded to Draft");
    }

    public void emailDrafter(ArrayList<Email> emails){
        try
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\Draft.txt")));

            for(Email email : emails) {
                objectOutputStream.writeObject(email);
            }
            objectOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Drafts saved!");
    }

    public static void EmailSender(Email email)
    {
            try
            {
                User sender = user;
                User receiver = sqlServer.userFromEmail(email.getReceiver().getEmail());

                sqlServer.sendEmail(sender.getId(), receiver.getId(), email.getText(), email.getSubject());

                //Sentebshi shenaxva
                email.setDateTime(Email.dateTimeToString(LocalDateTime.now()));

                ArrayList<Email> sents = getSent();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\Sent.txt")));
                for(Email e : sents) {
                    objectOutputStream.writeObject(e);
                }
                objectOutputStream.writeObject(email);
                objectOutputStream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        //pages(3);
    }

    public void FolderAccess()
    {
        Scanner scanner = new Scanner(System.in);

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
        Scanner scanner = new Scanner(System.in);

        System.out.println("Spam-List user: ");

        String decision = scanner.nextLine();

        spamblacklist.add(decision);
        System.out.println(decision + " added to the spam-List");
        System.out.println("*******************************");
        pages(3);
    }
    public void FolderDeleter(int decision)
    {
        if(decision == 1)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter( filePath + "\\Spam.txt"));
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
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Draft.txt"));
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
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Sent.txt"));
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
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Inbox.txt"));
                bufferedWriter.write("");
                System.out.println("Inbox folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        pages(3);
    }
    public void Folderviewer(int decision)
    {
        StringBuilder currentmail = new StringBuilder();
        if(decision == 1)
        {
            ArrayList<String> Spamemails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get(filePath + "\\Spam.txt"));
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
            Scanner scanner = new Scanner(System.in);
            ArrayList<Email> draftEmails = getDrafts();

            draftLoop:
            while(true) {
                for (int i = 0; i < draftEmails.size(); i++) {
                    Email email = draftEmails.get(i);
                    System.out.println(i + 1 + ". " + email.getSubject() + " | To : " + email.getReceiver().getEmail());
                }

                System.out.print("Choose email (hub : -1) : ");
                int emailChoice = scanner.nextInt();
                if(emailChoice == -1) break draftLoop;
                else if (emailChoice > 0 && emailChoice <= draftEmails.size()) {
                    System.out.println("\n*******************************");
                    System.out.println("\n" + draftEmails.get(emailChoice - 1).toString() + "\n");
                    System.out.println("*******************************");

                    System.out.println("\n1. Send" +
                            "\n2. Delete from drafts" +
                            "\n3. Back");

                    int input = scanner.nextInt();
                    if(input == 1){
                        EmailSender(draftEmails.get(emailChoice-1));
                    }
                    else if (input == 2) {
                        draftEmails.remove(emailChoice - 1);
                    }

                }
                else System.out.println("Invalid choice!");
            }



        }
        else if(decision == 3)
        {
            ArrayList<String> Sentemails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get(filePath + "\\Sent.txt"));
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
        pages(3);
    }

    public static ArrayList<Email> getInbox()
    {
            return sqlServer.getInbox(user.getId());
    }

    public static ArrayList<Email> getDrafts() {
        ArrayList<Email> draftEmails = new ArrayList<>();
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\Draft.txt")));

            Email cEmail = (Email) objectInputStream.readObject();
            while(cEmail != null){
                draftEmails.add(cEmail);
                cEmail = (Email) objectInputStream.readObject();
            }

        }
        catch (EOFException eof){
            System.out.println("Drafts read!");
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return draftEmails;
    }

    public static ArrayList<Email> getSent() {
        ArrayList<Email> draftEmails = new ArrayList<>();
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\Sent.txt")));

            Email cEmail = (Email) objectInputStream.readObject();
            while(cEmail != null){
                draftEmails.add(cEmail);
                cEmail = (Email) objectInputStream.readObject();
            }

        }
        catch (EOFException eof){
            System.out.println("Sents read!");
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return draftEmails;
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
                currentUser.append(c);
            }
        }
        if(!currentUser.isEmpty())
        {
            Users.add(currentUser.toString());
        }
        return Users;
    }

    public static User getUser() {
        return user;
    }

    public static SQLServer getSQLServer() {
        return sqlServer;
    }

    public static boolean isRememberMe() {
        return rememberMe;
    }

    public static void setRememberMe(boolean rememberMe) {
        Client.rememberMe = rememberMe;
    }
}
