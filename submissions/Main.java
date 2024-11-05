import java.util.Random;

public class Main {
	public static void main(String[] args) {
		Random rnd = new Random();

		int a = rnd.nextInt(100) + 1;
		int d = rnd.nextInt(100) + 1;
		int includedLength = rnd.nextInt(100) + 1;
		boolean[] included = new boolean[includedLength];

		for (int i = 0; i < includedLength - 1; i++) {
			included[i] = rnd.nextBoolean();
		}

		included[includedLength - 1] = true;

		for (int i = included.length - 1; i > 0; i--) {
			int j = rnd.nextInt(i + 1);
			boolean temp = included[i];
			included[i] = included[j];
			included[j] = temp;
		}

		int resultS = new Solution().solution(a, d, included);
		int resultV = new Verify().verify(a, d, included);

		System.out.println("result: " + (resultS == resultV ? "PASS" : "FAIL"));
	}
}