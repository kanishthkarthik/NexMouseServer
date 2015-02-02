import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.awt.Robot;
import java.awt.event.MouseEvent;

public class NexMouse implements Runnable{
    static int xcord,ycord;
    final float sensitivity = 1f;
    static float x,y;
    static int c;
    static String coord;
    static Robot robot;
    
    public static void main(String[] args) throws IOException {
       
       NexMouse runner;
       try{
       robot = new Robot();
       }
       catch(java.awt.AWTException e){}
      
       Thread t;
       ServerSocket listener = new ServerSocket(5267);        
       java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
       
       //Initialization
       coord = "";
       x=0;
       y=0;
       c=0;
       xcord = screenSize.width/2;
       ycord = screenSize.height/2;       
       
       runner = new NexMouse(); 
       t = new Thread(runner); 
       t.start();       
       
       try {
            while (true) 
            {
                Socket socket = listener.accept();
                try {    
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    coord = input.readLine();   
                    
                    //t.interrupt()
                    getint(coord);
                    
                    runner = new NexMouse(); 
                    t = new Thread(runner); 
                    t.start(); 
                       
                    //System.out.println(coord);
                    BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt",true));
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(coord);
                    pw.close();
                    
               } finally {
                    socket.close();
               }
            }
        }
        finally {
            listener.close();
            if(coord=="close")
            System.exit(0);
        }
    }
    public void run() 
    { 
        try{            
            while(true)
            {
                if(x>2||x<2)
                xcord += x*sensitivity;
                if(y>2||y<2)
                ycord += y*sensitivity;
                
                if(x>2||x<2||y<2||y<2)
                {
                    System.out.println(coord);
                    robot.mouseMove(xcord, ycord);
                }
                Thread.sleep(1000);
            }
            
        }catch(Exception e){}
    } 
    
    public static void getint(String raw)
    {
        String temp = "";
        int i = 0;
        for(; i<raw.length(); i++)
        {
            if(raw.charAt(i)==',')
            break;
            else
            temp += raw.charAt(i);
        }
        x = Float.parseFloat(temp);
        temp = ""; i++;
        for(; i<raw.length(); i++)
        {
            if(raw.charAt(i)==',')
            break;
            else
            temp += raw.charAt(i);
        }
        y = Float.parseFloat(temp);     
        temp = ""; i++;
        for(; i<raw.length(); i++)
        {
            if(raw.charAt(i)==',')
            break;
            else
            temp += raw.charAt(i);
        }
        c = Integer.parseInt(temp);     
        if(c==1)
        {
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);
        }
        else if(c==2)
        {
            robot.mousePress(MouseEvent.BUTTON2_MASK);
            robot.mouseRelease(MouseEvent.BUTTON2_MASK);
        }
        
    }
}