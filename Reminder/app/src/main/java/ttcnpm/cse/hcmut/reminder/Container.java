package ttcnpm.cse.hcmut.reminder;

/**
 * Created by david on 16/11/2015.
 */
public class Container {
    String title;
    String body;
    String time;
    long id;
    public Container(String title,String body,String time, long id){
        this.title=title;
        this.body=body;
        this.time=time;
        this.id=id;
    }
    public String getTitle(){return this.title;}
    public String getBody(){return this.body;}
    public String getTime(){return this.time;}
    public long getID(){return this.id;}
}
