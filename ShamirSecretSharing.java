package expense;




	import java.util.*;
	import java.math.BigInteger;
	public class ShamirSecretSharing {
	
	    static class Point {
	        int x;
	        BigInteger y;
	        Point(int x, BigInteger y) {
	            this.x = x;
	            this.y = y;
	        }
	    }

	    static BigInteger decodeValue(String base, String value) {
	        return new BigInteger(value, Integer.parseInt(base));
	    }

	    static BigInteger lagrangeInterpolation(List<Point> points) {
	        BigInteger secret = BigInteger.ZERO;
	        int k = points.size();

	        for (int i = 0; i < k; i++) {
	            BigInteger numerator = BigInteger.ONE;
	            BigInteger denominator = BigInteger.ONE;

	            for (int j = 0; j < k; j++) {
	                if (i != j) {
	                    numerator = numerator.multiply(BigInteger.valueOf(-points.get(j).x));
	                    denominator = denominator.multiply(BigInteger.valueOf(points.get(i).x - points.get(j).x));
	                }
	            }

	            BigInteger lagrangeTerm = points.get(i).y.multiply(numerator).divide(denominator);
	            secret = secret.add(lagrangeTerm);
	        }

	        return secret;
	    }

	    static BigInteger findSecret(Map<String, Object> data) {
	        Map<String, Object> keys = (Map<String, Object>) data.get("keys");
	        int n = (int) keys.get("n");
	        int k = (int) keys.get("k");

	        List<Point> points = new ArrayList<>();

	        for (int i = 1; i <= n; i++) {
	            String key = String.valueOf(i);
	            if (data.containsKey(key)) {
	                Map<String, String> point = (Map<String, String>) data.get(key);
	                int x = i;
	                BigInteger y = decodeValue(point.get("base"), point.get("value"));
	                points.add(new Point(x, y));
	            }
	        }

	        if (points.size() < k) {
	            throw new IllegalArgumentException("Not enough points to reconstruct the secret.");
	        }

	        return lagrangeInterpolation(points.subList(0, k));
	    }

	    public static void main(String[] args) {
	        // Test Case 1
	        Map<String, Object> testCase1 = new HashMap<>();
	        testCase1.put("keys", Map.of("n", 4, "k", 3));
	        testCase1.put("1", Map.of("base", "10", "value", "4"));
	        testCase1.put("2", Map.of("base", "2", "value", "111"));
	        testCase1.put("3", Map.of("base", "10", "value", "12"));
	        testCase1.put("6", Map.of("base", "4", "value", "213"));

	        // Test Case 2
	        Map<String, Object> testCase2 = new HashMap<>();
	        testCase2.put("keys", Map.of("n", 9, "k", 6));
	        testCase2.put("1", Map.of("base", "10", "value", "28735619723837"));
	        testCase2.put("2", Map.of("base", "16", "value", "1A228867F0CA"));
	        testCase2.put("3", Map.of("base", "12", "value", "32811A4AA0B7B"));
	        testCase2.put("4", Map.of("base", "11", "value", "917978721331A"));
	        testCase2.put("5", Map.of("base", "16", "value", "1A22886782E1"));
	        testCase2.put("6", Map.of("base", "10", "value", "28735619654702"));
	        testCase2.put("7", Map.of("base", "14", "value", "71AB5070CC4B"));
	        testCase2.put("8", Map.of("base", "9", "value", "122662581541670"));
	        testCase2.put("9", Map.of("base", "8", "value", "642121030037605"));

	        System.out.println("Secret for Test Case 1: " + findSecret(testCase1));
	        System.out.println("Secret for Test Case 2: " + findSecret(testCase2));
	    }
	}
	
	

