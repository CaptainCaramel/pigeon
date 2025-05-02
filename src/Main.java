public class Main
{
     public static void main(String[] args)
     {
        PassHasher passHasher = new PassHasher();
        System.out.println(passHasher.hasher("ArminHange1!"));
        System.out.println(passHasher.backuppassword());
     }
}