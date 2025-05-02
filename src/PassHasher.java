import java.util.ArrayList;
import java.util.stream.Stream;

public class PassHasher
{
    public String hasher(String password)
    {
        //პაროლის hashing
        StringBuilder stringBuilder = new StringBuilder(password);
        String hashedpassword = AsciMult(stringBuilder.toString(), Ascier(stringBuilder.toString()));
        return hashedpassword;
    }

    private int Ascier(String password)
    {
        //მოცემულ მეთოდში password-ს ვაქცევთ ციფრად რომელიც არის მისი ყველა ასოს ასკი მნიშვნელობის ჯამი
        ArrayList<Character> passwordindchars = new ArrayList<>();
        for(char c : password.toCharArray())
        {
            passwordindchars.add(c);
        }
        ArrayList<Integer> Indasci = new ArrayList<>();
        for(int i = password.length() - 1; i > -1;i--)
        {
            Indasci.add(Integer.valueOf(passwordindchars.get(i)));
        }
        Integer sum = Indasci.stream().reduce(0, (tmp1, tmp2) -> tmp1 + tmp2);
        return sum;
    }

    private String AsciMult(String password, long ascivalue)
    {
        //მოცემულ მეთოდში, ზემოთხსენებულ ინტს ვამრავლებთ თავის თავზე password-ის სიგრძემდე
        for(int i = 0; i < password.length(); i++)
        {
            ascivalue *= ascivalue;
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(ascivalue));
        stringBuilder.reverse();
        String result = stringBuilder.toString();
        return result;
    }
}
