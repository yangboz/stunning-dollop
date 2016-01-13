import com.sun.tools.javac.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yangboz on 1/11/16.
 *
 * @author smartkit@msn.com
 */
public class ConferenceTrackManagement {

    public static void main(String[] args) throws ParseException {
        System.out.println("Hello Conference Track Management!");
        //Now starting conference track management inspired by Genetic Algorithm.
        //1.Initialization
        ConferTrackManagement conferTrackManagement = new ConferTrackManagement();
        conferTrackManagement.initialize();
        Assert.checkNonNull(conferTrackManagement.defaultTalkers);
        //
        conferTrackManagement.populateTalkerList();
        Assert.checkNonNull(conferTrackManagement.populatedTalkerLists);
        //
        conferTrackManagement.calculateConferTrackers();
    }

    //helper func to calculate time.
    private static Date addMinutesToDate(int minutes, Date beforeTime) {
        final long ONE_MINUTE_IN_MILLIS = 60000;//milli-secs

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    //Iterator design pattern
    public interface Iterator {
        public boolean hasNext();

        public Object next();
    }

    //
    public interface IteratorContainer {
        public Iterator getIterator();
    }

    static class ConferTrackManagement {

        ConferTracker confTracker = new ConferTracker();
        private List<Talker> defaultTalkers = new ArrayList<Talker>();
        private List<List<Talker>> populatedTalkerLists = new ArrayList<List<Talker>>();
        private List<Session> sessions = null;

        public ConferTrackManagement() {
            //
        }

        public List<Session> getSessions() {
            return sessions;
        }

        public void setSessions(List<Session> sessions) {
            this.sessions = sessions;
        }

        //Test input
        public void initialize() {
            defaultTalkers.add(new Talker("Writing Fast Tests Against Enterprise Rails 60min", 60));
            defaultTalkers.add(new Talker("Overdoing it in Python 45min", 45));
            defaultTalkers.add(new Talker("Lua for the Masses 30min", 30));
            defaultTalkers.add(new Talker("Ruby Errors from Mismatched Gem Versions 45min", 45));
            defaultTalkers.add(new Talker("Common Ruby Errors 45min", 45));
            defaultTalkers.add(new Talker("Rails for Python Developers lightning", 5));
            defaultTalkers.add(new Talker("Communicating Over Distance 60min", 60));
            defaultTalkers.add(new Talker("Accounting-Driven Development 45min", 45));
            defaultTalkers.add(new Talker("Woah 30min", 30));
            defaultTalkers.add(new Talker("Sit Down and Write 30min", 30));
            defaultTalkers.add(new Talker("Pair Programming vs Noise 45min", 45));
            defaultTalkers.add(new Talker("Rails Magic 60min", 60));
            defaultTalkers.add(new Talker("Ruby on Rails: Why We Should Move On 60min", 60));
            defaultTalkers.add(new Talker("Clojure Ate Scala (on my project) 45min", 45));
            defaultTalkers.add(new Talker("Programming in the Boondocks of Seattle 30min", 30));
            defaultTalkers.add(new Talker("Ruby vs. Clojure for Back-End Development 30min", 30));
            defaultTalkers.add(new Talker("Ruby on Rails Legacy App Maintenance 60min", 60));
            defaultTalkers.add(new Talker("A World Without HackerNews 30min", 30));
            defaultTalkers.add(new Talker("User Interface CSS in Rails Apps 60min", 60));
        }

        public void populateTalkerList() {
            //Simply randomize it.
            for (int i = 0; i < this.defaultTalkers.size(); i++) {//XXX:should permutation all of array list items?
                long seed = System.nanoTime();
//                System.out.println("before shuffle:" + this.defaultTalkers.toString());
                Collections.shuffle(this.defaultTalkers, new Random(seed));
//                System.out.println("after shuffle:" + this.defaultTalkers.toString());
                //shuffle then clone it.
                List<Talker> shuffledTalkers = new ArrayList<Talker>(this.defaultTalkers);
                populatedTalkerLists.add(shuffledTalkers);
            }
//            System.out.println("after population:" + populatedTalkerLists.toString());
        }

        public void calculateConferTrackers() throws ParseException {
            //2.Evaluation
            //3.Selection
            Session morningSession = new Session(9, 12, Session.FLAG_MORNING);
            Session lunchSession = new Session(12, 13, Session.FLAG_LUNCH);
            Session afternoonSession = new Session(13, 17, Session.FLAG_AFTERNOON);
            //4.Repeat
            int trackerIndex = 1;
            for (List<Talker> talkerList : populatedTalkerLists) {
//                System.out.println("Input talkerList:" + talkerList.toString());
                //Morning
                confTracker.evaluation(morningSession);
                confTracker.selection(talkerList);
                confTracker.output(trackerIndex);
                //Lunch
                confTracker.evaluation(lunchSession);
                confTracker.selection(talkerList);
                confTracker.output(trackerIndex);
                //Afternoon
                confTracker.evaluation(afternoonSession);
                confTracker.selection(talkerList);
                confTracker.output(trackerIndex);
                //
                trackerIndex++;
            }
        }
    }

    static class ConferTracker implements IteratorContainer {

        Talker firstTalker = null;
        private List<Talker> inputTalks = new ArrayList<Talker>();
        private int remainMinutes = 0;
        private Session currentSession = null;
        private Date currentTalkerTime = null;
        private Talker lunchTalker = new Talker("Lunch", 60);//fixed lunch time.
        private Talker lastTalker = new Talker("Networking Event", 999);//always the last.
        private Talker previousTalker = null;

        //XXX:Evaluation the session related time value.
        public void evaluation(Session session) {
            this.currentSession = session;
            this.remainMinutes = session.getDuration();
//            System.out.println(session.flag + ",RemainMinutes:" + this.remainMinutes + "mins");
//            ascendingTalkers
//            Collections.sort(this.allTalks, new Comparator<Talker>() {
//                public int compare(Talker o1, Talker o2) {
//                    if (o1.duration == o2.duration)
//                        return 0;
//                    return o1.duration < o2.duration ? -1 : 1;
//                }
//            });
        }

        //randomly select the first item.
        public void selection(List<Talker> talkers) throws ParseException {
            //accept input but clone it.
            this.inputTalks = new ArrayList<Talker>(talkers);
            //begin selection except lunch session.
            if (!currentSession.isLunch()) {
                this.firstTalker = this.previousTalker = this.inputTalks.remove(0);
                this.remainMinutes -= this.firstTalker.getDuration();
            }
            //calculate the current talker's time.
            this.currentTalkerTime = addMinutesToDate(0, this.currentSession.getBeginTime());
        }

        //Test output
        public void output(int index) throws ParseException {
            //
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            //By session periods(morning,lunch,afternoon)
            //Track title.
            if (currentSession.isMorning()) {
                System.out.println("Track " + index + ":");
            }
            ////Noon Lunch time.
            if (currentSession.isLunch()) {
                System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + this.lunchTalker.toString());
            } else {//Morning/Afternoon
                System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + this.firstTalker.toString());
                for (Iterator iterator = this.getIterator(); iterator.hasNext(); ) {
                    Talker talker = (Talker) iterator.next();
                    //time costing calculating...
                    this.currentTalkerTime = addMinutesToDate(this.previousTalker.getDuration(), this.currentTalkerTime);
                    System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + talker.toString());
                    this.previousTalker = talker;
                }
            }
            //fixed the last talker
            if (currentSession.isAfternoon()) {
                this.currentTalkerTime = addMinutesToDate(0, this.currentSession.getEndTime());
                System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + this.lastTalker.toString());
            }
        }

        @Override
        public Iterator getIterator() {
            return new TalkIterator();
        }

        private class TalkIterator implements Iterator {
            int index;

            @Override
            public boolean hasNext() {
                if (index < inputTalks.size()) {
//                    return true;
                    return indexOfByDuration(inputTalks, remainMinutes) != -1;
                }
                return false;
            }

            @Override
            public Object next() {
                if (this.hasNext()) {
                    index = indexOfByDuration(inputTalks, remainMinutes);
                    remainMinutes -= inputTalks.get(index).getDuration();
                    return inputTalks.remove(index);
                }
                return null;
            }

            //XXX:simply indexOf with duration constrained,will lost best index candidate.
            //XXX:select the best suitable index,such as simulated annealing algorithm.
            int indexOfByDuration(List<Talker> list, int leftDuration) {
                int i = 0;
                for (Talker o : list) {
                    if (o.getDuration() <= leftDuration) return i;
                    i++;
                }
                return -1;
            }
        }
    }

    static class Session {

        public static final String FLAG_MORNING = "AM";
        public static final String FLAG_AFTERNOON = "PM";
        public static final String FLAG_LUNCH = "_PM";
        private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        private int begin = 0;
        private int end = 0;
        private String flag = "";

        public Session(int begin, int end, String flag) {
            this.begin = begin;
            this.end = end;
            this.flag = flag;
        }

        public int getDuration() {
            return (this.end - this.begin) * 60;//hour to minutes.
        }

        public boolean isMorning() {
            return flag == FLAG_MORNING;
        }

        public boolean isLunch() {
            return flag == FLAG_LUNCH;
        }

        public boolean isAfternoon() {
            return flag == FLAG_AFTERNOON;
        }

        public Date getBeginTime() throws ParseException {
            Date beginDate = format.parse(String.valueOf(Integer.toString(this.begin) + ":00"));
            return beginDate;
        }

        public Date getEndTime() throws ParseException {
            Date endDate = format.parse(String.valueOf(Integer.toString(this.end) + ":00"));
            return endDate;
        }

        public String toString() {
            return flag;
        }
    }

    static class Talker {

        private String title;
        private int duration;//in minutes

        public Talker(String title, int duration) {
            this.title = title;
            this.duration = duration;
        }

        public int getDuration() {
            return duration;
        }

        public String toString() {
            return title;
        }

    }
}
