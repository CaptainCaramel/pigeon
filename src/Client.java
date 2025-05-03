import java.io.*;
import java.util.Scanner;


public class Client implements Serializable {
    private boolean rememberMe;

    @Serial
    private static final long serialVersionUID = 2309L;

    private final String filePath = new File("").getAbsolutePath();

    Client(){
        if (!loadSettings()) {
            //aq default settingebi
            setRememberMe(false);


            saveSettings();
        }
    }

    public boolean loadSettings(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\settings.txt")));
            Client loadedClient = (Client)objectInputStream.readObject();

            this.rememberMe = loadedClient.rememberMe;


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
            System.out.print("Enter Login : ");
            String login = scanner.nextLine();
            //LOGIN VALIDATION AQ

            //CHECK WITH THE DATABASE
            System.out.println();
            System.out.print("Enter Password : ");
            String password = scanner.nextLine();
            System.out.println();
            PassHasher passHasher = new PassHasher();
            String hashedPass = passHasher.hasher(password);
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
            System.out.print("Enter Login : ");
            String login = scanner.nextLine();
            //LOGIN VALIDATION AQ

            System.out.println();
            System.out.print("Enter Password : ");
            String password = scanner.nextLine();
            System.out.println();
            PassHasher passHasher = new PassHasher();
            String hashedPass = passHasher.hasher(password);
            //String recoveryPass = passHasher.

        }
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

}
