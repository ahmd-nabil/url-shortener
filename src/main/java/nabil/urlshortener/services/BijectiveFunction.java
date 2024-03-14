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

    /**
     * Initialize the maps with the base 62 characters
     * */
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

    /**
     * @param id - long value to be encoded
     * @return encoded string of the id
     * */
    public String encode(long id) {
        StringBuilder sb = new StringBuilder();
        while(id > 0) {
            sb.append(indexToChar.get(id % BASE));
            id /= BASE;
        }
        return sb.reverse().toString();
    }


    /**
     * @param shortUrl- encoded url
     * @return database id of that shortUrl
     * */
    public long decode(String shortUrl) {
        long id = 0;
        for(char c : shortUrl.toCharArray()) {
            id = id * BASE + charToIndex.get(c);
        }
        return id;
    }

}
