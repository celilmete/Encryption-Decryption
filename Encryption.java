
/*
CSE2023 4'th Assignment
Celil Mete 150116042
The purpose of the program is encrypt a text using affine chipper.
Then decrypt it using brute force algorithm
12/12/2018
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class Encryption {

    static int setOfA[] = {1,3,5,7,9,11,15,17,19,21,23};
    static int setOfB[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};

    public static void main(String[] args) throws Exception {

        encryptTxt(getRandomElement(setOfA),getRandomElement(setOfB),"test.txt","output.txt");
        int values[];
        values = force("decrypted.txt");
        System.out.println("a is : " + values[0]
        + "\nand b is : " + values[1]);

    }

    //the method to get random element from an int array
    private static int getRandomElement(int arr[]) {
        int rnd = (int)(System.currentTimeMillis() % arr.length);
        return arr[rnd];
    }

    //this is the method to force decryption and when decryption is done return the a and b values;
    private static int[] force(String decryptedText) throws Exception {
        int returnArr[] = {0,0};
        int a= 11,b = 26;
        boolean finishLoop = false;

        for(int i = 0 ; i < a ; i ++) {
            for(int j = 0 ; j < b ; j ++) {//for each a and b values try decryption
                decryptTxt(setOfA[i],setOfB[j],"output.txt","decrypted.txt");
                if(calculatePercentage( "decrypted.txt")){//when the percentages is okay
                    if(checkDecryption("decrypted.txt")){//then check decryption more
                        returnArr[0] = setOfA[i];returnArr[1] = setOfB[j];
                        finishLoop = true;
                        break;
                    }
                }
            }
            if (finishLoop)break;
        }



        return returnArr;
    }

    //the function to check is the decrytion right
    private static boolean checkDecryption(String decryptedText) throws Exception {
        double countOfwords = 0,totalCount = 0;

        File file = new File(decryptedText);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        String[] args;
        while ((st = br.readLine()) != null) {
            args = st.split(" ");

            for (String word : args) {
                if(isTheWordInDictionary(word,".//HW4_dictionary.dat")){
                    countOfwords++;
                    totalCount++;
                }else totalCount++;

            }

        }
        return countOfwords / totalCount > 0.5;
    }

    // to check if a string is in the dictionary
    private static boolean isTheWordInDictionary(String word,String dictionaryPath)throws Exception {

        File file = new File(dictionaryPath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null){
            st = st.toLowerCase();
            if(word.equals(st))return true;
        }

        return false;
    }

    //this is the method to make a guess that if a decrypted txt is decrypted correctly
    private static boolean calculatePercentage(String inputFile) throws  Exception{
        FileReader fr = new FileReader(inputFile);
        double total = 0,e = 0,t = 0,a = 0,o = 0,i = 0,n = 0,s = 0,h = 0,r = 0;//most common letters in a English txt
        int x;
        char ch ;
        while ((x=fr.read()) != -1) {
            ch = Character.toLowerCase((char)x);//make the char lowercase
            if(ch == 'e')e++;//check if the char is one of the most common letters
            else if(ch == 't')t++;
            else if(ch == 'a')a++;
            else if(ch == 'o')o++;
            else if(ch == 'i')i++;
            else if(ch == 'n')n++;
            else if(ch == 's')s++;
            else if(ch == 'h')h++;
            else if(ch == 'r')r++;

            total++;
        }
        //calculate the percentages
        e = (int)(e / total * 10000) / 100.0;
        t = (int)(t / total * 10000) / 100.0;
        a = (int)(a / total * 10000) / 100.0;
        o = (int)(o / total * 10000) / 100.0;
        i = (int)(i / total * 10000) / 100.0;
        n = (int)(n / total * 10000) / 100.0;
        s = (int)(s / total * 10000) / 100.0;
        h = (int)(h / total * 10000) / 100.0;
        r = (int)(r / total * 10000) / 100.0;
        return e > 3 && t > 3 && a > 3 && o > 3 && i > 3 && n > 3 && s > 3 && h > 3 && r > 3;//return true
    }                                                    //if most common letters are over some percentage

    //this is a method that decrypts a txt file with given a and b values
    private static void decryptTxt(int a,int b,String inputFile,String outputFile) throws Exception {

        File outFile = new File(outputFile);

        if (!outFile.exists()) {
            outFile.createNewFile();

        }

        PrintWriter output = new PrintWriter(outFile);
        FileReader fr = new FileReader(inputFile);

        String s = "";
        int i;
        while ((i=fr.read()) != -1) {

            s += (char)i;
            s = decryptionMessage(s,a,b);
            output.print(s);
            s = "";
        }


        output.close();
        fr.close();


    }

    //this is a method that decrypts a string with given a and b values
    private static String decryptionMessage(String CTxt,int a,int b) {
        String Msg = "";
        int inverOfA = 0;//
        int x = 0;
        //for loop to find the inverse of a value
        for (int i = 0; i < 26; i++) {
            x = (a * i) % 26;
            if (x == 1) {
                inverOfA = i;
                break;
            }
        }
        //use the b and inverse a values to decrypt the string
        for (int i = 0; i < CTxt.length(); i++) {
            if (Character.isLetter(CTxt.charAt(i))){//check if the char is letter
                if(Character.isLowerCase(CTxt.charAt(i))){//if the letter is uppercase
                    Msg = Msg + (char)(((inverOfA * ((CTxt.charAt(i) - 'a' - b)) % 26 + 26) % 26) + 'a');
                }else{//if the letter is lower case
                    Msg = Msg + (char)(((inverOfA * ((CTxt.charAt(i) - 'A' - b)) % 26 + 26) % 26) + 'A');
                }
            }
            else
                Msg = Msg + CTxt.charAt(i);

        }
        return Msg;
    }

    //this a method that encrypts a txt file then writes it in a txt file
    private static void encryptTxt(int a , int b,String inputFile,String outputFile) throws Exception {
        File outFile = new File(outputFile);

        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        PrintWriter output = new PrintWriter(outFile);
        FileReader fr = new FileReader(inputFile);

        String s = "";
        int i;
        while ((i=fr.read()) != -1) {//read txt by char

            s += (char)i;
            s = encryptMsg(a,b,s);//calling encryptMsg method
            output.print(s);//write the encryption in out file
            s = "";
        }

        output.close();
        fr.close();
    }

    //this is a method that encrypts a string
    private static String encryptMsg(int a, int b, String msg) {
        //returning string
        String returntxt = "";

        //for loop to encrypt string char by char
        for (int i = 0 ; i < msg.length() ; i++) {

            if(Character.isLetter(msg.charAt(i))) {//if it is a letter it will be encrypted
                if(Character.isLowerCase(msg.charAt(i))) {//lowercase char
                    returntxt = returntxt + (char)(((a * (msg.charAt(i) - 'a') + b ) % 26) + 'a');
                }else {//uppercase char
                    returntxt = returntxt + (char)(((a * (msg.charAt(i) - 'A') + b ) % 26) + 'A');
                }
            }
            else {//if the char is not a letter it will be left as it is
                returntxt = returntxt + msg.charAt(i);
            }

        }
        //return the encrypted string
        return returntxt;
    }

}
