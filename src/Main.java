import com.sun.tools.javac.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

//    public static void main(String[] args) {
//
//        // Set a candidate solution
////        FitnessCalc.setSolution("1111000000000000000000000000000000000000000000000000000000001111");
//        FitnessCalc.setSolution("11111111111100001111111111111111");
//
//        // Create an initial population
//        Population myPop = new Population(50, true);
//
//        // Evolve our population until we reach an optimum solution
//        int generationCount = 0;
//        while (myPop.getFittest().getFitness() < FitnessCalc.getMaxFitness()) {
//            generationCount++;
//            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
//            myPop = Algorithm.evolvePopulation(myPop);
//        }
//        System.out.println("Solution found!");
//        System.out.println("Generation: " + generationCount);
//        System.out.println("Genes:");
//        System.out.println(myPop.getFittest());
//
//    }

    public static void main(String[] args) throws ParseException {
        System.out.println("Hello Conference Trace Management!");
        //Now starting confer tracker management inspired by Genetic Algorithm.
        //1.Initialization
        ConfTracker confTracker = new ConfTracker();
        confTracker.initialize();
        Assert.checkNonNull(confTracker.getAllTalks());

        //2.Evaluation
        //3.Selection
        //4.Repeat
        Session morningSession = new Session(9, 12, Session.FLAG_MORNING);
        Session afternoonSession = new Session(13, 17, Session.FLAG_AFTERNOON);
        ///
        confTracker.evaluation(morningSession);
        confTracker.selection();
        confTracker.output();
        ///
        confTracker.evaluation(afternoonSession);
        confTracker.selection();
        confTracker.output();
    }

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

    static class ConfTracker implements IteratorContainer {

        Talker firstTalker = null;
        private List<Talker> allTalks = new ArrayList<Talker>();
        private int remainMinutes = 0;
        private Session currentSession = null;
        private Date currentTalkerTime = null;
        private Talker lastTalker = new Talker("Networking Event", 999);//always the last.
        private Talker previousTalker = null;

        //Test input
        public void initialize() {
            allTalks.add(new Talker("Writing Fast Tests Against Enterprise Rails 60min", 60));
            allTalks.add(new Talker("Overdoing it in Python 45min", 45));
            allTalks.add(new Talker("Lua for the Masses 30min", 30));
            allTalks.add(new Talker("Ruby Errors from Mismatched Gem Versions 45min", 45));
            allTalks.add(new Talker("Common Ruby Errors 45min", 45));
            allTalks.add(new Talker("Rails for Python Developers lightning", 5));
            allTalks.add(new Talker("Communicating Over Distance 60min", 60));
            allTalks.add(new Talker("Accounting-Driven Development 45min", 45));
            allTalks.add(new Talker("Woah 30min", 30));
            allTalks.add(new Talker("Sit Down and Write 30min", 30));
            allTalks.add(new Talker("Pair Programming vs Noise 45min", 45));
            allTalks.add(new Talker("Rails Magic 60min", 60));
            allTalks.add(new Talker("Ruby on Rails: Why We Should Move On 60min", 60));
            allTalks.add(new Talker("Clojure Ate Scala (on my project) 45min", 45));
            allTalks.add(new Talker("Programming in the Boondocks of Seattle 30min", 30));
            allTalks.add(new Talker("Ruby vs. Clojure for Back-End Development 30min", 30));
            allTalks.add(new Talker("Ruby on Rails Legacy App Maintenance 60min", 60));
            allTalks.add(new Talker("A World Without HackerNews 30min", 30));
            allTalks.add(new Talker("User Interface CSS in Rails Apps 60min", 60));
        }

        public void evaluation(Session session) {
            this.currentSession = session;
            this.remainMinutes = session.getDuration();
            System.out.println("RemainMinutes:" + this.remainMinutes + "mins");
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
        public void selection() throws ParseException {
            //Randomize
            long seed = System.nanoTime();
            Collections.shuffle(this.allTalks, new Random(seed));
            //begin selection.
            this.firstTalker = this.previousTalker = this.allTalks.remove(0);
            this.remainMinutes -= this.firstTalker.getDuration();
            this.currentTalkerTime = addMinutesToDate(0, this.currentSession.getBeginTime());
        }

        //Test output
        public void output() throws ParseException {
            //
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + this.firstTalker.toString());
            for (Iterator iterator = this.getIterator(); iterator.hasNext(); ) {
                Talker talker = (Talker) iterator.next();
                this.currentTalkerTime = addMinutesToDate(this.previousTalker.getDuration(), this.currentTalkerTime);
                System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + talker.toString());
                this.previousTalker = talker;
            }
            //hard-code output the last talker
            if (currentSession.isAfternoon()) {
                this.currentTalkerTime = addMinutesToDate(0, this.currentSession.getEndTime());
                System.out.println(sdf.format(this.currentTalkerTime) + this.currentSession + " " + this.lastTalker.toString());
            }
        }

        public List<Talker> getAllTalks() {
            return allTalks;
        }

        @Override
        public Iterator getIterator() {
            return new TalkIterator();
        }

        private class TalkIterator implements Iterator {
            int index;

            @Override
            public boolean hasNext() {
                if (index < allTalks.size()) {
//                    return true;
                    return indexOfByDuration(allTalks, remainMinutes) != -1;
                }
                return false;
            }

            @Override
            public Object next() {
                if (this.hasNext()) {
                    index = indexOfByDuration(allTalks, remainMinutes);
                    remainMinutes -= allTalks.get(index).getDuration();
                    return allTalks.remove(index);
                }
                return null;
            }

            //indexOf with duration constrained.
            //TODO:select the best suitable index.
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

        public boolean isAfternoon() {
            return flag == FLAG_AFTERNOON;
        }

        public Date getBeginTime() throws ParseException {
            Date beginDate = format.parse(String.valueOf(Integer.toString(this.begin) + ":00"));
            return beginDate;
        }

        public Date getEndTime() throws ParseException {
            Date beginDate = format.parse(String.valueOf(Integer.toString(this.end) + ":00"));
            return beginDate;
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
