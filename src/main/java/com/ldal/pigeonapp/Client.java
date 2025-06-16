package com.ldal.pigeonapp;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class Client implements Serializable {
    private static boolean rememberMe;
    private boolean savedRememberMe;



    @Serial
    private static final long serialVersionUID = 2309L;

    private static final String filePath = "C:\\Users\\User\\Documents\\Pigeon\\pigeon\\out\\artifacts\\PigeonApp_jar";

    private static SQLServer sqlServer = null;

    private static User user;
    private User savedUser;


    private static ArrayList<String> spamblacklist = new ArrayList<>();
    private static ArrayList<String> Recepients = new ArrayList<>();
    private static ArrayList<String> blockedblacklist = new ArrayList<>();


    private static ArrayList<CustomLabel> customLabels;
    private ArrayList<CustomLabel> savedCustomLabels;

    public static ArrayList<String> getSpamblacklist() {return spamblacklist;}
    public static ArrayList<String> getBlockedblacklist() {return blockedblacklist;}
    public static ArrayList<String> getRecepients() {return Recepients;}


    public static void setSpamblacklist(String user)
    {
        spamblacklist.add(user);
    }
    public static void setBlockedblacklist(String user) { blockedblacklist.add(user); }
    public static void setRecepients(String user) { Recepients.add(user); }
    public static void removeSpamblacklist(String user)
    {
        spamblacklist.removeIf(s -> s.equals(user));
    }
    public static void removeBlockedblacklist(String user) { blockedblacklist.removeIf(s -> s.equals(user)); }
    public static void removeRecepients(String user) { Recepients.removeIf(s -> s.equals(user)); }

    public Client()
    {
        if (!loadSettings())
        {
            //default settingebi

            rememberMe = false;
            customLabels = new ArrayList<>();
            spamblacklist = new ArrayList<>();


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

        }catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        sqlServer = new SQLServer();
        CustomLabel.setLabelAmount(customLabels.size());
    }


    public static ArrayList<CustomLabel> getCustomLabels() {
        if(customLabels == null) return new ArrayList<>();
        return customLabels;
    }

    public boolean loadSettings()
    {
        try{
            loadSpamblacklist();
            loadBlocklist();
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\settings.txt")));
            Client loadedClient = (Client)objectInputStream.readObject();
            rememberMe = loadedClient.savedRememberMe;

            if(rememberMe) {
                user = loadedClient.savedUser;
            }

            customLabels = loadedClient.savedCustomLabels;

            Recepients = loadedClient.Recepients;
            loadReceipientsInfo();
            objectInputStream.close();

        }
        catch (IOException | ClassNotFoundException | NullPointerException e)
    {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void loadSpamblacklist()
    {
        File spamFile = new File(filePath + "\\spamusers.txt");
        if (!spamFile.exists()) {
            try {
                spamFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(spamFile));

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                spamblacklist.add(line.trim());
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadBlocklist()
    {
        File blockedfile = new File(filePath + "\\blockedusers.txt");
        if (!blockedfile.exists()) {
            try {
                blockedfile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(blockedfile));

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                blockedblacklist.add(line.trim());
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void saveSpamBlacklist()
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\spamusers.txt"));
            for(String user : spamblacklist)
            {
                bufferedWriter.write(user);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void saveBlocklist()
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\blockedusers.txt"));
            for(String user : blockedblacklist)
            {
                bufferedWriter.write(user);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean loadSQLInfo()
    {
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "\\SQLInfo.txt"));
            SQLServer.userName = bufferedReader.readLine();
            SQLServer.password = bufferedReader.readLine();
            bufferedReader.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
    public void loadReceipientsInfo()
    {
        try
        {
            File recipientsFile = new File(filePath + "\\Recipients.txt");
            if (!recipientsFile.exists()) {
                try {
                    recipientsFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Recepients.clear();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "\\Recipients.txt"));

            String line;
            ArrayList<String> lines = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null)
            {
                lines.add(line.trim());
            }
            if(lines.get(lines.size() - 1).equals(getUser().getLogin()))
            {
                for(String s : lines)
                {
                    Recepients.add(s);
                }
                Recepients.remove(Recepients.get(Recepients.size() - 1));
            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void saveReceipientsInfo()
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Recipients.txt"));
            for(String s : Recepients)
            {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
            bufferedWriter.write(getUser().getLogin());
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void saveSQLInfo()
    {
        try
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\SQLInfo.txt"));
            bufferedWriter.write(SQLServer.userName);
            bufferedWriter.newLine();
            bufferedWriter.write(SQLServer.password);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSettings(){
        try{
            savedRememberMe = rememberMe;
            if(rememberMe)savedUser = user;
            savedCustomLabels = customLabels;

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\settings.txt")));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void login(String username){
        user = sqlServer.logIn(username);
    }

    public static void emailDrafter(Email email)
    {
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


    public static void emailSender(Email email)
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
                for(Email e : sents)
                {
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


    public static ArrayList<Email> getInbox()
    {
            return sqlServer.getInbox(user.getId());
    }

    public static ArrayList<Email> getDrafts()
    {
        ArrayList<Email> draftEmails = new ArrayList<>();
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\Draft.txt")));

            Email cEmail = (Email) objectInputStream.readObject();
            while(cEmail != null)
            {
                draftEmails.add(cEmail);
                cEmail = (Email) objectInputStream.readObject();
            }

        }
        catch (EOFException eof)
        {
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
            while(cEmail != null)
            {
                draftEmails.add(cEmail);
                cEmail = (Email) objectInputStream.readObject();
            }

        }
        catch (EOFException eof)
        {
            System.out.println("Sents read!");
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return draftEmails;
    }

    public static ArrayList<String> mostCommonRecepeints(ArrayList<String> recepients)
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
        for(Map.Entry<String, Integer> entry : frequencymap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        return topapperances;
    }

    public static ArrayList<String> indorGroupChecker(String string)
    {
        StringBuilder currentUser = new StringBuilder();
        ArrayList<String> Users = new ArrayList<>();

        for (Character c : string.toCharArray())
        {
            if (c.equals(','))
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
            Users.add(currentUser.toString().trim());
        }
        return Users;
    }

    public static void FolderDeleter(int decision) {
        if (decision == 1) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Spam.txt"));
                bufferedWriter.write("");
                System.out.println("Spam folder cleared out");
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (decision == 2) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Draft.txt"));
                bufferedWriter.write("");
                System.out.println("Draft folder cleared out");
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (decision == 3) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Sent.txt"));
                bufferedWriter.write("");
                System.out.println("Sent folder cleared out");
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (decision == 4) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Inbox.txt"));
                bufferedWriter.write("");
                System.out.println("Inbox folder cleared out");
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    public static void setCustomLabels(ArrayList<CustomLabel> customLabels) {
        Client.customLabels = customLabels;
    }
}
