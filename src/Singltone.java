public class Singltone {
    private Singltone(){

    }
   static private Singltone singltone= new Singltone();
   static Singltone newInstance(){
        return singltone;
    }
}
