import simpleGa.Algorithm;
import simpleGa.FitnessCalc;
import simpleGa.Population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Set a candidate solution
        FitnessCalc.setSolution("1111000000000000000000000000000000000000000000000000000000001111");

        // Create an initial population
        Population myPop = new Population(50, true);

        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        while (myPop.getFittest().getFitness() < FitnessCalc.getMaxFitness()) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
            myPop = Algorithm.evolvePopulation(myPop);
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        System.out.println(myPop.getFittest());

    }

//    public static void main(String[] args) {
//        System.out.println("Hello Conference Trace Management!");
//        //Now starting confer tracker management.
//        //1.Initialization
//        ConfTracker confTracker = new ConfTracker();
//        confTracker.initialize();
//        Assert.checkNonNull(confTracker.getAllTalks());
//        //2.Evaluation
//        Session morningSession = new Session(9, 12);
//        Session afternoonSession = new Session(11, 17);
//        List<Session> daySessions = new ArrayList<Session>();
//        daySessions.add(morningSession);
//        daySessions.add(afternoonSession);
//        confTracker.evaluation(daySessions);
//        //3.Selection
//        confTracker.selection(confTracker.getAllTalks());
//        //4.CrossOver
//        //5.Mutation
//        //6.Repeat
//        confTracker.output();
//    }

//    interface Comparable<T> {
//        public int compareTo(T o);
//    }

    static class ConfTracker {

        private List<Talker> allTalks = new ArrayList<Talker>();

        private Talker currentTalker = null;

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

        public void evaluation(List<Session> sessions) {
//            ascendingTalkers
            Collections.sort(this.allTalks, new Comparator<Talker>() {
                public int compare(Talker o1, Talker o2) {
                    if (o1.duration == o2.duration)
                        return 0;
                    return o1.duration < o2.duration ? -1 : 1;
                }
            });
            //
        }

        //RandomSelection
        public Talker selection(List<Talker> talkers) {
            int randomId = (int) (Math.random() * talkers.size());
            Talker selector = talkers.remove(randomId);
            return selector;
        }

        //Test output
        public String output() {
            return "" + currentTalker.toString();
        }

        public List<Talker> getAllTalks() {
            return allTalks;
        }
    }

    static class Session {

        private long duration = 0;
        private long begin = 0;
        private long end = 0;

        public Session(long begin, long end) {
            this.begin = begin;
            this.end = end;
        }

        public long getDuration() {
            return this.end - this.begin;
        }
    }

    static class Talker {

        private String title;
        private int duration;//in minutes

        public Talker(String title, int duration) {
            this.title = title;
            this.duration = duration;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String toString() {
            return title;
        }

    }
}
