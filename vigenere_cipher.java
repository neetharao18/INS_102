import java.util.Scanner;
import java.lang.*;
class vigenere_cipher{
    //generates the key
    static String generate_key(String plaintext, String key){
        int x=plaintext.length();
        for(int i=0;key.length()<x ;i++)
        {
            if(x==i){ i=0;
            }
            key+=key.charAt(i);
                
        }
        return key;
    }
    //to generate the cipher text
    static String cipherText(String plaintext, String key){
        String cipher_text="";
        for(int i=0;i<plaintext.length();i++){
            int x=(plaintext.charAt(i)-'A'+key.charAt(i)-'A')%26;
            x+='A';
            cipher_text+=(char)(x);
        }
        return cipher_text;
    }
    static String originalText(String cipher_text, String key)
    {
        String orig_text="";

        for (int i = 0 ; i < cipher_text.length() && i < key.length(); i++)
        {
            // converting in range 0-25
            int x = (cipher_text.charAt(i) - key.charAt(i) + 26) %26;

            // convert into alphabets(ASCII)
            x += 'A';
            orig_text+=(char)(x);
        }
        return orig_text;
    }
    public static void main(String[] args) {
        String plaintext="GEEKSFORGEEKS";
        String key="AYUSH";
        key=generate_key(plaintext, key);
        String cipher_text=cipherText(plaintext, key);
        System.out.println("cipherText: "+cipher_text);
        System.out.println("OriginalText: "+originalText(cipher_text, key));



    }
}