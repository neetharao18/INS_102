import java.util.Arrays;
import java.util.Scanner;

public class monoalphabet{
public static void main(String[] args){
    Scanner sc=new Scanner(System.in);
    System.out.println("enter the plain text");
    String pl_text=sc.nextLine();
    char[] arr={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    char[] key={'c','f','a','z','e','i','k','d','q','w','r','y','t','u','l','n','s','g','p','b','h','j','m','o','v','x'};
    
    char[] cipher_array=new char[pl_text.length()];
       
        for(int i=0;i<pl_text.length();i++){
            char plainChar=pl_text.charAt(i);
            if(Character.isLowerCase(plainChar)){
                int index=plainChar-'a';
    
                cipher_array[i]=key[index];

            }
            else{
                cipher_array[i]=plainChar;
            }
        }
       // int n=cipher_array.length;
        for(int i=0;i<pl_text.length();i++){
        System.out.print(cipher_array[i]);
        }
    //char cipher_text=generate_key(arr,key,pl_text);
    }
}
