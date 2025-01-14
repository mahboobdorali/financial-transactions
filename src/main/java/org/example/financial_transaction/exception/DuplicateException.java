 package org.example.financial_transaction.exception;

 public class DuplicateException extends RuntimeException {
     public DuplicateException(String nationalCode) {
         super("More than this number of customers with national code " + nationalCode + " are registered in the system");
     }
 }
