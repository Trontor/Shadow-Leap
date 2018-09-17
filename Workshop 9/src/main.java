import java.lang.reflect.Array;
import java.util.*;

public class main {
  public static void main(String[] args) {
    ArrayList<String> newList = new ArrayList<>();
    newList.add("test");
    newList.add("This Not Good ");
    newList.add("eminem > mgk ");
    newList.add("newtest");
    System.out.println(question1(newList));

    ArrayList<Customer> question2List = new ArrayList<>();
    question2List.add(new Customer("a", 1));
    question2List.add(new Customer("b", 0));
    question2List.add(new Customer("c", 67));
    question2List.add(new Customer("d", 50));
    System.out.println("Maximum priority = " + question2(question2List).peek().getName());

    ArrayList<String> ingredients = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ingredients.add("Apple");
    }
    for (int i = 0; i < 2; i++) {
      ingredients.add("Banana");
    }
    for (int i = 0; i < 10; i++) {
      ingredients.add("1 Drop of Vanilla Essence");
    }
    Recipe newRecipe = new Recipe(ingredients);
    HashMap<String, Integer> result = question3(newRecipe);
    System.out.println(result.get("Banana"));
  }

  private static HashMap<String, Integer> question3(Recipe input) {
    HashMap<String, Integer> returnMap = new HashMap<>();
    for (String ingredient : input.allIngredients) {
      if (returnMap.containsKey(ingredient)) {
        returnMap.put(ingredient, returnMap.get(ingredient) + 1);
      } else {
        returnMap.put(ingredient, 1);
      }
    }
    return returnMap;
  }

  private static PriorityQueue<Customer> question2(ArrayList<Customer> input) {
    PriorityQueue<Customer> test =
        new PriorityQueue<>(Comparator.comparingInt(Customer::getTimeOnHold));
    test.addAll(input);
    return test;
  }

  private static String question1(ArrayList<String> input) {
    StringBuilder outputStr = new StringBuilder();
    for (String s : input) {
      if (s.split(" ").length == 1) {
        outputStr.append(s).append(", ");
      }
    }
    if (outputStr.length() > 0) {
      int length = outputStr.toString().length();
      outputStr.delete(length - 2, length - 1);
    }
    return outputStr.toString();
  }

  public static class Customer {
    private int timeOnHold;
    private String name;

    public Customer(String name, int timeOnHold) {
      this.name = name;
      this.timeOnHold = timeOnHold;
    }

    public int getTimeOnHold() {
      return timeOnHold;
    }

    public String getName() {
      return name;
    }
  }

  public static class Recipe {
    public ArrayList<String> allIngredients;

    public Recipe(ArrayList<String> ingredients) {
      allIngredients = ingredients;
    }
  }
}
