/*
 * Created on Aug 22, 2008
 *
 */

/**
 * Copyright 2008 Thiyagaraj Krishna. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THIYAGARAJ KRISHNA ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THIYAGARAJ KRISHNA OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Thiyagaraj Krishna.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TuringMachine {
    HashMap transFunc=new HashMap();
    static String inFile;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    char blankSymbol=' ';
    TuringMachine()
    {
       
    }
   
    private void setHashMap(HashMap hm)
    {
        this.transFunc=hm;
    }
   
    private static HashMap loadUnaryAddition()
    {
        HashMap unAdd=new HashMap();
        unAdd.put("0| "," |R|0");
        unAdd.put("0|1","1|R|1");
        unAdd.put("1| ","1|R|2");
        unAdd.put("1|1","1|R|1");
        unAdd.put("2| "," |L|3");
        unAdd.put("2|1","1|R|2");
        unAdd.put("3| "," |L|3");
        unAdd.put("3|1"," |L|4");
        unAdd.put("4| "," |R|stop");
        unAdd.put("4|1","1|L|4");
        return unAdd;
    }
    private static void printHeader()
    {
        System.out.println("-------------------------");
        System.out.println("Universal Turing Machine");
        System.out.println("-------------------------");
    }
   
    private void goToNextState(int recur_num,String state,String tape, int tape_position)
    {
        char currentSymbol;
        System.out.println("----- Move "+recur_num+" ------");
        System.out.println("Current tape : "+tape);
        System.out.println("Tape Head    : "+getTapeHeadString(tape_position));
        System.out.println("Current State: "+state);
        currentSymbol=tape.charAt(tape_position-1);
        String keyToRetrieve="";
        keyToRetrieve=keyToRetrieve.concat(state+"|"+currentSymbol);
        // System.out.println("keyToRetrieve -- "+keyToRetrieve);
        String nextSet="";
        nextSet=(String)this.transFunc.get(keyToRetrieve);
        // System.out.println("nextSet"+nextSet);
        if(nextSet==null)
            {
            // Turing machine crashes if no mapping found
            System.out.println("Turing machine is crashing!\nNo mapping in transition function for current symbol "+currentSymbol+" and current state "+state);
            System.exit(0);
            }
        String[] sa=nextSet.split("\\|"); // Escape slash for compilation and another slash for the pipey
        if(sa.length<3)
        {
            // Turing machine crashes if incorrect mapping found
            System.out.println("Turing machine is crashing!\nIncorrect mapping for current symbol "+currentSymbol+" and current state "+state);
            System.exit(0);
        }
        String nextSymbol=sa[0];
        if(nextSymbol.length()>1)
        {
            //Turing machine crashes if next symbol gives more than one char!
            System.out.println("Turing machine is crashing!\nNext symbol returned more than one char for current symbol "+currentSymbol+" and current state "+state);
            System.exit(0);
        }
        String movement=sa[1];
        String nextState=sa[2];
       
        System.out.println("Movement     : "+movement);
        System.out.println("Next State   : "+nextState);
        System.out.println("Next Symbol  : "+nextSymbol);
        System.out.println();
        // System.out.println("nextState---"+nextState);
        String updTape=new String("");
        updTape=tape.substring(0,tape_position-1)+nextSymbol+tape.substring(tape_position);
        int newTapePosition=getNewTapePosition(tape_position,movement);
        //Append blankSymbols to front or back depending on movement and end of string
        if(newTapePosition>updTape.length())
        {
            updTape+=blankSymbol; //Append blank symbol at end
        }
        else if(tape_position==1 && movement.equals("L"))
        {
            updTape=blankSymbol+updTape;//Append a blank symbol at beginning
            newTapePosition++; //Incrementing current tape position to give offset for new blank symbol
        }
       
        if(nextState.equals("stop"))
        {
            System.out.println("\n\n\n*******************************************************");
            System.out.println("The Turing Machine has halted");
            System.out.println("Final State  = "+nextState);
            System.out.println("Final Tape   = "+updTape);
            System.out.println("Tape Head    = "+getTapeHeadString(newTapePosition));
            System.out.println("*******************************************************");
        }
        else
        {
            goToNextState(recur_num+1,nextState,updTape,newTapePosition);
        }
    }
   
    private int getNewTapePosition(int tape_position, String movement)
    {
        int newTapePosition=tape_position; //initializing
        if(movement=="L" && tape_position==1)
        {
            //Return 1, a blank symbol will be added to the left
            newTapePosition=1;
        }
        else if(movement.equals("L"))
        {
            newTapePosition=tape_position-1;
        }
        else if(movement.equals("R"))
        {
            newTapePosition=tape_position+1;
        }
        else if(movement.equals("N"))
        {
            newTapePosition=tape_position;
        }
        else
        {
            //Turing machine crashes if no proper movement
            System.out.println("Turing machine is crashing!\nIncorrect movement returned. Should be R,L or N");
            System.exit(0);
        }
        return newTapePosition;
    }
    private String getTapeHeadString(int position)
    {
       
        String tapeHead="";
        for(int i=0; i<(position-1);i++)
        {
            //Add spaces
            tapeHead+=" ";
        }
        //Add a marker to show position
        tapeHead+="*";
        return tapeHead;
    }
    private void startTuringMachine() throws IOException
    {
        int recur_num=1;
        String state=new String("");
        System.out.print("Enter blank symbol (Blank for space):");
        String bSym=br.readLine();
        if(bSym.equals(" ") || bSym.length()==0){
            this.blankSymbol=' ';
        }
        else if(bSym.length()>1)
        {
            System.out.println("Warning: Blank symbol cannot be more than one char");
            System.out.println("Defaulting to a blank space ' ' as the blank symbol.");
        }
        System.out.print("Enter the start state :");
        state=br.readLine();
        System.out.print("Please enter the intial tape :");
        String tape=br.readLine();
        System.out.print("Enter the start tape position: ");
        int tape_position=Integer.parseInt(br.readLine());
        this.goToNextState(recur_num,state,tape,tape_position);
    }
    public static void main(String[] args) throws IOException{
        TuringMachine tm=new TuringMachine();
        if(args.length>1)
        {
            inFile=args[1];
        }
        else
        {
            printHeader();
            System.out.println("- Input turing map is required -");
            System.out.println("Do you want to use one of the built in maps?");
            System.out.println("Press 1 to use the Unary Addition Map or any other key to exit: ");
            String selection=(String)br.readLine();
            if(Integer.parseInt(selection)==1)
            {
                System.out.println("Yeah!!!");
                tm.setHashMap(loadUnaryAddition());
                tm.startTuringMachine();
            }
        }
    }
   
   
}