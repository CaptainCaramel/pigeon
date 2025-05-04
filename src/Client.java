import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Client implements Serializable {
    private boolean rememberMe;

    @Serial
    private static final long serialVersionUID = 2309L;

    private final String filePath = new File("").getAbsolutePath();

    private final SQLServer sqlServer;

    private User user;

    private ArrayList<String> spamblacklist = new ArrayList<>();

    Client(){
        if (!loadSettings()) {
            //aq default settingebi
            setRememberMe(false);


            saveSettings();
        }

        sqlServer = new SQLServer();
    }

    public boolean loadSettings(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\settings.txt")));
            Client loadedClient = (Client)objectInputStream.readObject();

            this.rememberMe = loadedClient.rememberMe;
            if(rememberMe) this.user = loadedClient.user;

            objectInputStream.close();

        }catch (IOException | ClassNotFoundException e){
            //throw new RuntimeException(e);
            //e.printStackTrace();
            System.out.println("Error while importing settings! Resetting to defaults...");
            return false;
        }
        return true;
    }

    public void saveSettings(){
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\settings.txt")));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            System.out.println("Settings saved!");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    public void pages(int pID){
        Scanner scanner = new Scanner(System.in);
        //Select Login or Sign up page
        if(pID == 0){

            if(user != null){
                pages(3);
                return;
            }

            System.out.println("**********Welcome to Pigeon Mail**********\n" +
                    "     1.Log In     \n" +
                    "     2.Sign Up     \n" );


            int ans1 = scanner.nextInt();

            if(ans1 == 1 || ans1 == 2)pages(ans1);
            else {
                System.out.println("Invalid Input!");
                pages(0);
            }
        }

        //Login Page
        else if(pID == 1){
            System.out.println("**********Log In**********\n");

            String login;

            loginLoop:
            while(true) {
                System.out.print("Enter Login : ");
                login = scanner.nextLine();
                if(!User.validateLogin(login)) {
                    System.out.println("Invalid Login! Login can only contain : a-z, A-Z, 0-9, _ and be 4-25 characters long");
                    continue loginLoop;
                }

                if(sqlServer.validateLogin(login)) break;
                else System.out.println("User with this login not found!");
            }

            passwordLoop:
            while(true) {
                System.out.println();
                System.out.print("Enter Password : ");
                String password = scanner.nextLine();
                PassHasher passHasher = new PassHasher();
                String c_hashedPass = passHasher.hasher(password);
                if(sqlServer.validatePassword(login, c_hashedPass)){
                    System.out.println("Login successful!");
                    break passwordLoop;
                }
                else System.out.println("Wrong Password!");
            }

            System.out.println();
            System.out.println("Remember me? (Currently : " + this.rememberMe + ")");

            rememberMeLoop:
            while(true){
                String remMe = scanner.nextLine();
                if(remMe.equalsIgnoreCase("true")){
                    setRememberMe(true);
                    saveSettings();
                    break rememberMeLoop;
                }
                else if(remMe.equalsIgnoreCase("false")){
                    setRememberMe(false);
                    saveSettings();
                    break rememberMeLoop;
                }
                else System.out.println("Invalid input! Use \"True\" or \"False\"!");
            }
            System.out.println("**************************");
        }

        //Sign up page
        else if (pID == 2){
            System.out.println("**********Sign Up**********\n");

            String login;
            loginLoop:
            while(true) {
                System.out.print("Enter Login : ");
                login = scanner.nextLine();
                if(!User.validateLogin(login)) {
                    System.out.println("Invalid Login! Login can only contain : a-z, A-Z, 0-9, _, - and be 4-25 characters long");
                    continue loginLoop;
                }

                if(sqlServer.validateLogin(login)) {
                    System.out.println("Login already taken!");
                    continue loginLoop;
                }
                break loginLoop;
            }

            String password;
            passwordLoop:
            while(true) {
                System.out.println();
                System.out.print("Enter Password : ");
                password = scanner.nextLine();
                if(!User.validatePassword(password)){
                    System.out.println("Invalid Password! Password can only contain the alphabet, numbers and special characters(Except for \\ and \") and be 8-25 characters long");
                    continue passwordLoop;
                }
                break passwordLoop;

            }

            PassHasher passHasher = new PassHasher();
            String hashedPass = passHasher.hasher(password);
            String recoveryPass = passHasher.backuppassword();


            String email;
            emailLoop:
            while(true) {
                System.out.println();
                System.out.print("Enter Email (@pigeon.com will be auto-added) : ");
                email = scanner.nextLine();
                if(!User.validateEmail(email)) {
                    System.out.println("Invalid Email! Email can only contain : a-z, A-Z, 0-9, _, - and be 4-20 characters long");
                    continue emailLoop;
                }
                email += "@pigeon.com";
                break emailLoop;
            }

            user = sqlServer.SignUp(login, email, hashedPass, recoveryPass);
        }

        //Email Hub
        else if (pID == 3){
            System.out.println("********Welcome, " + "********");
            System.out.println();
            System.out.println("1.Compose E-mail");
            System.out.println("2.Inbox");
            System.out.println("3.Folders");

            int decision = scanner.nextInt();
            scanner.nextLine();
            System.out.println("*******************************");
        }

        //Email composer
        else if (pID == 4){
            try
            {
                StringBuilder log = new StringBuilder();

                System.out.print("To: ");
                log.append("To: ");
                String receiver = scanner.nextLine();
                log.append(receiver).append("\n");

                System.out.print("Subject: ");
                log.append("Subject: ");
                String subject = scanner.nextLine();
                log.append(subject).append("\n");

                System.out.println("---------------------------");
                log.append("---------------------------\n");

                String text = scanner.nextLine();
                log.append(text).append("\n");
                if (text.length() > 7500) throw new InvalidEmailException("Limit of 7500 characters has been exceeded");

                System.out.println("Destination: ");
                System.out.println("1.Send");
                System.out.println("2.Draft");
                int decision = scanner.nextInt();
                if(decision != 1 && decision != 2)throw new InvalidEmailException("Invalid response");
                EmailSender(decision, log);
            }
            catch (InvalidEmailException e) {
                System.out.println("ERROR: " + e.getMessage());
                pages(4);
            }
        }

        //Inbox
        else if(pID == 5){
            System.out.println("Inbox action: ");
            System.out.println("1.View inbox");
            System.out.println("2.Delete all in inbox ");

            int decision1 = scanner.nextInt();

            if(decision1 == 1) Folderviewer(4);
            else if(decision1 == 2) FolderDeleter(4);
        }
    }

    public void EmailSender(int decision, StringBuilder log)
    {
        if(decision == 1)
        {
            //aq mere serveris shit ra rom imena miuvdes u get me brochacho
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Sent.txt", true));
                bufferedWriter.append(log);
                bufferedWriter.write("\n---EMAIL-END---\n");
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
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter( filePath + "\\Draft.txt", true));
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
        pages(3);
    }

    public void FolderAcess()
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
            ArrayList<String> Draftemails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get(filePath + "\\Draft.txt"));
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
        else if(decision == 4)
        {

            ArrayList<String> emails = new ArrayList<>();
            try
            {
                List<String> jumpbledemails = Files.readAllLines(Paths.get(filePath + "\\Inbox.txt"));
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
        pages(3);
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

}
