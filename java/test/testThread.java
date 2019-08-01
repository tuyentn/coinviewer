/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author 8570w
 */
public class testThread {
    
    
    
    public static void main(String[] args){ 
        Thread t1 = new Thread(new myRunable (), "t1");
        while (true){
            t1.start();
        }
    }
    
}
