/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Gleb
 */
public class Consts {
    //How could I fetch those magical numbers from the database? 
    private static final int MinLoginLength = 4;
    private static final int MaxLoginLength = 64;
    private static final int MinPasswordLength = 4;
    private static final int MaxPasswordLength = 32;
    private static final int MinFullNameLength = 4;
    private static final int MaxFullNameLength = 64; 
    private static final int MinSubjectLength = 2; 
    private static final int MaxSubjectLength = 64; 
    private static final String TeacherPassword= "293";    
    
    public static final int getMinLoginLength(){
        return MinLoginLength;
    }
    
    public static final int getMaxLoginLength(){
        return MaxLoginLength;
    }
    
    public static final int getMinPasswordLength(){
        return MinPasswordLength;
    }
    
    public static final int getMaxPasswordLength(){
        return MaxPasswordLength;
    }
    
    public static final int getMinFullNameLength(){
        return MinFullNameLength;
    }      
    
    public static final int getMaxFullNameLength(){
        return MaxFullNameLength;
    }  
    
      public static final int getMinSubjectLength(){
        return MinSubjectLength;
    }  
      
        public static final int getMaxSubjectLength(){
        return MaxSubjectLength;
    }  
    
    public static final String getTeacherPassword(){
        return TeacherPassword;
    }  
}
