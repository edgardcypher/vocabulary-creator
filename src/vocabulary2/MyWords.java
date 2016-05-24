/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary2;

import java.io.Serializable;

public class MyWords implements Serializable {
    private String word=" ";
    private String definition=" ";
    public String getWord() { return word; }
    public String getDefn() { return definition; }
    public void setWord(String word) { this.word = word; }
    public void setDefn(String definition) { this.definition = definition; }
    public void setWordAndDefn (String word, String definition ) { 
        setWord(word); 
        setDefn(definition) ;
    }
    @Override
    public String toString() { // override so we can make our list
        return word;
    }
    public void display(){
      System.out.print(word+"  "+definition);
    }
}
