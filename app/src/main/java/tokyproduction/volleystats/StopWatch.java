package tokyproduction.volleystats;

/**
 * Created by Marco on 26.05.2017.
 */

public class StopWatch {
    private long startTime =0;
    private boolean running = false;

    private long currentTime =0;


    public void start(){
        this.startTime=System.currentTimeMillis();
        this.running=true;

    }
    public void stop(){
        this.running=false;
    }
    public void pause(){
        this.running=false;
        currentTime=System.currentTimeMillis()-startTime;
    }
    public void resume(){
        this.running=true;
        this.startTime=System.currentTimeMillis()-currentTime;
    }

    public long getElapsedTimeMili(){
        long elapsed =0;
        if(running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 100) % 1000;
        }
        return elapsed;



    }

    public long getElapsedTimeSecs(){
        long elapsed =0;
        if(running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
        }
        return elapsed;

   }

   public long getElapsedTimeMin(){
       long elapsed =0;
       if(running) {
           elapsed = (((System.currentTimeMillis() - startTime) / 1000)/60) % 60;
       }
       return elapsed;
   }

   public long getElapsedTimeHour(){
       long elapsed =0;
       if(running) {
           elapsed = (((System.currentTimeMillis() - startTime) / 1000)/60) / 60;
       }
       return elapsed;

   }
   private String getmilisStr(){
       if(getElapsedTimeMili()<10){
           return "0"+getElapsedTimeMili();
       }else{
           return ""+getElapsedTimeMili();
       }
   }
    private String getSecStr(){
        if(getElapsedTimeSecs()<10){
            return "0"+getElapsedTimeSecs();
        }else{
            return ""+getElapsedTimeSecs();
        }
    }
    private String getMinStr(){
        if(getElapsedTimeMin()<10){
            return "0"+getElapsedTimeMin();
        }else{
            return ""+getElapsedTimeMin();
        }
    }

    private String getHourStr(){
        if(getElapsedTimeHour()<10){
            return "0"+getElapsedTimeHour();
        }else{
            return ""+getElapsedTimeHour();
        }
    }

   public String toString(){
       return getHourStr()+":"+getMinStr()+":"+getSecStr();
   }
}
