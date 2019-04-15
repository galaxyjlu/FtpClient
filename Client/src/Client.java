import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    private Socket socket;
    private int port;
    private String IP;
    private String FilePath="E:/ND/";
    Client(Socket socket){
        setSocket(socket);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void send(String cmd) throws IOException {
        OutputStream os=getSocket().getOutputStream();
        PrintWriter pw=new PrintWriter(os);
        pw.write(cmd);
        pw.flush();
        pw.close();
        os.close();
        System.out.println("COMMAND HAS BEEN SENT SUCCESS");
    }

    public void Upload(String filename) throws IOException {
        send("UPLD "+filename);
        File file=new File(this.FilePath,filename);
        FileInputStream fis=new FileInputStream(file);
        DataOutputStream dos=new DataOutputStream(getSocket().getOutputStream());
        byte[] bytes=new byte[1024];
        int len=0;
        while((len=fis.read(bytes,0,bytes.length))!=-1){
            dos.write(bytes,0,len);
            dos.flush();
        }
        fis.close();
        dos.close();
        System.out.println("Succuss Upload");
    }

    public void Dowanload(String filename) throws IOException {
        send("DWLD "+filename);
        File file=new File(this.FilePath,filename);

        FileOutputStream fos=new FileOutputStream(file,true);
        DataInputStream dis=new DataInputStream(getSocket().getInputStream());
        byte[] bytes=new byte[1024];
        int len=0;
        while((len=dis.read(bytes,0,bytes.length))!=-1){
            fos.write(bytes,0,len);
            fos.flush();
        }
        fos.close();
        dis.close();
        System.out.println("Succuss Download");
    }

    public void ListAll(String cmd) throws IOException {
        send(cmd);
        InputStream is=getSocket().getInputStream();
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        String temp=null;
        String info="";
        while((temp=br.readLine())!=null){
            info+=temp;
        }
        if(info.equals("")){
            System.out.println("FAIL GET LIST ALL");
        }else{
            System.out.println(info);
        }
        is.close();
        isr.close();
        br.close();
    }

    public void Execute(String cmd) throws IOException {
        String upld="UPLD";
        String dwld="DWLD";
        String list="LISTALL";
        String[] temp=cmd.split(" ");
        if(temp[0].equals(upld)){
            if(temp.length>1) {
                Upload(temp[1]);
            }
        }
        if(temp[0].equals(dwld)){
            Dowanload(temp[1]);
        }
        if(temp[0].equals(list)){
            ListAll(cmd);
        }
    }


    public static void main(String[] args) {
        try{
            Socket socket=new Socket("localhost",8080);
            Client client=new Client(socket);
            Scanner scanner=new Scanner(System.in);
            System.out.println("INPUT COMMAD PLZ");
            String cmd=scanner.nextLine();
            client.Execute(cmd);
        }catch (Exception e){
            System.out.println("FAIL...");
        }



    }
}
