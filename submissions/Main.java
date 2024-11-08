import java.util.*;

public class Main {
	public static void main(String[] args) {
		Map<String, Object> data = new HashMap<>();
		data.put("\"message\"", "\"submit\"");

		Random rnd = new Random();

		int[] a = new int[20];
		int[] d = new int[20];
		int[] includedLen = new int[20];
		boolean[][] included = new boolean[20][];

		int passCnt = 0;
		List<Map<String, Object>> subdata = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			a[i] = rnd.nextInt(100) + 1;
			d[i] = rnd.nextInt(100) + 1;
			includedLen[i] = rnd.nextInt(100) + 1;
			included[i] = new boolean[includedLen[i]];

			for (int j = 0; j < includedLen[i] - 1; j++) {
				included[i][j] = rnd.nextBoolean();
			}

			included[i][includedLen[i] - 1] = true;

			for (int j = included[i].length - 1; j > 0; j--) {
				int k = rnd.nextInt(j + 1);
				boolean temp = included[i][j];
				included[i][j] = included[i][k];
				included[i][k] = temp;
			}

			Map<String, Object> map = new HashMap<>();

			int resultS = new Solution().solution(a[i], d[i], included[i]);
			int resultV = new Verify().verify(a[i], d[i], included[i]);

			passCnt += (resultS == resultV) ? 1 : 0;

			String includedStr = "\"[";
			for (int j = 0; j < included[i].length; j++) {
				if (j > 0) includedStr += ", ";
				includedStr += String.valueOf(included[i][j]);
			}
			includedStr += "]\"";

			map.put("\"a\"", a[i]);
			map.put("\"d\"", d[i]);
			map.put("\"included\"", includedStr);
			map.put("\"resultS\"", resultS);
			map.put("\"resultV\"", resultV);

			subdata.add(map);
		}

		data.put("\"passcnt\"", passCnt);
		data.put("\"subdata\"", subdata);

		System.out.println(data.toString().replaceAll("=", ":"));
	}
}