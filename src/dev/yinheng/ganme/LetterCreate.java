package dev.yinheng.ganme;

import java.util.Random;

/**
 * Created @2017/6/11 16:27
 */
public class LetterCreate {

   private Letters[] letterArray = Letters.values();
   private Random random = new Random();

    public Letters letterCreate() {
        int index = random.nextInt(26);
        return letterArray[index];
    }
}
