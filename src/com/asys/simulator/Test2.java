package com.asys.simulator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test2 {
  public static void main(String args[]) {
    Pattern p = Pattern.compile("Bond");

    String candidateString = "My name is Bond. James Bond.";

    String matchHelper[] = { "               ^", "              ^",
        "                           ^", "                          ^" };
    Matcher matcher = p.matcher(candidateString);

    matcher.find();
    int endIndex = matcher.end(0);
    System.out.println(candidateString);
    System.out.println(matchHelper[0] + endIndex);

    int nextIndex = matcher.end(1);
    System.out.println(candidateString);
    System.out.println(matchHelper[1] + nextIndex);

    matcher.find();
    endIndex = matcher.end(0);
    System.out.println(candidateString);
    System.out.println(matchHelper[2] + endIndex);

    nextIndex = matcher.end(1);
    System.out.println(candidateString);
    System.out.println(matchHelper[3] + nextIndex);
  }

}