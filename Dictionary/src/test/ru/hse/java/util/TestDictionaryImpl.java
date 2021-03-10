package ru.hse.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.*;

public class TestDictionaryImpl {
    private static final int N = 10000;

    @Test
    public void testPut() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        Assertions.assertNull(dictionary.put(3, "abacaba"));
        Assertions.assertEquals("abacaba", dictionary.put(3, "bb"));
        Assertions.assertNull(dictionary.put(27, "abacaba"));
        Assertions.assertEquals("abacaba", dictionary.put(27, "bb"));
        Assertions.assertEquals("bb", dictionary.put(3, "csSELFKA"));
        Assertions.assertNull(dictionary.put(19, "cc"));
        Assertions.assertEquals("cc", dictionary.put(19, "bb"));
    }

    @Test
    public void testRemove() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        Assertions.assertNull(dictionary.remove(3));
        dictionary.put(3, "abacaba");
        Assertions.assertEquals("abacaba", dictionary.remove(3));
        dictionary.put(3, "bb");
        dictionary.put(19, "cc");
        Assertions.assertEquals("bb", dictionary.remove(3));
        dictionary.put(3, "bb");
        Assertions.assertEquals("cc", dictionary.remove(19));
        Assertions.assertNull(dictionary.remove(27));
        dictionary.put(27, "abacaba");
        Assertions.assertEquals("abacaba", dictionary.remove(27));
    }

    private static String generateString(Random random) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = random.nextInt(30);
        for (int j = 0; j < len; ++j) {
            if (random.nextBoolean()) {
                stringBuilder.append((char) (random.nextInt(26) + 'A'));
            } else {
                stringBuilder.append((char) (random.nextInt(26) + 'a'));
            }
        }
        return stringBuilder.toString();
    }

    @Test
    public void testPutStress() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        Random random = new Random(15);
        for (int i = 0; i < N; i++) {
            if (i >= 10) {
                Assertions.assertEquals(0, 0);
            }
            int key = random.nextInt(1000);
            String value = generateString(random);
            if (random.nextBoolean()) {
                dictionary.put(key, value);
                hashMap.put(key, value);
                Set<Map.Entry<Integer, String> > dictionaryEntrySet = dictionary.entrySet();
                Set<Map.Entry<Integer, String> > hashMapEntrySet = hashMap.entrySet();
                Assertions.assertTrue(hashMapEntrySet.containsAll(dictionaryEntrySet));
                Assertions.assertTrue(dictionaryEntrySet.containsAll(hashMapEntrySet));
                Assertions.assertEquals(hashMapEntrySet.size(), dictionaryEntrySet.size());
                Assertions.assertEquals(hashMap.size(), dictionary.size());
            }
        }
    }

    @Test
    public void testRemoveStress() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        Random random = new Random(15);

        for (int i = 0; i < N; i++) {
            Set<Map.Entry<Integer, String> > dictionaryEntrySet = dictionary.entrySet();
            Set<Map.Entry<Integer, String> > hashMapEntrySet = hashMap.entrySet();
            int key = random.nextInt(1000);
            String value = generateString(random);
            Assertions.assertEquals(hashMap.size(), dictionary.size());
            if (random.nextBoolean()) {
                dictionary.put(key, value);
                hashMap.put(key, value);
                Assertions.assertTrue(hashMapEntrySet.containsAll(dictionaryEntrySet));
                Assertions.assertTrue(dictionaryEntrySet.containsAll(hashMapEntrySet));
                Assertions.assertEquals(hashMapEntrySet.size(), dictionaryEntrySet.size());
            }
            if (random.nextBoolean() && random.nextBoolean()) {
                dictionary.remove(key, value);
                hashMap.remove(key, value);
                Assertions.assertTrue(hashMapEntrySet.containsAll(dictionaryEntrySet));
                Assertions.assertTrue(dictionaryEntrySet.containsAll(hashMapEntrySet));
                Assertions.assertEquals(hashMapEntrySet.size(), dictionaryEntrySet.size());
            }
        }
    }

    @Test
    public void testGet() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        Assertions.assertNull(dictionary.get(3));
        dictionary.put(3, "abacaba");
        Assertions.assertEquals("abacaba", dictionary.get(3));
        dictionary.put(3, "bb");
        dictionary.put(19, "cc");
        Assertions.assertEquals("bb", dictionary.get(3));
        Assertions.assertEquals("cc", dictionary.get(19));
        dictionary.remove(3);
        Assertions.assertNull(dictionary.get(3));
        Assertions.assertNull(dictionary.get(27));
        dictionary.put(27, "abacaba");
        Assertions.assertEquals("abacaba", dictionary.get(27));
    }

    @Test
    public void testGetStress() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        Random random = new Random(15);

        for (int i = 0; i < N; i++) {
            int key = random.nextInt(1000);
            String value = generateString(random);
            Assertions.assertEquals(hashMap.get(key), dictionary.get(key));
            Assertions.assertEquals(hashMap.size(), dictionary.size());
            if (random.nextBoolean()) {
                dictionary.put(key, value);
                hashMap.put(key, value);
                Assertions.assertEquals(hashMap.get(key), dictionary.get(key));
            }
            if (random.nextBoolean() && random.nextBoolean()) {
                dictionary.remove(key, value);
                hashMap.remove(key, value);
                Assertions.assertEquals(hashMap.get(key), dictionary.get(key));
            }
        }
    }

    @Test
    public void testContainsKey() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        Assertions.assertFalse(dictionary.containsKey(3));
        dictionary.put(3, "abacaba");
        Assertions.assertTrue( dictionary.containsKey(3));
        Assertions.assertFalse( dictionary.containsKey(19));
        Assertions.assertFalse( dictionary.containsKey(27));
        dictionary.put(3, "bb");
        dictionary.put(19, "cc");
        Assertions.assertTrue(dictionary.containsKey(3));
        Assertions.assertTrue(dictionary.containsKey(19));
        Assertions.assertFalse( dictionary.containsKey(27));
        dictionary.put(27, "abacaba");
        Assertions.assertTrue(dictionary.containsKey(3));
        Assertions.assertTrue(dictionary.containsKey(19));
        Assertions.assertTrue( dictionary.containsKey(27));
    }

    @Test
    public void testContainsKeyStress() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        Random random = new Random(15);

        for (int i = 0; i < N; i++) {
            int key = random.nextInt(1000);
            String value = generateString(random);
            Assertions.assertEquals(hashMap.containsKey(key), dictionary.containsKey(key));
            Assertions.assertEquals(hashMap.size(), dictionary.size());
            if (random.nextBoolean()) {
                dictionary.put(key, value);
                hashMap.put(key, value);
                Assertions.assertEquals(hashMap.containsKey(key), dictionary.containsKey(key));
            }
            if (random.nextBoolean() && random.nextBoolean()) {
                dictionary.remove(key, value);
                hashMap.remove(key, value);
                Assertions.assertEquals(hashMap.containsKey(key), dictionary.containsKey(key));
            }
        }
    }

    @Test
    public void testSize() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        Assertions.assertEquals(0, dictionary.size());
        dictionary.put(3, "abacaba");
        Assertions.assertEquals(1, dictionary.size());
        dictionary.put(3, "bb");
        Assertions.assertEquals(1, dictionary.size());
        dictionary.put(19, "cc");
        Assertions.assertEquals(2, dictionary.size());
        dictionary.get(19);
        Assertions.assertEquals(2, dictionary.size());
        dictionary.remove(3);
        Assertions.assertEquals(1, dictionary.size());
        dictionary.get(3);
        Assertions.assertEquals(1, dictionary.size());
        dictionary.containsKey(19);
        Assertions.assertEquals(1, dictionary.size());
        dictionary.put(27, "abacaba");
        Assertions.assertEquals(2, dictionary.size());
    }

    @Test
    public void testClear() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        dictionary.put(3, "abacaba");
        dictionary.put(19, "cc");
        dictionary.put(20, "dd");
        dictionary.remove(19);
        dictionary.put(27, "bb");
        dictionary.clear();
        Assertions.assertEquals(0, dictionary.size());
        Assertions.assertFalse(dictionary.containsKey(3));
        Assertions.assertFalse(dictionary.containsKey(19));
        Assertions.assertFalse(dictionary.containsKey(20));
        Assertions.assertFalse(dictionary.containsKey(27));
        dictionary.put(3, "abacaba");
        Assertions.assertTrue(dictionary.containsKey(3));

    }

    @Test
    public void testClearStress() {
        DictionaryImpl<Integer, String> dictionary = new DictionaryImpl<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        Random random = new Random(15);
        for (int j = 0; j < 10; ++j) {

            for (int i = 0; i < N / 10; i++) {
                int key = random.nextInt(1000);
                String value = generateString(random);
                if (random.nextBoolean()) {
                    dictionary.put(key, value);
                    hashMap.put(key, value);
                }
                if (random.nextBoolean() && random.nextBoolean()) {
                    dictionary.remove(key, value);
                    hashMap.remove(key, value);
                }
            }

            dictionary.clear();
            hashMap.clear();

            for (int i = 0; i < N / 10; i++) {
                int key = random.nextInt(1000);
                String value = generateString(random);
                Set<Map.Entry<Integer, String>> dictionaryEntrySet = dictionary.entrySet();
                Set<Map.Entry<Integer, String>> hashMapEntrySet = hashMap.entrySet();
                if (random.nextBoolean()) {
                    dictionary.put(key, value);
                    hashMap.put(key, value);
                }
                Assertions.assertTrue(hashMapEntrySet.containsAll(dictionaryEntrySet));
                Assertions.assertTrue(dictionaryEntrySet.containsAll(hashMapEntrySet));
                Assertions.assertEquals(hashMapEntrySet.size(), dictionaryEntrySet.size());
                if (random.nextBoolean() && random.nextBoolean()) {
                    dictionary.remove(key, value);
                    hashMap.remove(key, value);
                }

                Assertions.assertTrue(hashMapEntrySet.containsAll(dictionaryEntrySet));
                Assertions.assertTrue(dictionaryEntrySet.containsAll(hashMapEntrySet));
                Assertions.assertEquals(hashMapEntrySet.size(), dictionaryEntrySet.size());
            }
        }

    }

    private void init(DictionaryImpl<Integer, String> myDictionary, HashMap<Integer, String> goodDictionary) {
        myDictionary.put(3, "abacaba");
        myDictionary.put(19, "bb");
        myDictionary.put(20, "dd");
        myDictionary.put(27, "cc");
        myDictionary.remove(19);

        goodDictionary.put(3, "abacaba");
        goodDictionary.put(19, "bb");
        goodDictionary.put(20, "dd");
        goodDictionary.put(27, "cc");
        goodDictionary.remove(19);

    }

    @Test
    public void testKeySet() {
        DictionaryImpl<Integer, String> myDictionary = new DictionaryImpl<>();
        HashMap<Integer, String> goodDictionary = new HashMap<>();
        init(myDictionary, goodDictionary);
        Set<Integer> myKeySet = myDictionary.keySet();
        Set<Integer> keySet = goodDictionary.keySet();
        Assertions.assertEquals(keySet, myKeySet);
        myDictionary.remove(3);
        goodDictionary.remove(3);
        Assertions.assertEquals(myKeySet, keySet);

        Iterator<Integer> iterator = myKeySet.iterator();
        Assertions.assertEquals(2, myKeySet.size());
        Assertions.assertEquals(20, iterator.next());
        iterator.remove();
        Assertions.assertEquals(1, myKeySet.size());
        Assertions.assertEquals(27, iterator.next());
        iterator.remove();
        Assertions.assertEquals(0, myKeySet.size());
        Assertions.assertFalse(iterator.hasNext());



    }

    @Test
    public void testValues() {
        DictionaryImpl<Integer, String> myDictionary = new DictionaryImpl<>();
        HashMap<Integer, String> goodDictionary = new HashMap<>();
        init(myDictionary, goodDictionary);
        Collection<String> myValues = myDictionary.values();
        Collection<String> values = goodDictionary.values();
        Assertions.assertTrue(values.containsAll(myValues));
        Assertions.assertTrue(myValues.containsAll(values));
        myDictionary.remove(3);
        goodDictionary.remove(3);
        Assertions.assertTrue(values.containsAll(myValues));
        Assertions.assertTrue(myValues.containsAll(values));

        Iterator<String> iterator = myValues.iterator();
        Assertions.assertEquals(2, myValues.size());
        Assertions.assertEquals("dd", iterator.next());
        iterator.remove();
        Assertions.assertEquals(1, myValues.size());
        Assertions.assertEquals("cc", iterator.next());
        iterator.remove();
        Assertions.assertEquals(0, myValues.size());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void testEntrySet() {
        DictionaryImpl<Integer, String> myDictionary = new DictionaryImpl<>();
        HashMap<Integer, String> goodDictionary = new HashMap<>();
        init(myDictionary, goodDictionary);
        Set<Map.Entry<Integer, String> > myEntrySet = myDictionary.entrySet();
        Set<Map.Entry<Integer, String> > entrySet = goodDictionary.entrySet();
        Assertions.assertEquals(entrySet, myEntrySet);
        myDictionary.remove(3);
        goodDictionary.remove(3);
        Assertions.assertEquals(entrySet, myEntrySet);
        Iterator<Map.Entry<Integer, String>> iterator = myEntrySet.iterator();
        Assertions.assertEquals(2, myEntrySet.size());
        Assertions.assertEquals(new AbstractMap.SimpleEntry<>(20, "dd"), iterator.next());
        iterator.hasNext();
        iterator.remove();
        Assertions.assertEquals(1, myEntrySet.size());
        Assertions.assertEquals(new AbstractMap.SimpleEntry<>(27, "cc"), iterator.next());
        iterator.remove();
        Assertions.assertEquals(0, myEntrySet.size());
        Assertions.assertFalse(iterator.hasNext());
    }

}
