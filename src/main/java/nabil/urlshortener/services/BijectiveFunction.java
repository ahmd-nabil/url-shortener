package nabil.urlshortener.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class BijectiveFunction {
    private final int BASE = 62;
    private final Map<Long, Character> indexToChar = new HashMap<>();
    private final Map<Character, Long> charToIndex = new HashMap<>();

    public BijectiveFunction() {
        init();
    }

    private void init() {
        long index = 0;
        for(char c = 'a'; c <= 'z'; c++) {
            indexToChar.put(index, c);
            charToIndex.put(c, index++);
        }
        for(char c = 'A'; c <= 'Z'; c++) {
            indexToChar.put(index, c);
            charToIndex.put(c, index++);
        }
        for(int i = 0; i <= 9; i++) {
            indexToChar.put(index, (char)(i + '0'));
            charToIndex.put((char)(i + '0'), index++);
        }
    }

    public String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while(id > 0) {
            sb.append(indexToChar.get(id % BASE));
            id /= BASE;
        }
        return sb.reverse().toString();
    }

    public long decode(String s) {
        long id = 0;
        for(char c : s.toCharArray()) {
            id = id * BASE + charToIndex.get(c);
        }
        return id;
    }

}
