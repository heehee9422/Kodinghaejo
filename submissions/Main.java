import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        Verify verify = new Verify();
        Random rnd = new Random();
        int num1 = rnd.nextInt(10) + 1;
        int num2 = rnd.nextInt(10) + 1;
        int result_s = solution.solution(num1, num2);
        int result_v = verify.verify(num1, num2);
        System.out.println("Test: " + (result_s == result_v ? "Pass" : "Fail"));
    }
}
