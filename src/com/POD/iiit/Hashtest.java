//package com.POD.iiit;
//
//class StringExcersise {
//
//    public static void possibleStrings(int maxLength, char[] alphabet, String curr) {
//
//        // If the current string has reached it's maximum length
//        if(curr.length() == maxLength) {
//            // System.out.println(curr);
//            hashFunction(curr);
//
//        // Else add each letter from the alphabet to new strings and process these new strings again
//        } else {
//            for(int i = 0; i < alphabet.length; i++) {
//                String oldCurr = curr;
//                curr += alphabet[i];
//                possibleStrings(maxLength,alphabet,curr);
//                curr = oldCurr;
//            }
//        }
//    }
//
//    public static void hashFunction(String decodeThis)
//    {
//        long result=0,base_2=2;
//        
//        for(int i =0;i<decodeThis.length();i++)
//        {
//        	result=result+ decodeThis.charAt(i)*base_2;
//        	base_2=base_2*2;
//        }
//    	
//        System.out.println("String " + decodeThis+" Hash value is : "+result);
//    }
//}
//
//
//public class Hashtest {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//	    char[] alphabet = new char[] {'a','b','c'};
//        // Find all possible combinations of this alphabet in the string size of 3
//        StringExcersise.possibleStrings(3, alphabet,"");
//    }
//
//}
//
//
