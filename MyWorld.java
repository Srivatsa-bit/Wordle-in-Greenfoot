import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.util.Random;
import java.io.*;
import java.util.HashMap;


public class MyWorld extends World
{
    
    private String string_letters = "abcdefghijklmnopqrstuvwxyz";
    private int yVal = 40;
    private String word = chooseWord();
    private int guesses = 1;
    private boolean gameOver = true;
    private HashMap<Character, Integer> letters = new HashMap<Character, Integer>();
    private HashMap<String, Integer> letterCount = new HashMap<String, Integer>();
    private int keyboard_yVal = 470;
    private int keyboard_xVal = 80;    
    private String[] wordListed = new String[5757];
    
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(700, 720, 1);
        for(int i = 0; i<string_letters.length(); i++){
            letters.put(string_letters.charAt(i), 0);
        }
        keyboard();
        
        for (int i = 0; i<word.length(); i++){
            if(letterCount.get(Character.toString(word.charAt(i))) == null){
                letterCount.put(Character.toString(word.charAt(i)), 1);
            }
            else{
                letterCount.put(Character.toString(word.charAt(i)), letterCount.get(Character.toString(word.charAt(i)))+1);
            }
        }
        
        try{
            Scanner wordFile = new Scanner(new File("sgb-words.txt"));
            int x = 0;
            while(wordFile.hasNextLine()){
                wordListed[x] = wordFile.nextLine();
                x++;
            }
            wordFile.close();
        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    
    }
    
    public void act()
    {
        
        String words = input();
        gameOver = true;
        
        display(words);
        
        yVal += 63;
        
        if (guesses==6){
            System.out.println("Your word was " + word);
            Greenfoot.stop();
            
        }
        if (gameOver){
            System.out.println("Your word was " + word);
            Greenfoot.stop();
        }
        guesses++;
        keyboard();
        
    }

    
    public void displayRed(char words, int xVal, int yVal){
        RedCircle circle = new RedCircle();
        Letter firstLetter = new Letter(Character.toString(words));
        addObject(circle, xVal, yVal);
        addObject(firstLetter, xVal, yVal);
            
    }
    
    public void displayGreen(char words, int xVal, int yVal){
        GreenCircle circle = new GreenCircle();
        Letter firstLetter = new Letter(Character.toString(words));
        addObject(circle, xVal, yVal);
        addObject(firstLetter, xVal, yVal);
    }
    
    public void displayYellow(char words, int xVal, int yVal){
        YellowCircle circle = new YellowCircle();
        Letter firstLetter = new Letter(Character.toString(words));
        addObject(circle, xVal, yVal);
        addObject(firstLetter, xVal, yVal);
    }
    
    public void display(String words){
        String[] strList = new String[5];
        HashMap<Character, Integer> matches = new HashMap<Character, Integer>();
        for (int i = 0; i<5; i++){
            strList[i] = Character.toString(words.charAt(i)) + " ";
            matches.put(words.charAt(i), 0);
        }
        
        for (int i = 0; i<5; i++){
            if (word.charAt(i) == words.charAt(i)){
                if(matches.get(words.charAt(i)) == null){
                    matches.put(words.charAt(i), 1);
                }
                else{
                    matches.put(words.charAt(i), matches.get(words.charAt(i))+1);
                }
                strList[i] = strList[i].replace(" ", "+");
            }

            
        }
        
        for (int i = 0; i<5; i++){
            if (containsChar(word, words.charAt(i))){
                if (matches.get(words.charAt(i)) < letterCount.get(Character.toString(words.charAt(i))) && containsChar(strList[i], '+') == false){
                    strList[i] = strList[i].replace(" ", "~");
                    matches.put(words.charAt(i), matches.get(words.charAt(i))+1);
                }
            }
        }
        
        for (int i = 0; i<5; i++){
            if (containsChar(strList[i], '+')){
                displayGreen(strList[i].charAt(0), i*80+200, yVal);
                if (letters.get(strList[i].charAt(0)) < 3){
                    letters.put(strList[i].charAt(0), 3);
                }
            }
            else if (containsChar(strList[i], '~')){
                displayYellow(strList[i].charAt(0), i*80+200, yVal);
                if (letters.get(strList[i].charAt(0)) < 2){
                    letters.put(strList[i].charAt(0), 2);
                }
                gameOver = false;
            }
            else{
                displayRed(strList[i].charAt(0), i*80+200, yVal);
                letters.put(strList[i].charAt(0), 1);
                gameOver = false;
            }
        }
    }
    
    public boolean containsChar(String s, char check){
        boolean contains = false;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == check){
                contains = true;
            }
        }
        return contains;
    }
    
    public String input(){
        Scanner input = new Scanner(System.in);
        String guess = input.nextLine();
        boolean realWord = false;
        while (!realWord){
            for (int i = 0; i<wordListed.length; i++){
                if (wordListed[i].compareTo(guess) == 0){
                    realWord = true;
                }
            }
            if (!realWord){
                System.out.println("Invalid guess");
                guess = input.nextLine();
            }
        }
        return guess;
        
    }
    
    public String chooseWord(){
        String chosenWord = "";
        Random rand = new Random();
        int randNum = rand.nextInt(5758);
        int i = 0;
        try{
            Scanner fileIn = new Scanner(new File("sgb-words.txt"));
            while(fileIn.hasNextLine() == true && i < randNum){
                chosenWord = fileIn.nextLine();
                i++;
            }
            fileIn.close();
        }
        catch (IOException ioe){
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
            
        return chosenWord;
    }
    
    public void keyboard(){
        keyboard_xVal = 80;
        keyboard_yVal = 470;
        for(char i: letters.keySet()){
            if (keyboard_xVal > 650){
                keyboard_yVal += 65;
                keyboard_xVal = 80;
            }
            if (letters.get(i) == 3){
                displayGreen(i, keyboard_xVal, keyboard_yVal);
                keyboard_xVal += 80;
            }else if (letters.get(i) == 2){
                displayYellow(i, keyboard_xVal, keyboard_yVal);
                keyboard_xVal += 80;
            }else if (letters.get(i) == 1){
                displayRed(i, keyboard_xVal, keyboard_yVal);
                keyboard_xVal += 80;
            }else {
                Letter firstLetter = new Letter(Character.toString(i));
                addObject(firstLetter, keyboard_xVal, keyboard_yVal);
                keyboard_xVal += 80;
            }
    
        }
    }
}
